/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diego Jacobs
 */
public class Graficar_Automata {
    
    public Graficar_Automata( String direccionDot, String direccionPng ){
		dibujar( direccionDot, direccionPng );
	}
    
    public void dibujar( String direccionDot, String direccionPng ){
	try
        {       
            ProcessBuilder pbuilder;
	
			/*
			 * Realiza la construccion del comando    
			 * en la linea de comandos esto es: 
			 * dot -Tpng -o archivo.png archivo.dot
			 */
            
            pbuilder = new ProcessBuilder( "dot", "-Tpng", "-o", direccionPng, direccionDot );
            pbuilder.redirectErrorStream( true );
            
            //Ejecuta el proceso
            pbuilder.start();
		    
	} catch (Exception e) { e.printStackTrace(); }
    }
    
}
