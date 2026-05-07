$(document).ready(function() {

    $('#exampleInputEmail, #exampleInputpwd').on('keyup change', function() {
        checkValidation($(this));

        if ($('#SignInForm').find('.validation-pending').length == 0) {
            $("#signInbtn").removeAttr("disabled");
        } else {
            $("#signInbtn").attr("disabled", "disabled");
        }
    });

    $(document).on('keypress', function(e) {
        if (e.which == 13) {
			e.preventDefault();  
                SignIn();
            
        }
    });

    $('.pass_eye').click(function() {

        if ($(this).hasClass('fa-eye-slash')) {
            $(this).removeClass('fa-eye-slash').addClass('fa-eye');
            $('#exampleInputpwd').attr('type', 'text');
        } else {
            $(this).removeClass('fa-eye').addClass('fa-eye-slash');
            $('#exampleInputpwd').attr('type', 'password');
        }
    });

});


function checkValidation(element) {

    switch (element.attr("id")) {

        case 'exampleInputEmail':

            if ($.trim(element.val()) != "") {
                validationSuccess(element);
            } else {
                validationFailure(element);
            }
            break;

        case 'exampleInputpwd':

            if ($.trim(element.val()) != "") {
                validationSuccess(element);
            } else {
                validationFailure(element);
            }
            break;
    }
}




function validationSuccess(element) {
    element.nextAll('span:first').hide();
    element.removeClass("validation-error validation-pending").addClass("validation-success");

    if ($('#SignInForm').find('.validation-pending').length == 0) {
        $("#signInbtn").removeAttr("disabled");
    }
}

function validationFailure(element) {
    $("#signInbtn").attr("disabled", "disabled");
    element.nextAll('span:first').show();
    element.removeClass("validation-success").addClass("validation-pending validation-error");
}


function SignIn() {

    var username = $('#exampleInputEmail').val();
    var password = $('#exampleInputpwd').val();

    if (!username || !password) {
        $.toast({
            text: 'Please enter username and password',
            position: 'top-right',
            icon: 'error'
        });
        return;
    }

    var data = {
        username: username,
        password: password
    };

    $.ajax({
        url: "signin.htm",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),

        beforeSend: function() {
            $("#loader").show();
        },

        success: function(data) {

            if (data.status === "success") {
				
				localStorage.setItem("token", data.token);

                $.toast({
                    text: data.message || "Login success",
                    position: 'top-right',
                    icon: 'success'
                });

                setTimeout(function() {
                    window.location.href = data.goto;
                }, 500);

            } else {

                $.toast({
                    text: data.message || "Invalid credentials",
                    position: 'top-right',
                    icon: 'error'
                });
            }
        },

        error: function() {
            $.toast({
                text: 'Server error occurred',
                position: 'top-right',
                icon: 'error'
            });
        },

        complete: function() {
            $("#loader").hide();
        }
    });
}

