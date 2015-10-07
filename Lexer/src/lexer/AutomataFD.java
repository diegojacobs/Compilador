/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs 13160
 */
public class AutomataFD {
    private EstadoFD inicio;
    private ArrayList<EstadoFD> fin;
    private ArrayList<EstadoFD> subconjuntos;
    
    private String alfabeto;

    public AutomataFD() 
    {
        
    }
    
    public AutomataFD(EstadoFD ini, ArrayList<EstadoFD> end, ArrayList<EstadoFD> estados, String alfa)
    {
        inicio = ini;
        fin = end;
        subconjuntos = estados;
        alfabeto = alfa;
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }

    public EstadoFD getInicio() {
        return inicio;
    }

    public void setInicio(EstadoFD inicio) {
        this.inicio = inicio;
    }

    public ArrayList<EstadoFD> getFin() {
        return fin;
    }

    public void setFin(EstadoFD fin) {
        this.fin.add(fin);
    }

    public ArrayList<EstadoFD> getSubconjuntos() {
        return subconjuntos;
    }

    public void setSubconjuntos(EstadoFD subconjunto) {
        this.subconjuntos.add(subconjunto);
    }

    
    
    
    @Override
    public String toString()
    {
        String res = new String();
        res += "ESTADOS = " + subconjuntos.toString() + "\r\n";
        res += "SIMBOLOS = ["+ alfabeto + "]\r\n";
        res += "INICIO = [" +  inicio + "]\r\n";
        res += "ACEPTACION = " + fin + "\r\n";
        res += "TRANSICION = ";
        res = this.subconjuntos.stream().map((est) -> est.getEnlaces()+"-").reduce(res, String::concat);
        
        return res;
    }
    
}
