$(document).ready(function(){
	$(".cours-article .card-body").on('show.bs.collapse', function () {
		$(this).parent().children(".card-header").children("h4").children("span.icone").removeClass("fa-folder").addClass("fa-folder-open");
	});
	
	$(".cours-article .card-body").on('hide.bs.collapse', function () {
		$(this).parent().children(".card-header").children("h4").children("span.icone").addClass("fa-folder").removeClass("fa-folder-open");
	});
	
	$(".tp-article .card-body").on('show.bs.collapse', function () {
		$(this).parent().addClass("rotated");
	});
	$(".tp-article .card-body").on('hide.bs.collapse', function () {
		$(this).parent().removeClass("rotated");
	});
});

