package Logica;

import java.io.Serializable;

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idMenu;         
    private String nombre;      
    private double precio;      
    private boolean disponible; 

    public Menu() {
    }

    public Menu(int idMenu, String nombre, double precio, boolean disponible) {
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    // Getters y Setters

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
