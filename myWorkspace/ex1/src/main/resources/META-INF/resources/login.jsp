<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous"/>
		<title>Authentification</title>
	</head>
	<body>
		<div style="width:400px;margin:auto">
			<form method="post">
			
				<label for="username">Username</label>
				<input type="text" name="username" class="form-control" id="username"/>
				
				<label for="password">Password</label>
				<input type="password" name="password" class="form-control" id="password"/>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input type="hidden" name="redirectUrl" value="${header['referer']}"/>
	
				<input type="submit" class="btn btn-primary" style="margin-top:25px" value="Log in"/>
			</form>
		</div>
	</body>
</html>