// $("#signup-box-link").click(function(){
//     $("#login").fadeOut(100);
//     $("#register").delay(100).fadeIn(100);
//     $("#login-box-link").removeClass("active white-btn");
//     $("#signup-box-link").addClass("active white-btn");
// });
// $("#login-box-link").click(function(){
//     $("#login").delay(100).fadeIn(100);;
//     $("#register").fadeOut(100);
//     $("#login-box-link").addClass("active white-btn");
//     $("#signup-box-link").removeClass("active white-btn");
//
// });

const formRegister = $('#register');


formRegister.validate({
    onkeyup: function (element) {
        $(element).valid()
    },
    onclick: false,
    onfocusout: false,
    rules: {
        fullName: {
            required: true,
            minlength: 5,
            maxlength: 25
        },
        phoneNumber: {
            required: true,
            isPhone: true
        },
        email: {
            required: true,
            isEmail: true
        },
        username: {
            required: true,
            minlength: 5
        },
        password: {
            required: true,
            minlength: 6
        },
        // passwordCm: {
        //     required: true,
        // }
    },
    messages: {
        fullName: {
            required: 'Vui lòng nhập fullName đầy đủ',
            minlength: 'fullName tối thiểu là 5 ký tự',
            maxlength: 'fullName tối đa là 25 ký tự'
        },
        phoneNumber: {
            required: 'Vui lòng nhập phone đầy đủ'

        },
        email: {
            required: 'Vui lòng nhập email đầy đủ'
        },
        username: {
            required: 'Vui lòng nhập username đầy đủ',
            minlength: 'username tối thiểu là 5 ký tự',
        },
        password: {
            required: 'Vui lòng nhập password đầy đủ',
            minlength: 'password tối thiểu là 6 ký tự',
        },
        // passwordCm: {
        //     required: 'Vui lòng nhập password đầy đủ',
        // }
    },
    // errorLabelContainer: ".error-area",
    // errorPlacement: function (error, element) {
    //     error.appendTo(".error-area");
    // },
    // showErrors: function (errorMap, errorList) {
    //     if (this.numberOfInvalids() > 0) {
    //         $('.text-danger').removeClass('show').addClass('hide').empty();
    //         $(".error-area").removeClass("hide").addClass("show");
    //     } else {
    //         $(".error-area").removeClass("show").addClass("hide").empty();
    //         $("input.error").removeClass("error");
    //         $('.text-danger').removeClass('hide').addClass('show').empty();
    //     }
    //     this.defaultShowErrors();
    // },
})

$.validator.addMethod("isEmail", function (value, element) {
    return this.optional(element) || /^[a-z]+@[a-z]+\.[a-z]+$/i.test(value);
}, "Vui lòng nhập đúng định dạng email");
$.validator.addMethod("isPhone", function (value, element) {
    return this.optional(element) || /^[0][0-9]{9}$/i.test(value);
}, "Vui lòng nhập 10 số bắt đầu là 0");
$.validator.addMethod("isNumber", function (value, element) {
    return this.optional(element) || /^[0-9]*$/i.test(value);
}, "Vui lòng nhập tiền giao dịch bằng ký tự số");

$("#passwordCm").on('input', ()=> {
    $("#errorPasswordCm").removeClass("show").addClass("hide");
    const password = $('#password').val();
    const passwordCm = $('#passwordCm').val();

    if (password !== passwordCm) {

        $("#errorPasswordCm").removeClass("hide").addClass("show").empty();
        const str = 'Mật khẩu không trùng khớp';
        $("#errorPasswordCm").text(str)

    }else{
        $("#errorPasswordCm").removeClass("show").addClass("hide").empty();
    }

})

$("#phoneNumber").on('input', ()=> {
    $("#errorPhoneNumber").removeClass("show").addClass("hide");

})

$("#email").on('input', ()=> {
    $("#errorEmail").removeClass("show").addClass("hide");

})

$("#username").on('input', ()=> {
    $("#errorUsername").removeClass("show").addClass("hide");

})

formRegister.on('submit', function (event) {
    const password = $('#password').val();
    const passwordCm = $('#passwordCm').val();

    if (password !== passwordCm) {
        event.preventDefault();

        $("#errorPasswordCm").removeClass("hide").addClass("show").empty();
        const str = 'Mật khẩu không trùng khớp';
        $("#errorPasswordCm").text(str)

    }else{
        $("#errorPasswordCm").removeClass("show").addClass("hide").empty();
    }
});