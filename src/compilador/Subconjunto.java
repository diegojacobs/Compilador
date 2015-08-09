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
    private ArrayList<EstadoFD> estados;
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

    public ArrayList<EstadoFD> getEstados() {
        return estados;
    }

    public void setEstados(EstadoFD estado) {
        this.estados.add(estado);
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
    
    public void sort(ArrayList<Estado> estados)
    {
        int i = 0;
        boolean flag = false;
        
        while(i<estados.size() && !flag)
        {
            i++;
            flag=true;
            for (int j=0;j<estados.size() - i;j++)
                if (estados.get(j).getNum() > estados.get(j+1).getNum())
                {
                    flag=false;
                    Estado temp = estados.get(j);
                    estados.set(j, estados.get(j+1));
                    estados.set(j+1, temp);
                }
        }
    }
    
    public void Construir()
    {
        //Creamos un stack donde meteremos los estados que se deben revisar
        Stack<EstadoFD> st = new Stack();
        ArrayList<EstadoFD> sub = new ArrayList();
        ArrayList<EstadoFD> check = new ArrayList();
        ArrayList<EstadoFD> fin = new ArrayList();
        Estado inicial = afn.getInicio();
        
        //Tendremos nuestro contador de estados creados
        int contS = 0;
        
        //Agregamos el estado inicial de nuestro AFN a un arraylist
        ArrayList<Estado> ini = new ArrayList();        
        ini.add(inicial);
        
        //Le hacemos eclosure al estado inicial y este sera nuestro estado inicial del AFD
        ArrayList<Estado> temp = eclosure(ini);
        EstadoFD state = new EstadoFD(temp);
        state.setId(contS);
        contS++;
       
        //Agregamos el estado a los subconjuntos
        sub.add(state);
        
        //Lo metemos a la pila para crear sus transiciones
        st.push(state);
        
        //Mientras hayan estados por revisar en la pila, revisamos
        while(st.size() > 0)
        {
            //sacamos un estado y obtenemos el arraylist que forma este estado del AFD
            state = st.pop();
            temp = state.getEstados();
            
            //Agregamos a los estados revisados el estado actual ya que lo vamos a revisar
            //revisados.add(state);
            check.add(state);
            //Debemos revisar las trasnsiciones con cada simbolo del alfabeto
            for (Character c :alfabeto.toCharArray())
            {
                /*Debemos hacer un movedel subconjunto de estados 
                 sin perder este subconjunto con un caracter del alfabeto
                */
                ArrayList<Estado> res = move(temp,(T)c);
                
                //Hacemos eclosure del resultado del move
                res = eclosure(res);
                
                sort(res);
                
                //Creamos un estado del AFD con el subconjunto de estados obtenidos
                //Ademas creamos la tansicion entre el estado origen al estado obtenido
                EstadoFD stateD =new EstadoFD(res);
                TransicionFD enlace = new TransicionFD(state,stateD,c);
                
                //Revisamos si nuestro estado resultante contiene el estado final del AFN
                //Si lo tiene le asignamos el subconjunto como uno de los finales al AFD
                if (res.contains(afn.getFin()))
                    fin.add(stateD);
                
                //Revisamos si nuestro estado revisado ya con tiene la transicion creada
                //Si no la tiene se la agregamos
                if (!state.getEnlaces().contains(enlace))
                    state.setEnlace(enlace);
                
                //Revisamos si nuestro estado resultante ya forma parte de los estados del AFD
                //Si no forma parte lo agregamos
                boolean flag1 = false;
                boolean agregar = false;
                int i = 0;
                while (i<sub.size() && !flag1)
                {
                    EstadoFD comp = sub.get(i);
                    if (comp.getEstados().equals(stateD.getEstados()))
                        flag1 = true;
                    i++;
                }
                if (!flag1)
                {
                    agregar = true;
                    sub.add(stateD);
                    stateD.setId(contS);
                    contS++;
                }
                
                //Debemos ver si el estado no esta entre los estados por revisar en ele stack
                ArrayList<EstadoFD> compA = new ArrayList();
                while (!flag1 && st.size() > 0)
                    compA.add(st.pop());
                
                boolean flag2 = false;
                i=0;
                
                while (i<compA.size() && !flag2 && !flag1)
                {
                    EstadoFD comp = compA.get(i);
                    if (comp.getEstados().equals(stateD.getEstados()))
                        flag2 = true;
                    i++;
                }
                boolean lleno = false;
                while (!flag1 && !lleno)
                {
                    for (i = 0; i<compA.size();i++)
                        st.push(compA.get(i));
                    lleno = true;
                }
                
                //Revisamos si el estado resultante ya a sido revisado
                boolean flag3 = false;
                i = 0;
                while (i<check.size() && !flag3 && !flag1)
                {
                    EstadoFD comp = check.get(i);
                    if (comp.getEstados().equals(stateD.getEstados()))
                        flag3 = true;
                    i++;
                }
                
                //Si no a sido revisado se agrega al stack
                if (agregar && !flag3 && !flag2)
                    st.push(stateD);
                
            }
        }
        
        temp = eclosure(ini);
        state = new EstadoFD(temp);
        //Ponemos como estado inicial de nuestro AFD el resultado del eclosure
        //Ponemos como estado final nuestros estados finales obtenidos
        //ponemos como subconjuntos los subconjuntos obtenidos
        //Lo agregamos a nuestro subconjunto de estados
        newauto = new AutomataFD(state,fin,sub,this.alfabeto);
    }
    
}
