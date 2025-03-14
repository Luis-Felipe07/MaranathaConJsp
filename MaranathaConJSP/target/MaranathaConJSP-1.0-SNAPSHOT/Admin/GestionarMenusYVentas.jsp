<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Total Menús Vendidos</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Archivos Css/EstiloMenuYVentas.css">
</head>
<body>
    <div class="myv-container">
        <h1>TOTAL MENÚS VENDIDOS</h1>
        
        
        <c:if test="${not empty mensajeExito}">
            <p class="exito">${mensajeExito}</p>
        </c:if>
        <c:if test="${not empty mensajeError}">
            <p class="error">${mensajeError}</p>
        </c:if>
        
        <div class="myv-panels">
            
            <div class="panel">
                <h2>Menús Vendidos</h2>
                <table>
                    <tr>
                        <th>Menú</th>
                        <th>Total Ventas</th>
                    </tr>
                    <c:if test="${not empty menusVendidos}">
                        <c:forEach var="dto" items="${menusVendidos}">
                            <tr>
                                <td><c:out value="${dto.nombreMenu}" /></td>
                                <td><c:out value="${dto.totalVentas}" /></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
            </div>
            
           
            <div class="panel">
                <h2>Total Ganancias En Ventas</h2>
                <c:if test="${not empty totalGanancias}">
                    <p>
                        <strong>$ <c:out value="${totalGanancias}" /></strong>
                    </p>
                </c:if>
            </div>
        </div>
        
        
        <div class="myv-button-container">
            <form method="post" action="${pageContext.request.contextPath}/GestionarMenusYVentasServlet">
                <input type="hidden" name="accion" value="verDatos" />
                <button type="submit">Ver Menús Vendidos y Ganancias</button>
            </form>
        </div>
    </div>
</body>
</html>
