function validEmail(email) {
	if (!email) {
		return '이메일 주소를 입력하세요.';
	}
	if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
		return '유효한 이메일 주소가 아닙니다.';
	}
	return '';
}

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