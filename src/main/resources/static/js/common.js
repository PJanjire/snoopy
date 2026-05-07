$(document).ready(function() {
    $(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError) {
        $('#loading-coupon').hide();

        if (jqXHR.status == 403) {
            window.location.href = "error_403.htm";
        } else if (jqXHR.status == 401) {

            localStorage.removeItem("token");

            window.location.href = "/login.htm";
        }
        else if (jqXHR.status == 404) {
            window.location.href = "error_404.htm";
        }
        else if (jqXHR.status == 500) {
            window.location.href = "error_500.htm";
        }/*else{
			alert("You don't have rights to perform this action.");
		}*/

    });

  

    /*$('.datepicker').datepicker({
        uiLibrary: 'bootstrap',
        format: "dd/mm/yyyy",
         clearBtn: true
	
    });
*/


    // Fix dropdown toggle
    $(document).on('click', '.dropdown-toggle', function(e) {
        e.preventDefault();
        e.stopPropagation();

        var $parent = $(this).parent();

        // Close all other dropdowns
        $('.dropdown').not($parent).removeClass('open');

        // Toggle current dropdown
        $parent.toggleClass('open');
    });


    $(".side-nav li a[data-toggle='collapse']").on("click", function(e) {
        e.preventDefault();

        var target = $(this).attr("data-target");

        // toggle clicked menu
        $(target).slideToggle(200);

        // optional: close others
        $(".collapse-level-1").not(target).slideUp(200);
    });

    // Close dropdown when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.dropdown').length) {
            $('.dropdown').removeClass('open');
        }
    });

    $(document).ajaxSuccess(function(event, request, settings) {
        $('#loading-coupon').hide();
        if (settings.type == "POST") {
            $("#uiToken").val(request.getResponseHeader("uiToken"));
        }
    });



    $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
        options.xhrFields = {
            withCredentials: true
        };
        $('#loading-coupon').show();
        /*var jwtToken = $("#jwtToken").val();
        if (jwtToken) {
          jqXHR.setRequestHeader('Authorization', "Bearer "+ jwtToken);
          return jqXHR;
        }*/


        var uiToken = $("#uiToken").val();
        var jwtToken = $("#jwtToken").val();
        if (jwtToken) {
            jqXHR.setRequestHeader('Authorization', "Bearer " + jwtToken);
            jqXHR.setRequestHeader('uiToken', uiToken);
            return jqXHR;
        }
    });
});



function clsforEmail(e) {

    var regex = new RegExp("^[a-zA-Z0-9@. ]*$");

    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }

    e.preventDefault();
    return false;
}




function clsAlphaNoOnly(e) {  // Accept only alpha numerics, no special characters 
    var regex = new RegExp("^[a-zA-Z0-9_@. ]*$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }

    e.preventDefault();
    return false;
}
function clsAlphaNoSplOnly(e) {  // Accept only alpha numerics, no special characters 
    var regex = new RegExp("^[a-zA-Z0-9/. ]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }

    e.preventDefault();
    return false;
}
function clsAlphaOnly2(e) {  // Accept only alpha numerics, no special characters 
    var regex = new RegExp("^[a-zA-Z ]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }

    e.preventDefault();
    return false;
}

function clsAlphaOnly(e) {  // Accept only alpha numerics, no special characters 
    var regex = new RegExp("^[a-zA-Z._()&', ]+$");
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (regex.test(str)) {
        return true;
    }

    e.preventDefault();
    return false;
}
function onlyNumeric(e, t) {

    try {

        if (window.event) {
            var charCode = window.event.keyCode;
        } else if (e) {
            var charCode = e.which;
        } else {
            return true;
        }
        if ((charCode == 0) || (charCode == 8) || (charCode == 45) || (charCode > 47 && charCode < 58))
            return true;
        else
            return false;
    } catch (err) {
    }
}
function submitMenu(menuName) {
    document.getElementById("csrfForm").action = menuName;
    document.getElementById("csrfForm").submit();
}


function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

function ajaxErrorBlock(xhr) {
    if (xhr.status == 403) {
        window.location.href = "error_403.htm";
    } else if (xhr.status == 401) {
        window.location.href = "error_401.htm";
    }
    else if (xhr.status == 404) {
        window.location.href = "error_404.htm";
    }
    else if (xhr.status == 500) {
        window.location.href = "error_500.htm";
    }/*else{
			alert("You don't have rights to perform this action.");
		}*/
    //unblockUI();
    /*if(xhr.status == 901){
        window.location.href = "show-login.htm?isSessionExpired=true";
    } else if(xhr.status == 500){
        window.location.href = "system-error.htm";
    } else if(xhr.status == 902){*/

    //}
}

/*function LogoutClick() {
    sessionStorage.clear();
    window.location.href = "/Vigilance/login.htm";   
 }*/


function onlyNumeric(e, _t) {

    try {

        if (window.event) {
            var charCode = window.event.keyCode;
        } else if (e) {
            var charCode = e.which;
        } else {
            return true;
        }
        if ((charCode == 0) || (charCode == 8) || (charCode == 45) || (charCode > 47 && charCode < 58))
            return true;
        else
            return false;
    } catch (err) {
    }
}

function logout() {
    $("#loader").show();
    $.ajax({
        url: "logout.htm",
        type: "GET",
        success: function() {
            window.location.href = "login.htm";
        },
        complete: function() {
            $("#loader").hide();
        }
    });
}

function onlyNumeric1(e, _t) {
    try {
        if (window.event) {
            var charCode = window.event.keyCode;
        } else if (e) {
            var charCode = e.which;
        } else {
            return true;
        }

        // Allow only specific key codes: numbers (0-9), decimal point (.), backspace (8), and minus sign (-)
        if (
            charCode == 0 ||
            charCode == 8 ||
            charCode == 45 || // minus sign
            charCode == 46 || // decimal point
            (charCode > 47 && charCode < 58) // numbers 0-9
        ) {
            return true;
        } else {
            return false;
        }
    } catch (err) {
    }
}
