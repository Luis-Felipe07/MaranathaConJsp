package Controladores;

import AccesoDatos.UsuarioDAO;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
    /**
     * Método que unifica la lógica de autenticación para GET y POST.
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtenengo parámetros del formulario
        String usuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("contrasenia");

        // Autentico usuario usando UsuarioDAO
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioLogueado = usuarioDAO.autenticarUsuario(usuario, contrasenia);

        if (usuarioLogueado != null) {
            // Guardo en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuarioLogueado);

            // Redirigijó según el tipo de usuario
            if (usuarioLogueado.esAdmin()) {
                response.sendRedirect(request.getContextPath() + "/Admin/Admin.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/Cliente/HacerPedido.jsp");
            }
        } else {
            // si Credenciales inválidas
            request.setAttribute("error", "Credenciales inválidas. Intente nuevamente.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Servlet que maneja la autenticación de usuarios (admin o cliente).";
    }
}
