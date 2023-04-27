
// 비밀번호 유효성 검증
const pwdElement = document.querySelector('input[name="password"]');
pwdElement.addEventListener('blur', () => {
	const pwdValue = pwdElement.value;
	const errorMessage = validPassword(pwdValue);
	const pwdErrElement = document.querySelector('.signform-error-password');
	pwdErrElement.innerText = errorMessage;
	if (errorMessage) {
		pwdErrElement.style.display = 'block';
	} else {
		pwdErrElement.style.display = 'block';
	}
})


// 이메일 유효성 검증
const emailElement = document.querySelector('input[name="email"]');

emailElement.addEventListener('blur', () => {
	const emailValue = emailElement.value;
	const errorMessage = validEmail(emailValue);
	const emailErrElement = document.querySelector('.signform-error-email');
	emailErrElement.innerText = errorMessage;
	if (errorMessage) {
		emailErrElement.style.display = 'block';
	} else {
		emailErrElement.style.display = 'none';
	}
});

// 휴대폰번호 유효성 검증
const phElement = document.querySelector('input[name="phone"]');
phElement.addEventListener('blur', () => {
	const phoneValue = phElement.value;
	const phErrElement = document.querySelector('.signform-error-phone');
	phErrElement.innerText = validPhone(phoneValue)
	if (validPhone(phoneValue)) {
		phErrElement.style.display = 'block';
	} else {
		phErrElement.style.display = 'none';
	}
})
function validPhone(phoneNumber) {
	const phoneRegExp = /^01([0|1|6|7|8|9]*)-(\d{3}|\d{4})-(\d{4})$/;
	return phoneRegExp.test(phoneNumber) ? '' : '유효하지 않은 휴대폰번호 입니다.';
}

// form 제출 유효성 검증
function validForm() {
	const nameElement = document.querySelector('input[name="name"]');
	const passwordElement = document.querySelector('input[name="password"]');
	const emailElement = document.querySelector('input[name="email"]');
	const phoneElement = document.querySelector('input[name="phone"]');

	if (!nameElement.value) {
		alert('이름을 입력해주세요.');
		return false;
	}
	if (!passwordElement.value) {
		alert('비밀번호를 입력해주세요.');
		return false;
	}
	if (!emailElement.value) {
		alert('이메일을 입력해주세요.');
		return false;
	}
	if (!phoneElement.value) {
		alert('휴대폰 번호를 입력해주세요.');
		return false;
	}
	return !(validPhone(phoneElement.value)
			|| validPassword(passwordElement.value)
			|| validEmail(emailElement.value));
}

