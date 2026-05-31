function validateForm() {
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('passwordConfirm').value;

    const passwordError = document.getElementById('passwordError');
    const passwordMatchError = document.getElementById('passwordMatchError');

    passwordError.style.display = 'none';
    passwordMatchError.style.display = 'none';

    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    if (!passwordRegex.test(password)) {
        passwordError.style.display = 'block';
        document.getElementById('password').focus();
        return false;
    }

    if (password !== passwordConfirm) {
        passwordMatchError.style.display = 'block';
        document.getElementById('passwordConfirm').focus();
        return false;
    }

    return true;
}