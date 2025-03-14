<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String requestURI = request.getRequestURI();
    String servletPath = request.getContextPath() + "/PedidoServlet";

    // se accede directamente al JSP, y redirijo al servlet
    if (requestURI.endsWith("HacerPedido.jsp")
            && request.getAttribute("listaMenus") == null) {
        response.sendRedirect(servletPath);
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hacer Pedido</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Archivos Css/EstiloPedido.css">
    </head>
    <body>
        <div class="pedido-container">
            <h1>Menús del Día</h1>

            
            <div class="pedidos-predeterminados">

                <div class="pedidos-imagenes">
                    <img src="${pageContext.request.contextPath}/img/bandeja paisa.jpg" alt="Pedido Predeterminado 1">
                    <img src="${pageContext.request.contextPath}/img/carne asada.jpg" alt="Pedido Predeterminado 2">
                    <img src="${pageContext.request.contextPath}/img/carne molida.png" alt="Pedido Predeterminado 3">
                </div>
            </div>

            <!-- Sección del formulario: se mueve todo el contenido dentro del <form> -->
            <div class="pedido-form">
                <form id="pedidoForm" action="${pageContext.request.contextPath}/PedidoServlet" method="post">
                    
                    <div class="menus-grid">
                        <div class="menu-select-block">
                            <label for="menus">Seleccione un menú</label>
                            <select name="menus" id="menus">
                                <option value="">-- Seleccione --</option>
                                <c:forEach var="menu" items="${listaMenus}">
                                   
                                    <option value="${menu.idMenu}">${menu.nombre} - $${menu.precio}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Datos adicionales para el pedido -->
                    <label for="mesa">Seleccione un número de mesa (si comerá en el restaurante)</label>
                    <select name="mesa" id="mesa">
                        <option value="">Seleccione...</option>
                        <c:forEach var="mesa" items="${mesasDisponibles}">
                            <option value="${mesa.numero}">Mesa ${mesa.numero}</option>
                        </c:forEach>
                    </select>

                    <label for="metodoPago">Seleccione un método de pago</label>
                    <select name="metodoPago" id="metodoPago">
                        <option value="Daviplata"  disabled selected>-------</option>
                        <option value="Pse">Pse</option>
                        <option value="Nequi">Nequi</option>
                        <option value="Bancolombia">Bancolombia</option>
                        <option value="Daviplata">Daviplata</option>
                        <option value="Efectivo">Efectivo</option>
                    </select>

                    <label for="esDomicilio">¿El pedido es a domicilio?</label>
                    <input type="checkbox" name="esDomicilio" id="esDomicilio" value="Si" />

                    <label for="nombre">Nombre Completo</label>
                    <input type="text" name="nombre" id="nombre" placeholder="Ingrese su nombre">

                    <label for="telefono">Teléfono</label>
                    <input type="text" name="telefono" id="telefono" placeholder="Ingrese su teléfono">

                    <label for="direccion">Dirección (si es a domicilio)</label>
                    <input type="text" name="direccion" id="direccion" placeholder="Ingrese su dirección">

                    <button type="submit">Confirmar Pedido</button>
                </form>
            </div>

            <!-- Mensajes de éxito o error -->
            <c:if test="${not empty mensajeExito}">
                <div class="mensaje-exito">${mensajeExito}</div>
            </c:if>
            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>
        </div>



        <script>
    // Referencias a los elementos del formulario
    const mesaSelect = document.getElementById('mesa');
    const esDomicilioCheckbox = document.getElementById('esDomicilio');
    const direccionField = document.getElementById('direccion');
    const direccionLabel = document.querySelector('label[for="direccion"]');
    
    // Función para manejar la visibilidad del campo de dirección
    function actualizarCampoDireccion() {
        if (esDomicilioCheckbox.checked) {
            direccionField.style.display = 'block';
            direccionLabel.style.display = 'block';
            direccionField.setAttribute('required', 'required');
        } else {
            direccionField.style.display = 'none';
            direccionLabel.style.display = 'none';
            direccionField.removeAttribute('required');
            direccionField.value = '';
        }
    }
    
    // Evento para cuando se selecciona una mesa
    mesaSelect.addEventListener('change', function() {
        if (this.value !== '') {
            
            esDomicilioCheckbox.checked = false;
            esDomicilioCheckbox.disabled = true;
        } else {
          
            esDomicilioCheckbox.disabled = false;
        }
        
       
        actualizarCampoDireccion();
    });
    
    // Evento para cuando se marca/desmarca domicilio
    esDomicilioCheckbox.addEventListener('change', function() {
        if (this.checked) {
            
            mesaSelect.value = '';
            mesaSelect.disabled = true;
        } else {
            
            mesaSelect.disabled = false;
        }
        
       
        actualizarCampoDireccion();
    });
    
    // Inicialización al cargar la página
    window.onload = function() {
        
        actualizarCampoDireccion();
        
        // Verificar estado inicial de la mesa y domicilio
        if (mesaSelect.value !== '') {
            esDomicilioCheckbox.disabled = true;
        } else if (esDomicilioCheckbox.checked) {
            mesaSelect.disabled = true;
        }
    };
    
    // Validación del formulario antes de enviar
    document.getElementById('pedidoForm').addEventListener('submit', function(event) {
        
        const tieneMesa = mesaSelect.value !== '';
        const esDomicilio = esDomicilioCheckbox.checked;
        
        if (!tieneMesa && !esDomicilio) {
            alert('Debe seleccionar una mesa o marcar el pedido como domicilio');
            event.preventDefault();
            return;
        }
        
        // Verificar que si es domicilio tenga dirección
        if (esDomicilio && direccionField.value.trim() === '') {
            alert('Si el pedido es a domicilio, debe proporcionar una dirección');
            direccionField.focus();
            event.preventDefault();
        }
    });
</script>         
    </body>
</html>
