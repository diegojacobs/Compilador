/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Automata {
    private Estado inicio;
    private Estado fin;
    private ArrayList<Estado> estados = new ArrayList();
    private String alfabeto;

    public Automata() 
    {
        
    }
    
    public Automata(Estado ini, Estado fin)
    {
        this.inicio = ini;
        this.fin = fin;
    }
    
    public Estado getInicio() {
        return inicio;
    }

    public void setInicio(Estado inicio) {
        this.inicio = inicio;
    }

    public Estado getFin() {
        return fin;
    }

    public void setFin(Estado fin) {
        this.fin = fin;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstado(Estado estado) {
        this.estados.add(estado);
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }
    
    @Override
    public String toString()
    {
        String res = new String();
        res += "ESTADOS = " + estados.toString() + "\r\n";
        res += "SIMBOLOS = ["+ this.alfabeto + "]\r\n";
        res += "INICIO = " +  inicio + "\r\n";
        res += "ACEPTACION = " + fin + "\r\n";
        res += "TRANSICION = ";
        res = this.estados.stream().map((est) -> est.getEnlaces()+"-").reduce(res, String::concat);
        
        return res;
    }
}
