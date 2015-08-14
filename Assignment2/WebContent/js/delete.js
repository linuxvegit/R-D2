function deleteMember() {
	var id = $('#id').val();
	jQuery.ajax({
		type : 'POST',
		url : 'delete.do',
		data : {
			'id' : id,
		},
		dataType : 'text',
		success : function(data) {
			$('#msg').html(data);
		}
	});
}