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
    private ArrayList<Estado> fin = new ArrayList();
    private ArrayList<Estado> nofin = new ArrayList();
    private ArrayList<Estado> estados = new ArrayList();
    private String alfabeto;

    public Automata() 
    {
        
    }
    
    public Automata(Estado ini, Estado fin)
    {
        this.inicio = ini;
        this.fin.add(fin);
    }
    
    public Automata(Estado ini, ArrayList<Estado> fin, ArrayList<Estado> states, String alfa)
    {
        this.inicio = ini;
        this.fin = fin;
        this.estados = states;
        this.alfabeto = alfa;
    }
    
    public Estado getInicio() {
        return inicio;
    }

    public void setInicio(Estado inicio) {
        this.inicio = inicio;
    }

    public ArrayList<Estado> getFin() {
        return fin;
    }

    public void setFin(Estado fin) {
        this.fin.add(fin);
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

    public ArrayList<Estado> getNofin() {
        return nofin;
    }

    public void setNofin(Estado nofin) {
        this.nofin.add(nofin);
    }
    
    public Estado findEstadobyId(int id)
    {
        Estado state = new Estado(null);
        for (Estado temp : estados)
            if (temp.getId() == id)
                state=temp;
        
        return state;
    }
    
    @Override
    public String toString()
    {
        String res = new String();
        res += "ESTADOS = " + estados.toString() + "\r\n";
        res += "SIMBOLOS = ["+ this.alfabeto + "]\r\n";
        res += "INICIO = " +  inicio.getId() + "\r\n";
        res += "ACEPTACION = [";
        for(Estado s : fin)
        {
            res += s.getId() + ",";
        }
        res +="]\r\n";
        res += "TRANSICION = ";
        res = this.estados.stream().map((est) ->est.getEnlaces()+", ").reduce(res, String::concat);
        
        return res;
    }
}
