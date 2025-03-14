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
public class Mesa implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int idMesa;     
    private int numero;     
    private boolean ocupada; 

    public Mesa() {
    }

    public Mesa(int idMesa, int numero, boolean ocupada) {
        this.idMesa = idMesa;
        this.numero = numero;
        this.ocupada = ocupada;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
}
