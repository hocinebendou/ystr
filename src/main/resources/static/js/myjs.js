$(function() {

    $('.ystr_val').select2();

    $('#clear-btn').click(function (e) {
        $('select[id^="dys"]').each(function( index ) {
            $(this).val("");
        });
    });
});
