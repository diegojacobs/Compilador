/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 
 * @author Diego Jacobs 13160
 */

//Caracteres especiales
//$ sirve para operaciones unarias en el arbol
//# sirve para construccion directa del AFD

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        // TODO code application logic here
        
        /* NUEVOS CARACTERES DE AUTOMATAS
        System.out.println((int)'四'+" = (");
        System.out.println((int)'枼'+" = )");
        System.out.println((int)'北'+" = +");
        System.out.println((int)'要'+" = .");
        System.out.println((int)'少'+" = |");
        System.out.println((int)'永'+" = @");
        System.out.println((int)'背'+" = ?");
        System.out.println((int)'砂'+" = *");        
        */
        
        /********************************************************************/
        /********************************************************************/
        /********************************************************************/
        /**********************       FASE 2        *************************/
        /********************************************************************/
        /********************************************************************/
        /********************************************************************/
        
        System.out.println("Ingrese el nombre del archivo con la especificación léxica en Cocol(CocoR.txt): ");
        Scanner input = new Scanner(System.in);
        String txtfile = input.nextLine(); //CocoR.txt
        
        Archivo ar = new Archivo(txtfile);
        ar.muestraContenido();
        
        ArrayList<String> lineasCode = new ArrayList();
        int i = 0;
        for (String linea : ar.getLineas())
        { 
            System.out.println(++i +".  "+linea);
            if (!linea.trim().equals(""))
                lineasCode.add(linea);
        }
        
        LectorGramatica lg = new LectorGramatica(lineasCode,ar.getLineas());
        lg.revisar();

        if (!lg.getErrores().isEmpty())
            for (String linea : lg.getErrores())
                System.out.println(linea);
        else
        {
            System.out.println("No hay errores.");
            System.out.println("El lexer ha sido generado.");
         
            //creamos un archivo con los ident de tokens
            Archivo idtokens = new Archivo("tokensId.comp");
            idtokens.createFile("tokensId", lg.getIdstokens());
            
            //Creamos un archivo con todas las expresiones para los automatas de tokens
            Archivo tokens = new Archivo("tokens.comp");
            tokens.createFile("tokens", lg.getEqualstokens());
            
            //Creamos un archivo con las exeptions keywords
            Archivo exceptions = new Archivo("exceptions.comp");
            exceptions.createFile("exceptions", lg.getExceptKeys());
            
            //Creamos un archivo con los ident de keywords
            Archivo idkeys = new Archivo("keysId.comp");
            idkeys.createFile("keysId", lg.getIdskeys());
  
            //Creamos un archivo con todas las expresiones para los automatas de keywords
            Archivo keywords = new Archivo("keywords.comp");
            keywords.createFile("keywords", lg.getEqualskeys());
            
        //Lab 6
            System.out.println("Ingrese el nombre del archivo con el código que desea lexear(Program.txt): ");
            input = new Scanner(System.in);
            txtfile = input.nextLine(); //Program.txt
            Archivo ar2 = new Archivo(txtfile);
            ar2.muestraContenido();
        
            lineasCode = new ArrayList();
            i = 0;
            for (String linea : ar2.getLineas())
            { 
                System.out.println(++i +".  "+linea);
                if (!linea.trim().equals(""))
                    lineasCode.add(linea);
            }
        
            LectorPrograma lp = new LectorPrograma(ar2.getLineas(),lg.getEqualstokens(),lg.getIdstokens(), lg.getIdskeys(),lg.getExceptKeys(), lg.getEqualskeys());
            lp.revisar();
            
            System.out.println("Resultado: ");
            for (String res :lp.getRes())
                System.out.println(res);
        }
        /********************************************************************/
        /********************************************************************/
        /********************************************************************/
        /**********************       FASE 1        *************************/
        /********************************************************************/
        /********************************************************************/
        /********************************************************************/
        
    /*
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
            if (!simbolos.contains(c.toString()) && !c.equals('@') && !c.equals('$') && !c.equals('#')) {
                simbolos += c;
            } 
        }
        
        //Creamos el archivo
        String txt = "AFN.txt";
        Archivo file = new Archivo(txt);
        
        //Creamos el automata y le asignamos el alfabeto
        Thompson aut = new Thompson(fin);
        aut.armar();
        aut.getAuto().setAlfabeto(simbolos);
        
        Graficar_Automata grafica = new Graficar_Automata(aut.getAuto());
        grafica.Graficar("GraficaAFN");
        
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
        
        grafica = new Graficar_Automata(nuevo.getNewauto());
        grafica.Graficar("GraficaAFDSubconjuntos");
        
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
    
        //Lenamos un stack para hacer el arbol
        arbol.llenarStack(post+"#.");
        //armamos el arbol
        arbol.armar(arbol);
        
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
        
        //Simulamos el AFD con nuestra expresión
        simu2 = new SimulacionAFD(cons.getAFD(),cadena);
        time_start = System.currentTimeMillis();
        System.out.println("Contiene la cadena Ingresada: "+simu2.SimularFD());
        time_end = System.currentTimeMillis();
        System.out.println("La simulación tomo: "+ ( time_end - time_start ) +" millisegundos");
        txt = "SimulacionAFDDirecto.txt";
        file = new Archivo(txt);
        if (simu2.SimularFD())
            file.agregar("La cadena: "+ cadena +" a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        else
            file.agregar("La cadena: "+ cadena +" no a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        
        grafica = new Graficar_Automata(cons.getAFD());
        grafica.Graficar("GraficaAFDDirecto");
        
        //Minimizamos nuestro AFD Subconjuntos
        MinimizarAFD min = new MinimizarAFD(nuevo.getNewauto(),alfabeto);
        min.minimizar();
        
        System.out.println("\nAutomata AFD Subconjuntos Minimo ");
        System.out.println(min.getNewAFD().toString());
        
        //Creamos el archivo con los datos de la minimización del AFD subconjuntos
        txt = "Minimizacion-AFD-Subconjuntos.txt";
        file = new Archivo(txt);
        file.escribir(min.getNewAFD().toString());
        
        //Simulamos el AFD con nuestra expresión
        simu2 = new SimulacionAFD(min.getNewAFD(),cadena);
        time_start = System.currentTimeMillis();
        System.out.println("Contiene la cadena Ingresada: "+simu2.SimularFD());
        time_end = System.currentTimeMillis();
        System.out.println("La simulación tomo: "+ ( time_end - time_start ) +" millisegundos");
        txt = "SimulacionAFDMinSubconjuntos.txt";
        file = new Archivo(txt);
        if (simu2.SimularFD())
            file.agregar("La cadena: "+ cadena +" a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        else
            file.agregar("La cadena: "+ cadena +" no a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        
        //Minimizamos nuestro AFD Directo
        min = new MinimizarAFD(cons.getAFD(),alfabeto);
        min.minimizar();
        System.out.println("\nAutomata AFD Directo Minimo ");
        System.out.println(min.getNewAFD().toString());
       
        grafica = new Graficar_Automata(min.getNewAFD());
        grafica.Graficar("GraficaAFDMinSubconjuntos");
        
        //Creamos el archivo con los datos de la minimización del AFD Directo
        txt = "Minimizacion-AFD-Directo.txt";
        file = new Archivo(txt);
        file.escribir(min.getNewAFD().toString());
        
        grafica = new Graficar_Automata(min.getNewAFD());
        grafica.Graficar("GraficaAFDMinDirecto");
        
        //Simulamos el AFD con nuestra expresión
        simu2 = new SimulacionAFD(min.getNewAFD(),cadena);
        time_start = System.currentTimeMillis();
        System.out.println("Contiene la cadena Ingresada: "+simu2.SimularFD());
        time_end = System.currentTimeMillis();
        System.out.println("La simulación tomo: "+ ( time_end - time_start ) +" millisegundos");
        txt = "SimulacionAFDMinDirecto.txt";
        file = new Archivo(txt);
        if (simu2.SimularFD())
            file.agregar("La cadena: "+ cadena +" a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
        else
            file.agregar("La cadena: "+ cadena +" no a sido aceptada."+"\r\n La simulación tomo: "+ ( time_end - time_start )+" millisegundos");
    */

    }
    
}
