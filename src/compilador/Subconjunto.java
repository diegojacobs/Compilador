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
public class Subconjunto<T> {
    private Automata afn;
    private AutomataFD newauto;
    private ArrayList<EstadoFD> revisados;
    private String alfabeto;
    
    
    public Subconjunto(Automata auto, String alfa) 
    {
        afn = auto;
        alfabeto = alfa;
    }

    public AutomataFD getNewauto() {
        return newauto;
    }

    public void setNewauto(AutomataFD newauto) {
        this.newauto = newauto;
    }
    
    public ArrayList<Estado> simple_move(Estado estado, T c)
    {
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
                if (enlace.getT() == c)
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
        
        ArrayList<Estado> revisados_temp = new ArrayList();
        //Metemos mi conjunto de estados a un stack
        for (Estado estado : estados)
            st.push(estado);
        
        //Revisamos estado por estado
        while (st.size()>0)
        {
            Estado estado = st.pop();
            
            ArrayList<Estado> temp;
            temp = simple_move(estado, (T) "@");
            
            revisados_temp.add(estado);
            for (Estado state : temp)
            {
                if (!visitados.contains(state))
                    visitados.add(state);
                if (!estados.contains(state) && !revisados_temp.contains(state))
                    st.push(state);
            }
        }
        return  visitados;
    }
    
    public void Construir()
    {
        //Creamos un stack donde meteremos los estados que se deben revisar
        Stack<EstadoFD> st = new Stack();
        Estado inicial = afn.getInicio();
        
        //Agregamos el estado inicial de nuestro AFN a un arraylist
        ArrayList<Estado> ini = new ArrayList();        
        ini.add(inicial);
        
        //Le hacemos eclosure al estado inicial y este sera nuestro estado inicial del AFD
        ArrayList<Estado> temp = eclosure(ini);
        EstadoFD state = new EstadoFD(temp);
        
        //Ponemos como estado inicial de nuestro AFD el resultado del eclosure
        //Lo agregamos a nuestro subconjunto de estados
        AutomataFD autotemp = new AutomataFD(state);
        autotemp.setSubconjuntos(state);
        
        //Lo metemos a la pila para crear sus transiciones
        st.push(state);
        
        //Mientras hayan estados por revisar en la pila, revisamos
        while(st.size() > 0)
        {
            //sacamos un estado y obtenemos el arraylist que forma este estado del AFD
            state = st.pop();
            temp = state.getEstados();
            
            //Agregamos a los estados revisados el estado actual ya que lo vamos a revisar
            revisados.add(state);
            
            //Debemos revisar las trasnsiciones con cada simbolo del alfabeto
            for (Character c :alfabeto.toCharArray())
            {
                /*Debemos hacer un movedel subconjunto de estados 
                 sin perder este subconjunto con un caracter del alfabeto
                */
                ArrayList<Estado> res = move(temp,(T)c);
                
                //Hacemos eclosure del resultado del move
                res = eclosure(res);
                
                //Creamos un estado del AFD con el subconjunto de estados obtenidos
                //Ademas creamos la tansicion entre el estado origen al estado obtenido
                EstadoFD stateD =new EstadoFD(res);
                TransicionFD enlace = new TransicionFD(state,stateD,c);
                
                //Revisamos si nuestro estado resultante contiene el estado final del AFN
                //Si lo tiene le asignamos el subconjunto como uno de los finales al AFD
                if (res.contains(afn.getFin()))
                    autotemp.setFin(stateD);
                
                //Revisamos si nuestro estado revisado ya con tiene la transicion creada
                //Si no la tiene se la agregamos
                if (!state.getEnlaces().contains(enlace))
                    state.setEnlace(enlace);
                
                //Revisamos si nuestro estado resultante ya forma parte de los estados del AFD
                //Si no forma parte lo agregamos
                if (!autotemp.getSubconjuntos().contains(stateD))
                {
                    autotemp.setSubconjuntos(stateD);
                }
                
                //Revisamos si el estado resultante ya a sido revisado
                //Si no a sido revisado se agrega al stack
                //Ademas debemos ver si no esta entre lso estados por revisar
                if(!revisados.contains(stateD) && !st.contains(stateD))
                    st.push(stateD);
            }
        }
        
        //Nuestro automata temporal se convierte nuestro nuevo AFD
        newauto = autotemp;
    }
    
}
