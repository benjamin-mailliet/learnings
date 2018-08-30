$(document).ready(function(){
    var coursBodies = $(".cours-article .card-body");
    coursBodies.on('show.bs.collapse', function () {
		$(this).parent().children(".card-header").children(".title").children("h2").children("span.icone").removeClass("fa-folder").addClass("fa-folder-open");
        $(this).parent().addClass("expanded");
	});
	
	coursBodies.on('hide.bs.collapse', function () {
		$(this).parent().children(".card-header").children(".title").children("h2").children("span.icone").addClass("fa-folder").removeClass("fa-folder-open");
        $(this).parent().removeClass("expanded");
	});

	var tpBodies = $(".tp-article .card-body");
    tpBodies.on('show.bs.collapse', function () {
		$(this).parent().addClass("rotated");
        $(this).parent().addClass("expanded");
	});
    tpBodies.on('hide.bs.collapse', function () {
		$(this).parent().removeClass("rotated");
        $(this).parent().removeClass("expanded");
	});
});

