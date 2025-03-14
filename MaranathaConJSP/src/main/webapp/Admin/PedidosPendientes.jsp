<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pedidos Pendientes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Archivos Css/EstiloPedidosPendientes.css">
</head>
<body>
    <div class="container">
        <h1>Pedidos Pendientes</h1>
        
        <c:if test="${not empty mensajeExito}">
            <div class="mensaje exito">${mensajeExito}</div>
        </c:if>
        <c:if test="${not empty mensajeError}">
            <div class="mensaje error">${mensajeError}</div>
        </c:if>
        
        <table>
            <tr>
                <th>ID Pedido</th>
                <th>Cliente</th>
                <th>Teléfono</th>
                <th>Menú Elegido</th>
                <th>Método de Pago</th>
                <th>Es Domicilio</th>
                <th>Dirección</th>
                <th>Mesa</th>
                <th>Acciones</th>
            </tr>
            <c:forEach var="pedido" items="${pedidosPendientes}">
                <tr>
                    <td>${pedido.idPedido}</td>
                    <td>${pedido.cliente.nombre}</td>
                    <td>${pedido.cliente.telefono}</td>
                    <td>${pedido.menu.nombre}</td>
                    <td>${pedido.metodoPago}</td>
                    <td>
                        <c:choose>
                            <c:when test="${pedido.esDomicilio}">Sí</c:when>
                            <c:otherwise>No</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${pedido.direccion}</td>
                    <td>
                        <c:if test="${not empty pedido.mesa}">
                            Mesa ${pedido.mesa.numero}
                        </c:if>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/PedidosPendientesServlet" method="post" onsubmit="return confirm('¿Seguro que deseas eliminar este pedido?');">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="idPedido" value="${pedido.idPedido}">
                            <button type="submit" class="btn">Eliminar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>
