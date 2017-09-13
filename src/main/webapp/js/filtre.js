var filter = function(elementToFilter, stringToFilter) {
    $(".hidden").each(function (index, element) {
        $(element).removeClass("hidden");
    });

    $(".displayed").each(function (index, element) {
        $(element).removeClass("displayed");
    });

    $(".filter[data-filter='"+elementToFilter+"']").each(function (index, element) {
        var jqElement = $(element);
        var jqParent = $(jqElement.parent());
        if ($(jqElement).text().toUpperCase().indexOf(stringToFilter.toUpperCase()) > -1) {
            jqParent.removeClass("hidden");
            jqParent.addClass("displayed");
        } else {
            if (!jqParent.hasClass("hidden") && !jqParent.hasClass("displayed")) {
                jqParent.addClass("hidden");
            }
        }
    });
};

$(document).ready(function () {
    $(".query-filter").keyup(function(){
        filter($(this).attr("data-query-filter"), $( this ).val());
    });
});