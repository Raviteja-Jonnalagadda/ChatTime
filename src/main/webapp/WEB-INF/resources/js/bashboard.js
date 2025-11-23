/**
 * Dashboard JS is desined for CHAT TIME App main screen for the bashboard 
 * It is uses the JQUERY V-3.7.1.min.js file .
 * All the sysntex are in JQUERY no other libirers used .
 * It handels all the screen redirections by handling the iframe .
 * 
 * @author Raviteja J
 */


const pageurl = "/Page/AllRedirect";

$(document).ready(function() {
	console.log('[bashboard.js] JS file loaded sucessfully');

	updatetime();
	setInterval(updatetime, 1000);
	TabBuilder();
	/**
	 * Tab Click Handler
	 *          Activictes when the Tab is clicked and gets the redirect path and sends to the IframeHandler finctiion
	 */
	$('.ctap-main-menu').on('click', async function() {
		const val = $(this).attr('id');
		const redirectPath = $(this).attr('redirect');

		// Set page type in localStorage
		if (val === 'Chats') {
			localStorage.setItem('page_type', 'ChatList');
		} else if (val === 'Groups') {
			localStorage.setItem('page_type', 'GroupList');
		} else if (val === 'Importent') {
			localStorage.setItem('page_type', 'IMPList');
		}

		console.log('[bashboard.js] [ctap-main-menu click event] extracted id: ' + val + ', path: ' + redirectPath);

		const data = { pname: val, pdata: val };
		const url = await CtapAjax(data, pageurl);
		console.log('[bashboard.js] The Ajax Response or redirection URL is ---> ' + url);
		IframeHandler(url, val);
	});


	/**
	 * Search Button Handler
	 * It allowes the users to Search the other chatTime Users by there ChatTime ID or (User Id)
	 */

	$('#ctap_id_search_btn').off('click').on('click', async function() {
	    var SearchId = $('#ctap_id_search').val();
	    if (SearchId.length == 0) {
	        showError("Please Enter the User Id Before Search");
	        return false;
	    }
	    let data = { search_id: SearchId };
	    let SearchStatus = await CtapAjax(data, "/CTSearchID");
		console.log("[ctap_id_search_btn] [click] Result "+SearchStatus);
		let res=JSON.parse(SearchStatus);
		if (res.status === 'DONE') {
		    let box = $('#Search_Sugg');
		    box.empty().show(); // Show suggestion box
		    let dataArr = res.Suggestion_data;
		    let table = $('<table>', {
		        id: 'ctap_search_table',
		        class: 'ctap-search-table',
		    });
			let lineBreak = $('<br>');
		    dataArr.forEach(obj => {
		        let val = obj.CTUSER_ID;
		        let tr = $('<tr>', { class: 'ctap-row' });
		        let td = $('<td>', {
		            class: 'ctap-search-tb-data',
		            text: val
		        });
		        td.on('click', function () {
		            $('#ctap_id_search').val(val);
		            box.hide();
		            console.log("[Select] User selected:", val);
		        });
		        tr.append(td);
		        table.append(tr);
		    });
		    box.append(table);
		}
	});
	$(document).on('click', function (e) {
	    if (!$(e.target).closest('#ctap_id_search, #Search_Sugg').length) {
	        $('#Search_Sugg').hide();
	    }
	});



});

/**
 * Date Time Function 
 * 
 *          Creating an object in the onload to a gloable variable and using the
 * "toLocaleDateString()" method getting the date and 
 * "toLocaleTimeString" method for getting the time
 */

function updatetime() {
	const dttm = new Date();
	const formattedDate = dttm.toLocaleDateString('en-GB', {
		day: '2-digit',
		month: 'short',
		year: 'numeric'
	}).replace(',', '').replace(' ', '-').replace(' ', '-');
	/*console.clear();
	console.log('[bashboard.js] [updatetime] '+formattedDate);
	console.log('[bashboard.js] [updatetime] '+dttm.toLocaleTimeString());*/
	$('#date').text(formattedDate);
	$('#time').text(dttm.toLocaleTimeString());
}

/**
 * Tab Builder Function
 * 
 *          Buildes the menus from the json data it takes the array of menus from the "ctmenu" (ChatTime menus)
 * is extracts the menus and build them in the li and appends them to the <ul> tag which is in the page
 */
function TabBuilder() {
	/**
	 * Sample JSON Data for testing purpose in real time all the menus will come from the backend .
	 * <NOTE> : Use only for development or testing purpose in real time it comes from the ajax .
	 **/
	let smpmenu = `{
  "ctmenu": {
    "Chats": "../html/ctap_ChatList.html",
    "Groups": "../html/ctap_ChatList.html",
    "Importent": "../html/ctap_ChatList.html",
    "Others": "../../MrIntrovertProjects/bashboard.html"
  }
}`;
	smpmenu = JSON.parse(smpmenu);
	let almenu = smpmenu;
	console.log('[bashboard.js] [menubuilder] ' + JSON.stringify(almenu));
	let menu = almenu.ctmenu;
	console.log('[bashboard.js] [menubuilder] ' + JSON.stringify(menu));
	Object.entries(menu).forEach(([key, value]) => {
		let li = $(`<li>`, {
			id: key,
			class: 'ctap-main-menu',
			text: key,
			redirect: value
		});
		$('#menu').append(li);
	});
}