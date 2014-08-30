<%@page contentType="text/html" pageEncoding="UTF-8"%>
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="./">Administration</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="${param.pageSelectionnee == 'utilisateur' ? 'active' : ''}"><a href="utilisateur">Gestion des utilisateurs</a></li>
				<li class="${param.pageSelectionnee == 'travailtp' ? 'active' : ''}"><a href="travailtp">Travaux rendus</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="../eleve/">Site principal</a></li>
				<li class="dropdown">
          			<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-cog"></span></a>
		          	<ul class="dropdown-menu" role="menu">
		            	<li><a href="../eleve/compte">Mon compte</a></li>
		            	<li class="divider"></li>
						<li><a href="../deconnexion">Déconnexion</a></li>
		          	</ul>
       		 	</li>
			</ul>
		</div>
	</nav>