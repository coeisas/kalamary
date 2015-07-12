$(document).ready(function () {

    $('.InputText').keydown(function () {
        $(".InputText").val(function (i, val) {
            return val.toUpperCase();
        });
    });
});
