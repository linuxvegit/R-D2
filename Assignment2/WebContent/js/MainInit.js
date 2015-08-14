function initMain() {
	window.userId = ($.cookie('userId'));
	window.clubName = ($.cookie('club'));
	window.info = 'ID';
	window.maxZIndex = 0;

	checkUser();

	// init events
	$('#sSubmit').click(onSearch);
	$('#updateButton').click(updateShow);
	$('#submit').click(onSubmit);
	$('#psSubmit').click(onPsSubmit);
	$('#deleteButton').click(onDelete);
	$('#sortSubmit').click(onSort);
	$('#logoutButton').click(onLogout);
	$('#dismissButton').click(onDismiss);
	$('.panel-right-center').click(function(event) {
		$(this).css('z-index', ++window.maxZIndex);
	});
	$('#updatePassword').click(function(event) {
		console.log('update password');
		event.stopPropagation();
		$('#passwordPanel').show('normal').css('z-index', ++window.maxZIndex);
	});

	// var msg = $.parseJSON(result);
	// var data = msg['data'];
	// drawList(data, window.info);

	jQuery.ajax({
		type : 'POST',
		url : 'query.do',
		timeout : 30 * 1000,
		data : {
			'query' : '',
			'orders' : 'ID=asc'
		},
		dataType : 'text',
		success : function(data) {
			var result = new Result(data);
			if (result.operation == 'redirect')
				window.location = result.data;
			else if (result.operation == 'process') {
				var msg = (result.data);
				if (msg['success'] == '0')
					drawList(msg['data'], window.info);
				else
					alert(msg['msg'] + ' Please try again later.');
			}
		},
		error : function() {
			alert('Internet error! Please try again later.');
		}
	});
}

function checkUser() {
	console.log('check' + (!(window.userId + '')));
	if ((window.userId + '') == 'null' || (window.clubName + '') == 'null') {
		console.log('jump');
		window.location = 'login.html';
	}
	$('#profileId').text('ID:' + window.userId);

	$('#dismissButton').hide();
	if (window.userId != 1) {
		$('#addRow').hide();
		$('#deleteButton').hide();
		return;
	}
	$('.pin-right-top').bind('mouseover', function() {
		$('#dismissButton').slideDown('fast');
	});
	$('.pin-right-top').bind('mouseleave', function() {
		$('#dismissButton').slideUp('fast');
	});
}

function drawList(list, info, filter) {
	$('#memberList').empty();
	window.members = new Array();
	window.filtered = new Array();
	window.info = info;
	for ( var i = 0; i < list.length; i++) {
		if (!list[i])
			continue;
		if (list[i]['id'] == window.userId) {
			drawProfile(new Member(list[i], filter));
		} else {
			var member = new Member(list[i], filter);
			// console.log(member.toString());
			window.members.push(member);
			window.filter = filter || window.filter;
			console.log(window.filter);
			if (!window.filter || window.filter(member))
				drawMember(member);
		}
	}
}

function drawProfile(member) {
	window.profile = member;
	var values = [ 'Name', 'Gender', 'Age', 'Contact', 'Address', 'Addition' ];
	for ( var i in values) {
		var value = member[values[i].toLowerCase()];
		if (value === '') {
			member[values[i].toLowerCase()] = '';
			$('#profile' + values[i]).text('No information');
		} else
			$('#profile' + values[i]).text(value);
	}
}

function drawMember(member) {
	var info = window.info;

	var span = '<span>' + info + ':<br>'
			+ member[info.replace(/\s+/, '').toLowerCase()] + '</span>';
	var table = '<table><tr><td><img src="Face.gif"></td></tr><tr><td>' + span
			+ '</td></tr></table>';
	var div = $('<div class="member-card"></div>').click(memberClick).append(
			table).attr('userId', member['id']);

	$('#memberList').append(div);
}

function memberClick(event) {
	var target = event.target;
	while (!$(target).attr('userId')) {
		target = target.parentNode;
	}
	var userId = $(target).attr('userId');
	console.log(userId);
	showInfo(userId);
	return false;
}

function showInfo(userId) {
	var member = null;
	$.each(window.members, function(n, value) {
		if (value['id'] == userId) {
			member = value;
			return false;
		}
		return true;
	});
	if (member === null)
		return;
	var values = [ 'Id', 'Name', 'Gender', 'Birthday', 'Age', 'Contact',
			'Address', 'JoinDate', 'JoinDays', 'Addition' ];
	for ( var i in values) {
		// if (member[values[i].toLowerCase()] === '')
		// continue;
		$('#info' + values[i]).val(member[values[i].toLowerCase()]);
	}
	$('#infoPanel').show('normal').css('z-index', ++window.maxZIndex);
}

function onSearch(event) {
	console.log('search');
	event.preventDefault();
	event.stopPropagation();

	var selected = $('#searchInfo').val().toLowerCase();
	var selValue = $('#searchInfo option:selected').text();
	var from = $('#sFrom').val();
	var to = $('#sTo').val();

	function filter(member) {
		var value = member[selected];

		if (from && value < from)
			return false;
		if (to && value > to)
			return false;

		window.filtered.push(member);
		return true;

	}

	drawList(window.members, selValue, filter);
}

function onSubmit(event) {
	event.preventDefault();
	event.stopPropagation();
	var target = event.target;
	$(target).val('...').attr('disabled', 'disabled');
	var op = $(target).attr('op');
	if (op == 'add') {
		console.log('onAdd');
		var values = [ 'name', 'gender', 'birthday', 'contact', 'address',
				'addition' ];
		var shift = [ 0, 2, 3, 4, 5, 7 ];
		var permission = 2;
		var data = genAddData(values, shift, permission, 'writable');
		// for ( var p in data) {
		// if (!data.hasOwnProperty(p))
		// continue;
		// console.log('[' + p + '=' + data[p] + ']');
		// }
		data['id'] = window.userId;
		data['club'] = window.clubName;
		jQuery.ajax({
			type : 'POST',
			url : 'insert.do',
			timeout : 30 * 1000,
			data : data,
			dataType : 'text',
			success : function(data) {
				// console.log(data);
				var result = new Result(data);
				if (result.operation == 'redirect')
					window.location = result.data;
				else if (result.operation == 'process') {
					var msg = (result.data);
					if (msg['success'] == '0') {
						var member = new Member(msg['data']);
						window.members.push(member);
						if (!window.filter || window.filter(member))
							drawMember(member);
						alert('Add success!\r\nID: ' + member['id']
								+ '\r\nPassword: ' + member['password']);
					} else
						alert('Add member failure! Please try again later.');
				}
			},
			error : function() {
				alert('Add member failure! Please try again later.');
			},
			complete : function() {
				$(target).removeAttr('disabled').val('Submit');
			}
		});
	}

	if (op == 'update') {
		console.log('onUpdate');
		var values = [ 'name', 'gender', 'birthday', 'contact', 'address',
				'joinDate', 'addition' ];
		var shift = [ 0, 2, 3, 4, 5, 6, 7 ];
		var permission = 0;
		var data = genAddData(values, shift, permission, 'readable');
		data['password'] = window.profile['password'];
		// for ( var p in data) {
		// if (!data.hasOwnProperty(p))
		// continue;
		// console.log('[' + p + '=' + data[p] + ']');
		// }
		data['id'] = window.userId;
		data['club'] = window.clubName;
		jQuery
				.ajax({
					type : 'POST',
					url : 'update.do',
					timeout : 30 * 1000,
					data : data,
					dataType : 'text',
					success : function(data) {
						console.log(data);
						var result = new Result(data);
						if (result.operation == 'redirect')
							window.location = result.data;
						else if (result.operation == 'process') {
							var msg = (result.data);
							if (msg['success'] == '0') {
								var member = new Member(msg['data']);
								drawProfile(member);
								alert('Update information success!');
							} else
								alert('Update information failure. Please try again later.');
						}
					},
					error : function() {
						alert('Update information failure. Please try again later.');
					},
					complete : function() {
						$(target).removeAttr('disabled').val('Submit');
					}
				});
	}
}

function onDelete(event) {
	console.log('delete');
	event.preventDefault();
	event.stopPropagation();

	var id = $('#infoId').val();

	var conf = window.confirm('You sure want to delete this member?');
	if (!conf)
		return;
	conf = window.confirm('You REALLY sure want to dismiss this member ID: '
			+ id + '?\r\n' + 'The operation can not undo!');
	if (!conf)
		return;

	var target = event.target;
	$(target).html('...').attr('disabled', 'disabled');

	jQuery.ajax({
		type : 'POST',
		url : 'delete.do',
		timeout : 30 * 1000,
		data : {
			'delete' : id,
			'id' : window.userId,
			'club' : window.clubName
		},
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			var result = new Result(data);
			if (result.operation == 'redirect')
				window.location = result.data;
			else if (result.operation == 'process') {
				var msg = (result.data);
				if (msg['success'] == '0') {
					$('#infoPanel').hide();
					$.each(window.members, function(n, value) {
						if (value['id'] == id) {
							delete window.members[n];
						}
					});
					alert('Delete member success.');
					var filter = window.filter || function() {
						return true;
					};
					drawList(window.members, window.info, filter);
				} else
					alert('Delete member failed. Please try again later.');
			}
		},
		error : function() {
			alert('Delete member failure! Please try again later.');
		},
		complete : function() {
			$(target).removeAttr('disabled').html('Delete');
		}
	});

}

function onPsSubmit(event) {
	console.log('update password');
	event.preventDefault();
	event.stopPropagation();

	var old = passwordHash($('#oldPassword').val());
	var newPs = $('#newPassword').val();
	var confirm = $('#confirmPassword').val();

	if (old != window.profile['password']) {
		alert('Wrong password');
		return false;
	}
	if (newPs !== confirm) {
		alert('Different password');
		return false;
	}
	if (newPs.length < 6) {
		alert('Please input password equals or longer than 6 chars');
		return false;
	}
	var newPassword = passwordHash(newPs);

	var target = event.target;
	$(target).val('...').attr('disabled', 'disabled');

	jQuery.ajax({
		type : 'POST',
		url : 'password.do',
		timeout : 30 * 1000,
		data : {
			'club' : window.clubName,
			'id' : window.userId,
			'password' : newPassword
		},
		dataType : 'text',
		success : function(data) {
			var result = new Result(data);
			if (result.operation == 'redirect')
				window.location = result.data;
			else if (result.operation == 'process') {
				var msg = (result.data);
				if (msg['success'] == '0') {
					// console.log(newPassword);
					window.profile['password'] = newPassword;
					alert('Update password success.');
				} else {
					window.profile['password'] = old;
					alert('Update password failed. Please try again later.');
				}
			}
		},
		error : function() {
			alert('Update password failed! Please try again later.');
		},
		complete : function() {
			$(target).removeAttr('disabled').val('Submit');
		}
	});
}

function onSort(event) {
	console.log('sort');
	event.preventDefault();
	event.stopPropagation();

	var target = event.target;
	$(target).val('...').attr('disabled', 'disabled');

	var orders = '';
	$('.sort-selector').each(function(n, value) {
		order = $('#' + $(value).attr('order')).val();
		key = $(value).val();
		orders += key + '=' + order + ',';
	});
	orders = orders.slice(0, orders.length - 1);
	console.log(orders);

	jQuery.ajax({
		type : 'POST',
		url : 'query.do',
		timeout : 30 * 1000,
		data : {
			'id' : window.userId,
			'club' : window.clubName,
			'query' : '',
			'orders' : orders
		},
		dataType : 'text',
		success : function(data) {
			var result = new Result(data);
			if (result.operation == 'redirect')
				window.location = result.data;
			else if (result.operation == 'process') {
				var msg = (result.data);
				if (msg['success'] == '0')
					drawList(msg['data'], window.info);
				else
					alert(msg['msg'] + ' Please try again later.');
			}
		},
		error : function() {
			alert('Internet error! Please try again later.');
		},
		complete : function() {
			$(target).removeAttr('disabled').val('Submit');
		}
	});
}

function onLogout(event) {
	console.log('logout');
	$.cookie('userId', null);
	$.cookie('club', null);
	$('#memberList').empty();
	location.href = 'login.html';
}

function onDismiss(event) {
	console.log('dismiss');
	var conf = window.confirm('You sure want to dismiss the club?');
	if (!conf)
		return;
	conf = window.confirm('You REALLY sure want to dismiss the club?\r\n'
			+ 'The operation can not undo!');
	if (!conf)
		return;

	jQuery.ajax({
		type : 'POST',
		url : 'dismiss.do',
		timeout : 30 * 1000,
		data : {
			'id' : window.userId,
			'club' : window.clubName
		},
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			var result = new Result(data);
			if (result.operation == 'redirect')
				window.location = result.data;
			else if (result.operation == 'process') {
				var msg = (result.data);
				if (msg['success'] == '0') {
					alert('Dismiss club success.');
					window.location = 'login.html';
				} else
					alert('Dismiss club failed. Please try again later.');
			}
		},
		error : function() {
			alert('Delete member failure! Please try again later.');
		}
	});

}

function addShow(event) {
	console.log('add');
	$('#addTitle').html('Add member');
	$('.for-write').show();
	$('.for-read').hide();
	$('#updatePassword').hide();
	$('#submit').attr('op', 'add');

	var values = [ 'name', 'gender', 'birthday', 'contact', 'address',
			'addition' ];
	for ( var i in values) {
		$('#' + values[i] + 'Input').val('');
	}

	$('#joinDateInput').val(new Date().Format('yyyy/MM/dd'));

	$('#addPanel').show('normal').css('z-index', ++window.maxZIndex);
}

function updateShow(event) {
	console.log('update');
	$('#addTitle').html('Update information');
	$('.for-write').hide();
	$('.for-read').show();
	$('#updatePassword').show();
	$('#submit').attr('op', 'update');

	var profile = window.profile;

	var values = [ 'name', 'password', 'gender', 'birthday', 'contact',
			'address', 'joinDate', 'addition' ];
	for ( var i in values) {
		if (!(profile['writable'] & (1 << i)))
			$('#' + values[i] + 'Input').attr('readonly', 'readonly');
		$('#' + values[i] + 'Input').val(profile[values[i].toLowerCase()]);
	}

	$('#addPanel').show('normal').css('z-index', ++window.maxZIndex);
}

function getTimeFromStr(str) {
	if (!str)
		return 'null';
	var times = str.split('/')
	try {
		var year = parseInt(times[0]);
		var month = parseInt(times[1]) - 1;
		var day = parseInt(times[2]);
	} catch (e) {
		return null;
	}
	var time = new Date(year, month, day);
	return time.getTime();
}

function genAddData(values, shift, initPerm, rw) {
	var data = {};
	var forClass = '.for-write';
	if (rw === 'readable')
		forClass = '.for-read';
	var permission = initPerm;
	for ( var i = 0; i < values.length; i++) {
		var writable = $('.' + values[i] + 'Check' + forClass).val();
		var isWritable = (writable === rw) ? 1 : 0;
		permission += (1 << shift[i]) * isWritable;

		var value = $('#' + values[i] + 'Input').val();
		if (i == 2)
			if ((value = getTimeFromStr(value)) === null) {
				alert('Please input valid date format for birthday!');
				return false;
			}
		data[values[i]] = value;
	}
	data[rw] = permission;
	return data;
}