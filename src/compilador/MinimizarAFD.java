/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.TreeSet;

/**
 *
 * @author Diego Jacobs 13160
 */
public class MinimizarAFD<T> {
    private Automata AFD;
    private Automata newAFD;
    private String alfabeto;
    
    public MinimizarAFD(Automata afd, String alfa)
    {
        this.AFD = afd;
        newAFD = new Automata();
        alfabeto = alfa;
        newAFD.setAlfabeto(alfabeto);
    }

    public Automata getAFD() {
        return AFD;
    }

    public void setAFD(Automata AFD) {
        this.AFD = AFD;
    }

    public Automata getNewAFD() {
        return newAFD;
    }

    public void setNewAFD(Automata newAFD) {
        this.newAFD = newAFD;
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }
    
    public Estado move(Estado state, Character c)
    {
        Estado destino = new Estado();
        //Mandamos a traer los enlaces de nuestr estado
        ArrayList<Transicion> enlaces = state.getEnlaces();
            
        for (Transicion enlace : enlaces) 
        {
            T simb = (T)enlace.getT();
            String s = simb.toString();
            for (Character c1 : s.toCharArray())
                if (c1 == c)
                    destino = enlace.getDestino();    
        }
        
        return destino;
    }
    
    public void minimizar()
    {
        //ArrayList donde pondremos las particiones
        ArrayList<ArrayList<Estado>> particion = new ArrayList();
        
        //Lista donde tendremos los estados con sus transiciones
        ArrayList<HashMap> L = new ArrayList();
        ArrayList<ArrayList<Estado>> L2 = new ArrayList();
        
        //Obtenemos los estados que son de aceptacion y los que no
        ArrayList<Estado> nofinales = AFD.getNofin();
        ArrayList<Estado> finales = AFD.getFin();

        //Creamos nuestra particion apartir de los estados obtenidos
        if(nofinales != null && nofinales.size() > 0)
            particion.add(nofinales);
      
        if(finales != null && finales.size() > 0)
            particion.add(finales);
        
        
        int index=0;
        Estado state = new Estado();
        //Para cada grupo g de la Particion
        while (particion.size() > index)
        {
            ArrayList<Estado> g = particion.get(index);
            
            //Para cada estado del grupo g
          for (Estado s : g)
            {
                HashMap Ds = new HashMap();
                ArrayList<Estado> D2s = new ArrayList();
                     
                //Para cada simbolo del alfabeto
                for (Character c : alfabeto.toCharArray())
                {
                    //Buscamos el estado destino con la transicion
                    state = move(s,c);
                    //Para cada grupo h de la particion 
                    for (ArrayList<Estado> h : particion)
                    {
                        if (h.contains(state))
                        {
                            ArrayList<Estado> tempS = new ArrayList();
                            int j=0;       
                            while (h.size() > j)
                            {
                                Estado stateT = h.get(j);
                                D2s.add(stateT);
                                j++;
                            }
                            tempS.add(state);
                            Ds.put(s.getId(), tempS);
                        }
                    }
                }
                //Agregamos Ds a L
                if (state.getNum() !=null)
                {
                    L.add(Ds);
                    L2.add(D2s);
                }
            }
                    
            int i =0;
            //Creamos nuestra estructura Ki
            ArrayList<ArrayList<Estado>> K = new ArrayList();
            while (L.size()>0)
            {
                //Obtenemos nuestro Dx
                HashMap Dx = L.get(0);
                        
                //Obtenemos el key de nuestro Dx
                int key = 0;
                while (!Dx.containsKey(key))
                    key++;
                
                //Obtenemos nuestros Estados
                ArrayList<Estado> D2x = L2.get(L.indexOf(Dx));
                
                //Buscamos el Estado perteneciente a Dx
                Estado X = AFD.findEstadobyId(key);
                        
                //Lo metemos a nuestro subconjunto Ki
                //Nuestro estado X
                ArrayList<Estado> estados = new ArrayList();
                estados.add(X);
                K.add(i,estados);
                        
                //Removemos Dx de L
                L.remove(Dx);
                L2.remove(D2x);
                        
                //Metemos nuestro Dx en un ArrayLis para poder comparar
                ArrayList<Estado> tempDx = D2x;
                    
                int j = 0;
                while (L.size() > j)
                {
                    HashMap Dy = L.get(j);
                    //Buscamos el key de nuestro Dy
                    int key2 = 0;
                    while (!Dy.containsKey(key2))
                        key2++;
                    //Agregamos nuestro Dy a un ArrayList para comparar
                    ArrayList<Estado> tempDy = L2.get(L.indexOf(Dy));
                        
                    if (tempDx.equals(tempDy))
                    {
                        //Buscamos el estado asociado a Dy
                        Estado Y = AFD.findEstadobyId(key2);
                             
                        ArrayList temp2 = new ArrayList();
                        int q = 0;
                        while (K.get(i).size() > q)
                        {
                            ArrayList<Estado> states = K.get(i);
                            temp2.add(states.get(q));
                            q++;
                        }
                        
                        K.remove(i);
                        temp2.add(Y);
                        //Lo agregamos a nuestro conjunto
                        K.add(i, temp2);
                                
                        //Quitamos Dy de L
                        L.remove(Dy);
                        L2.remove(tempDy);
                    }
                    j++;
                }
                i++;
            }
            
            //Si K0 es diferente a nuestro grupo g
            if (K.size() > 0 && !K.get(0).equals(g) && state.getNum() != null)
            {
                //Removemos el grupo g de nuestra particion
                particion.remove(g);
                        
                //Agregamos cada Ki a nuestra particion
                int j=0;
                while(K.size() > j)
                {
                    ArrayList temp = new ArrayList();
                    temp = K.get(j);
                    particion.add(temp);
                    j++;
                }
                    
                index = 0;
            }
            else
            {
                index++;
            }
                
        }
        
        //Creamos el nuevo AFD
        index=0;
        for (ArrayList<Estado> J : particion)
        {
            //Creamos un nuevo estado
            Estado newstate = new Estado(J);
            newstate.setId(index);
            newAFD.setEstado(newstate);
            
            //Miramos si es un estado inicial
            if (J.contains(AFD.getInicio()))
            {
                newAFD.setInicio(newstate);
            }   
            
            boolean esFin = false;
            //Miramos si es un estado final
            for (Estado fin : AFD.getFin())
                if (J.contains(fin))
                    esFin = true;
            
            //Si es final lo agregamos a estados finales, sino a los estados no finales
            if (esFin)
            {
                newAFD.setFin(newstate);
            }
            else
                newAFD.setNofin(newstate);
            
            //Miramos las transiciones de lso estados
        for (Estado s: J)
        {
            ArrayList<Transicion> enlaces = s.getEnlaces();
            for (Transicion enlace  : enlaces)
            {
                Estado destino = enlace.getDestino();
                for (ArrayList<Estado> estados : particion)
                {
                    Estado destinoF = new Estado(estados);
                    if (estados.contains(destino))
                    {
                        ArrayList<Transicion> enlacesF = newstate.getEnlaces();
                        boolean existe = false;
                        for (Transicion enlaceF : enlacesF)
                            if(enlaceF.getT()==enlace.getT())
                                existe =true;
                        if (!existe)
                            newstate.setEnlace(new Transicion(newstate,destinoF,enlace.getT()));
                    }
                }
            
            }
        }
            index++;
        }
        
        
    }
    
    
}
