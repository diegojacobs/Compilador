/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Diego Jacobs
 */
//Caracteres especiales
//$ sirve para operaciones unarias en el arbol
//# sirve para construccion directa del AFD

public class Lexer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ArrayList<String> Equalstokens = new ArrayList();
        ArrayList<String> Idstokens = new ArrayList();
        ArrayList<String> Idskeys = new ArrayList();
        ArrayList<String> ExceptKeys = new ArrayList();
        ArrayList<String> Equalskeys = new ArrayList();
        
        Archivo tokens = new Archivo("tokens.comp");
        tokens.muestraContenido();
        Equalstokens = tokens.getLineas();
        
        Archivo tokensId = new Archivo("tokensId.comp");
        tokensId.muestraContenido();
        Idstokens = tokensId.getLineas();
        
        Archivo keywords = new Archivo("keywords.comp");
        keywords.muestraContenido();
        Equalskeys = keywords.getLineas();
        
        Archivo keysId = new Archivo("keysId.comp");
        keysId.muestraContenido();
        Idskeys = keysId.getLineas();
        
        Archivo exceptions = new Archivo("exceptions.comp");
        exceptions.muestraContenido();
        ExceptKeys = exceptions.getLineas();
        
        System.out.println("Ingrese el nombre del archivo con el código que desea lexear(Program.txt): ");
        Scanner input = new Scanner(System.in);
        String txtfile = input.nextLine(); //Program.txt
        Archivo ar = new Archivo(txtfile);
        ar.muestraContenido();
        
        ArrayList<String> lineasCode = new ArrayList();
        int i = 0;
        for (Object linea : ar.getLineas())
        { 
            System.out.println(++i +".  "+linea);
            if (!linea.toString().trim().equals(""))
                lineasCode.add(linea.toString());
        }
        
        LectorPrograma lp = new LectorPrograma(ar.getLineas(),Equalstokens, Idstokens, Idskeys, ExceptKeys, Equalskeys);
        lp.revisar();
            
        System.out.println("Resultado: ");
        for (String res :lp.getRes())
            System.out.println(res);
        
        /*
        lp.check(ar.getText());
        System.out.println("Resultado: ");
        for (String res :lp.getRes())
            System.out.println(res);
        */
    }
    
}
