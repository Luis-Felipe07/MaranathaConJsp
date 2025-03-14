<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" href="Archivos Css/EstiloLogin.css">
</head>
<body>
    <div class="login-container">
        <h1>Iniciar Sesión</h1>
        <% 
            String error = (String) request.getAttribute("error");
            if (error != null) { 
        %>
            <div class="error-message"><%= error %></div>
        <% } %>
        <form action="LoginServlet" method="post">
            <label for="username">Usuario</label>
            <input type="text" id="username" name="usuario" required>

            <label for="password">Contraseña</label>
            <input type="password" id="password" name="contrasenia" required>

            <button type="submit">Acceder</button>
        </form>
    </div>
</body>
</html>
