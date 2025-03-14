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
public class MenusVendidosDTO implements Serializable {
    
    private String nombreMenu;
    private int totalVentas;

    public MenusVendidosDTO(String nombreMenu, int totalVentas) {
        this.nombreMenu = nombreMenu;
        this.totalVentas = totalVentas;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public int getTotalVentas() {
        return totalVentas;
    }
}
