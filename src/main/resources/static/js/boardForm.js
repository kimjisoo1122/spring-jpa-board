const textAreaElement = document.querySelector('textarea');
textAreaElement.addEventListener('input', () => {
	textAreaElement.style.height = 'auto';
	textAreaElement.style.height = `${textAreaElement.scrollHeight - 20}px`;
})

function cancel() {
	const page = document.querySelector('input[name="page"]').value;
	const size = document.querySelector('input[name="size"]').value;
	location.href = `/board?page=${page}&size=${size}`;
}