function dismissMember() {
	jQuery.ajax({
		type : 'POST',
		url : 'dismiss.do',
		dataType : 'text',
		success : function(data) {
			$('#msg').html(data);
		}
	});
}