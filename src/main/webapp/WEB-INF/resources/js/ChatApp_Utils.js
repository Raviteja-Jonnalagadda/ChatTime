/**
 * IframeHandler
 *          Handles the Iframe and sets the src to the required page and redirects to it 
 * 
 * @author Raviteja J
 */
function IframeHandler(path, val) {
    let $iframe = $('#ctap_main_frame');

    if ($iframe.length === 0) {
        // iframe not found in this document, try parent
        if (window.parent && window.parent.IframeHandler) {
            console.log('[ChatApp_Utils.js] Forwarding call to parent');
            window.parent.IframeHandler(path, val);
            return;
        }
        console.error('Iframe not found in this document or parent');
        return;
    }

    console.log('[ChatApp_Utils.js] Redirecting iframe to -->', path);
    console.log('Before change, iframe.src =', $iframe.prop('src'));
    // Change iframe src and set custom attribute
    $iframe.prop('src', path);
    $iframe.attr('currentscreen', val);

    // Listen for load event
    $iframe.off('load').on('load', function () {
        console.log('Onload fired, iframe.src =', $(this).prop('src'));
    });
}

/**
 * CtapAjax 
 * (Universial Ajax)
 * 				It handels the most of the Ajax Functionalites and checks the Ajax got sucess or failer as a first layer of filter
 * then sends the actuall response to the requested variable which is commming from the Backend.
 * 
 * @author Raviteja J
 */

async function CtapAjax(data, endpoint) {
    const fnm = '[ChatApp_Utils.js] [CtapAjax] ';
    console.log(fnm + "The received Ajax request [ " + JSON.stringify(data) + " ] endpoint [ " + endpoint + " ]");

    if (!data || !endpoint) {
        console.error(fnm + 'Some values are null. Cannot process request.');
        return "FAIL~Cant process null Values";
    }

    try {
        const response = await $.ajax({
            url: endpoint,
            method: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json"
        });
        console.log(fnm + 'The received Ajax response is ---> ' + response);
        return response;

    } catch (err) {
        if (err.status) {
			console.log(fnm + 'The Ajax response status is ---> ' + err.status);
            switch (err.status) {
                case 404:
                    console.error(fnm + 'Error 404: Not Found.');
                    window.location.href = '/errors/404.html';
                    break;
                case 500:
                    console.error(fnm + 'Error 500: Internal Server Error.');
                    window.location.href = '/errors/500.html';
                    break;
                case 403:
                    console.error(fnm + 'Error 403: Forbidden.');
                    window.location.href = '/errors/403.html';
                    break;
                default:
                    console.error(fnm + 'Unexpected error:', err);
                    window.location.href = '/errors/general.html';
                    break;
            }
        } else {
            console.error(fnm + 'Ajax call failed:', err);
        }
        return "FAIL~Unexpected Error Occurred";
    }
}

/**
 * Show Success Message
 */
function showSuccess(message) {
	if ($('#successPopup').length) {
		$('#errorPopup').hide();
		$('#successPopup .popup-msg').text('✔️ ' + message);
		$('#successPopup').fadeIn().delay(1000).slideUp(1000);
	} else {
		console.log("Success popup not found in the DOM.");
	}
}

/**
 * Show Error Message
 */
function showError(message) {
	$('#successPopup').hide();
	$('#errorPopup .popup-msg').text('❌ ' + message);
	$('#errorPopup').fadeIn().delay(5000).slideUp(1000);
}

/**
 * Closes the Sucess or Error Message Popups
 */
function closePopup(id) {
	$('#' + id).fadeOut();
}

/**
 * Shows the Spiner for loading
 */
function showSpinner() {
	$('#chatSpinner').css('display', 'flex');
	$('#chatSpinner').fadeIn();
}

/**
 * Hides the Spiner After loading
 */
function hideSpinner() {
	$('#chatSpinner').css('display', 'none');
	$('#chatSpinner').fadeOut();
}

/**
 * Shows the Send Message PopUp for the Searched User in the Search Bar
 */

function SendMessage(CTID){
	if(CTID.length==0){
		showError('Chat Id is null Please enter the Valid ChatTime User ID for sending the message');
		return false;
	}
	
}







