<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Fichiers utiles</title>
		<c:import url="../includes/htmlheader.jsp" />
		<link href="../css/fichiers-utiles.css" rel="stylesheet">
		<script src="../js/fichiers-utiles.js"></script>
	</head>
	<body>
		<c:import url="../includes/menuprincipal.jsp">
			<c:param name="pageSelectionnee" value="fichiers"/>
		</c:import>		
		<div class="container">
			<header class="page-header"><h1>Cours et TPs</h1></header>
			<section class="panel-group">
				<article class="cours-article panel panel-default">
					<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#cours1">
						<h4><span  class="glyphicon glyphicon-folder-close"></span> Chapitre 1 : HTML / CSS <small>(cliquez pour ouvrir)</small></h4>
						<time>12/09/2014</time>
					</header>
					<section class="details panel-body collapse" id=cours1>
						<ul class="chapters">
							<li>Rappel HTML</li>
							<li>HTML</li>
							<li>Rappel CSS</li>
							<li>CSS 3</li>
						</ul>
						<a href="#">Lien de téléchargement du cours</a>
					</section>
				</article>
				<article class="tp-article panel panel-default">
					<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#tp1">
						<h4><span  class="glyphicon glyphicon-cog"></span> TP 1 : HTML / CSS - Les animaux laids <small>(cliquez pour ouvrir)</small></h4>
						<time>12/09/2014</time>
					</header>
					<section class="details panel-body collapse" id=tp1>
						<a href="#">Lien de téléchargement du TP</a>
					</section>
				</article>
				<article class="tp-article panel panel-default">
					<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#tp2">
						<h4><span  class="glyphicon glyphicon-cog"></span> TP 2 : CSS / Bootstrap - Cursus culturel <small>(cliquez pour ouvrir)</small></h4>
						<time>19/09/2014</time>
					</header>
					<section class="details panel-body collapse" id=tp2>
						<a href="#">Lien de téléchargement du TP</a>
					</section>
				</article>
				
				<article class="cours-article panel panel-default">
					<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#cours2">
						<h4><span class="glyphicon glyphicon-folder-close"></span> Chapitre 2 : Javascript <small>(cliquez pour ouvrir)</small></h4>
						<time>10/10/2014</time>
					</header>
					<section class="details panel-body collapse" id=cours2>
						<ul class="chapters">
							<li>Syntaxe</li>
							<li>??????</li>
							<li>??????</li>
							<li>??????</li>
							<li>??????</li>
						</ul>
						<a href="#">Lien de téléchargement du cours</a>
					</section>
				</article>
			</section>
		</div>
	</body>
</html>