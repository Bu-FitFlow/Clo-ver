from fastapi import FastAPI, UploadFile, Form
import cv2
import numpy as np
from ultralytics import YOLO

app = FastAPI()

# 모델은 서버 시작 시 1회만 로드
pose_model = YOLO('yolov8n-pose.pt')
seg_model = YOLO('yolov8n-seg.pt')


def get_data(img):
    r_seg = seg_model.predict(img, conf = 0.3, verbose = False)[0]
    r_pose = pose_model.predict(img, conf = 0.3, verbose = False)[0]
    if r_seg.masks is None or r_pose.keypoints is None: return None, None, None

    mask = r_seg.masks.data[0].cpu().numpy()
    mask_resized = cv2.resize(mask, (img.shape[1], img.shape[0]))
    binary_mask = (mask_resized > 0.5).astype(np.uint8) * 255

    kernel = np.ones((5, 5), np.uint8)
    binary_mask = cv2.erode(binary_mask, kernel, iterations = 2)

    kp = r_pose.keypoints.xy[0].cpu().numpy()
    bbox = r_seg.boxes.xyxy[0].cpu().numpy()
    return binary_mask, kp, bbox


def get_naked_width(b_mask, y, center_x, max_ratio, torso):
    if y < 0 or y >= b_mask.shape[0]: return 1
    idx = np.where(b_mask[y, :] == 255)[0]
    if len(idx) < 2: return 1
    max_dist = torso * max_ratio
    real_left = max(idx[0], center_x - max_dist)
    real_right = min(idx[-1], center_x + max_dist)
    return max(real_right - real_left, 1)


def get_w(b_mask, y):
    if y < 0 or y >= b_mask.shape[0]: return 1
    idx = np.where(b_mask[y, :] == 255)[0]
    return (idx[-1] - idx[0]) if len(idx) > 1 else 1


def resize_image(img, target_height = 640):
    h, w = img.shape[:2]
    if h > target_height:
        ratio = target_height / float(h)
        new_w = int(w * ratio)
        return cv2.resize(img, (new_w, target_height), interpolation = cv2.INTER_AREA)
    return img


@app.post("/analyze")
async def analyze_body(
        gender: str = Form(...),
        front_img: UploadFile = Form(...),
        side_img: UploadFile = Form(...)
):
    gender = gender.strip().upper()

    # HTTP로 받은 이미지 바이트 배열을 OpenCV 이미지로 디코딩
    front_bytes = await front_img.read()
    side_bytes = await side_img.read()

    f_cv = cv2.imdecode(np.frombuffer(front_bytes, np.uint8), cv2.IMREAD_COLOR)
    s_cv = cv2.imdecode(np.frombuffer(side_bytes, np.uint8), cv2.IMREAD_COLOR)

    f_cv = resize_image(f_cv)
    s_cv = resize_image(s_cv)

    f_mask, f_kp, f_bbox = get_data(f_cv)
    s_mask, s_kp, s_bbox = get_data(s_cv)

    if f_mask is None or s_mask is None:
        return { "error": "분석 실패! 사람이 화면에 제대로 나오지 않았습니다." }

    f_sy = int((f_kp[5][1] + f_kp[6][1]) / 2)
    f_hy = int((f_kp[11][1] + f_kp[12][1]) / 2)
    f_torso = max(f_hy - f_sy, 1)
    f_cx = int((f_kp[5][0] + f_kp[6][0] + f_kp[11][0] + f_kp[12][0]) / 4)

    f_s_w = get_naked_width(f_mask, f_sy + int(f_torso * 0.05), f_cx, 0.35, f_torso)
    f_c_w = get_naked_width(f_mask, f_sy + int(f_torso * 0.25), f_cx, 0.35, f_torso)
    f_w_w = get_naked_width(f_mask, f_sy + int(f_torso * 0.65), f_cx, 0.35, f_torso)
    f_h_w = get_naked_width(f_mask, f_hy + int(f_torso * 0.05), f_cx, 0.35, f_torso)

    s_to_h = f_s_w / f_h_w
    w_to_h = f_w_w / f_h_w
    w_to_s = f_w_w / f_s_w
    f_aspect = (f_bbox[2] - f_bbox[0]) / (f_bbox[3] - f_bbox[1])

    s_sy = int(max(s_kp[5][1], s_kp[6][1]))
    s_hy = int(max(s_kp[11][1], s_kp[12][1]))
    s_torso = max(s_hy - s_sy, 1)

    s_chest_d = get_w(s_mask, s_sy + int(s_torso * 0.25))
    s_belly_d = get_w(s_mask, s_sy + int(s_torso * 0.65))
    side_fat_ratio = s_belly_d / s_chest_d

    body_type = ""
    if gender == 'M':
        if f_aspect < 0.38:
            body_type = "The Lean Column"
        elif (side_fat_ratio > 1.02 or f_aspect > 0.48) and w_to_h > 0.90:
            body_type = "The Apple"
        elif side_fat_ratio < 0.98 and s_to_h > 1.18 and w_to_s < 0.80:
            body_type = "The Inverted Triangle"
        elif s_to_h < 0.98 and w_to_h < 0.95:
            body_type = "The Pear"
        else:
            if f_w_w / ((f_s_w + f_h_w) / 2) < 0.85:
                body_type = "The Hour Glass"
            else:
                body_type = "The Rectangle"
    elif gender == 'F':
        if (side_fat_ratio > 1.02 or f_aspect > 0.45) and w_to_h > 0.92:
            body_type = "The Apple"
        elif f_w_w / ((f_s_w + f_h_w) / 2) < 0.78 and abs(f_s_w - f_h_w) < (f_h_w * 0.1):
            body_type = "The Hour Glass"
        elif s_to_h < 0.92:
            body_type = "The Pear"
        elif s_to_h > 1.08:
            body_type = "The Inverted Triangle"
        else:
            if f_aspect < 0.38:
                body_type = "The Lean Column"
            else:
                body_type = "The Rectangle"

    return {
        "gender": gender,
        "body_type": body_type,
        "metrics": {
            "f_aspect": float(f_aspect),
            "side_fat_ratio": float(side_fat_ratio)
        }
    }
