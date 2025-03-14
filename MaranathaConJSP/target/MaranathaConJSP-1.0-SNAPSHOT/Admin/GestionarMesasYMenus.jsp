<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String requestURI = request.getRequestURI();
    String servletPath = request.getContextPath() + "/GestionarMesasYMenusServlet";
    
   
    if (requestURI.endsWith("GestionarMesasYMenus.jsp") && 
        request.getAttribute("listaMenus") == null) {
        response.sendRedirect(servletPath);
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Mesas y Menús</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Archivos Css/EstilosGestionar.css">
</head>
<body>
    <div class="gestion-container">
        <h1>GESTIÓN DE MESAS Y MENÚS</h1>
        
        
        <c:if test="${not empty mensajeExito}">
            <p class="exito">${mensajeExito}</p>
        </c:if>
        
        <div class="gestion-panels">
           
            <div class="panel">
                <h2>Mesas Ocupadas</h2>
                <form action="${pageContext.request.contextPath}/GestionarMesasYMenusServlet" method="post">
                    <input type="hidden" name="accion" value="liberarMesa">
                    <select name="idMesa">
                        <option value="">Seleccione una mesa</option>
                        <c:forEach var="mesa" items="${mesasOcupadas}">
                            <option value="${mesa.idMesa}">Mesa ${mesa.numero}</option>
                        </c:forEach>
                    </select>
                    <button type="submit">Liberar Mesa</button>
                </form>
                <c:if test="${not empty mesaError}">
                    <p class="error">${mesaError}</p>
                </c:if>
            </div>

           
            <div class="panel">
                <h2>Agregar Nuevo Menú</h2>
                <form action="${pageContext.request.contextPath}/GestionarMesasYMenusServlet" method="post">
                    <input type="hidden" name="accion" value="agregarMenu">
                    <label for="nuevoMenu">Ingrese el nuevo menú</label>
                    <input type="text" id="nuevoMenu" name="nombreMenu" required>
                    
                    <label for="precioMenu">Ingrese el precio</label>
                    <input type="text" id="precioMenu" name="precioMenu" required>
                    
                    <button type="submit">Agregar</button>
                </form>
                <c:if test="${not empty menuError}">
                    <p class="error">${menuError}</p>
                </c:if>
            </div>

           
            <div class="panel">
                <h2>Eliminar Menú</h2>
                <form action="${pageContext.request.contextPath}/GestionarMesasYMenusServlet" method="post">
                    <input type="hidden" name="accion" value="eliminarMenu">
                    <label for="menuEliminar">Ingrese el nombre del menú</label>
                    <input type="text" id="menuEliminar" name="menuEliminar" required>
                    <button type="submit">Eliminar</button>
                </form>
                <c:if test="${not empty menuError}">
                    <p class="error">${menuError}</p>
                </c:if>
            </div>

            
            <div class="panel">
                <h2>Mesas Disponibles</h2>
                <ul>
                    <c:forEach var="mesa" items="${mesasDisponibles}">
                        <li>Mesa ${mesa.numero}</li>
                    </c:forEach>
                </ul>
            </div>
            
            
            <div class="panel">
                <h2>Menús Disponibles</h2>
                <ul>
                    <c:forEach var="menu" items="${listaMenus}">
                        <li>${menu.nombre} - $${menu.precio}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>
