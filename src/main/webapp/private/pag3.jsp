<%@ page session="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Página 3 · web_auth</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="../css/bootstrap.min.css" rel="stylesheet" />
<style type="text/css">
.content {
    margin-top: 30%;
}
</style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <div class="content">
				<h1>Página 3</h1>
				<form action="/logout" method="post">
		    		<input class="btn btn-lg btn-success" type="submit" name="submit" value="Cerrar sessión">
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
