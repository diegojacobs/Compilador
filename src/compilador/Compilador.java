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
        System.out.println("InfixToPostfix: "+post);
        
        //Lenamos un stack para hacer el arbol
        arbol.llenarStack(post);
        //armamos el arbol
        arbol.armar(arbol);
        
        //Lo recorremos con un inorder
        String end_tree = arbol.postOrder(arbol.getRaiz());
        System.out.println("Recorrido postOrder: "+end_tree);
        
        String fin = new String();
        //Le quitamos los $ que nos sirvieron para hacer el * del arbol
        for (int i=0;i<end_tree.length();i++)
            if (end_tree.charAt(i)!='$')
                fin += end_tree.charAt(i);
        
        System.out.println("Sin simbolos usados para armar arbol: "+fin);
        
        //Obtenemos las hojas del arbol
        String hojas = arbol.getHojas(arbol.getRaiz());
        System.out.println("Hojas: "+hojas);
        
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
        System.out.println("Simbolos del alfabeto: "+simbolos);
        
        //Creamos el archivo
        Archivo file = new Archivo();
        
        //Creamos el automata y le asignamos el alfabeto
        Thompson aut = new Thompson(fin);
        aut.armar();
        aut.getAuto().setAlfabeto(simbolos);
        
       
        System.out.println(aut.toString());
        
        String res = aut.toString();
        file.agregar(res);
        
        System.out.println("Ingrese su cadena: ");
        Scanner input2 = new Scanner(System.in);
        String exp2 = input2.nextLine();
        
        Simulacion simu = new Simulacion(aut.getAuto(),exp2);
        
        System.out.println(simu.Simular());
        
        String alfabeto = simbolos.replaceAll(",", "");
        
        System.out.println("Alfabeto: "+alfabeto);
        
        Subconjunto nuevo = new Subconjunto(aut.getAuto(),alfabeto);
        
        nuevo.Construir();
        
        String AFD = nuevo.getNewauto().toString();
        
        System.out.println(AFD);
    }
    
}
