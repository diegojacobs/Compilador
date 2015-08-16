/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diego Jacobs 13160
 */

//Caracteres especiales
//@ es epsilon
//$ sirve para operaciones unarias en el arbol
//# sirve para construccion directa del AFD

import java.util.Scanner;

public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Ingrese su expresión regular: ");
        Scanner input = new Scanner(System.in);
        String exp = input.nextLine(); 
        System.out.println("Ingrese su cadena a simular: ");
        String cadena = input.nextLine();
        
        myPostfix postfix = new myPostfix();
        tree arbol = new tree(); //Nuestro arbol
        long time_start, time_end; //Para medir tiempos de ejecucion
       
        //Lo pasamos a postfix
        String post = postfix.infixToPostfix(exp);
        
        //Lenamos un stack para hacer el arbol
        arbol.llenarStack(post);
        //armamos el arbol
        arbol.armar(arbol);
        
        //Lo recorremos con un postOrder
        String end_tree = arbol.postOrder(arbol.getRaiz());
        
        String fin = new String();
        //Le quitamos los $ que nos sirvieron para hacer el * del arbol
        for (int i=0;i<end_tree.length();i++)
            if (end_tree.charAt(i)!='$')
                fin += end_tree.charAt(i);
       
        //Obtenemos las hojas del arbol
        String hojas = arbol.getHojas(arbol.getRaiz());
        
        //Obtenemos los simbolos de nuestro alfabeto
        String simbolos = new String();
        for (int i=0;i<hojas.length();i++)
        {
            Character c = hojas.charAt(i);
            if (!simbolos.contains(c.toString()) && !c.equals('@') && !c.equals('$')) {
                simbolos += c+",";
            } else {
            }
        }
        
        //Quitamos la ultima ,
        simbolos = simbolos.substring(0,simbolos.length()-1);

        //Creamos el archivo
        String txt = "AFN.txt";
        Archivo file = new Archivo(txt);
        
        //Creamos el automata y le asignamos el alfabeto
        Thompson aut = new Thompson(fin);
        aut.armar();
        aut.getAuto().setAlfabeto(simbolos);
        
        //Imprimimos el Automata AFN
        System.out.println(aut.toString());
        
        String res = aut.toString();
        file.agregar(res);
        
        //Simulamos la cadena ingresada en el AFN
        SimulacionAFN simu = new SimulacionAFN(aut.getAuto(),cadena);
        time_start = System.currentTimeMillis();
        System.out.println("Contiene la cadena Ingresada: "+simu.Simular());
        time_end = System.currentTimeMillis();
        System.out.println("La simulación tomo: "+ ( time_end - time_start ) +" millisegundos");
        txt = "SimulacionAFN.txt";
        file = new Archivo(txt);
        if (simu.Simular())
        {
            file.agregar("La cadena: "+ cadena +" a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        }
        else
            file.agregar("La cadena "+ cadena +" no a sido aceptada"+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        
        String alfabeto = simbolos.replaceAll(",", "");
        
        //Mandamos a nuestra clase subconjunto el AFN y nuestro alfabeto
        Subconjunto nuevo = new Subconjunto(aut.getAuto(),alfabeto);
        
        //Construimos el AFD
        nuevo.Construir();
        
        //Obtenemos los datos del AFD
        String AFD = nuevo.getNewauto().toString();
        System.out.println(AFD);
        
        //Creamos el archivo con los datos del AFD
        txt = "AFD-Subconjuntos.txt";
        file = new Archivo(txt);
        file.agregar(nuevo.getNewauto().toString());
        
        
        
        //Simulamos el AFD con nuestra expresión
        SimulacionAFD simu2;
        simu2 = new SimulacionAFD(nuevo.getNewauto(),cadena);
        time_start = System.currentTimeMillis();
        System.out.println("Contiene la cadena Ingresada: "+simu2.SimularFD());
        time_end = System.currentTimeMillis();
        System.out.println("La simulación tomo: "+ ( time_end - time_start ) +" millisegundos");
        txt = "SimulacionAFD.txt";
        file = new Archivo(txt);
        if (simu2.SimularFD())
            file.agregar("La cadena: "+ cadena +" a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        else
            file.agregar("La cadena: "+ cadena +" no a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
    
    
        Construccion_Directa cons = new Construccion_Directa(arbol,alfabeto);
        String directo = arbol.inOrder(cons.getArbol().getRaiz());
        System.out.println(directo);
        
        //Le ponemos un numero a cada hoja
        arbol.ponerNum(arbol.getRaiz());
        
        cons.construir();
        
        System.out.println(cons.getAFD().toString());
        
        //Creamos el archivo con los datos del AFD
        txt = "AFD-Directo.txt";
        file = new Archivo(txt);
        file.agregar(cons.getAFD().toString());
    }
    
}
