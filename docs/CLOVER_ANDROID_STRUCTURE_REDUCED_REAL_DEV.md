

## 최종 파일트리

```text
CLO-VER-Android
│
├── 📄 settings.gradle.kts                         (프로젝트 모듈 포함 설정)
├── 📄 build.gradle.kts                            (프로젝트 공통 Gradle 설정)
├── 📄 gradle.properties                           (Gradle 빌드 옵션 / AndroidX 설정)
├── 📄 local.properties                            (로컬 Android SDK 경로 / Git 업로드 제외)
├── 📄 README.md                                   (프로젝트 실행 방법 / 개발 규칙 정리)
│
└── 📂 app                                         (안드로이드 앱 메인 모듈)
    ├── 📄 build.gradle.kts                        (앱 모듈 의존성 / Kotlin / Compose 설정)
    ├── 📄 proguard-rules.pro                      (릴리즈 빌드 난독화 규칙)
    │
    └── 📂 src                                     (앱 소스 코드 루트)
        ├── 📂 main                                (실제 앱 실행 코드 영역)
        │   ├── 📄 AndroidManifest.xml             (앱 권한 / Activity / Application 등록)
        │   │
        │   ├── 📂 java 또는 kotlin                (Kotlin 소스 코드 영역)
        │   │   └── 📂 com
        │   │       └── 📂 fitflow
        │   │           └── 📂 clover              (CLO-VER 앱 기본 패키지)
        │   │               ├── 📄 CloverApplication.kt              (앱 전역 Application 클래스 / Hilt 사용 시)
        │   │               ├── 📄 MainActivity.kt                    (앱 진입 Activity / Compose setContent 실행)
        │   │               │
        │   │               ├── 📂 core                              (앱 전체 공통 기반 코드)
        │   │               │   ├── 📂 network                       (서버 통신 공통 설정)
        │   │               │   │   ├── 📄 ApiResult.kt              (API 성공 / 실패 결과 래핑)
        │   │               │   │   ├── 📄 NetworkConstants.kt       (Base URL / timeout 상수)
        │   │               │   │   ├── 📄 AuthInterceptor.kt        (요청 Header에 AccessToken 추가)
        │   │               │   │   ├── 📄 TokenAuthenticator.kt     (토큰 만료 시 재발급 처리)
        │   │               │   │   └── 📄 ErrorResponse.kt          (서버 공통 에러 응답 모델)
        │   │               │   │
        │   │               │   ├── 📂 theme                         (Compose 디자인 시스템)
        │   │               │   │   ├── 📄 Color.kt                  (CLO-VER 대표 색상 정의)
        │   │               │   │   ├── 📄 Type.kt                   (폰트 / 글자 크기 정의)
        │   │               │   │   ├── 📄 Shape.kt                  (버튼 / 카드 모서리 값 정의)
        │   │               │   │   └── 📄 Theme.kt                  (CloverTheme 구성)
        │   │               │   │
        │   │               │   ├── 📂 component                     (앱 전체 공통 UI 컴포넌트)
        │   │               │   │   ├── 📄 CloverButton.kt           (공통 기본 버튼 / 외곽선 버튼 포함 가능)
        │   │               │   │   ├── 📄 CloverTextField.kt        (공통 입력 필드)
        │   │               │   │   ├── 📄 CloverRadioButton.kt      (공통 라디오 버튼)
        │   │               │   │   ├── 📄 CloverTopBar.kt           (공통 상단 바)
        │   │               │   │   ├── 📄 CloverBottomNavigation.kt (공통 하단 네비게이션 바)
        │   │               │   │   ├── 📄 CloverDialog.kt           (공통 다이얼로그 / 에러 / 완료 안내 포함)
        │   │               │   │   └── 📄 CloverStateView.kt        (로딩 / 빈 화면 / 에러 화면 통합)
        │   │               │   │
        │   │               │   ├── 📂 navigation                    (앱 전체 화면 이동 관리)
        │   │               │   │   ├── 📄 ScreenRoute.kt            (전체 화면 라우트 정의)
        │   │               │   │   ├── 📄 CloverNavHost.kt          (NavHost 화면 연결)
        │   │               │   │   └── 📄 BottomNavItem.kt          (하단 탭 메뉴 정의)
        │   │               │   │
        │   │               │   ├── 📂 util                          (공통 유틸 함수)
        │   │               │   │   ├── 📄 DateFormatter.kt          (날짜 표시 변환)
        │   │               │   │   ├── 📄 ValidationUtil.kt         (이메일 / 비밀번호 / 닉네임 검증)
        │   │               │   │   ├── 📄 ImageUtil.kt              (이미지 선택 / 용량 검증 통합)
        │   │               │   │   └── 📄 UiEvent.kt                (Snackbar / Navigate 일회성 이벤트)
        │   │               │   │
        │   │               │   └── 📂 constant                      (앱 공통 상수)
        │   │               │       ├── 📄 AppConstants.kt           (앱 공통 상수)
        │   │               │       └── 📄 FeatureConstants.kt       (진단 / 상품 / 거래 상태 상수 통합)
        │   │               │
        │   │               ├── 📂 di                                (의존성 주입 모듈)
        │   │               │   ├── 📄 NetworkModule.kt              (Retrofit / OkHttp / API 제공)
        │   │               │   ├── 📄 RepositoryModule.kt           (Repository 구현체 바인딩)
        │   │               │   └── 📄 LocalModule.kt                (DataStore / Room 제공)
        │   │               │
        │   │               ├── 📂 data                              (서버 통신 / 로컬 저장소 계층)
        │   │               │   ├── 📂 remote                        (서버 API / DTO 영역)
        │   │               │   │   ├── 📂 api                       (Retrofit API 인터페이스)
        │   │               │   │   │   ├── 📄 AuthApi.kt             (로그인 / 회원가입 / 이메일 인증 API)
        │   │               │   │   │   ├── 📄 DiagnosisApi.kt        (체형 분석 / 퍼스널 컬러 진단 API)
        │   │               │   │   │   ├── 📄 ProductApi.kt          (상품 목록 / 상세 / 등록 / 수정 / 삭제 API)
        │   │               │   │   │   ├── 📄 TradeApi.kt            (거래 요청 / 찜 / 신고 API 통합)
        │   │               │   │   │   ├── 📄 CommunityApi.kt        (커뮤니티 글 / 댓글 API)
        │   │               │   │   │   ├── 📄 ChatApi.kt             (채팅방 / 메시지 API)
        │   │               │   │   │   └── 📄 UserApi.kt             (마이페이지 / 프로필 / 알림 API 통합)
        │   │               │   │   │
        │   │               │   │   └── 📂 dto                       (서버 요청 / 응답 DTO)
        │   │               │   │       ├── 📄 AuthDto.kt             (로그인 / 회원가입 / 토큰 / 이메일 인증 DTO 통합)
        │   │               │   │       ├── 📄 DiagnosisDto.kt        (체형 분석 / 퍼스널 컬러 진단 DTO 통합)
        │   │               │   │       ├── 📄 ProductDto.kt          (상품 등록 / 수정 / 목록 / 상세 / 이미지 DTO 통합)
        │   │               │   │       ├── 📄 TradeDto.kt            (거래 / 찜 / 신고 DTO 통합)
        │   │               │   │       ├── 📄 CommunityDto.kt        (게시글 / 댓글 / 해시태그 DTO 통합)
        │   │               │   │       ├── 📄 ChatDto.kt             (채팅방 / 메시지 DTO 통합)
        │   │               │   │       └── 📄 UserDto.kt             (프로필 / 알림 / 보안 설정 DTO 통합)
        │   │               │   │
        │   │               │   ├── 📂 local                         (로컬 저장소 영역)
        │   │               │   │   ├── 📄 TokenDataStore.kt          (AccessToken / RefreshToken 저장)
        │   │               │   │   ├── 📄 UserPreferenceDataStore.kt (온보딩 여부 / 사용자 설정 저장)
        │   │               │   │   └── 📄 CloverDatabase.kt          (Room DB 메인 클래스 / 필요 시 DAO 포함)
        │   │               │   │
        │   │               │   └── 📂 repository                    (Repository 구현체)
        │   │               │       ├── 📄 AuthRepositoryImpl.kt      (인증 API / 토큰 저장소 연결)
        │   │               │       ├── 📄 DiagnosisRepositoryImpl.kt (진단 API / 결과 계산 연결)
        │   │               │       ├── 📄 ProductRepositoryImpl.kt   (상품 API / 이미지 업로드 연결)
        │   │               │       ├── 📄 TradeRepositoryImpl.kt     (거래 / 찜 / 신고 기능 연결)
        │   │               │       ├── 📄 CommunityRepositoryImpl.kt (커뮤니티 / 댓글 기능 연결)
        │   │               │       ├── 📄 ChatRepositoryImpl.kt      (채팅방 / 메시지 기능 연결)
        │   │               │       └── 📄 UserRepositoryImpl.kt      (마이페이지 / 프로필 / 알림 기능 연결)
        │   │               │
        │   │               ├── 📂 domain                            (앱 내부 모델 / 핵심 로직 계층)
        │   │               │   ├── 📂 model                         (앱 내부 데이터 모델)
        │   │               │   │   ├── 📄 AuthModel.kt               (회원 / 토큰 / 보안 방식 모델 통합)
        │   │               │   │   ├── 📄 DiagnosisModel.kt          (체형 분석 / 퍼스널 컬러 질문·답변·결과 모델 통합)
        │   │               │   │   ├── 📄 ProductModel.kt            (상품 / 이미지 / 필터 / 상태 / 사이즈 모델 통합)
        │   │               │   │   ├── 📄 TradeModel.kt              (거래 / 찜 / 신고 관련 모델 통합)
        │   │               │   │   ├── 📄 CommunityModel.kt          (게시글 / 댓글 / 해시태그 모델 통합)
        │   │               │   │   ├── 📄 ChatModel.kt               (채팅방 / 채팅 메시지 모델 통합)
        │   │               │   │   └── 📄 UserModel.kt               (프로필 / 알림 / 사용자 설정 모델 통합)
        │   │               │   │
        │   │               │   ├── 📂 repository                    (Repository 인터페이스)
        │   │               │   │   ├── 📄 AuthRepository.kt          (인증 관련 데이터 접근 규칙)
        │   │               │   │   ├── 📄 DiagnosisRepository.kt     (진단 관련 데이터 접근 규칙)
        │   │               │   │   ├── 📄 ProductRepository.kt       (상품 관련 데이터 접근 규칙)
        │   │               │   │   ├── 📄 TradeRepository.kt         (거래 / 찜 / 신고 데이터 접근 규칙)
        │   │               │   │   ├── 📄 CommunityRepository.kt     (커뮤니티 데이터 접근 규칙)
        │   │               │   │   ├── 📄 ChatRepository.kt          (채팅 데이터 접근 규칙)
        │   │               │   │   └── 📄 UserRepository.kt          (마이페이지 / 프로필 / 알림 데이터 접근 규칙)
        │   │               │   │
        │   │               │   └── 📂 usecase                       (복잡한 기능 로직만 별도 분리)
        │   │               │       ├── 📄 AuthUseCase.kt            (로그인 / 회원가입 / 로그아웃 흐름 통합)
        │   │               │       ├── 📄 DiagnosisUseCase.kt       (체형 분석 / 퍼스널 컬러 계산 로직 통합)
        │   │               │       ├── 📄 ProductUseCase.kt         (상품 등록 / 수정 / 검색 / 가격 비교 로직 통합)
        │   │               │       └── 📄 UserUseCase.kt            (프로필 수정 / 회원 탈퇴 / 설정 변경 로직 통합)
        │   │               │
        │   │               └── 📂 presentation                       (화면 / ViewModel / UI 상태 계층)
        │   │                   ├── 📂 splash                          (앱 시작 화면)
        │   │                   │   ├── 📄 SplashScreen.kt              (앱 실행 직후 보여주는 시작 화면)
        │   │                   │   └── 📄 SplashViewModel.kt           (자동 로그인 / 온보딩 분기 상태 관리)
        │   │                   │
        │   │                   ├── 📂 main                            (메인 홈 / 하단 네비게이션)
        │   │                   │   ├── 📄 MainScreen.kt                (메인 Scaffold / 하단 네비게이션 구성)
        │   │                   │   ├── 📄 MainViewModel.kt             (탭 상태 / 홈 화면 상태 관리)
        │   │                   │   └── 📄 HomeScreen.kt                (앱 홈 화면 UI)
        │   │                   │
        │   │                   ├── 📂 auth                            (로그인 / 회원가입 / 인증 화면)
        │   │                   │   ├── 📄 AuthScreen.kt                (로그인 / 회원가입 / 이메일 인증 화면 통합)
        │   │                   │   ├── 📄 AuthViewModel.kt             (인증 입력값 / 검증 / 요청 상태 관리)
        │   │                   │   └── 📄 AuthUiState.kt               (인증 화면 상태 통합)
        │   │                   │
        │   │                   ├── 📂 diagnosis                       (체형 분석 / 퍼스널 컬러 진단 화면)
        │   │                   │   ├── 📄 BodyAnalysisScreen.kt        (체형 분석 Intro / Question / Result 화면 통합)
        │   │                   │   ├── 📄 PersonalColorScreen.kt       (퍼스널 컬러 Intro / Question / Result / Retry 화면 통합)
        │   │                   │   ├── 📄 DiagnosisViewModel.kt        (체형 분석 / 퍼스널 컬러 선택값과 결과 상태 관리)
        │   │                   │   └── 📄 DiagnosisUiState.kt          (진단 화면 상태 통합)
        │   │                   │
        │   │                   ├── 📂 product                         (상품 목록 / 상세 / 등록 / 수정 화면)
        │   │                   │   ├── 📄 ProductListScreen.kt         (상품 목록 화면)
        │   │                   │   ├── 📄 ProductDetailScreen.kt       (상품 상세 화면)
        │   │                   │   ├── 📄 ProductEditScreen.kt         (상품 등록 / 수정 화면 통합)
        │   │                   │   ├── 📄 ProductComponent.kt          (상품 카드 / 이미지 영역 / 필터 바텀시트 통합)
        │   │                   │   ├── 📄 ProductViewModel.kt          (상품 목록 / 상세 / 등록 / 수정 상태 관리)
        │   │                   │   └── 📄 ProductUiState.kt            (상품 화면 상태 통합)
        │   │                   │
        │   │                   ├── 📂 trade                           (거래 / 찜 / 신고 화면)
        │   │                   │   ├── 📄 TradeScreen.kt               (거래 요청 / 거래 상태 화면 통합)
        │   │                   │   ├── 📄 WishlistScreen.kt            (찜 목록 화면)
        │   │                   │   ├── 📄 ReportComponent.kt           (신고 바텀시트 / 신고 다이얼로그 통합)
        │   │                   │   ├── 📄 TradeViewModel.kt            (거래 / 찜 / 신고 상태 관리)
        │   │                   │   └── 📄 TradeUiState.kt              (거래 관련 화면 상태 통합)
        │   │                   │
        │   │                   ├── 📂 community                       (커뮤니티 화면)
        │   │                   │   ├── 📄 CommunityScreen.kt           (게시글 목록 / 상세 / 작성 / 수정 화면 통합)
        │   │                   │   ├── 📄 CommunityComponent.kt        (댓글 아이템 / 게시글 카드 통합)
        │   │                   │   ├── 📄 CommunityViewModel.kt        (게시글 / 댓글 상태 관리)
        │   │                   │   └── 📄 CommunityUiState.kt          (커뮤니티 화면 상태 통합)
        │   │                   │
        │   │                   ├── 📂 chat                            (채팅 화면)
        │   │                   │   ├── 📄 ChatScreen.kt                (채팅방 목록 / 메시지 화면 통합)
        │   │                   │   ├── 📄 ChatComponent.kt             (채팅 메시지 아이템 UI)
        │   │                   │   ├── 📄 ChatViewModel.kt             (채팅방 / 메시지 상태 관리)
        │   │                   │   └── 📄 ChatUiState.kt               (채팅 화면 상태 통합)
        │   │                   │
        │   │                   └── 📂 mypage                          (마이페이지 화면)
        │   │                       ├── 📄 MyPageScreen.kt              (마이페이지 / 프로필 수정 / 보안 설정 화면 통합)
        │   │                       ├── 📄 MyActivityScreen.kt          (내 진단 결과 / 내 상품 / 알림 화면 통합)
        │   │                       ├── 📄 MyPageViewModel.kt           (프로필 / 알림 / 내 활동 상태 관리)
        │   │                       └── 📄 MyPageUiState.kt             (마이페이지 화면 상태 통합)
        │   │
        │   └── 📂 res                                  (앱 리소스 저장 영역)
        │       ├── 📂 drawable                         (이미지 / 벡터 아이콘 / 배경 shape XML)
        │       ├── 📂 mipmap-hdpi                      (hdpi 앱 아이콘)
        │       ├── 📂 mipmap-mdpi                      (mdpi 앱 아이콘)
        │       ├── 📂 mipmap-xhdpi                     (xhdpi 앱 아이콘)
        │       ├── 📂 mipmap-xxhdpi                    (xxhdpi 앱 아이콘)
        │       ├── 📂 mipmap-xxxhdpi                   (xxxhdpi 앱 아이콘)
        │       ├── 📂 values                           (colors.xml / strings.xml / themes.xml)
        │       └── 📂 xml                              (backup_rules.xml / data_extraction_rules.xml / network_security_config.xml)
        │
        ├── 📂 test                                    (단위 테스트 코드)
        │   └── 📂 java 또는 kotlin
        │       └── 📂 com.fitflow.clover
        │           └── 📄 ExampleUnitTest.kt           (로컬 단위 테스트 예시 파일)
        │
        └── 📂 androidTest                             (안드로이드 UI 테스트 코드)
            └── 📂 java 또는 kotlin
                └── 📂 com.fitflow.clover
                    └── 📄 ExampleInstrumentedTest.kt   (안드로이드 기기/에뮬레이터 UI 테스트 예시 파일)
```

