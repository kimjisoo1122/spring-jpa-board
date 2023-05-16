const textAreaElement = document.querySelector('textarea');
textAreaElement.addEventListener('input', () => {
	textAreaElement.style.height = 'auto';
	textAreaElement.style.height = `${textAreaElement.scrollHeight}px`;
})