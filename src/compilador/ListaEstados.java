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
public class ListaEstados {
    private ArrayList<Estado> estados;

    public ListaEstados() 
    {
        
    }

    public ArrayList<Estado> getEstados() 
    {
        return estados;
    }

    public void setEstados(ArrayList<Estado> Estados) 
    {
        for (Estado Estado : Estados) {
            this.estados.add(Estado);
        }
    }    
}
