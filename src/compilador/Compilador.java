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
//

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
        myPostfix postfix = new myPostfix();
        tree arbol = new tree();
       
        //Lo pasamos a postfix
        String post = postfix.infixToPostfix(exp);
        
        //Lenamos un stack para hacer el arbol
        arbol.llenarStack(post);
        //armamos el arbol
        arbol.armar(arbol);
        
        //Lo recorremos con un inorder
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
        
        System.out.println("Ingrese su cadena: ");
        Scanner input2 = new Scanner(System.in);
        String exp2 = input2.nextLine();
        
        //Simulamos la cadena ingresada en el AFN
        SimulacionAFN simu = new SimulacionAFN(aut.getAuto(),exp2);
        System.out.println(simu.Simular());
        txt = "SimulacionAFN.txt";
        file = new Archivo(txt);
        if (simu.Simular())
            file.agregar("La cadena: "+ exp2 +" a sido aceptada");
        else
            file.agregar("La cadena "+ exp2 +" no a sido aceptada");
        
        String alfabeto = simbolos.replaceAll(",", "");
        
        Subconjunto nuevo = new Subconjunto(aut.getAuto(),alfabeto);
        
        nuevo.Construir();
        
        String AFD = nuevo.getNewauto().toString();
        
        System.out.println(AFD);
        
        txt = "AFD.txt";
        file = new Archivo(txt);
        file.agregar(nuevo.toString());
        
        SimulacionAFD simu2;
        simu2 = new SimulacionAFD(nuevo.getNewauto(),exp2);
        System.out.println(simu2.SimularFD());
        txt = "SimulacionAFD.txt";
        file = new Archivo(txt);
        if (simu2.SimularFD())
            file.agregar("La cadena: "+ exp2 +" a sido aceptada");
        else
            file.agregar("La cadena: "+ exp2 +" no a sido aceptada");
    }
    
}
