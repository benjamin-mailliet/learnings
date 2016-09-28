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

var getAppelsLocalStorage = function () {
    var appelsSauvegardes = localStorage.getItem("appels");
    if (!appelsSauvegardes) {
        appelsSauvegardes = {};
    } else {
        appelsSauvegardes = JSON.parse(appelsSauvegardes);
    }
    return appelsSauvegardes;
};

var enregistrerSauvegardeLocale = function() {
    console.log("sauvegarde");
    var appels = {};
    $(".radio-appel:checked").each(function () {
        var radio = $(this)[0];
        appels[radio.name] = radio.value;
    });
    console.log(JSON.stringify(appels));

    var appelsSauvegardes = getAppelsLocalStorage();
    appelsSauvegardes['seance_'+getParameterByName("idSeance")] = appels;
    localStorage.setItem("appels", JSON.stringify(appelsSauvegardes));
};

var chargerSauvegardeLocale = function() {
    var appelsSauvegardes = getAppelsLocalStorage();
    var appelsSeance = appelsSauvegardes['seance_' + getParameterByName("idSeance")];
    for (var radioName in appelsSeance) {
        if (appelsSeance.hasOwnProperty(radioName)) {
            $("input[name='"+radioName+"'][value=" + appelsSeance[radioName] + "]").prop('checked', true).change();
        }
    }
};

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

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

    $("#form-appel").on("submit", function () {
        enregistrerSauvegardeLocale();
    });

    $("#boutonChargementSauvegardeLocale").on("click", function () {
        chargerSauvegardeLocale();
    });
});