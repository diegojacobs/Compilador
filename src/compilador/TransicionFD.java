/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diego Jacobs 13160
 */
public class TransicionFD {
    private EstadoFD origen;
    private EstadoFD destino;
    private Character simb;

    public TransicionFD(EstadoFD ant, EstadoFD sig, Character t) {
        this.origen = ant;
        this.destino = sig;
        this.simb = t;
    }

    public EstadoFD getOrigen() {
        return origen;
    }

    public void setOrigen(EstadoFD ant) {
        this.origen = ant;
    }

    public EstadoFD getDestino() {
        return destino;
    }

    public void setDestino(EstadoFD sig) {
        this.destino = sig;
    }

    public Character getSimbolo() {
        return simb;
    }

    public void setSimbolo(Character t) {
        this.simb = t;
    }
    
    @Override
    public String toString()
    {
        return "(" + origen.getEstados()+ ", " + simb + ", " + destino.getEstados() + ")";
    }
}
