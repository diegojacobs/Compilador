/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs
 */
public class Graficar_Automata {
    private final Automata auto;
    
    public Graficar_Automata(Automata auto){
	this.auto = auto;
    }
    
    public void Graficar(String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        PrintWriter writer;
        writer= new PrintWriter(fileName + ".dot", "UTF-8");
        writer.println("digraph " + fileName + " {");
        writer.println("rankdir=LR;");
        for (Estado s : auto.getEstados())
        {
            String shape = "circle";
            if(auto.getInicio().equals(s))
            {
                String codigo = new String();
                codigo += "{\r\nnode [style = invisible]\r\n";
                codigo += "INI\r\n";
                codigo += "}\r\n";
                String ini = Integer.toString(auto.getInicio().getId());
                codigo += "INI -> "+ini+"[label=\"Inicio\"]\r\n";
                writer.println(codigo);
                writer.println(s.getId() + " [ shape=\"circle\"]");
            }
            else if(auto.getFin().contains(s))
            {
                writer.println(s.getId() + " [ shape=\"doublecircle\"]");
                writer.println(s.getId()+"[fillcolor=\"#1D22E8\"];");
            }
            else 
                writer.println(s.getId() + " [ shape=\"circle\"]");
        }
        for (Estado s : auto.getEstados())
        {
            ArrayList<Transicion> enlaces = s.getEnlaces();
            
            for (Transicion tran : enlaces)
            {
                writer.println(tran.getOrigen().getId() + "->" + tran.getDestino().getId() + "[label=\"" + tran.getT() + "\"]");
            }
        }
        writer.println("}");
        writer.close();
        Runtime rt = Runtime.getRuntime();
        rt.exec("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe -Tjpg C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Compilador\\" + fileName + ".dot -o C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Compilador\\" + fileName + ".jpg");
        
    }
}
