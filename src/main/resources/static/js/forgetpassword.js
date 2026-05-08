$(document).ready(function() {

    // ================= LIVE VALIDATION =================
    $('#exampleInputEmail').on('keyup change', function() {
        checkValidation($(this));

        if ($('#foergetPassForm').find('.validation-pending').length == 0) {
            $("#resetBtn").removeAttr("disabled");
        } else {
            $("#resetBtn").attr("disabled", "disabled");
        }
    });

    // ================= ENTER KEY =================
    $(document).on('keypress', function(e) {
        if (e.which == 13) {
            $("#resetBtn").click();
        }
    });

});



function resetPass() {

    checkValidation($("#exampleInputEmail"));

    if ($('#foergetPassForm').find('.validation-pending').length != 0) {
        return;
    }

    var data = {
        email: $("#exampleInputEmail").val(),

    };

    $.ajax({
        url: "resetPassMail.htm",
        type: "POST",
        data: data,

        beforeSend: function() {
            $("#loader").show();
        },

        success: function(data) {

            if (data.status) {

                $.toast({
                    text: data.message || "Success",
                    position: 'top-right',
                    icon: 'success',
                    hideAfter: 3000
                });

                setTimeout(function() {
                    window.location.href = "login.htm";
                }, 2000);

            } else {

                $.toast({
                    text: data.message || "Validation error",
                    position: 'top-right',
                    icon: 'error',
                    hideAfter: 3000
                });
            }
        },

        error: function(xhr, status, error) {

            let msg = "Something went wrong";

            if (xhr.responseJSON && xhr.responseJSON.message) {
                msg = xhr.responseJSON.message;
            }

            $.toast({
                text: msg,
                position: 'top-right',
                icon: 'error',
                hideAfter: 3000
            });

            console.log("AJAX Error:", xhr.responseText);
        },

        complete: function() {
            $("#loader").hide();
        }
    });
}


function checkValidation(element) {

    switch (element.attr("id")) {

        case 'exampleInputEmail':
            var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (regex.test($.trim(element.val()))) {
                validationSuccess(element);
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