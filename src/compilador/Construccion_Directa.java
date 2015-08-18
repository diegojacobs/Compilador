package compilador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Diego Jacobs 13160
 */
public class Construccion_Directa {
    private tree arbol;
    private Automata AFD;
    private ArrayList<Nodo> Afirstpos = new ArrayList();
    private ArrayList<Nodo> Alastpos = new ArrayList();
    private HashMap followpos_table = new HashMap();
    private String alfabeto;

    
    public Construccion_Directa(tree set, String alfa)
    {
        AFD = new Automata();
        AFD.setAlfabeto(alfa);
        this.arbol = set;
        agregarHashtag();
        alfabeto = alfa;
    }

    public tree getArbol() {
        return arbol;
    }

    public void setArbol(tree arbol) {
        this.arbol = arbol;
    }

    public Automata getAFD() {
        return AFD;
    }

    public void setAFD(Automata AFD) {
        this.AFD = AFD;
    }
    
    public void agregarHashtag()
    {
        Nodo node = new Nodo('.',null);
        Nodo node2 = new Nodo('#',node);
        arbol.setNodos(node);
        arbol.setNodos(node2);
        arbol.getRaiz().setPrevious(node);
        node.setLeft(arbol.getRaiz());
        node.setRight(node2);
        arbol.setRaiz(node);
    }
    
    public void sort(ArrayList<Nodo> lista)
    {
        
        int i = 0;
        boolean flag = false;
        
        while(i<lista.size() && !flag)
        {
            i++;
            flag=true;
            for (int j=0;j<lista.size() - i;j++)
                if (lista.get(j).getId() > lista.get(j+1).getId())
                {
                    flag=false;
                    Nodo temp = lista.get(j);
                    lista.set(j, lista.get(j+1));
                    lista.set(j+1, temp);
                }
        }
        
        //Debemos eliminar los repetidos
        for(i=1;i<lista.size();i++){
            int numero1 = lista.get(i).getId();
            int numero2 = lista.get(i-1).getId();
            if(numero1==numero2){
                lista.remove(i--);
            }
        }
    }
    
    public boolean nullable(Nodo exp)
    {
        if (('*'==exp.getCarac()))
            return true;
        else if (('|'==exp.getCarac()))
            return (nullable(exp.getLeft()) || nullable(exp.getRight()));
        else if (('.'==exp.getCarac()))
            return (nullable(exp.getLeft()) && nullable(exp.getRight()));
        else
            return (exp.getCarac()=='@');
    }
    
    public ArrayList firstpos(Nodo node){
        
        ArrayList Afirstpos = new ArrayList();
        
        //No regresamos nada si es epsilon
        if (node.getCarac() == '@')
            return Afirstpos;
        
        //Regresamos el id del nodo en caso de que sea una hoja
        else if (node.isHoja())
        {
            Afirstpos.add(node);
            return Afirstpos;
        }
        //Regresamos el firstpos de ambos hijos en el caso que sea |
        else if (node.getCarac() == '|')
        {
           Afirstpos.addAll(firstpos(node.getLeft()));
           Afirstpos.addAll(firstpos(node.getRight()));
        }
        /*
            Revisamos si el de la izquierda es nullable
            si es devolvemos el firstpos de ambos hijos
            si no es solo el firstpos de la derecha
        */
        else if (node.getCarac() == '.')
        {
            if (nullable(node.getLeft()))
            {
                Afirstpos.addAll(firstpos(node.getLeft()));
                Afirstpos.addAll(firstpos(node.getRight()));
            }
            else
                Afirstpos.addAll(firstpos(node.getLeft()));
        }
        //Regresamos el firstpos del hijo izquierdo en case de que sea *
        else if (node.getCarac() == '*')
            Afirstpos.addAll(firstpos(node.getLeft()));
        
        return Afirstpos;
    }
    
    public ArrayList lastpos(Nodo node)
    {
        
        ArrayList Alastpos = new ArrayList();
        
        //No regresamos nada si es epsilon
        if (node.getCarac() == '@')
            return Alastpos;
        //Regresamos el id del nodo en caso de que sea una hoja  
        else if (node.isHoja())
        {
           Alastpos.add(node);
           return Alastpos;
        }
        //Rgresamos el lastpos de ambos hijos en caso de que sea |
        else if (node.getCarac() == '|')
        {
            Alastpos.addAll(lastpos(node.getLeft()));
            Alastpos.addAll(lastpos(node.getRight()));
        }
        /*
            Revisamos si el hijo del lado derecho es nullable
            si es regresamos el lastpost de ambos hijos
            si no es solo el del lado derecho
        */
        else if (node.getCarac() == '.')
        {
            if (nullable(node.getRight()))
            {    
                Alastpos.addAll(lastpos(node.getLeft()));
                Alastpos.addAll(lastpos(node.getRight()));
            }
            else
                Alastpos.addAll(lastpos(node.getRight()));
            
        }
        //Regresamos el lastpos del hijo izquierdo en caso de que sea *
        else if (node.getCarac()  == '*')
            Alastpos.addAll(lastpos(node.getLeft()));
        
        return Alastpos;
    }
    
    
    public HashMap followpos(Nodo node)
    {
        if (node.getCarac() == '*')
        {    
            /*
                Debemos tomar el firstpos de este nodo
                Ademas del lastpos del mismo
            */
            ArrayList<Nodo> first = firstpos(node);
            ArrayList<Nodo> last = lastpos(node);
              
              
            for (Nodo last1 : last) 
            {
                int num = (int) last1.getId();
                /*
                    Agregamos los id's que ya esten en la tabla
                    Los ordenamos y elliminamos los repetidos
                */
                if (followpos_table.containsKey(num))
                {
                    first.addAll((Collection) followpos_table.get(num));
                    sort(first);
                }
                followpos_table.put(num, first);
            }
        }

        else if (node.getCarac() == '.')
        {
            /*
                Debemos hacer el firstpos del hijo del lado derecho
                Ademas de lastpos del hijo del lado izquierdo
            */
            ArrayList<Nodo> first = firstpos(node.getRight());
            ArrayList<Nodo> last = lastpos(node.getLeft());

            for (int i = 0;i<last.size();i++)
            {
                int num = (int) last.get(i).getId();
                /*
                    Agregamos los id's que ya esten en la tabla
                    Los ordenamos y elliminamos los repetidos
                */
                if (followpos_table.containsKey(num))
                {
                    first.addAll((Collection) followpos_table.get(num));
                    sort(first);
                }  
                followpos_table.put(num, first);
            }
        }
        return followpos_table;
    }
    
    public void construir()
    {
        int cont_est = 0;
        ArrayList<Nodo> nodos = arbol.getNodos();
        for (Nodo node : nodos)
            followpos(node);
        
        ArrayList<Nodo> inicial;
        ArrayList<Estado> fin = new ArrayList();
        ArrayList<Estado> nofin = new ArrayList();
        
        //Miramos el firstpos de nuestro arbol(expresión) 
        inicial = firstpos(arbol.getRaiz());
        
        //Creamos un estado con los id devueltos del firstpos
        Estado state = new Estado(inicial);
        state.setId(cont_est++);
        
        //Agregamos el estado y lo ponemos como inicial
        AFD.setEstado(state);
        AFD.setInicio(state);
        //Revisamos si el nuevo estado es un estado de aceptación
        Nodo end = arbol.getRaiz().getRight();
        if (inicial.contains(end))
            AFD.setFin(state);
        else
            AFD.setNofin(state);
        
        Stack<ArrayList<Nodo>> st = new Stack();
        st.push(inicial);
        
        while(0 < st.size())
        {
            
            ArrayList<Nodo> actual = st.pop();
            
            //Revisamos cada elemento del alfabeto 
            for (Character c : alfabeto.toCharArray())
            {
                ArrayList<Nodo> temp = new ArrayList();
                
                //Miramos que elementos del estado son iguales 
                //al elemento del alfabeto que se esta revisando
                for (Nodo n : actual){
                    if (n.getCarac() == c){
                        temp.addAll((ArrayList<Nodo>) followpos_table.get(n.getId()));
                    }
                }
                
                sort (temp);
                
                //Revisamos si el estado ya fue creado
                boolean igual = false;
                for (Estado s : AFD.getEstados())
                    if (s.getNum().equals(temp))
                        igual = true;
                
                if (!igual)
                {
                    //Creamos el nuevo estado
                    Estado destino = new Estado(temp);
                    destino.setId(cont_est++);
                    
                    //Buscamos la posicion del estado anterior
                    int index = 0;
                    boolean flag = false;
                    for (Estado stateT : AFD.getEstados()) 
                    {
                        if (actual == stateT.getNum())
                            flag=true;
                        if (!flag)
                            index++;
                    }
                    
                    //Creamos la nueva transición
                    Estado origen = AFD.getEstados().get(index);
                    origen.setEnlace(new Transicion(origen,destino,c));
                    AFD.setEstado(destino);
                    
                    //Revisamos si el nuevo estado es un estado de aceptación
                    end = arbol.getRaiz().getRight();
                    if (temp.contains(end))
                        AFD.setFin(destino);
                    else
                        AFD.setNofin(destino);
                    
                    //Lo metemos al stack para revisarlo
                    st.push(temp);
                }
                else
                {
                    //Buscamos la posicion del estado anterior
                    int index = 0;
                    boolean flag = false;
                    for (Estado stateT : AFD.getEstados()) 
                    {
                        if (actual == stateT.getNum())
                            flag=true;
                        if (!flag)
                            index++;
                    }
                    
                    //Buscamos la posicion del estado destino
                    int index2 = 0;
                    flag = false;
                    for (Estado stateT : AFD.getEstados()) 
                    {
                        if (temp.equals(stateT.getNum()))
                            flag=true;
                        if (!flag)
                            index2++;
                    }
                    
                    //Creamos la transicion
                    Estado origen = AFD.getEstados().get(index);
                    Estado destino = AFD.getEstados().get(index2);
                    origen.setEnlace(new Transicion(origen,destino,c));
                }
            }
        }
    }

}
