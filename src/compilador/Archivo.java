/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Diego Jacobs 13160
 */
public class Archivo {
    private String doc = new String();
    private ArrayList<String> lineas = new ArrayList();
    
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

    public ArrayList<String> getLineas() {
        return lineas;
    }
    
    public String getLineas(int index) {
        return lineas.get(index);
    }

    public void setLineas(String linea) {
        this.lineas.add(linea);
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

        }catch(IOException e){System.out.println("No se pudo excribir en el archivo.");}
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

        }catch(IOException e){System.out.println("No se pudo excribir en el archivo.");}
    }
    
    public void muestraContenido() throws FileNotFoundException, IOException {
      String cadena;
      FileReader f = new FileReader(doc);
      BufferedReader buffer = new BufferedReader(f);
      while((cadena = buffer.readLine())!=null) {
          lineas.add(cadena);
      }
      buffer.close();
    }
    
    public void createFile(String name,ArrayList lista) throws IOException
    {
        final File parentDir = new File("Lexer");
        parentDir.mkdir();
        final String hash = name;
        final String fileName = hash + ".comp";
        final File file = new File(parentDir,fileName);
        file.createNewFile(); // Creates file crawl_html/abc.txt
        
        String texto = new String();
        for (Object temp : lista) 
        {
            texto +=temp.toString()+"\r\n";
        }
        
        File f;
        f = new File(parentDir, fileName);
        try{
            FileWriter w = new FileWriter(f);
            
            BufferedWriter bw = new BufferedWriter(w);

            PrintWriter wr = new PrintWriter(bw);  

            wr.write(texto); 
            
            wr.close();

            bw.close();

        }catch(IOException e){System.out.println("No se pudo excribir en el archivo.");}
    }
}
