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
 * @author Usuario
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
    
    public ArrayList<EstadoFD> move(ArrayList<EstadoFD> estados, Character c)
    {
        ArrayList<EstadoFD> visitados = new ArrayList();
        Stack<EstadoFD> st = new Stack();
        
        //Metemos mi conjunto de estados a un stack
        for (EstadoFD estado : estados)
            st.push(estado);
        
        while (st.size()>0)
        {
            EstadoFD estado = st.pop();
            
            //Mandamos a traer los enlaces de nuestr estado
            ArrayList<TransicionFD> enlaces = estado.getEnlaces();
            
            for (TransicionFD enlace : enlaces) 
            {
                if (enlace.getSimbolo() == c)
                {
                    EstadoFD destino = enlace.getDestino();
                    if (!visitados.contains(destino))
                        visitados.add(destino);
                }
            }
        }
        
        return visitados;
    }
    
    
    //Revisamos si el alfabeto acepta la cadena ingresada
    public boolean SimularFD()
    {
        ArrayList<EstadoFD> estados = new ArrayList();
        EstadoFD inicial = auto.getInicio();
        
        estados.add(inicial);
        
        for (Character c : exp.toCharArray())
        {
            estados = move(estados,c);
        }
        
        return estados.contains(auto.getFin());
    }    
}
