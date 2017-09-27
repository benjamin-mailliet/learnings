var filter = function(elementToFilter, stringToFilter) {
    var hiddenClass = "hidden-" + elementToFilter;
    var displayedClass = "displayed-"+elementToFilter;

    $("."+hiddenClass).each(function (index, element) {
        $(element).removeClass(hiddenClass);
    });

    $("."+displayedClass).each(function (index, element) {
        $(element).removeClass(displayedClass);
    });

    $(".filter[data-filter='"+elementToFilter+"']").each(function (index, element) {
        var jqElement = $(element);
        var jqParent = $(jqElement.parent());
        if ($(jqElement).text().toUpperCase().indexOf(stringToFilter.toUpperCase()) > -1) {
            jqParent.removeClass(hiddenClass);
            jqParent.addClass(displayedClass);
        } else {
            if (!jqParent.hasClass(hiddenClass) && !jqParent.hasClass(displayedClass)) {
                jqParent.addClass(hiddenClass);
            }
        }
    });
};

$(document).ready(function () {
    $("input.query-filter").keyup(function(){
        filter($(this).attr("data-query-filter"), $( this ).val());
    });
    $("select.query-filter").change(function(){
        filter($(this).attr("data-query-filter"), $( this ).val());
    });
});