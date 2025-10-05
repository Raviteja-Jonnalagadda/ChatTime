<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="ct" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script src="<ct:url value='/resources/js/ChatApp_Utils.js?v=<%= System.currentTimeMillis() %>' />"></script>
<style>
.ctap-ct-list-table {
	width: 100%;
	background-color: #d8dad9;
	padding: 5px;
	border-radius: 10px;
}

.ctap-chatlist {
	width: 98%;
	background-color: burlywood;
	padding: 3% !important;
	border-radius: 10px !important;
}

.ctap-ct-list-tdata {
	display: flex;
	border-bottom: solid #4b4a4a 1px;
}

.ctap-chatlist {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 6px;
	margin: 5px 0;
	background-color: #f9f9f9;
}

.ctap-cl-lab {
	font-weight: bold;
	font-size: 16px;
}

.ctap-cl-urmsg-cnt {
	background-color: rgb(122, 225, 249);
	color: rgb(15, 0, 0);
	font-size: 14px;
	padding: 4px 8px;
	border-radius: 50%;
	min-width: 24px;
	text-align: center;
}
</style>
<script src="<ct:url value='/resources/js/jquery-3.7.1.min.js' />"></script>
<script>
            $(document).ready(function (){
                let pagetype = localStorage.getItem('page_type');
                console.log('the page type is --->  '+pagetype);
                pagetype=(pagetype)?pagetype:'NO_DATA'; 
                if(pagetype == 'ChatList'){
                 let chatlist = `{
                "count": "4",
                "ctuserlist": [
                    "Raviteja~2",
                    "Mr.Introvert~1",
                    "Ms.Extrovert~3",
                    "Me~0"
                ]
            }`;
                 chatlistbuilder(chatlist);   
        }
            else if(pagetype == 'GroupList'){
                  let grouplist = `{
                "count": "3",
                "ctuserlist": [
                    "Introvert Group~23",
                    "Developer Comunity~14",
                    "Fun Group ~39"
                    ]
            }`;
                chatlistbuilder(grouplist);
        }
         else if(pagetype == 'IMPList'){
                  let implist = `{
                "count": "3",
                "ctuserlist": [
                    "Introvert~2",
                    "Extrovert~1",
                    "Alchimest~3"
                    ]
            }`;
                chatlistbuilder(implist);
        }
        else if(pagetype == 'NO_DATA'){
            alert('Unidentifed Visit Kindly reLogin and try again ');
        }
        else{
            alert('Unable to process your request . Please try again later . If it repets kindly check with the administer .')
        }

                console.log('[ChatList] [document.ready] '+pagetype);
               //chatlistbuilder();

    /**
     * Chat Person click handler
     *              Activites when any of the chat is clicked and thake the value (chat name) and stores into the localstorage and the called the IframeHandler to redirect to the ctap_msg page
     */
               $(".ctap-chatlist").on('click',function(){
                    console.log('[ChatList] [ctap-chatlist] [click] In the on click function for the ctap-chatlist class (<div>) ');
                        let person_id = $(this).attr('value');
                        console.log('[ChatList] [ctap-chatlist] [click] the selected chat id is --> [ '+person_id+' ]');
                        localStorage.setItem('chat_person_id',person_id);
                        IframeHandler('/ctap_msg.html','Chats');
               });
            });

            function chatlistbuilder(chatlist){
                let ctlist = JSON.parse(chatlist);
                console.log('[ChatList] [chatlistbuilder] '+JSON.stringify(ctlist));
                let ctlistcnt = (ctlist.count)?ctlist.count:'0';
                console.log('[ChatList] [chatlistbuilder] '+ctlistcnt);
                let listdata = ctlist.ctuserlist;
                console.log('[ChatList] [chatlistbuilder] '+listdata);
                let cnt = 0;
                listdata.forEach(val => {
                    console.log('[ChatList] [chatlistbuilder] '+val);
                    let data=val.split("~");
                    let name = (data[0])?data[0]:'NO_DATA';
                    let UnReadMsgCount = (data[1])?data[1]:'0';
                    console.log('[ChatList] [chatlistbuilder] name '+name);
                    console.log('[ChatList] [chatlistbuilder] UnReadMsgCount '+UnReadMsgCount);
                    cnt +=1;
                    try{  
                        let tr = $('<tr>').addClass('ctap-ct-list-trow');
                        let td = $('<td>',{
                            class:'ctap-ct-list-tdata',
                            id:'chat_person_box'
                        });
                        let maindiv = $('<div>',{
                            id:'ctap_list_'+(cnt),
                            class:'ctap-chatlist',
                            value: name
                        });
                        let namlb = $('<label>',{
                            class:'ctap-cl-lab',
                            id:'ctap_cl_lab_'+(cnt),
                            text:name
                        });
                        let cntdiv = $('<div>',{
                            class:'ctap-cnt',
                            id:'ctap_cnt'
                        });
                        let ctlab = null;
                        if(UnReadMsgCount>0){
                        ctlab = $('<label>',{
                            class:'ctap-cl-urmsg-cnt',
                            id:'ctap_cl_urmsg_cnt',
                            text:UnReadMsgCount
                        });}
                        cntdiv.append(ctlab);
                        maindiv.append(namlb);
                        maindiv.append(cntdiv);
                        td.append(maindiv);
                        tr.append(td);
                        console.log(tr);
                        $('#ctap_main_list').append(tr);
                    }
                    catch{

                    }


                });
                
            }
        </script>
</head>
<body>
	<table id="ctap_main_list" class="ctap-ct-list-table">
	</table>
</body>
</html>