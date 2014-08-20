$(document).ready(function(){
	$(".cours-article .panel-body").on('show.bs.collapse', function () {
		$(this).parent().children(".panel-heading").children("h4").children("span.glyphicon").removeClass("glyphicon-folder-close").addClass("glyphicon-folder-open");
	});
	
	$(".cours-article .panel-body").on('hide.bs.collapse', function () {
		$(this).parent().children(".panel-heading").children("h4").children("span.glyphicon").addClass("glyphicon-folder-close").removeClass("glyphicon-folder-open");
	});
	
	$(".tp-article .panel-body").on('show.bs.collapse', function () {
		$(this).parent().addClass("rotated");
	});
	$(".tp-article .panel-body").on('hide.bs.collapse', function () {
		$(this).parent().removeClass("rotated");
	});
});

