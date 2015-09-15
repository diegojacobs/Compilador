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
    private ArrayList<String> ids = new ArrayList();
    private int contL = 0;
    private int lastindex;
    Thompson afnLETTER;
    Thompson afnNUMBER;
    Thompson afnDIGIT;
    Thompson afnIDENT;
    Thompson afnSTRING;
    
    
    //Vocabulario de Cocol
    private final String letter = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|ñ|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|Ñ|O|P|Q|R|S|T|U|V|W|X|Y|Z)";
    private final String digit = "(0|1|2|3|4|5|6|7|8|9)";
    private final String any = "("+letter+"|"+digit+"|#|!|¡|¿|%|/|=|¸|;|:|,|€|<|>|¨|{|}|[|]|^|~|·|½|-|_)";
    private final String ident = letter + "("+letter+"|"+digit+")*";
    private final String number = digit+"("+digit+")*";
    private final String string = "\""+"("+any+")*"+"\"";
    private final String caracter = "\'"+"("+any+")"+"\'";
    
    public LectorGramatica(ArrayList<String> lineas)
    {
        gramatica = lineas;
        myPostfix postfix = new myPostfix();
        
        /*
        * Creamos un afn para revisar letras
        */
        String post = postfix.infixToPostfix(letter);
        afnLETTER = new Thompson(post);
        afnLETTER.armar();
        
        /*
        * Creamos un afn para revisar digitos
        */
        post = postfix.infixToPostfix(digit);
        afnDIGIT = new Thompson(post);
        afnDIGIT.armar();
        
        /*
        * Creamos un afn para revisar numeros
        */
        post = postfix.infixToPostfix(number);
        afnNUMBER = new Thompson(post);
        afnNUMBER.armar();
        
        /*
        * Creamos un afn para revisar ident
        */
        post = postfix.infixToPostfix(ident);
        afnIDENT = new Thompson(post);
        afnIDENT.armar();
        
        /*
        * Creamos un afn para revisar strings
        */
        post = postfix.infixToPostfix(string);
        afnSTRING = new Thompson(post);
        afnSTRING.armar();
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
    
    /*
    *Comparamos dos Strings caracter por caracter
    *debemos asegurarnos de que lo que queremos buscar no sea mas grande que 
    *el String donde lo estamos buscando
    *dejamos guardado elultimo index del String que fue aceptado
    *devolvemos si fue aceptado lo que estabamos buscando
    */
    private boolean checkString (String expr, String comp)
    {
        boolean igual = true;

        
        for (int i=0; i<expr.length();i++)
            if (expr.length()>i && comp.length()>i)
                if (expr.charAt(i) != comp.charAt(i))
                    igual = false;
                else
                    this.lastindex++;
            else
                igual = false;
        
        return igual;
    }
    public void revisar()
    {
        //Revisamos la primera Linea
        String temp = gramatica.get(contL);
        if (!checkString("Compiler",temp))
            errores.add("Error en Linea "+Integer.toString(contL+1)+".");
        
        ident(temp,false);
        
        if (this.lastindex+1 < temp.length())
            this.errores.add("Error en Linea "+Integer.toString(contL+1)+" "+temp.substring(this.lastindex+1)+" no pudo ser reconocido.");
        
        //Comenzamos una nueva linea
        this.lastindex = 0;
        contL++;
        
        //Realizamos el ScannerSpecification
        ScannerSpecification();
        
        //Revisamos solo END
        temp = gramatica.get(contL);
        if (!checkString("End",temp))
            errores.add("Error en Linea "+Integer.toString(contL+1)+".");
        
        //Buscamos el ident
        ident(temp, true);
       
        //Revisamos que el ident de inicio sea igual al de fin
        if (!ids.get(0).equals(ids.get(ids.size()-1)))
            errores.add("El ident inicial no coincide con el ident final.");
        
        //Buscamos que termine con .
        temp = gramatica.get(contL);
        if (!checkString(".",temp.substring(this.lastindex+1)))
            errores.add("Error en Linea "+Integer.toString(contL+1)+".");
        
    }
    
    private void ident(String expr, boolean fin)
    {
        String id = "";
        
        /*
        *Buscamos el primer caracter que no sea espacio en blanco
        *Para empezar a buscar desde ahi
        */
        int index = this.lastindex;
        Character c = expr.charAt(index);
        while (c == ' ')
        {
            index++;
            c = expr.charAt(index);
        }
        
        /*
        *Copiamos el index donde debemos empezar a buscar a nuestro ultimo index 
        *buscamos el primer caracter a simular 
        *tenemos una bandera para saber cuando deja de poder simular
        *ademas debemos de asegurarnos de parar tambien si ya no tenemos mas que simular
        */
        this.lastindex = index;
        String temp = expr.substring(index,index+1);
        SimulacionAFN simu = new SimulacionAFN(afnIDENT.getAuto(),temp.toString());
        boolean flag = simu.Simular();
        while (((this.lastindex+1)<expr.length()) && flag)
        {
            this.lastindex++;
            temp = expr.substring(index,this.lastindex+1);
            simu = new SimulacionAFN(afnIDENT.getAuto(),temp.toString());
            flag = simu.Simular();
        }
        
        /*Revisamos si pudo simular toda la expresión
        *Si la pudo simular copiamos lo que simulo al id
        *si no la pudo simular copiamos lo ultimo que logro simular
        */
        if (flag)
            id = temp;
        else
        {
            this.lastindex--;
            id=temp.substring(0, temp.length()-1);
        }
        /*
        if (!letter.contains(c))
            this.errores.add("Error en Linea "+Integer.toString(contL+1)+". ident no aceptado. Debe empezar con Letra.");
 
        for (int i=1; i<temp.length();i++)
        {
            c = temp.substring(i,i+1);
            if(letter.contains(c) || digit.contains(c)) 
            {
                id += temp.charAt(i);
            } 
            else 
            {
                id += temp.charAt(i);
                this.errores.add("Error en Linea "+Integer.toString(contL+1)+". ident no aceptado. El caracter \'"+c+"\' no puede ser utilizado.");
            }
        }
        */
        if (ids.contains(id) && !fin)
            this.errores.add("El ident en la linea "+Integer.toString(contL+1)+" ya ha sido usado anteriormente.");
        
        ids.add(id);
    }
    private void ScannerSpecification()
    {
        //CHARACTERS
        String temp = gramatica.get(contL);
        temp = temp.trim();
       
        boolean flagC = checkString("CHARACTERS",temp.substring(0, temp.length()));
        if (flagC)
        {
            setDecl();
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
        }
        //KEYWORDS
        
        temp = gramatica.get(contL);
        temp = temp.trim();
        
        boolean flagK = checkString("KEYWORDS",temp.substring(0, temp.length()));
        if (flagK)
        {
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
            KeywordDecl();
        }
        
        //WhiteSpaceDecl
        temp = gramatica.get(contL);
        temp = temp.trim();
        
        boolean flagW = checkString("IGNORE",temp.substring(0, temp.length()));
        if (flagW)
        {
            WhiteSpaceDecl();
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
        }
    }
    
    private void setDecl()
    {
        System.out.println("CHAR");
    }
    
    private void KeywordDecl()
    {
        //Revisamos el ident
        System.out.println("Key"); 
        String temp = gramatica.get(contL);
        ident(temp,false);
       
        //Revisamos el igual
        if (!checkString("=",temp.substring(this.lastindex+1).trim()))
           errores.add("Error en Linea "+Integer.toString(contL+1)+".");
        
        //Revisamos el string
        String temp2 = temp.substring(this.lastindex+1);
        int index = -1;
        int index2 = -1;
        for (int i=0;i<temp2.length();i++)
        {
            if(temp2.charAt(i) == '"' && index == -1)
                index = i;
            if(temp2.charAt(i) == '"' && index != -1)
                index2 = i;
        }
        
        
        //Revisamos el .
        
        
        //Comenzamos una nueva linea
        this.lastindex = 0;
        contL++;
    }
    
    private void WhiteSpaceDecl()
    {
        System.out.println("White");
    }
}
