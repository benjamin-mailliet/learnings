$(document).ready(function(){
	$(".cours-article .card-body").on('show.bs.collapse', function () {
		$(this).parent().children(".card-header").children(".title").children("h2").children("span.icone").removeClass("fa-folder").addClass("fa-folder-open");
        $(this).parent().addClass("expanded");
	});
	
	$(".cours-article .card-body").on('hide.bs.collapse', function () {
		$(this).parent().children(".card-header").children(".title").children("h2").children("span.icone").addClass("fa-folder").removeClass("fa-folder-open");
        $(this).parent().removeClass("expanded");
	});
	
	$(".tp-article .card-body").on('show.bs.collapse', function () {
		$(this).parent().addClass("rotated");
        $(this).parent().addClass("expanded");
	});
	$(".tp-article .card-body").on('hide.bs.collapse', function () {
		$(this).parent().removeClass("rotated");
        $(this).parent().removeClass("expanded");
	});
});

