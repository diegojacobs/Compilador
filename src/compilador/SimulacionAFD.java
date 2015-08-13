/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * @author Diego Jacobs 13160
 */
public class SimulacionAFD<T> {
    private AutomataFD auto;
    private String exp;
    private ArrayList<EstadoFD> alcanzados;
    
    public SimulacionAFD(AutomataFD at, String cadena)
    {
        this.auto = at;
        this.exp = cadena;
    }

    public AutomataFD getAutoFD() {
        return auto;
    }

    public void setAutoFD (AutomataFD auto) {
        this.auto = auto;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public ArrayList<EstadoFD> getAlcanzados() {
        return alcanzados;
    }

    public void setAlcanzado(EstadoFD alcanzado) {
        this.alcanzados.add(alcanzado);
    }
    
    public EstadoFD move(EstadoFD estado, Character c)
    {
        EstadoFD visitados = new EstadoFD();
        
        //Mandamos a traer los enlaces de nuestr estado
        ArrayList<TransicionFD> enlaces = estado.getEnlaces();
            
        for (TransicionFD enlace : enlaces) 
        {
            if (enlace.getSimbolo() == c)
            {
                visitados = enlace.getDestino();
            }
        }
        
        
        return visitados;
    }
    
    
    //Revisamos si el alfabeto acepta la cadena ingresada
    public boolean SimularFD()
    {
        EstadoFD estados = new EstadoFD(auto.getInicio().getEstados());
       
        estados = auto.getInicio();
        //estados.add(auto.getInicio());
        
        for (Character c : exp.toCharArray())
        {
            estados = move(estados,c);
        }
        
        boolean flag = false;
        for (EstadoFD fin : auto.getFin()) 
        {
            if (estados.equals(fin))
                flag = true;
        }
        return flag;
    }    
}
