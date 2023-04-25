// 이메일 유효성 검증
const emailElement = document.querySelector('input[name="email"]');
const emailErrorElement = document.querySelector('.loginform-error')
emailElement.addEventListener('blur', () => {
	const emailValue = emailElement.value;
	const errorMessage = validEmail(emailValue);
	emailErrorElement.innerText = errorMessage;
	if (errorMessage) {
		emailErrorElement.style.display = 'block';
	} else {
		emailErrorElement.style.display = 'none';
	}
});

// 비밀번호 유효성 검증
const pwdElement = document.querySelector('input[name="password"]');
pwdElement.addEventListener('blur', () => {
	const pwdValue = pwdElement.value;
	const errorMessage = validPassword(pwdValue);
	const pwdErrElement = document.querySelector('.loginform-error-password');
	pwdErrElement.innerText = errorMessage;
	if (errorMessage) {
		pwdErrElement.style.display = 'block';
	} else {
		pwdErrElement.style.display = 'block';
	}
})

function validForm() {
	const passwordElement = document.querySelector('input[name="password"]');
	const emailElement = document.querySelector('input[name="email"]');

	if (!emailElement.value) {
		alert('이메일을 입력해주세요.');
		return false;
	}
	if (!passwordElement.value) {
		alert('비밀번호를 입력해주세요.');
		return false;
	}
	return !(validPassword(passwordElement.value)
			&& validEmail(emailElement.value));
}