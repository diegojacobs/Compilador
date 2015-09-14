/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;


/**
 *
 * @author Diego Jacobs
 */
public class LectorGramatica {
    ArrayList<String> gramatica;
    private ArrayList<String> errores = new ArrayList();
    
    public LectorGramatica(ArrayList<String> lineas)
    {
        gramatica = lineas;
    }

    public ArrayList<String> getGramatica() {
        return gramatica;
    }

    public void setGramatica(ArrayList<String> gramatica) {
        this.gramatica = gramatica;
    }

    public ArrayList<String> getErrores() {
        return errores;
    }

    public void setErrores(ArrayList<String> errores) {
        this.errores = errores;
    }
    
    void validacion()
    {
        String temp = gramatica.get(0);
        if (!temp.trim().equals("Cocolo = \"Compiler\" ident".trim()))
            errores.add("Error en Linea 1.");
        
        temp =gramatica.get(1);
        if (!temp.trim().equals("ScannerSpecification".trim()))
            errores.add("Error en Linea 2.");
        
        temp = gramatica.get(gramatica.size()-1);
        if (!temp.trim().equals("\"End\" ident'.'.".trim()))
            errores.add("Error en Linea "+Integer.toString(gramatica.size()-1)+".");
        
        for (int i=3;i<gramatica.size();i++)
        {
            temp = gramatica.get(i);
            if (temp.equals("ScannerSpecification ="))
                System.out.print("hola");
            
        }
    }
}
