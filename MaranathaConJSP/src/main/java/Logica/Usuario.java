/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;

/**
 *
 * @author Luis Felipe
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombreUsuario;
    private String contrasenia;
    private String tipo; 

    // Constructor
    public Usuario(String nombreUsuario, String contrasenia, String tipo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.tipo = tipo;
    }

    // Getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getTipo() {
        return tipo;
    }

    // Métodos para verificar el tipo de usuario
    public boolean esAdmin() {
        return "admin".equals(tipo);
    }

    public boolean esCliente() {
        return "cliente".equals(tipo);
    }
}
