<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="ct" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link
	href="<ct:url value='/resources/css/chat_msg.css?v=<%=System.currentTimeMillis()%>' />"
	rel="stylesheet">
<link
	href="<ct:url value='/resources/css/chat_msg_768.css?v=<%=System.currentTimeMillis()%>' />"
	rel="stylesheet">

<script src="<ct:url value='/resources/js/jquery-3.7.1.min.js' />"></script>
<script
	src="<ct:url value='/resources/js/ChatMsg.js?v=<%=System.currentTimeMillis()%>' />"></script>
<script
	src="<ct:url value='/resources/js/ChatApp_Utils.js?v=<%=System.currentTimeMillis()%>' />"></script>
<script>
	
</script>

</head>
<body>
	<div class="ctap-usr-info" id="ctap_usr_info">
		<div class="ctap-usr-avtr">
			<img src="<ct:url value='/resources/images/boy_basic_img_1.png' />" id="ctap_usr_avtr_img12" class="ctap-usr-avtr-img">
		</div>
		<div class="ctap-usr-nm" id="ctap_usr_nm">
			<label id="ctapusrname"></label>
		</div>
		<!-- <div>
            <div class="ctap-usr-status">
                <label id="ctapusrstatus">Online</label>
            </div>
        </div> -->
	</div>
	<div id="master_div" class="master-div">
		<div id="chat_main_continer" class="chat-container"></div>
		<div class="ctap-msg-typ" id="ctap_msg_typ">
			<table>
				<tr>
					<td><input type="text" class="ctap-mtp-txt" name="smsg"
						id="smsg" placeholder="Type Your Message Hear ...." /></td>
					<td><input type="button" name="smsg_btn" id="smsg_btn"
						class="ctap-mtp-btn" value="Send" /></td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>