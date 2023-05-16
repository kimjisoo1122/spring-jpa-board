const now = new Date();
const createDateElements = document.querySelectorAll('.board-user-createDate');
createDateElements.forEach(e => {
	const createDate = new Date(e.getAttribute('data-createDate'));
	const diff = now - createDate;
	const diffMinutes = Math.floor(diff / (1000 * 60));
	let diffMessage = ''
	if (diffMinutes < 60) {
		if (diffMinutes == 0) {
			diffMessage = '방금 전'
		} else {
			diffMessage = diffMinutes + '분 전';
		}
	} else if (diffMinutes < 60 * 24) {
		diffMessage = Math.floor(diffMinutes / 60) + '시간 전';
	} else {
		diffMessage = Math.floor(diffMinutes / (60 * 24)) + '일 전';
	}
	e.innerHTML = e.innerHTML + '  ' + diffMessage;
})