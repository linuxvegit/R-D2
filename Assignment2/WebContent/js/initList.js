function initList() {
    jQuery.ajax({
        type: 'POST',
        url: 'query.do',
        data: {
            'query': '',
            'orders': 'id=asc'
        },
        dataType: 'text',
        success: function (data) {
            var result = data.split('$');
            var key = result[0];
            if (key == 'redirect') window.location = result[1];
            else $('#msg').html(result[1]);
        }
    });
}