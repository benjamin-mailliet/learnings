<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="./">Learnings</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="${param.pageSelectionnee == 'seances' ? 'active' : ''}"><a href="seances">Séances</a></li>
				<li class="${param.pageSelectionnee == 'remisetp' ? 'active' : ''}"><a href="remisetp">Rendre un TP</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right ">
				<c:if test="${sessionScope.utilisateur.admin}">
					<li><a href="../admin/">Administration</a></li>
				</c:if>
				<li class="dropdown">
          			<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-cog"></span></a>
		          	<ul class="dropdown-menu" role="menu">
		            	<li><a href="compte">Mon compte</a></li>
		            	<li class="divider"></li>
						<li><a href="../deconnexion">Déconnexion</a></li>
		          	</ul>
       		 	</li>
			</ul>
		</div>
	</nav>