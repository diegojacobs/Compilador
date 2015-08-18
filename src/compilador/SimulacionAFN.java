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
public class SimulacionAFN<T> {
    private Automata auto;
    private String exp;
    private ArrayList<Estado> alcanzados;
    
    public SimulacionAFN(Automata at, String cadena)
    {
        this.auto = at;
        this.exp = cadena;
    }

    public Automata getAuto() {
        return auto;
    }

    public void setAuto(Automata auto) {
        this.auto = auto;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public ArrayList<Estado> getAlcanzados() {
        return alcanzados;
    }

    public void setAlcanzado(Estado alcanzado) {
        this.alcanzados.add(alcanzado);
    }
    
    public ArrayList<Estado> simple_move(Estado estado, T c)
    {
        Stack<Estado> st = new Stack();
        ArrayList<Estado> visitados = new ArrayList();
       
        
        //Mandamos a traer los enlaces de nuestr estado
        ArrayList<Transicion> enlaces = estado.getEnlaces();
        for (Transicion enlace : enlaces) 
        {
            T simb;
            simb = (T) enlace.getT();
            if (Objects.equals(simb, c))
            {
                Estado destino = enlace.getDestino();
                if (!visitados.contains(destino))
                    visitados.add(destino);
                    
            }
        }
        
        /*
        Debo revisar si fue llamado de un eclosure
        que se encuentre entre los estados visitados el estado de donde fue llamado
        o estados donde fue llamado
        */
        if (Objects.equals("@", c)) 
            if (!visitados.contains(estado))
                visitados.add(estado);
        
        return visitados;
    }
    
    public ArrayList<Estado> move(ArrayList<Estado> estados, T c)
    {
        ArrayList<Estado> visitados = new ArrayList();
        Stack<Estado> st = new Stack();
        
        //Metemos mi conjunto de estados a un stack
        for (Estado estado : estados)
            st.push(estado);
        
        while (st.size()>0)
        {
            Estado estado = st.pop();
            
            //Mandamos a traer los enlaces de nuestr estado
            ArrayList<Transicion> enlaces = estado.getEnlaces();
            
            for (Transicion enlace : enlaces) 
            {
                T simb;
                simb = (T) enlace.getT();
                if (Objects.equals(simb, c))
                {
                    Estado destino = enlace.getDestino();
                    if (!visitados.contains(destino))
                        visitados.add(destino);
                }
            }
        }
        /*
        Debo revisar si fue llamado de un eclosure
        que se encuentre entre los estados visitados el estado de donde fue llamado
        o estados donde fue llamado
        */
        if (Objects.equals("@", c))
            for (Estado estado : estados) 
                if (!visitados.contains(estado))
                    visitados.add(estado);
        
        return visitados;
    }
    
    public ArrayList<Estado> eclosure(ArrayList<Estado> estados)
    {
        Stack<Estado> st = new Stack();
        ArrayList<Estado> visitados = new ArrayList();
        
        ArrayList<Estado> revisados = new ArrayList();
        //Metemos mi conjunto de estados a un stack
        for (Estado estado : estados)
            st.push(estado);
        
        //Revisamos estado por estado
        while (st.size()>0)
        {
            Estado estado = st.pop();
            
            ArrayList<Estado> temp;
            temp = simple_move(estado, (T) "@");
            
            revisados.add(estado);
            for (Estado state : temp)
            {
                if (!visitados.contains(state))
                    visitados.add(state);
                if (!estados.contains(state) && !revisados.contains(state))
                    st.push(state);
            }
        }
        return  visitados;
    }
    
    //Revisamos si el alfabeto acepta la cadena ingresada
    public boolean Simular()
    {
        ArrayList<Estado> estados = new ArrayList();
        Estado inicial = auto.getInicio();
        
        estados.add(inicial);
        
        estados = eclosure(estados);
        for (Character c : exp.toCharArray())
        {
            estados = move(estados,(T)c);
            estados = eclosure(estados);
        }
        
        boolean aceptado = false;
        for (Estado state : estados)
            if (auto.getFin().contains(state))
                aceptado = true;
        return aceptado;
    }
}
