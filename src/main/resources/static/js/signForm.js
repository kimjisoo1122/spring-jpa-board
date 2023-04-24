
// 비밀번호 유효성 검증
const pwdElement = document.querySelector('input[name="password"]');
pwdElement.addEventListener('blur', () => {
	const pwdValue = pwdElement.value;
	const errorMessage = validPassword(pwdValue);
	const pwdErrElement = document.querySelector('.signform-error-password');
	pwdErrElement.innerText = errorMessage;
	if (errorMessage) {
		pwdErrElement.classList.add('form-error');
		pwdErrElement.style.display = 'block';
	} else {
		pwdErrElement.classList.remove('form-error');
		pwdErrElement.style.display = 'block';
	}
})
function validPassword(password) {
	if (password.length < 8) {
		return '비밀번호는 8자 이상이어야 합니다.';
	}
	if (password.indexOf(' ') >= 0) {
		return '비밀번호에 공백을 사용할 수 없습니다.';
	}
	if (password.search(/[0-9]/) < 0) {
		return '비밀번호는 숫자를 포함해야 합니다.';
	}
	if (password.search(/[a-zA-Z]/) < 0) {
		return '비밀번호는 영문자를 포함해야 합니다.';
	}
	return '';
}

// 이메일 유효성 검증
const emailElement = document.querySelector('input[name="email"]');
const emailErrorElement = document.querySelector('.signform-error-email');

emailElement.addEventListener('blur', () => {
	const emailValue = emailElement.value;
	const errorMessage = validEmail(emailValue);
	emailErrorElement.innerText = errorMessage;
	if (errorMessage) {
		emailErrorElement.style.display = 'block';
		emailErrorElement.classList.add('form-error');
	} else {
		emailErrorElement.style.display = 'none';
		emailErrorElement.classList.remove('form-error');
	}
});

function validEmail(email) {
	if (!email) {
		return '이메일 주소를 입력하세요.';
	}
	if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
		return '유효한 이메일 주소가 아닙니다.';
	}
	return '';
}

// 휴대폰번호 유효성 검증
const phElement = document.querySelector('input[name="phone"]');
phElement.addEventListener('blur', () => {
	const phoneValue = phElement.value;

	const phErrElement = document.querySelector('.signform-error-phone');
	phErrElement.innerText = validPhone(phoneValue)
	if (validPhone(phoneValue)) {
		phErrElement.style.display = 'block';
		phErrElement.classList.add('form-error');
	} else {
		phErrElement.style.display = 'none';
		phErrElement.classList.remove('form-error');
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
			&& validPassword(passwordElement.value)
			&& validEmail(emailElement.value));
}

