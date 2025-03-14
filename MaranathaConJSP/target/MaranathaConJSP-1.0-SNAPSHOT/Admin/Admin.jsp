<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Panel de Administración</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Archivos Css/EstilosAdmin.css">
</head>
<body>
    <div class="admin-container">
        <h1>BIENVENIDO ADMIN</h1>
        <p>¿Qué deseas hacer hoy?</p>

        <div class="admin-buttons">
           
            <a href="${pageContext.request.contextPath}/Admin/GestionarMenusYVentas.jsp" class="admin-btn">
                Ver Ventas y Menús Vendidos
            </a>
            
            
            <a href="${pageContext.request.contextPath}/Admin/GestionarMesasYMenus.jsp" class="admin-btn">
                Gestionar Menús y Mesas
            </a>
            
           
            <a href="${pageContext.request.contextPath}/PedidosPendientesServlet" class="admin-btn">
                Pedidos Pendientes
            </a>
        </div>
    </div>
</body>
</html>
