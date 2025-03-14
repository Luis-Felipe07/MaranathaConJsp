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
public class Pedido implements Serializable {
     private static final long serialVersionUID = 1L;
    
  
    private int idPedido;
    private Cliente cliente;     
    private Menu menu;           
    private String metodoPago;   
    private boolean esDomicilio; 
    private String direccion;   
    private Mesa mesa;          

    // Constructor vacío 
    public Pedido() {
    }

    // Constructor con parametros
    public Pedido(int idPedido, Cliente cliente, Menu menu,
                  String metodoPago, boolean esDomicilio,
                  String direccion, Mesa mesa) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.menu = menu;
        this.metodoPago = metodoPago;
        this.esDomicilio = esDomicilio;
        this.direccion = direccion;
        this.mesa = mesa;
    }

    // Getters y Setters

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean isEsDomicilio() {
        return esDomicilio;
    }

    public void setEsDomicilio(boolean esDomicilio) {
        this.esDomicilio = esDomicilio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
