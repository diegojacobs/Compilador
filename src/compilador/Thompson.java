/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Thompson<T>{
    private Automata auto;
    private String exp;
    
    public Thompson (String exp)
    {
        this.exp = exp;
    }
    
    public Automata getAuto() {
        return auto;
    }

    public void setAuto(Automata auto) {
        this.auto = auto;
    }
   
    //Armamos el automata
    public void armar(){
        Stack st = new Stack();
        
        //recorremos toda la expresion caracter por caracter
        for (Character c : exp.toCharArray()) {
           switch(c){
               //si encontramos una concatenacion
               case '.':
                   //debemos sacar dos objetos del stack para operarlos
                   Automata a_pun = (Automata)st.pop();
                   Automata b_pun = (Automata)st.pop();
                    
                   //Creamos un nuevo automata con la concatenación
                   Automata res_pun = concatenacion(a_pun,b_pun);
                    
                   //Metemos nuestro mini automata al stack nuevamente 
                   st.push(res_pun);
                   //Nuestro automata se vuelve nuestro mini automata
                   auto = res_pun;
                   break;
               //Si encontramos una union 
               case '|':
                   //debemos sacar dos objetos del stack para operarlos
                   Automata a_un = (Automata)st.pop();
                   Automata b_un = (Automata)st.pop();
                    
                   //Creamos un mini automata con la union
                   Automata res_un = union(a_un,b_un);
                    
                   //metemos nuestro nuevo automata al stack 
                   st.push(res_un);
                    
                   //Nuestro automata se convierte en nuestro nuevo autoamta
                   auto = res_un;
                   break;
                   
               //Si encontramos un asterisco    
               case '*':
                    //Hacemos un nuevo automata haciendole kleene a un objeto qeu saquemos de nuestro stack
                    Automata a_as = aterisco((Automata) st.pop());
                     
                    //Metemos a nuestro stack nuestro nuevo automata
                    st.push(a_as);
                    
                    //Nuestro autmata se convierte en nuestro nuevo automata
                    auto = a_as;
                   break;
               //Si no es ningun operador
               default:
                   //Simplemente creamos un automata de un simbolo y lo metemos a nuestro stack
                   Automata simple = simbolo((T)c);
                   st.push(simple); 
           }
       }
    }
    
    //Creacion de un automata de solo un simbolo
    public Automata simbolo(T caracter)
    {
       Automata auto_temp = new Automata();
        
       //Le creamos sus estados origen y destino
       Estado origen = new Estado(0);
       Estado destino = new Estado(1);
       //Creamos una transicion usando unicamente el simbolo
       Transicion enlace = new Transicion(origen, destino, caracter);
       origen.setEnlace(enlace);
        
       //Agregamos los estados creados
       auto_temp.setEstado(origen);
       auto_temp.setEstado(destino);
        
       //Asignamos como inicio su origen
       auto_temp.setInicio(origen);
        
       //Asignamos como fin su destino
       auto_temp.setFin(destino);
        
       return auto_temp;
       
    }   
    
    //Creacion de un automata mediante la concatenacion de dos objetos
    public Automata concatenacion(Automata auto1, Automata auto2){
       
        Automata auto_res = new Automata();
        
        //Debemos llevar un nuevo contador de nuestros estados
        int cont;
        /*
        *Como deciamos concatenar el segundo objeto sacado del stack con el primero 
        *ya que en el stack se encuentra mas abajo el que va primero
        *por esto primero recorremos el segundo objeto
        */
        for (cont=0; cont < auto2.getEstados().size(); cont++) 
        {
            Estado temp = (Estado) auto2.getEstados().get(cont);
            temp.setNum(cont);
            //Ponemos nuestro nuevo estado inicial 
            if (cont==0)
                auto_res.setInicio(temp);
            
            /*Cuando llegamos al ultimo estado de nuestro primer objeto 
            *lo concatenamos con el inicio del segundo automata
            */
            if (cont == auto2.getEstados().size()-1)
            {
                Estado origen = auto2.getFin().get(0);
                temp.setEnlace(new Transicion(origen,auto1.getInicio(),"@"));
            }
           
            auto_res.setEstado(temp);
        }
       
        /*Ahora debemos terminar de agregar los estados de nuestro segundo objeto
        *el cual fue el primer objeto que sacamos de nuestro stack 
        */
        for (int j =0;j<auto1.getEstados().size();j++)
        {
            Estado temp = (Estado) auto1.getEstados().get(j);
            temp.setNum(cont); 

            //Ponemos nuestro ultimo estado como nuevo estado de aceptacion
            if (auto1.getEstados().size()-1==j)
                auto_res.setFin(temp);
            auto_res.setEstado(temp);
            cont++;
        }
        
        return auto_res;
    }
    
    //Creacion de un automata mediante la union de dos objetos
    public Automata union(Automata auto1, Automata auto2)
    {
        
        Automata hambu = new Automata();
       
       /*
        Debemos crear un nuevo inicio por lo que creamos un nuevo estado inicial
        y concatenamos el estado inicial de nuestro segund objeto con una epsilon
        */
        Estado nuevoInicio = new Estado(0);
        nuevoInicio.setEnlace(new Transicion(nuevoInicio,auto2.getInicio(),"@"));
        
        /*
        Tambien debemos cambiar el inicio de nuestro otro objeto
        por lo que buscamos su inicio y ahora lo conectamos con el nuevo mediante epsilon
        */
        Estado ant_ini = auto1.getInicio();
        nuevoInicio.getEnlaces().add(new Transicion(nuevoInicio, ant_ini, "@"));
        

        hambu.setEstado(nuevoInicio);
        hambu.setInicio(nuevoInicio);
        
        //Debemos llevar un nuevo contador de nuestros estados
        int cont;
        
        /*
        Debemos recorrer el segundo objeto sacado del stack
        y agregarselo al nuevo automata que estamos creando    
        */
        for (cont=0; cont < auto2.getEstados().size(); cont++) 
        {
            Estado temp = (Estado) auto2.getEstados().get(cont);
            temp.setNum(cont + 1);
            hambu.setEstado(temp);
        }
        /*
        Recorremos el primer objeto sacado del stack
        y se lo agregamos al nuevo stack
        */
        for (int j=0; j < auto1.getEstados().size(); j++) 
        {
            Estado temp = (Estado) auto1.getEstados().get(j);
            temp.setNum(cont + 1);
            hambu.setEstado(temp);
            cont++;
        }
        
        //Creamos el nuevo estado final de nuestro nuevo automata
        Estado nuevoFin = new Estado(auto1.getEstados().size() +auto2.getEstados().size()+ 1);
        hambu.setEstado(nuevoFin);
        hambu.setFin(nuevoFin);
        
        /*
        Debemos buscar los finales anteriores de ambos de nuestros objetos
        y enlasarlos con nuestro nuevo fin mediante epsilon
        */
        Estado fin_ant = auto1.getFin().get(0);
        Estado fin_ant2 = auto2.getFin().get(0);
         
        fin_ant.getEnlaces().add(new Transicion(fin_ant, nuevoFin, "@"));
        fin_ant2.getEnlaces().add(new Transicion(fin_ant2,nuevoFin,"@"));
            
        return hambu;
    }
    
    //Creamos un nuevo automata haciendole kleene a uno ya existente
    public Automata aterisco(Automata auto1)
    {
        Automata barco = new Automata();
                
        /*
        Debemos crear un nuevo inicio y este enlasarlo con nuestro inicio anterior
        con una epsilon
        */
        Estado nuevoInicio = new Estado(0);
        barco.setEstado(nuevoInicio);
        barco.setInicio(nuevoInicio);
        
        Estado ant_ini = auto1.getInicio();
        nuevoInicio.getEnlaces().add(new Transicion(nuevoInicio, ant_ini, "@"));
        
        //Agregamos todos los estados de nuestro objeto sacado del stack
        for (int i=0; i < auto1.getEstados().size(); i++) 
        {
            Estado temp = (Estado) auto1.getEstados().get(i);
            temp.setNum(i + 1);
            barco.setEstado(temp);
        }
         
        /*
        Ahora debemos crear un nuevo fin y enlasar este con el anterior 
        y este a la vez con el nuevo inicio para poder crear el kleene
        con una epsilon
        */
        Estado nuevoFin = new Estado(auto1.getEstados().size() + 1);
        barco.setEstado(nuevoFin);
        barco.setFin(nuevoFin);
        
        Estado ant_fin = auto1.getFin().get(0);
        
       
        nuevoInicio.getEnlaces().add(new Transicion(nuevoInicio, nuevoFin, "@"));
        ant_fin.getEnlaces().add(new Transicion(ant_fin, nuevoFin, "@"));
        
        //Ahora debemos enlasar nuestro anterior inicio con nuestro anterior fin con epsilon
        ant_fin.getEnlaces().add(new Transicion(ant_fin, ant_ini,"@"));
        
        return barco;
    }
    
    @Override
    public String toString()
    {
        return auto.toString();
    }
}
