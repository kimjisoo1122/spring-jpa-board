const boardId = document.querySelector('#board-hidden-id').value;
const recommendationStatus = document.querySelector('#board-hidden-recommend').value;

createFormatDate(document.querySelector('.board-user-createDate'), 'data-createDate')
updateRecommend(recommendationStatus);
getReplies();

function updateRecommend(recommendationStatus) {
	if (recommendationStatus === 'UP_VOTED') {
		document.querySelector('.board-recommend-add').style.color = 'lightskyblue';
	} else if (recommendationStatus === 'DOWN_VOTED') {
		document.querySelector('.board-recommend-remove').style.color = 'tomato';
	} else {
		const recommendBtns = document.querySelectorAll('.board-recommend button');
		recommendBtns.forEach(btn => btn.style.color = 'lightgray');
	}
}

function createFormatDate(element, attributeName) {
	const now = new Date();
	const createDateElement = element
	const diff = now - new Date(createDateElement.getAttribute(attributeName));
	const diffMinutes = Math.floor(diff / (1000 * 60));
	let diffMessage = ''
	if (diffMinutes < 60) {
		diffMessage = diffMinutes + '분 전';
	} else if (diffMinutes < 60 * 24) {
		diffMessage = Math.floor(diffMinutes / 60) + '시간 전';
	} else {
		diffMessage = Math.floor(diffMinutes / (60 * 24)) + '일 전';
	}
	createDateElement.innerHTML = createDateElement.innerHTML + '  ' + diffMessage;
}

function getReplies() {
	axios.get(`/reply/${boardId}`)
			.then(res => {
				const replies = res.data.map(reply => {
					return `
							 <div class="board-reply">
                <div class="board-reply-header">
                  <div class="board-reply-user">
                    <div class="board-reply-name">${reply.memberName}</div>
                    <div class="board-reply-date" data-reply-date="${reply.createDate}"></div>
                  </div>
                  <div class="board-reply-recommend">
                    <button class="board-reply-recommend-remove">&lt;</button>
                    <p class="board-reply-recommend-cnt">${reply.recommendCnt}</p>
                    <button class="board-reply-recommend-add">&gt;</button>
                  </div>
                </div>
                <div class="board-reply-content">${reply.content}</div>
              </div>
							`
				}).join('');
				document.querySelector('.board-reply-container').innerHTML = replies;
				document.querySelectorAll('.board-reply-date').forEach(e => createFormatDate(e, 'data-reply-date'))
			})
			.catch(err => {
				console.log(err);
			});
}

function removeRecommendation() {
	axios.post(`/board/recommend/remove/${boardId}`)
			.then(res => {
				document.querySelector('.board-recommend-cnt').innerHTML = res.data.recommendCnt;
				updateRecommend(res.data.recommendationStatus);
			})
			.catch(err => {
				console.log(err);
			})
}

function addRecommendation() {
	axios.post(`/board/recommend/add/${boardId}`)
			.then(res => {
				document.querySelector('.board-recommend-cnt').innerHTML = res.data.recommendCnt;
				updateRecommend(res.data.recommendationStatus);
			})
			.catch(err => {
				console.log(err);
			});
}

function registerReply() {
	const replyDTO = {
		boardId: boardId,
		content: document.querySelector('.board-reply-register-text').value
	}
	axios.post('/reply/', replyDTO)
			.then(res => {
				getReplies();
				document.querySelector('.board-reply-register-text').value = '';
			})
			.catch(err => {
				console.log(err)
			})
}