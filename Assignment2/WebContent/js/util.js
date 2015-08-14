/**
 * Created by mouxk on 15-8-8.
 */

function parseMsg(msg) {
	var msgs = msg.split(':');
	return $.trim(msgs[1]);
}

function Result(msg) {
	var result = $.parseJSON(msg);
	this.operation = result['operation'];
	this.data = result['data'];
}

function passwordHash(password) {
	return $.md5(password);
}
