function sendRequest(url, method) {
	const baserdUrl = 'http://localhost:8080'
	console.log(baserdUrl + url);
	// AJAX 통신 수행
	const xhr = new XMLHttpRequest();
	xhr.open(method, baserdUrl + url);
	xhr.setRequestHeader('Authentication', 'test');
	xhr.onreadystatechange = function() {
		if (xhr.readyState === XMLHttpRequest.DONE) {
			if (xhr.status === 200) {
				console.log(xhr.responseText);
			} else {
				console.error(xhr.statusText);
			}
		}
	};
	xhr.send();
}