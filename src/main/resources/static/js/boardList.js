const now = new Date();
const createDateElements = document.querySelectorAll('.board-user-createDate');
createDateElements.forEach(e => {
	const createDate = new Date(e.getAttribute('data-createDate'));
	const diff = now - createDate;
	const diffMinutes = Math.floor(diff / (1000 * 60));
	let diffMessage = ''
	if (diffMinutes < 60) {
		if (diffMinutes === 0) {
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

function viewUpdateBox(element) {
	const updateBoxElement = element.previousElementSibling;
	updateBoxElement.classList.toggle('flex-on');
}

function deleteBoard(element) {
	if (confirm("삭제하시겠습니까?")) {
		const parentElement = element.parentElement;
		const page = parentElement.getAttribute('data-page');
		const size = parentElement.getAttribute('data-size');
		const boardId = parentElement.getAttribute('data-id');
		axios.delete(`/board/${boardId}`, {
					params: {
						page: page,
						size: size
					}})
					.then(res => {
						location.href = `/board?page=${page}&size=${size}`;
					})
					.catch(err => {
						console.log(err);
					})
	}
}

function updateBoard(element) {

	if (confirm("수정하시겠습니까?")) {
		const parentElement = element.parentElement;
		const page = parentElement.getAttribute('data-page');
		const size = parentElement.getAttribute('data-size');
		const boardId = parentElement.getAttribute('data-id');

		location.href = `/board/update/${boardId}?&page=${page}&size=${size}`;
	}
}
