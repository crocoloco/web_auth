<%@ page session="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Login · web_auth</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="css/bootstrap.min.css" rel="stylesheet" />
<style type="text/css">
.form-login {
    max-width: 330px;
    padding: 15px;
    margin: 0 auto;
}
.card-login {
    margin-top: 40%;
    padding: 0px 0px 20px 0px;
    background-color: #f7f7f7;
    -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
    -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}
</style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <div class="card-login">
                <img class="profile-img" src="img/loginIcon.png"
                    alt="">
                <form class="form-login" action="/login" method="post">
                    <div class="form-group">
			    	    <input class="form-control" placeholder="Usuario" name="username" type="text">
                    </div>
			    	<div class="form-group">
                        <input class="form-control" placeholder="Password" name="password" type="password" value="">
                    </div>
		    		<input class="btn btn-lg btn-success btn-block" type="submit" name="submit" value="Entrar">
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
