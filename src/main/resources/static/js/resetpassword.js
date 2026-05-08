$(document).ready(function() {

    // ================= LIVE VALIDATION =================
    $('#InputPass, #InputConfirmPass').on('keyup change', function() {
        checkValidation($(this));

        if ($('#resetPassForm').find('.validation-pending').length == 0) {
            $("#updatePass").removeAttr("disabled");
        } else {
            $("#updatePass").attr("disabled", "disabled");
        }
    });

    // ================= ENTER KEY =================
    $(document).on('keypress', function(e) {
        if (e.which == 13) {
            $("#updatePass").click();
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



function updatePassword() {

    checkValidation($("#InputPass"));
    checkValidation($("#InputConfirmPass"));

    if ($('#resetPassForm').find('.validation-pending').length != 0) {
        return;
    }

    if ($("#InputPass").val() != $("#InputConfirmPass").val()) {

        $.toast({
            text: "Password and Confirm Password must match",
            position: 'top-right',
            icon: 'error'
        });

        return;
    }

    $.ajax({

        url: "updatepassword.htm",
        type: "POST",

        data: {
            password: $("#InputPass").val(),
            token: $("#token").val()
        },


        beforeSend: function() {
            $("#loader").show();
        },

        success: function(data) {

            if (data.status) {

                $.toast({
                    text: data.message,
                    position: 'top-right',
                    icon: 'success',
                    hideAfter: 3000
                });

                setTimeout(function() {
                    window.location.href = "login.htm";
                }, 2000);

            } else {

                $.toast({
                    text: data.message,
                    position: 'top-right',
                    icon: 'error',
                    hideAfter: 3000
                });
            }
        },

        error: function() {

            $.toast({
                text: "Something went wrong",
                position: 'top-right',
                icon: 'error', 
				hideAfter: 3000

            });
        },

        complete: function() {
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