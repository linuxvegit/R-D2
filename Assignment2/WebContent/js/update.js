function updateMember() {
	var name = $('#name').val();
	var password = $('#password').val();
	var gender = $('#gender').val();
	var birthday = $('#birthday').val();
	var contact = $('#contact').val();
	var address = $('#address').val();
	var addition = $('#addition').val();
	var readable = $('#readable').val();
	jQuery.ajax({
		type : 'POST',
		url : 'update.do',
		data : {
			'name' : name || 'anonymous',
			'password' : password,
			'gender' : gender || 'M',
			'birthday' : birthday || '0',// ×ª»»ÎªÊý×Ö
			'contact' : contact || 'Secret',
			'address' : address || 'Secret',
			'addition' : addition || 'Nothing',
			'readable' : readable || '255'
		},
		dataType : 'text',
		success : function(data) {
			$('#msg').html(data);
		}
	});
}