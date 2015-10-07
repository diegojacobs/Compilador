/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * @author Diego Jacobs 13160
 */
public class SimulacionAFD<T> {
    private Automata auto;
    private String exp;
    private ArrayList<EstadoFD> alcanzados;
    
    public SimulacionAFD(Automata at, String cadena)
    {
        this.auto = at;
        this.exp = cadena;
    }

    public Automata getAutoFD() {
        return auto;
    }

    public void setAutoFD (Automata auto) {
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
    
    public Estado move(Estado estado, T c)
    {
        Estado visitados = new Estado(null);
        
        //Mandamos a traer los enlaces de nuestr estado
        ArrayList<Transicion> enlaces = estado.getEnlaces();
            
        for (Transicion enlace : enlaces) 
        {
            if (enlace.getT() == c)
            {
                visitados = enlace.getDestino();
            }
        }
        
        
        return visitados;
    }
    
    
    //Revisamos si el alfabeto acepta la cadena ingresada
    public boolean SimularFD()
    {
        Estado estados = new Estado(auto.getInicio());
       
        estados = auto.getInicio();
        //estados.add(auto.getInicio());
        
        for (Character c : exp.toCharArray())
        {
            estados = move(estados,(T)c);
        }
        
        boolean flag = false;
        for (Estado fin : auto.getFin()) 
        {
            if (estados.equals(fin))
                flag = true;
        }
        return flag;
    }    
}
