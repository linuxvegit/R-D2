function insertMember() {
	var name = $('#name').val();
	var writable = $('#writable').val();
	jQuery.ajax({
		type : 'POST',
		url : 'insert.do',
		data : {
			'name' : name,
			'writable' : writable
		},
		dataType : 'text',
		success : function(data) {
			$('#msg').html(data);
		}
	});
}