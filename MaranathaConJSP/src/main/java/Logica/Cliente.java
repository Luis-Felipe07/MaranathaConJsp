package Logica;

import java.io.Serializable;

public class Cliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String telefono;
    private String email;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters para id
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    // Getters y Setters para nombre
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters para teléfono
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
