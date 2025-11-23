$(document).ready(function() {
	console.log('[ctap_msg] [document ready] JS Loaded Successfully');

	var msgdat = {
		'msgcnt': '6',
		'usrname': 'Mr.Introvert',
		'usravtr': '../images/boy_basic_img_1.png',
		'msg': {
			'smsg1': 'Hi I am Mr Itrovert',
			'omsg1': 'Hello I am Ms Extrovert',
			'smsg2': 'You are so Beautiful',
			'omsg2': 'Hoo Thanks You to look nice',
			'smsg3': `The quick brown fox jumps over the lazy dog.  

ChatGPT is generating some sample sentences so you can test your CSS properly.  

Sometimes, very long words like Supercalifragilisticexpialidocious can cause trouble if word-breaking is not set correctly.  

This_is_a_really_really_really_long_word_without_any_spaces_that_should_wrap_properly_if_you_set_the_CSS_correctly.  

Normal text with spaces should wrap onto the next line without breaking words.  

A URL-like string: https://www.thisisaverylongdomainnamethatmightnotfit.com/somepath/to/a/resource  

Line breaks  
should also  
work as expected if white-space is set to normal.
 `,
			'omsg3': 'Thanks Namastha',
			'omsg4': 'hii hello'
		}
	};
	messagebuilder('amsg', JSON.stringify(msgdat));

	$('#msg_right').on('mousedown touchstart', function() {
		console.log('Right clicked');
	});

	$('#msg_left').on('click', function() {

	});

	$('#smsg_btn').on('click', function() {
		let smsgval = $('#smsg').val();
		console.log('smsg is -->  ' + smsgval);
		if (smsgval.trim().length === 0) {
			alert('Please enter the message before submit');
		}
		else {
			console.log('Sending the smsg to the smessagehandler -->  ' + smsgval);
			let smsgstatus = smessagehandler(smsgval);
			console.log('smsgstatus is --->  ' + smsgstatus);
			if (smsgstatus.substring(0, smsgstatus.indexOf('~')) === 'DONE') {
				$('#smsg').val('');
			}
			else if (smsgstatus.substring(0, smsgstatus.indexOf('~')) === 'FAIL') {
				alert('Issue while --->  ' + smsgstatus.substring(5));
			}
			else {
				alert('Unexpected error occured while handling the issue please retry .');
			}
		}
	});

});

function smessagehandler(smsg) {
	let rmsg = null;
	let smsgDBstatus = false;

	if (smsg.length <= 0) {
		console.log('FAIL~Cant Process Null Messages')
		rmsg = 'FAIL~Cant Process Null Messages'
		return rmsg;
	}
	/**
	 * Block 1 Sends the message to the BACKEND and store the message in DB .
	 **/
	smsgDBstatus = sockethandler(smsg);
	if (smsgDBstatus == false) {
		console.log('FAIL~Unable to send message . Please check your Internet connectivity and try again .');
		rmsg = 'FAIL~Unable to send message . Please check your Internet connectivity and try again .';
		return rmsg;
	}
	else if (smsgDBstatus == true) {
		console.log('storing in db is completed');
		console.log('sendting the message to the selfmessagbuilder -->  ' + smsg);
		let msgbldstatus = selfmessagbuilder(smsg);
		console.log('msgbldstatus  ' + msgbldstatus);
		if (msgbldstatus.substring(0, msgbldstatus.indexOf('~')) === 'DONE') {
			console.log('DONE');
			return msgbldstatus;
		}
		else if (msgbldstatus.substring(0, msgbldstatus.indexOf('~')) === 'FAIL') {
			console.log('FAIL');
			let emsg = console.log(msgbldstatus.indexOf('~') + 1);
			return 'FAIL~' + emsg;
		}
	}
	else {
		console.log('FAIL~Unknown Exception occured . Please check you Internet connectivity and try again .');
		rmsg = 'FAIL~Unknown Exception occured . Please check you Internet connectivity and try again .';
		return rmsg;
	}

}
function sockethandler(smsg) {
	console.log('in the sockethandler for sending the messag to Backend --->  ' + smsg);
	return true;

}

function selfmessagbuilder(msg) {
	console.log('in the selfmessagbuilder --->  ' + msg);
	let findtype = 'mtype';
	let msgtyp = 'smsg';
	let result;
	let msgboxid = messageboxplacefinder(findtype, msgtyp);
	if (msgboxid.substring(0, msgboxid.indexOf('~')) == 'DONE') {
		msgboxid = msgboxid.substring(msgboxid.indexOf('~') + 1);
		console.log('Got the Place for new message -->  ' + msgboxid);
		let newmsgid = msgtyp + (parseInt(msgboxid.substring(msgtyp.length)) + 1);
		console.log('new message id is ' + newmsgid);
		let msgdat = { [newmsgid]: msg };
		var smsgbuildstatus = messagebuilder(msgtyp, JSON.stringify(msgdat));
		console.log('smsgbuildstatus  ' + smsgbuildstatus);
		if (smsgbuildstatus.substring(0, smsgbuildstatus.indexOf('~')) == 'DONE') {
			result = smsgbuildstatus;
		}
		else if (smsgbuildstatus.substring(0, smsgbuildstatus.indexOf('~')) === 'FAIL') {
			result = 'FAIL~' + smsgbuildstatus.indexOf('~') + 1;
		}
		else {
			result = 'FAIL~Unexpected error while building the message . Please try again .';
		}
	}
	else if (msgboxid.substring(0, msgboxid.indexOf('~')) == 'FAIL') {
		console.log('Error while getting the Place for new message -->  ' + msgboxid.substring(5));
		result = 'FAIL~' + msgboxid.indexOf('~') + 1;
	}
	else {
		console.log('Unexpected Error while getting the Place for new message because of [messageboxplacefinder] returns this -->  ' + msgboxid);
		result = 'FAIL~Unexpected error while building the message . Please try again or refresh the page  .';
	}
	return result;
}

function messageboxplacefinder(type, val) {
	let result = null;
	let elementlist = $('[' + type + '="' + val + '"]');
	console.log('all the left elements are --->  ' + JSON.stringify(elementlist));
	var lastelement = elementlist.toArray().reverse().find(function(element) {
		return $(element).attr('id');
	});
	if (lastelement) {
		let lastsmsgid = $(lastelement).attr('id');
		console.log('the last self message id is -->  ' + lastsmsgid);
		result = 'DONE~' + lastsmsgid;
	}
	else {
		console.log('Cant find the last type in the present DOM .' + window.location.pathname);
		result = 'FAIL~Cant find the last type in the present DOM .' + window.location.pathname;
	}
	return result;
}

function messagebuilder(type, msgdat) {
	console.log('Recived type is -->  ' + type);
	console.log('[ctap_msg] [messagebuilder] Recived Message Data is -->  ' + msgdat);
	try {
		let messages = null;
		let mdata = null;
		if (type === 'amsg') {
			mdata = JSON.parse(msgdat);
			let ctunm = (mdata.usrname) ? mdata.usrname : 'NO_DATA';
			let usravt = (mdata.usravtr) ? mdata.usravtr : 'NO_DATA';
			$('#ctap_usr_avtr_img').attr('src', usravt);
			$('#ctapusrname').text(ctunm);
			let mcnt = (mdata.msgcnt) ? mdata.msgcnt : '0';
			if (mcnt > 0) {
				console.log('the final messag that is building is --->  ' + mdata.msg);
				messages = mdata.msg;
			}
		}
		else if (type === 'smsg') {
			mdata = JSON.parse(msgdat);
			console.log('the final messag that is building is --->  ' + mdata);
			messages = mdata;
		}
		else if (type === 'omsg') {
			mdata = JSON.parse(msgdat);
			console.log('the final messag that is building is --->  ' + mdata);
			messages = mdata;
		}
		else {

		}
		Object.entries(messages).forEach(([key, val]) => {
			let mtype = key;
			if (mtype.substring(0, 4) === 'smsg') {
				console.log('self message is -->  ' + val);
				let msgdiv = $('<div>', {
					class: 'message-box',
					id: key,
					text: val
				});
				msgdiv.attr('mtype', 'smsg');
				let mndiv = $('<div>', {
					class: 'message left',
					id: 'msg_left' + key.substring(5)
				});
				mndiv.append(msgdiv);
				$('#chat_main_continer').append(mndiv);
			}
			else if (mtype.substring(0, 4) === 'omsg') {
				console.log('other message is -->  ' + val);
				let msgdiv = $('<div>', {
					class: 'message-box',
					id: key,
					text: val
				});
				msgdiv.attr('mtype', 'omsg');
				let mndiv = $('<div>', {
					class: 'message right',
					id: 'msg_right' + key.substring(5)
				});
				mndiv.append(msgdiv);
				$('#chat_main_continer').append(mndiv);
			}
		});
		$('#chat_main_continer').scrollTop($('#chat_main_continer')[0].scrollHeight);
		return 'DONE~Message Appended Sucessfully'
	}
	catch (error) {
		console.error('[] [] Error while Building the messages --->  [ ' + error + ' ]');
		return 'FAIL~Error While buulding the messages Please try again .'
	}
}