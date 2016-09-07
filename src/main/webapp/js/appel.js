var purgerClasses = function(element) {
    $(element).removeClass("success");
    $(element).removeClass("info");
    $(element).removeClass("warning");
    $(element).removeClass("danger");
};


var appliquerStyle = function(trElement, nouveauStyle) {
    $(trElement).children("td").each(function (index, element) {
        purgerClasses(element);
        if($(element).hasClass("cellule-generale") || $(element).hasClass("text-"+nouveauStyle)) {
            $(element).addClass(nouveauStyle);
        }
    });

};

$(document).ready(function () {
    $(".radio-present").change(function () {
        appliquerStyle($(this).parents("tr"), "success");
    });

    $(".radio-retard").change(function () {
        appliquerStyle($(this).parents("tr"), "warning");
    });

    $(".radio-excuse").change(function () {
        appliquerStyle($(this).parents("tr"), "info");
    });

    $(".radio-absent").change(function () {
        appliquerStyle($(this).parents("tr"), "danger");
    });

    $(".radio-non-saisi").change(function () {
        appliquerStyle($(this).parents("tr"), "active");
    });
});