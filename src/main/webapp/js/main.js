$(document).ready(function () {

    $("textarea.editeur-riche").sceditor({
        plugins: "xhtml",
        style: "../sceditor/jquery.sceditor.default.min.css",
        emoticonsEnabled:false,toolbar:"bold,italic,underline|size,color|bulletlist,orderedlist,code,quote,link,unlink|source"
    });

    $('[data-toggle="tooltip"], [data-tooltip="tooltip"]').tooltip()
});