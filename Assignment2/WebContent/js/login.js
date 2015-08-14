function onLogin(event) {
	event.preventDefault();
	event.stopPropagation();

	// check input
	var clubName = $('#club').val();
	var userId = $('#id').val();
	var password = $('#password').val();
	var authCode = $('#authCode').val();
	var valid = true;
	if (clubName === null || clubName.trim() === '') {
		checkVal.apply($('#club'));
		valid = false;
	}
	if (userId === null || userId.trim() === '') {
		checkVal.apply($('#id'));
		valid = false;
	}
	if (password === null || password.trim() === '') {
		checkVal.apply($('#password'));
		valid = false;
	}
	if (!valid) {
		$('#msg').text('Input illegal.');
		return false;
	}

	if (!checkClubName(clubName)) {
		$('#club').val('').addClass('alert-warning').focus();
		$('#msg').text('Club name can\'t starts with digits');
		return false;
	}

	// display connecting
	$('#loginButton').val('Log in...').attr('disabled', 'disabled');
	clearMsg();

	// //////////////////////////////////////
	// var msg = '{"msg":"Login: Does not have this id.","success":-1}';
	// var message = parseMsg($.parseJSON(msg)['msg']);
	// $('#msg').text(message);
	// /////////////////////////////////////

	jQuery.ajax({
		type : 'POST',
		url : 'login.do',
		timeout : 30 * 1000,
		data : {
			'club' : clubName,
			'id' : userId,
			'password' : passwordHash(password),
			'authCode' : authCode
		},
		dataType : 'text',
		success : function(data) {
			var msg = $.parseJSON(data);
			if (msg['success'] === 0) {
				$.cookie('userId', userId);
				$.cookie('club', clubName);
				window.location = 'main.html';
			}
			$('#msg').text(parseMsg(msg['msg']));
		},
		error : function() {
			$('#msg').text('Internet unavailable');
		},
		complete : function() {
			changeAuth();
			$('#loginButton').removeAttr('disabled').val('Log in');
		}
	});
};

function init() {
	var inputText = $('input[type="text"],input[type="password"]');
	$.each(inputText, function(n, value) {
		$(value).blur(checkVal);
		$(value).bind('keypress', checkKeyUp);
	});
	$('#loginButton').click(onLogin);
	$('#createButton').click(onCreate);

	// ///////////////////////////////////////////
	$.each($('.switcher'), function(n, value) {
		console.log('switcher');
		$(value).click(onSwitch);
	});

	$.each($('img'), function(n, value) {
		$(value).click(function(event) {
			var target = event.target;
			var src = $(target).attr('src').split('?')[0];
			console.log(src);
			src = src + '?t=' + Math.random();
			$(target).attr('src', src);
		});
	});

	// changeAuth();
	$('#authCode,#cAuthCode').focus(function() {
		console.log('focus');
		changeAuth();
	});
}

function changeAuth() {
	console.log('change auth');
	$.each($('img'), function(n, value) {
		var src = $(value).attr('src').split('?')[0];
		console.log(src);
		src = src + '?t=' + Math.random();
		$(value).attr('src', src);
	});
}

function onSwitch() {
	var interval = 812;
	$('#logindiv').slideToggle({
		'duration' : interval,
		'complete' : function() {
			$('#showLogin').toggle('fast');
		}
	});
	$('#creatediv').slideToggle({
		'duration' : interval,
		'complete' : function() {
			$('#showCreate').toggle('fast');
		}
	});
}

function checkVal() {
	var text = $.trim($(this).val());
	$(this).val(text);
	var id = $(this).attr('id');
	var warning;
	if (id === 'club')
		warning = 'Please input club name';
	if (id === 'id')
		warning = 'Please input member ID';
	if (id === 'password')
		warning = 'Please input password';
	if (id === 'cclub')
		warning = 'Please input club name';
	if (id === 'password1')
		warning = 'Please input password';
	if (id === 'password2')
		warning = 'Please confirm password';
	if (text === null || text.trim() === '') {
		$(this).addClass('alert-warning');
		$(this).addClass('font-warning');
		$(this).attr('placeholder', warning);
	}
}

function checkKeyUp() {
	$(this).removeClass('alert-warning');
	clearMsg();
}

function clearMsg() {
	$('#msg').text('');
	$('#cmsg').text('');
}

function checkClubName(clubName) {
	var pattern = /^\d/;
	return !pattern.test(clubName);
}