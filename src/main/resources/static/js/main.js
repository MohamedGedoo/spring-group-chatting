'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var chatEndPage = document.querySelector('#chat-end-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var password = null;

var colors = [ '#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107',
		'#ff85af', '#FF9800', '#39bbb0' ];

function connect(event) {
	username = document.querySelector('#name').value.trim();
	password = document.querySelector('#pass').value.trim();

	if (username && password) {

		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, onConnected, onError);
		usernamePage.classList.add('hidden');
		chatPage.classList.remove('hidden');
	}
	event.preventDefault();
}

function onConnected() {
	// Subscribe to the Public Topic
	stompClient.subscribe('/topic/public', onMessageReceived);

	// Tell your username to the server
	stompClient.send("/app/chat.addUser", {}, JSON.stringify({
		sender : username,
		password : password,
		type : 'JOIN'
	}))

	connectingElement.classList.add('hidden');
}

function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();
	if (messageContent && stompClient) {
		var chatInfo = {
			sender : username,
			content : messageInput.value,
			type : 'CHAT'
		};
		stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatInfo));
		messageInput.value = '';
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = null;
	var testPay = JSON.parse(payload.body);
	if (testPay.body) {
		message = testPay.body;
	} else {
		message = JSON.parse(payload.body);
	}

	var messageElement = document.createElement('li');

	if (message.type === 'JOIN') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' joined!';
	} else if (message.type === 'LEAVE') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' left!';
	} else {
		messageElement.classList.add('chat-message');

		var avatarElement = document.createElement('i');
		var avatarText = document.createTextNode(message.sender[0]);
		avatarElement.appendChild(avatarText);
		avatarElement.style['background-color'] = getAvatarColor(message.sender);

		messageElement.appendChild(avatarElement);

		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
	if (message.content.toLowerCase().includes("bye bye")) {
		chatPage.classList.add('hidden');
		chatEndPage.classList.remove('hidden');
	}
}

function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}
	var index = Math.abs(hash % colors.length);
	return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
