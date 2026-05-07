$(document).ready(function() {

    // ================= LIVE VALIDATION =================
    $('#username, #email, #mobnumber, #InputPass, #InputConfirmPass').on('keyup change', function() {
        checkValidation($(this));

        if ($('#signUpForm').find('.validation-pending').length == 0) {
            $("#signUnbtn").removeAttr("disabled");
        } else {
            $("#signUnbtn").attr("disabled", "disabled");
        }
    });

    // ================= ENTER KEY =================
    $(document).on('keypress', function(e) {
        if (e.which == 13) {
            $("#signUnbtn").click();
        }
    });

});

$('.pass_eye').click(function() {

		if ($(this).hasClass('fa-eye-slash')) {
			$(this).removeClass('fa-eye-slash');
			$(this).addClass('fa-eye');
			$('#InputPass').attr('type', 'text');
		} else {
			$(this).removeClass('fa-eye');
			$(this).addClass('fa-eye-slash');
			$('#InputPass').attr('type', 'password');
		}
	});


	$('.con_pass_eye').click(function() {

		if ($(this).hasClass('fa-eye-slash')) {
			$(this).removeClass('fa-eye-slash');
			$(this).addClass('fa-eye');
			$('#InputConfirmPass').attr('type', 'text');
		} else {
			$(this).removeClass('fa-eye');
			$(this).addClass('fa-eye-slash');
			$('#InputConfirmPass').attr('type', 'password');
		}
	});

$("#mobnumber").on("input", function() {
    this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);
});


function SignUp() {

    checkValidation($("#username"));
    checkValidation($("#email"));
    checkValidation($("#mobnumber"));
    checkValidation($("#InputPass"));
    checkValidation($("#InputConfirmPass"));

    if ($('#signUpForm').find('.validation-pending').length != 0) {
        return;
    }

    var data = {
        username: $("#username").val(),
        email: $("#email").val(),
        mobilenumber: $("#mobnumber").val(),
        password: $("#InputPass").val()
    };

	$.ajax({
	    url: "register.htm",
	    type: "POST",
	    contentType: "application/json",
	    data: JSON.stringify(data),

	    beforeSend: function () {
	        $("#loader").show();
	    },

	    success: function (data) {

	        if (data.status === "success") {

	            $.toast({
	                text: data.message || "Success",
	                position: 'top-right',
	                icon: 'success'
	            });

	            setTimeout(function () {
	                window.location.href = "login.htm";
	            }, 500);

	        } else {

	            // ❌ SERVER VALIDATION ERROR (like email exists)
	            $.toast({
	                text: data.message || "Validation error",
	                position: 'top-right',
	                icon: 'error'
	            });
	        }
	    },

	    error: function (xhr, status, error) {

	        // ❌ ACTUAL SERVER ERROR (500, 404, exception)
	        let msg = "Something went wrong";

	        if (xhr.responseJSON && xhr.responseJSON.message) {
	            msg = xhr.responseJSON.message;
	        }

	        $.toast({
	            text: msg,
	            position: 'top-right',
	            icon: 'error'
	        });

	        console.log("AJAX Error:", xhr.responseText);
	    },

	    complete: function () {
	        $("#loader").hide();
	    }
	});
}

// ================= PASSWORD POLICY (YOUR ORIGINAL - UNCHANGED) =================
function checkValidationONPass(pass) {

    var ucase = new RegExp("[A-Z]+");
    var lcase = new RegExp("[a-z]+");
    var num = new RegExp("[0-9]+");
    var splchar = new RegExp("[$&+,:;=?@#|'<>.^*()%!-]");

    var uppercase = false;
    var lowercase = false;
    var numbercase = false;
    var eightlength = false;
    var splcharcase = false;

    if (pass.length >= 8) {
        eightlength = true;
    }

    if (ucase.test(pass)) {
        uppercase = true;
    }

    if (lcase.test(pass)) {
        lowercase = true;
    }

    if (num.test(pass)) {
        numbercase = true;
    }

    if (splchar.test(pass)) {
        splcharcase = true;
    }

    return (uppercase && lowercase && numbercase && eightlength && splcharcase);
}


// ================= SIGNUP VALIDATION ONLY =================
function checkValidation(element) {

    switch (element.attr("id")) {

        case 'username':
            if ($.trim(element.val()) != "") {
                validationSuccess(element);
            } else {
                validationFailure(element);
            }
            break;

        case 'email':
            var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (regex.test($.trim(element.val()))) {
                validationSuccess(element);
            } else {
                validationFailure(element);
            }
            break;

        case 'mobnumber':

            var value = $.trim(element.val());

            // only 10 digits and starts with 6-9
            var mobRegex = /^[6-9][0-9]{9}$/;

            if (mobRegex.test(value)) {
                validationSuccess(element);
            } else {
                validationFailure(element);
            }

            break;

        case 'InputPass':

            if ($.trim(element.val()) != "") {

                if (checkValidationONPass(element.val())) {
                    validationSuccess(element);
                } else {
                    element.nextAll('span:first').text(
                        "Password must be 8+ chars with uppercase, lowercase, number & special character."
                    );
                    validationFailure(element);
                }

            } else {
                validationFailure(element);
            }
            break;

        case 'InputConfirmPass':

            if ($.trim(element.val()) != "") {

                if (element.val() == $("#InputPass").val()) {
                    validationSuccessBoth();
                } else {
                    element.nextAll('span:first').text("Password and confirm password doesn't match.");
                    validationFailure(element);
                }

            } else {
                validationFailure(element);
            }
            break;
    }
}


// ================= YOUR ORIGINAL VALIDATION FUNCTIONS =================
function validationSuccessBoth() {
    $("#InputPass").nextAll('span:first').hide();
    $("#InputConfirmPass").nextAll('span:first').hide();

    $("#InputPass").removeClass("validation-error validation-pending").addClass("validation-success");
    $("#InputConfirmPass").removeClass("validation-error validation-pending").addClass("validation-success");

    if ($('#signUpForm').find('.validation-pending').length == 0) {
        $("#signUnbtn").removeAttr("disabled");
    }
}

function validationSuccess(element) {
    element.nextAll('span:first').hide();
    element.removeClass("validation-error validation-pending").addClass("validation-success");

    if ($('#signUpForm').find('.validation-pending').length == 0) {
        $("#signUnbtn").removeAttr("disabled");
    }
}

function validationFailure(element) {
    $("#signUnbtn").attr("disabled", "disabled");
    element.nextAll('span:first').show();
    element.removeClass("validation-success").addClass("validation-pending validation-error");
}