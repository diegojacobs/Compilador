/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.*;
/**
 *
 * @author Diego Jacobs 13160
 */
public class Archivo {
    
    public void Archivo()
    {
        
    }
    
    public void escribir(String texto)
    {
        File f;
        f = new File("Resultados.txt");
        try{
            FileWriter w = new FileWriter(f);
            
            BufferedWriter bw = new BufferedWriter(w);

            PrintWriter wr = new PrintWriter(bw);  

            wr.write(texto); //sobreescribimos el archivo con el nuevo texto
            //ahora cerramos los flujos de canales de datos, al cerrarlos el archivo quedará guardado con información escrita

            //de no hacerlo no se escribirá nada en el archivo

            wr.close();

            bw.close();

        }catch(IOException e){}
    }
    
    public void agregar(String texto)
    {
        File f;
        f = new File("Resultados.txt");
        try{
            FileWriter w = new FileWriter(f);
            
            BufferedWriter bw = new BufferedWriter(w);

            PrintWriter wr = new PrintWriter(bw);  

            wr.append(texto); //concatenamos en el archivo sin borrar lo existente
            //ahora cerramos los flujos de canales de datos, al cerrarlos el archivo quedará guardado con información escrita

            //de no hacerlo no se escribirá nada en el archivo

            wr.close();

            bw.close();

        }catch(IOException e){}
    }
    
}
