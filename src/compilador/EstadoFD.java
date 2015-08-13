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
public class EstadoFD {
    private ArrayList<Estado> estados;
    private ArrayList<TransicionFD> enlaces = new ArrayList();
    private int id;

    public EstadoFD()
    {
        
    }
    public EstadoFD(ArrayList<Estado> num)
    {
        this.estados = num;
    }
    public EstadoFD(ArrayList<Estado> num, ArrayList<TransicionFD> enlaces) {
        this.estados = num;
        this.enlaces = enlaces;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Estado> num) {
        this.estados = num;
    }

    public ArrayList<TransicionFD> getEnlaces() {
        return enlaces;
    }

    public void setEnlace(TransicionFD enlace) {
        this.enlaces.add(enlace);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        String states = new String();
        if (estados != null)
        {
            for (Estado estado : estados) 
                states += estado.toString()+",";
            states = states.substring(0, states.length()-1);
            
            return this.id+" = {"+states+"}";
        }
        else
            return states;
    }
    
}
