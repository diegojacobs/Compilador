/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Transicion<T> {
    private Estado origen;
    private Estado destino;
    private T simb;

    public Transicion(Estado ant, Estado sig, T t) {
        this.origen = ant;
        this.destino = sig;
        this.simb = t;
    }

    public Estado getOrigen() {
        return origen;
    }

    public void setOrigen(Estado ant) {
        this.origen = ant;
    }

    public Estado getDestino() {
        return destino;
    }

    public void setDestino(Estado sig) {
        this.destino = sig;
    }

    public T getT() {
        return simb;
    }

    public void setT(T t) {
        this.simb = t;
    }
    
    @Override
    public String toString()
    {
        if (origen.getId() == -1 || destino.getId()== -1)
            return "(" + origen.getNum()+ " --" + simb + "--> " + destino.getNum() + ")";
        else
            return "(" + origen.getId()+ " --" + simb + "--> " + destino.getId() + ")";
    }
}
