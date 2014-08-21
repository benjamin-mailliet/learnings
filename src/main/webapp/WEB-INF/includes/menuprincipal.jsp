<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="./">Gestionnaire</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="${param.pageSelectionnee == 'fichiers' ? 'active' : ''}"><a href="fichiers">Fichiers</a></li>
				<li class="${param.pageSelectionnee == 'remisetp' ? 'active' : ''}"><a href="remisetp">Rendre son TP</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<c:if test="${sessionScope.utilisateur.admin}">
					<li><a href="../admin/">Administration</a></li>
				</c:if>
				<li><a href="../deconnexion">Déconnexion</a></li>
			</ul>
		</div>
	</nav>