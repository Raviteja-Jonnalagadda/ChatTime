let countdown;
let timeLeft = 300; // 5 minutes in seconds

function openOtpPopup() {
	$('#otpInput').val('');
	$('#otpPopup').css('display', 'flex');
	startOtpTimer();
	$('#verifyOtpBtn').prop('disabled', false);
	$('#resendOtpBtn').prop('disabled', true);
}

function closeOtpPopup() {
	$('#otpPopup').hide();
	clearInterval(countdown);
	$('#otpTimer').text('05:00');
	timeLeft = 300;
}

function startOtpTimer() {
	clearInterval(countdown);
	countdown = setInterval(function() {
		if (timeLeft <= 0) {
			clearInterval(countdown);
			$('#otpTimer').text('Expired');
			$('#verifyOtpBtn').prop('disabled', true);
			$('#resendOtpBtn').prop('disabled', false);
			return;
		}

		let minutes = Math.floor(timeLeft / 60);
		let seconds = timeLeft % 60;
		let timeStr = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
		$('#otpTimer').text(timeStr);

		timeLeft--;
	}, 1000);
}



function resendOtp() {
	alert("Resending OTP...");

	timeLeft = 300;
	startOtpTimer();
	$('#verifyOtpBtn').prop('disabled', false);
	$('#resendOtpBtn').prop('disabled', true);
}

function updateDateTime() {
	const now = new Date();
	const dateStr = now.toLocaleDateString('en-IN', {
		weekday: 'short', year: 'numeric', month: 'short', day: 'numeric'
	});
	const timeStr = now.toLocaleTimeString('en-IN');
	$('#date').text(dateStr);
	$('#time').text(timeStr);
}
function showSuccess(message) {
	if ($('#successPopup').length) {
		$('#errorPopup').hide();
		$('#successPopup .popup-msg').text('✔️ ' + message);
		$('#successPopup').fadeIn().delay(1000).slideUp(1000);
	} else {
		console.log("Success popup not found in the DOM.");
	}
}

function showError(message) {
	$('#successPopup').hide();
	$('#errorPopup .popup-msg').text('❌ ' + message);
	$('#errorPopup').fadeIn().delay(5000).slideUp(1000);
}

function closePopup(id) {
	$('#' + id).fadeOut();
}
function showSpinner() {
	$('#chatSpinner').css('display', 'flex');
	$('#chatSpinner').fadeIn();
}

function hideSpinner() {
	$('#chatSpinner').css('display', 'none');
	$('#chatSpinner').fadeOut();
}

function CheckChatTimeId(ctid) {
	console.log('[CheckChatTimeId] Received ChatTimeId for checking -->' + ctid);
	return new Promise((resolve, reject) => {
		try {
			$.ajax({
				method: 'POST',
				url: '/ChatTimeIdCheck',
				data: { CTID: ctid },
				success: function(response) {
					hideSpinner();
					console.log('[CheckChatTimeId] response is  -->' + response);
					if (response === "DONE") {
						$('#ChatTimeId').css({ border: 'solid green 2px' });
						console.log("[CheckChatTimeId] pass: " + response);
						resolve(true);
					} else if (response === "NO") {
						showError("The Id is already Taken Please try with different Id .");
						$('#ChatTimeId').css({ border: 'solid red 2px' });
						console.log('[CheckChatTimeId] response is  FAIL');
						resolve(false);
					} else {
						$('#ChatTimeId').css({ border: 'solid red 2px' });
						showError("Error in checking the ID please try again with changing the ID .");
						console.log('[CheckChatTimeId] response is  FAIL');
						reject(error);
					}
				},
				error(status, error, xhr) {
					hideSpinner();
					showError("Error in checking the ID please try again with changing the ID .");
					$('#ChatTimeId').css({ border: 'solid red 2px' });
					console.log('[CheckChatTimeId] response is error -->' + error);
					reject(error);
				}
			});
		} catch (error) {
			hideSpinner();
			showError("Error in checking the ID please try again with changing the ID .");
			console.error('[CheckChatTimeId] ChatTime ID check failed:', error);
			reject(error);
		}
	});
}
async function sendemailotp(emailid, name) {
	return new Promise((resolve, reject) => {
		$.ajax({
			method: 'POST',
			url: '/SendOTPEmail',
			data: {
				mailtype: 'SIGNUPOTP',
				mailid: emailid,
				Name: name
			},
			success(response) {
				hideSpinner();
				console.log('[SendOTPEmail Response] --->  ' + response);

				if (response === 'DONE') {
					showSuccess("OTP has been sent to your Email ID. Please enter it below.");
					openOtpPopup();

					// ✅ Setup click handler for Verify OTP button
					$('#verifyOtpBtn').off('click').on('click', async function() {
						let otp = $('#otpInput').val();

						if (otp.length !== 6 || isNaN(otp)) {
							showError("Please enter a valid 6-digit OTP.");
							return;
						}

						let dt = { sign: 'SIGNUPOTPVERIFY', otp_val: otp, email: emailid };

						try {
							showSpinner();

							// Send verification request
							const verifyResponse = await $.ajax({
								method: 'POST',
								url: '/VerifyOTP',
								data: dt
							});

							hideSpinner();

							if (verifyResponse === 'DONE') {
								showSuccess("OTP verified successfully! .");
								closeOtpPopup();
								proceedWithSignup(otp); 
								resolve(true);
							}
							else if (verifyResponse === 'OTPMISSMATCH') {
								showError("Invalid OTP. Please try again With a Valid OTP");
								resolve(false);
							}
							else if (verifyResponse === 'OTPTIMEOUT') {
								showError("Time Out. Need to enter the OTP with in 5 min ");
								resolve(false);
							}
							else if (verifyResponse === 'FAIL') {
								showError("Internal Error happned while Verifing the OTP . Please try again with the valid OTP");
								resolve(false);
							}
							else {
								showError("Unexpected Error Happned while Verifing the OTP please try again");
								resolve(false);
							}
						} catch (err) {
							hideSpinner();
							console.error("OTP Verification failed: ", err);
							showError("Error verifying OTP. Please try again.");
							resolve(false);
						}
					});
				} else if (response === 'NO') {
					showError("This email is not registered. Please check again.");
					resolve(false);
				} else {
					showError("Failed to send OTP. Please try again.");
					resolve(false);
				}
			},
			error(xhr, status, error) {
				hideSpinner();
				console.error("AJAX Error: ", error);
				showError("Unexpected error while sending OTP.");
				reject(error);
			}
		});
	});
}

async function proceedWithSignup(otp) {
	let fnam = $("#FullName").val();
	let emid = $("#emailid").val();
	let mbno = $("#mobileno").val();
	let dobr = $("#dob").val();
	let gend = $("input[name='gender']:checked").val();
	let ctid = $("#ChatTimeId").val();
	let pswd = $("#Pword").val();
	let pwht = $("#pwordhint").val();

	let dt = { fnm: fnam, emd: emid, mbn: mbno, dob: dobr, ged: gend, ctid: ctid, pwd: pswd, pwh: pwht,sgnotp:otp };
alert("in the proceedWithSignup ");
	showSpinner();
		await $.ajax({
			method:'POST',
			url:'/ChatTimeSignUp',
			data:JSON.stringify(dt),
			contentType: 'application/json',
			success:function(response){
				hideSpinner();
				if(response == 'DONE'){
					showSuccess("You Details are registred in ChatTime Sucessfully.");
					window.location.href = "../html/ChatLogin.html";
				}
				else if(response == 'FAIL'){
					showError("Issue while Regstring details .Kindly recheck all the details and try again .");
				}
			},
			error(status,xhr,error){
				hideSpinner();
				console.log("[proceedWithSignup] status --->  "+status);
				console.log("[proceedWithSignup] xhr    --->  "+xhr);
				console.log("[proceedWithSignup] error  --->  "+error);
				showError("Internal Issue happned while Regstring details .Kindly recheck all the details and try again .");
			}
		});
	hideSpinner();

}



$(document).ready(function() {
	//window.alert = function() { }; console.log = console.info = console.warn = console.error = function() { };
	updateDateTime();
	setInterval(updateDateTime, 1000);

	/**
	 * Sign In Part 
	 * 
	 */
	if (pagetype === 'Signin') {
		$('#dob').datepicker({
			dateFormat: 'dd-M-yy',
			maxDate: 0,
			changeMonth: true,
			changeYear: true,
			yearRange: "-100:+0",
			defaultDate: '-18y',
			onSelect: function() {
				$(this).datepicker('hide');
			}
		});
	}

	$('#ChatTimeId').on('change', async function() {
		showSpinner();
		try {
			const isValid = await CheckChatTimeId($(this).val());
			if (isValid) {
				signin = true;
			} else {
				signin = false;
			}
		} catch (err) {
			console.error("Error while checking ChatTimeId", err);
			signin = false;
		}
		hideSpinner();
	});


	/**
	 * Forgot Password Page
	 */

	$('#ctap_sendotp').on('click', function() {
		let email = $('#ctap_emailid').val();
		if (!email) {
			showError("Please enter the email for getting the OTP");
			return false;
		}
		let dt = { mailtype: 'ForgotPasswordOTP', mailid: email, Name: "CHATTIME USER" }
		$.ajax({
			method: 'POST',
			url: '/SendOTPEmail',
			data: dt,
			success(response) {
				hideSpinner();
				console.log('[ForgotPassword Page OTP Status] --->  ' + response);
				if (response == 'DONE') {
					showSuccess("OTP Has been send to your Email Id Please enter it below .");
					$('.ctap-new-password').css({ display: 'block' });
				}
				else if (response == 'NO') {
					showError("The given email id is not Registred with the ChatTime . Kindly recheck the EmailId and try again");
				}
				else {
					showError("Error While Sending the OTP . Please try again .");
				}
			},
			error(error, xhr, status) {
				hideSpinner();
				console.log("[ForgotPassword Page OTP Status] error is --->  " + error);
				showError("Unexpected Error while sending the OTP Please try after some time .");
			}
		});


	});


	$('#ctap_signin_submit').on('click', async function() {
		if (!signin) {
			showError("Please enter all the values ");
		}
		else {
			let fnam = $("#FullName").val();
			let emid = $("#emailid").val();
			let mbno = $("#mobileno").val();
			let dobr = $("#dob").val();
			let gend = $("input[name='gender']:checked").val();
			let ctid = $("#ChatTimeId").val();
			let pswd = $("#Pword").val();
			let pwht = $("#pwordhint").val();
			let sotp = "123456";

			let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			let phoneRegex = /^[6-9]\d{9}$/;

			if (fnam.trim() && emid.trim() && dobr.trim() && mbno.trim() && gend && ctid.trim() && pswd.trim() && pwht.trim()) {
				if (!emailRegex.test(emid)) {
					showError("Please enter a valid email address.");
				}
				else if (!phoneRegex.test(mbno)) {
					showError("Please enter a valid 10-digit mobile number.");
				}
				else {
					showSpinner();
					let emailstatus = await sendemailotp(emid, fnam);
					hideSpinner();
					if (!emailstatus) {
						showError("Error In OTP verification Please try again");
						return false;
					} else {
						/*alert("Started the Registracting a customer In DB ");
						let data = { fnm: fnam, emd: emid, mbn: mbno, dob: dobr, ged: gend, ctid: ctd, pwd: pswd, pwh: pwht, otp: sotp }
						console.log("[ctap_signin_submit] [Onclick] The data sedning to ajax is -->  " + data);
						alert("[ctap_signin_submit] [Onclick] The data sedning to ajax is -->  " + data);
						showSpinner();
						let DetailsStore = await CtapAjax(data, "ChatTimeSignUp");
						alert("[ctap_signin_submit] [Onclick] Result of storing is -->  " + DetailsStore);
						hideSpinner();
						alert("Thanks For sigin up");*/
					}
				}
			}
			else {
				showError("Please Enter all the values");
			}
		}
});
	

	$('#ctap_submit').on('click', function() {
		const uid = $('#ctap_uid').val().trim();
		const pwd = $('#ctap_pwd').val().trim();

		if (!uid || !pwd) { 
			showError("Please enter both User ID and Password.");
		} else {
			let ldata = {
				uname: $('#ctap_uid').val(),
				pwd: $('#ctap_pwd').val()
			};
			showSpinner();
			$.ajax({
				url: '/ctapLogin',
				contentType: 'application/json',
				method: 'POST',
				data: JSON.stringify(ldata),
				success(response) {
					hideSpinner();
					let signval = response.substring(0, 4);
					console.log("signval -->  [ " + signval + " ]");
					if (signval === 'DONE') {
						console.log("Login Response: ", response);
						showSuccess("Login successful!");
						$('#ct_login_form').attr('action', '../SucessLogin');
						$('#ct_login_form').submit();
					}
					else if (signval === 'FAIL') {
						let val = JSON.parse(response.substring(5));
						console.log("val -->  [ " + JSON.stringify(val) + " ]");
						let ermsg = val.emessage;
						console.log("the error message is --->  [ " + ermsg + " ]");
						showError(ermsg);
					}
					else {
						console.log("Unknow Resopnse");
						showError("Unexpexted Response Please try again . If this repets please contact our team")
					}
				},
				error: function(xhr, status, error) {
					hideSpinner();
					console.error("Login Error: ", error);
					showError("Unexpected Issue " + error);
				}
			});
			hideSpinner();
		}
	});
});
