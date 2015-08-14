function onCreate(event) {
	event.preventDefault();
	event.stopPropagation();

	$('#cmsg').html('click');
	// check input
	var clubName = $('#cclub').val();
	var password1 = $('#password1').val();
	var password2 = $('#password2').val();
	var authCode = $('#cAuthCode').val();
	if (!checkPassword(password1, password2)) {
		return false;
	}
	var valid = true;
	if (clubName === null || clubName.trim() === '') {
		checkVal.apply($('#cclub'));
		valid = false;
	}
	if (password1 === null || password1.trim() === '') {
		checkVal.apply($('#password1'));
		valid = false;
	}
	if (password2 === null || password2.trim() === '') {
		checkVal.apply($('#password2'));
		valid = false;
	}
	if (!valid) {
		$('#cmsg').text('Input illegal.');
		return false;
	}

	if (!checkClubName(clubName)) {
		$('#cclub').val('').addClass('alert-warning').focus();
		$('#cmsg').text('Club name can not starts with digits');
		return false;
	}

	// display connecting
	$('#createButton').val('Creating...').attr('disabled', 'disabled');
	clearMsg();

	// //////////////////////////////////////
	// var msg = '{"msg":"Login: Does not have this id.","success":-1}';
	// var message = parseMsg($.parseJSON(msg)['msg']);
	// $('#cmsg').text(message);
	// /////////////////////////////////////

	jQuery.ajax({
		type : 'POST',
		url : 'createClub.do',
		timeout : 30 * 1000,
		data : {
			'name' : clubName,
			'password' : passwordHash(password1),
			'authCode' : authCode
		},
		dataType : 'text',
		success : function(data) {
			var msg = $.parseJSON(data);
			if (msg['success'] === 0) {
				alert('Create club success! Please log in.');
				onSwitch();
			}
			$('#cmsg').text(parseMsg(msg['msg']));
		},
		error : function() {
			$('#cmsg').text('Internet unavailable');
		},
		complete : function() {
			changeAuth();
			$('#createButton').removeAttr('disabled').val('Create');
		}
	});
	// var clubName = $('#club').val();
	// var password = $('#password').val();
	// $('#msg').html(clubName + password);
	// jQuery.ajax({
	// type : 'POST',
	// url : 'createClub.do',
	// data : {
	// 'name' : clubName,
	// 'password' : password
	// },
	// dataType : 'text',
	// success : function(data) {
	// $('#msg').html(data);
	// }
	// });
};

function checkPassword(password1, password2) {
	if (password1 !== password2) {
		$('#cmsg').text('Password does not match');
		$('#password1').addClass('alert-warning').focus();
		$('#password2').addClass('alert-warning');
		return false;
	}
	if (password1.toString().length < 6) {
		$('#cmsg').text('Password can not less than 6 characters');
		$('#password1').addClass('alert-warning');
		$('#password2').addClass('alert-warning');
		return false;
	}
	return true;
}