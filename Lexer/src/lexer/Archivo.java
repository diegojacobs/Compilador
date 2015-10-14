/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 *
 * @author Diego Jacobs 13160
 */
public class Archivo {
    private String doc = new String();
    private ArrayList lineas = new ArrayList();
    private char[] text;
    
    public Archivo(String txt)
    {
        this.doc = txt;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public ArrayList getLineas() {
        return lineas;
    }
    
    public Object getLineas(int index) {
        return lineas.get(index);
    }

    public void setLineas(String linea) {
        this.lineas.add(linea);
    }

    public char[] getText() {
        return text;
    }

    public void setText(char[] text) {
        this.text = text;
    }
    
    public void escribir(String texto)
    {
        File f;
        f = new File(doc);
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
        f = new File(doc);
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
    
    void muestraContenido() throws FileNotFoundException, IOException {
      String cadena;
      FileReader f = new FileReader(doc);
      BufferedReader buffer = new BufferedReader(f);
      String txt = new String();
      while((cadena = buffer.readLine())!=null) {
          lineas.add(cadena);
          txt += cadena+"文";
      }
      char[] array = txt.toCharArray();
      this.text = array;
      buffer.close();
    }
}
