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
    //ArrayList que contienen el archivo
    ArrayList<String> gramatica;
    ArrayList<String> archivo;
    
    //ArrayList donde guardamos todos los errres encontrados
    private ArrayList<String> errores = new ArrayList();
    
    //Arraylist donde guardamos los idents y String que nos sirve para guardar un id temporal
    private ArrayList<String> ids = new ArrayList();
    private String id = new String();
    
    //ArrayList con la igualdad de los ident y string donde guaramos una igualdad temporal
    private ArrayList<String> equals = new ArrayList();
    private String equal = new String();
    
    //ArrayList con los tokens y su igualdad
    private ArrayList<String> idstokens = new ArrayList();
    private ArrayList<String> equalstokens = new ArrayList();
    private ArrayList<Boolean> ExceptKeys = new ArrayList();
    
    //ArrayList con las keywords
    private ArrayList<String> idskeys = new ArrayList();
    private ArrayList<String> equalskeys = new ArrayList();
    
    //Contador de linea donde nos encontramos
    private int contL = 0;
    
    //Ultimo indice reconocido
    private int lastindex = 0;
    
    private boolean ignorar = false;
    //AFN's de terminales
    Thompson afnLETTER;
    Thompson afnNUMBER;
    Thompson afnDIGIT;
    Thompson afnIDENT;
    Thompson afnSTRING;
    Thompson afnCHAR;
    
    
    //Vocabulario de Cocol
    private final String letter = "四a少b少c少d少e少f少g少h少i少j少k少l少m少n少ñ少o少p少q少r少s少t少u少v少w少x少y少z少A少B少C少D少E少F少G少H少I少J少K少L少M少N少Ñ少O少P少Q少R少S少T少U少V少W少X少Y少Z枼";
    private final String digit = "四0少1少2少3少4少5少6少7少8少9枼";
    private final String any = "四"+letter+"少"+digit+"少 少#少!少¡少¿少%少/少=少¸少;少:少,少€少<少>少¨少{少}少[少]少^少~少·少½少-少_少)少(少*少.少|少+少?少$少\\少'少\"枼";
    private final String ident = letter + "四"+letter+"少"+digit+"枼砂";
    private final String number = digit+"四"+digit+"枼砂";
    private final String string = "四"+any+"枼砂";
    private final String caracter = "四"+any+"枼";
    
    public LectorGramatica(ArrayList<String> lineas, ArrayList<String> archivo)
    {
        gramatica = lineas;
        this.archivo = archivo;
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
        
        /*
        * Creamos un afn para revisar chars
        */
        post = postfix.infixToPostfix(caracter);
        afnCHAR = new Thompson(post);
        afnCHAR.armar();
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

    public ArrayList<String> getEquals() {
        return equals;
    }

    public void setEquals(ArrayList<String> equals) {
        this.equals = equals;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public ArrayList<String> getIdstokens() {
        return idstokens;
    }

    public void setIdstokens(ArrayList<String> idstokens) {
        this.idstokens = idstokens;
    }

    public ArrayList<String> getEqualstokens() {
        return equalstokens;
    }

    public void setEqualstokens(ArrayList<String> equalstokens) {
        this.equalstokens = equalstokens;
    }

    public ArrayList<Boolean> getExceptKeys() {
        return ExceptKeys;
    }

    public void setExceptKeys(ArrayList<Boolean> ExceptKeys) {
        this.ExceptKeys = ExceptKeys;
    }

    public ArrayList<String> getIdskeys() {
        return idskeys;
    }

    public void setIdskeys(ArrayList<String> idskeys) {
        this.idskeys = idskeys;
    }

    public ArrayList<String> getEqualskeys() {
        return equalskeys;
    }

    public void setEqualskeys(ArrayList<String> equalskeys) {
        this.equalskeys = equalskeys;
    }
    
    //Nos sirve para obtener el verdadero valor de la linea que estamos revisando
    public int getIndex(String linea)
    {
        return archivo.indexOf(linea)+1;
    }
    
    public void revisar()
    {
        //Revisamos la primera Linea
        String temp = gramatica.get(contL);
        if (!checkString("COMPILER",temp))
            errores.add("Error en Linea "+Integer.toString(getIndex(temp))+".");
        
        if (!Ident(temp,false,true))
            this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Ident no reconocido.");
        else
            this.ids.add(this.id);
        
        if (this.lastindex+1 < temp.length())
            this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+" "+temp.substring(this.lastindex+1)+" no pudo ser reconocido.");
        
        //Comenzamos una nueva linea
        this.lastindex = 0;
        contL++;
        
        //Realizamos el ScannerSpecification
        ScannerSpecification();
        
        if (this.contL >= gramatica.size())
        {
            this.errores.add("Error en Linea hace falta End");
        }
        else
        {
            //Revisamos solo END
            temp = gramatica.get(contL);
            if (!checkString("END",temp))
                errores.add("Error en Linea "+Integer.toString(getIndex(temp))+".");

            //Buscamos el ident
            if (!Ident(temp,true,true))
                this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Ident no reconocido.");
            else
                this.ids.add(this.id);
            //Revisamos que el ident de inicio sea igual al de fin
            if (!ids.get(0).equals(ids.get(ids.size()-1)))
                errores.add("El ident inicial no coincide con el ident final.");

            //Buscamos que termine con .
            temp = gramatica.get(contL);
            if (!checkString(".",temp.substring(this.lastindex)))
                errores.add("Error en Linea "+Integer.toString(getIndex(temp))+".");
        }
    }
    
    /****
    *Comparamos dos Strings caracter por caracter
    *debemos asegurarnos de que lo que queremos buscar no sea mas grande que 
    *el String donde lo estamos buscando
    *dejamos guardado elultimo index del String que fue aceptado
    *devolvemos si fue aceptado lo que estabamos buscando
    */
    private boolean checkString (String expr, String comp)
    {
        boolean igual = true;

        /*Debemos buscar la primera letra en comp
        *para empezar a revisar des ahi
        *ademas debemos asegurarnos que el tamaño de comp sea mayor a 0
        */
        int index = 0;
        if (comp.length() > index)
        {
            Character c = comp.charAt(index);
            while (c == ' ')
            {
                index++;
                c = comp.charAt(index);
                this.lastindex++;
            }
            comp = comp.substring(index);
            
            for (int i=0; i<expr.length() && igual;i++)
                if (expr.length()>i && comp.length()>i)
                    if (expr.charAt(i) != comp.charAt(i))
                        igual = false;
                    else
                        this.lastindex++;
                else
                    igual = false;
        }
        else
            igual = false;
        
        return igual;
    }
    
    /****
     * Revisamos lis ident 
     * si son aceptados los guardamos en el arraylist de ids
     * si es uno repetido reportamos el error
     * 
     * @param expr recibimos un String y el mira cual fue el ultimo index que acepto
     * @param fin nos sirve para ver si es el ident final
     * 
     * Devolvemos si fue aceptado o no
     * para esto debemos revisar si no fue aceptado, si lo que continua
     * puede ser aceptado por la siguiente instruccion
     */
    private boolean Ident(String expr, boolean fin, boolean idIzq)
    {
        int tempindex = this.lastindex;
        boolean flag = true;
        
        this.id = "";
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
        SimulacionAFN simu = new SimulacionAFN(afnIDENT.getAuto(),temp);
        flag = simu.Simular();
        if (flag)
        {
            while (((this.lastindex+1)<expr.length()) && flag)
            {
                this.lastindex++;
                temp = expr.substring(index,this.lastindex+1);
                simu = new SimulacionAFN(afnIDENT.getAuto(),temp);
                flag = simu.Simular();
            }
        }
        else
            return false;
        
        /*
        * Si no logro ser simulada
        * debemos revisar si el caracter que devolvio el false fue 
        * uno con lo que puede continuar lo que viene.
        */
        if (flag)
            this.id = temp;
        else
            if (expr.charAt(lastindex) == ' ' || expr.charAt(lastindex) == '+' || expr.charAt(lastindex) == '-' || expr.charAt(lastindex) == '.' || expr.charAt(lastindex) == '=')
            {
                this.lastindex--;
                flag = true;
                this.id = expr.substring(index, this.lastindex+1);
            }
            else
                if (expr.charAt(lastindex) == '(' || expr.charAt(lastindex) == ')' || expr.charAt(lastindex) == '[' || expr.charAt(lastindex) == ']' || expr.charAt(lastindex) == '{' || expr.charAt(lastindex) == '}')
                {
                    this.lastindex--;
                    flag = true;
                    this.id = expr.substring(index, this.lastindex+1);
                }
        if (flag)
        {
            this.lastindex++;
            if (ids.contains(this.id) && !fin && idIzq)
                this.errores.add("El ident en la linea "+Integer.toString(getIndex(expr))+" ya ha sido usado anteriormente.");
            
            return true;
        }
        else 
        {
            this.lastindex = tempindex;
            return false;
        }
    }
    /***
    * Metodo para simular un string
    * recibimos un string en el cual debemos buscar dos comillas
    * simulamos en un afn lo que este dentro
    * si es aceptado es guardado
    * si no es aceptado se da el mensaje de error
    * devolvemos si logro o no ser aceptada la cadena
    ***/
    private boolean string(String expr)
    {
        int indexOriginal = this.lastindex;
        String temp2 = expr.substring(this.lastindex);
        int index = -1;
        int index2 = -1;
        boolean flag = true;
        for (int i=0;i<temp2.length() && flag;i++)
        {
            char c = temp2.charAt(i);
            
            if (c!='\\')
            {
                if (index == -1)
                    if (c == '.' || c == '-' || c =='+' || c =='\'' || c=='|' || c=='(' || c=='[' || c=='{')
                        flag = false;
            
                if(c == '"' && index != -1)
                {
                    flag=false;
                    index2 = i;
                }
                if(c == '"' && index == -1)
                    index = i;
            }
            else
                i++;
        }
        
        /*
        * Miramos si logramos encontrar las comillas
        * Si las encontramos simulamos en afnString nuestra cadena
        * Si no reportamos error en la linea
        */
        
        if (index == -1 || index2==index)
            return false;
        else
        {
            //debemos sumarle a nuestros index el ultimo index que reconocio 
            index += this.lastindex;
            index2 += this.lastindex;
            temp2 = "";
            temp2 += expr.charAt(index+1);
            this.lastindex = index+1;
            SimulacionAFN simu = new SimulacionAFN(afnSTRING.getAuto(),temp2);
            flag = simu.Simular();
            while (((this.lastindex+1)<index2) && flag)
            {
                this.lastindex++;
                temp2 = expr.substring(index+1,this.lastindex+1);
                simu = new SimulacionAFN(afnSTRING.getAuto(),temp2);
                flag = simu.Simular();
            }
            
            /*Revisamos si pudo simular toda la expresión
            *Si la pudo simular volvemos nuestro lastindex el index de la segunda comilla
            *si no la pudo simular volvemos nuestro lastindex el index con e que entro
            */
            if (flag)
            {
                this.lastindex = index2+1;
                return true;
            }
            else
            {
                this.lastindex = indexOriginal;
                return false;
             }
        }
    }
    
    private boolean caracter(String expr)
    {
        int indexOriginal = this.lastindex;
        String temp2 = expr.substring(this.lastindex);
        
        int index = -1;
        int index2 = -1;
        boolean flag=true;
        boolean diag = false;
        for (int i=0;i<temp2.length() && flag;i++)
        {
            char c = temp2.charAt(i);
            if (c != '\\')
            {
                if (index == -1)
                    if (c == '.' || c == '-' || c =='+' || c =='\"' || c=='|' || c=='(' || c=='[' || c=='{')
                        flag = false;
            
                if(c == '\'' && index != -1)
                {
                    flag=false;
                    index2 = i;
                }
                if(c == '\'' && index == -1)
                    index = i;
            }
            else
            {
                i++;
                diag = true;
            }
        }
        if (diag)
        {
            index++;
        }
        
        /*
        * Miramos si logramos encontrar los apostrofes
        * Si las encontramos simulamos en afnCHAR nuestra cadena
        * Si no reportamos error en la linea
        */
        
        if (index == -1 || index2==index || index+2!=index2)
            return false;
        else
        {
            //debemos sumarle a nuestros index el ultimo index que reconocio 
            index += this.lastindex;
            index2 += this.lastindex;
            temp2 = "";
            temp2 += expr.charAt(index+1);
            this.lastindex = index+1;
            SimulacionAFN simu = new SimulacionAFN(afnCHAR.getAuto(),temp2);
            flag = simu.Simular();
            
            /*Revisamos si pudo simular toda la expresión
            *Si la pudo simular volvemos nuestro lastindex el index de segundo apostrofe
            *si no la pudo simular volvemos nuestro lastindex el index con e que entro
            */
            if (flag)
            {
                this.lastindex = index2+1;
                return true;
            }
            else
            {
                this.lastindex = indexOriginal;
                return false;
             }
        }
    }
    
    private boolean number(String expr)
    {
        int indexOriginal = this.lastindex;
        String temp2 = expr.substring(this.lastindex);
        int index = -1;
        int index2 = -1;
        boolean flag=true;
        for (int i=0;i<temp2.length() && flag;i++)
        {
            char c = temp2.charAt(i);
            if(c == ')' && index!=-1)
            {
                flag=false;
                index2 = i;
            }
            if(c == '(' && index == -1)
                index = i;
        }
        
        /*
        * Miramos si logramos encontrar los parentesis
        * Si las encontramos simulamos en afnNUMBER nuestra cadena
        * Si no reportamos error en la linea
        */
        
        if (index == -1 || index2 == -1 || index2==index )
            return false;
        else
        {
            //debemos sumarle a nuestros index el ultimo index que reconocio 
            index += this.lastindex;
            index2 += this.lastindex;
            temp2 = "";
            temp2 += expr.charAt(index+1);
            this.lastindex = index+1;
            SimulacionAFN simu = new SimulacionAFN(afnNUMBER.getAuto(),temp2);
            flag = simu.Simular();
            while (((this.lastindex+1)<index2) && flag)
            {
                this.lastindex++;
                temp2 = expr.substring(index+1,this.lastindex+1);
                simu = new SimulacionAFN(afnNUMBER.getAuto(),temp2);
                flag = simu.Simular();
            }
            
            /*Revisamos si pudo simular toda la expresión
            *Si la pudo simular volvemos nuestro lastindex el index de segundo parentesis
            *si no la pudo simular volvemos nuestro lastindex el index con e que entro
            */
            if (flag)
            {
                this.lastindex = index2+1;
                return true;
            }
            else
            {
                this.lastindex = indexOriginal;
                return false;
             }
        }
    }
    
    /****
     * Debemos buscar CHARACTERS si esto no se encuentra
     * debemos buscar KEYWORDS
     * si esto no se encunetra buscamos IGNORE
     */
    private void ScannerSpecification()
    {
        if (contL<gramatica.size())
        {
            //CHARACTERS
            String temp = gramatica.get(contL);
            temp = temp.trim();
       
            boolean flagC = checkString("CHARACTERS",temp.substring(0, temp.length()));
            if (flagC)
            {
                //Comenzamos una nueva linea
                this.lastindex = 0;
                contL++;
                setDecl();
            }
        }
        
        if (this.contL < gramatica.size())
        {
            //KEYWORDS
        
            String temp = gramatica.get(contL);
            temp = temp.trim();
        
            boolean flagK = checkString("KEYWORDS",temp.substring(0, temp.length()));
            if (flagK)
            {
                //Comenzamos una nueva linea
                this.lastindex = 0;
                contL++;
                KeywordDecl();
            }
        }
        if (contL<gramatica.size())
        {
            //Tokens
            String temp = gramatica.get(contL);
            temp = temp.trim();
        
            boolean flagW = checkString("TOKENS",temp.substring(0, temp.length()));
            if (flagW)
            {
                //Comenzamos una nueva linea
                this.lastindex = 0;
                contL++;
                TokenDecl();
            }
        }
        
        if (contL<gramatica.size())
        {
            //WhiteSpaceDecl
            String temp = gramatica.get(contL);
            temp = temp.trim();
        
            boolean flagW = checkString("IGNORE",temp.substring(0, temp.length()));
            if (flagW)
            {
                WhiteSpaceDecl(temp);
            }
        }
    }
    
    /****
     * Debemos buscar ident = Set.
     * Mientras lo que leemos no sea KEYWORDS o IGNORE o End
     */
    private void setDecl()
    {
        while (contL<gramatica.size()  && !checkString("TOKENS",gramatica.get(contL).trim()) && !checkString("KEYWORDS",gramatica.get(contL).trim()) && !checkString("IGNORE",gramatica.get(contL).trim()) && !checkString("END",gramatica.get(contL).trim()))
        {
            String temp = gramatica.get(contL);
        
            //Revisamos el ident
            if (!Ident(temp,false,true))
                this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Ident no reconocido.");
            else
            {
                this.ids.add(this.id);
            
        
                //Revisamos el igual
                if (!checkString("=",temp.substring(this.lastindex)))
                    errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Falto el = para asignar algo.");
                else
                {
                    //Revisamos Set
                    Set(temp);
            
                    //Revisamos el .
                    if (!checkString(".",temp.substring(this.lastindex)))
                        errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Falto el . para terminar linea.");
                }
            }
            //Debemos comenzar una nueva linea
            this.contL++;
            this.lastindex = 0;
            
            //Guardamos la igualdad
            this.equal = addOr(this.equal);
            this.equals.add(this.equal);
            this.equal = new String();
        }
    }
    
    /****
     * Debemos buscar un BasicSet
     * y puede que tambien tengamos mas de un + o -
     * seguido de otro BasicSet
     * @param expr 
     */
    private void Set(String expr)
    {
        BasicSet(expr);
        boolean flag = true;
        while (!checkString(".",expr.substring(this.lastindex)) && flag)
            if (checkString("-",expr.substring(lastindex)))
            {
                this.equal += '要';
                BasicSet(expr);
                removeChar();
            }
            else
                if(checkString("+",expr.substring(lastindex)))
                {
                    //Debemos concatenare a equal lo que viene
                    this.equal += '少';
                    BasicSet(expr);
                }
                else
                {
                    flag = false;
                    this.errores.add("Error en Linea "+Integer.toString(getIndex(expr))+".");
                }
            
        this.lastindex--;
    }
    /****
     * Debemos ver si nuestra expr recibida es
     * string|ident|Char
     * si es alguna de estas debemos guardarla
     * si no reportar un error.
     * @param expr 
     */
    private void BasicSet(String expr)
    {
        int index = this.lastindex;
        
        if(string(expr))
        {
            this.equal += saveString(expr.substring(index,this.lastindex-1));
        }
        else
        {
            this.lastindex = index;
            if (Ident(expr,false,false) && !this.id.equals("CHR"))
            {
                if(this.ids.contains(this.id) )
                    this.equal += "四"+this.equals.get(ids.indexOf(id)-1)+"枼";
                else
                    if(!this.ignorar)
                        this.errores.add("El ident "+this.id+" no ha sido declarado.");
            }
            else 
            {
                this.lastindex = index;
                int indexequal = this.equal.length();
                if (Char(expr,index))
                {
                    int tempindex = this.lastindex;
                    if(checkString("..",expr.substring(this.lastindex)))
                    {
                        if (Char(expr,this.lastindex+1))
                        {
                            String cadena = this.equal.substring(indexequal);
                            String temp = convertChar(cadena);
                            cadena =this.equal.substring(0,indexequal);
                            this.equal = cadena + temp;
                        }
                        else
                        {
                            this.errores.add("Error en linea "+Integer.toString(getIndex(gramatica.get(contL)))+".");
                        }
                    }
                    else
                    {
                        this.lastindex = tempindex;
                    }
                }
                else
                {
                    this.lastindex = index;
                    this.errores.add("Error en linea "+Integer.toString(getIndex(gramatica.get(contL)))+".");
                }
            }
        }
    }
    
    private boolean Char(String expr, int index)
    {
        //Revisamos el caracter
        if (caracter(expr))
        {
            this.equal += saveCaracter(expr.substring(index,this.lastindex-1));
            return true;
        }
        else
            if (checkString("CHR",expr.substring(this.lastindex)))
            {
                index = this.lastindex+2;
                if (number(expr))
                {
                    this.equal += saveCHR(expr.substring(index, this.lastindex-1));
                    return true;
                }
                else
                {       
                    return false;
                }
                
            }
            else
            {
                this.errores.add("Error en Linea "+Integer.toString(getIndex(expr))+". Char no reconocido.");
                return false;
            }
    }
    /****
     * Debemos buscar ident = string.
     * Mientras o que leamos no sea IGNORE o End
     */
    private void KeywordDecl()
    {
        while (contL<gramatica.size() && !checkString("TOKENS",gramatica.get(contL).trim()) && !checkString("IGNORE",gramatica.get(contL).trim()) && !checkString("END",gramatica.get(contL).trim()))
        {
            //Debemos resetear el lastindex
            this.lastindex = 0;
            
            //Revisamos el ident
            String temp = gramatica.get(contL);
            if (!Ident(temp,false,true))
                this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Ident no reconocido.");
            else
            {
                this.ids.add(this.id);//borrar
                this.idskeys.add(this.id);
       
                //Revisamos el igual
                if (!checkString("=",temp.substring(this.lastindex)))
                    errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Falto el = para asignar algo.");
                else
                {
                    int index = this.lastindex;
                    
                    //Revisamos el string
                    if (!string(temp))
                        errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". El string no fue aceptado.");
                    else
                    {
                        this.equal += saveString(temp.substring(index,this.lastindex-1));
        
                        //Revisamos el .
                        if (!checkString(".",temp.substring(this.lastindex)))
                            errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Falto el . para terminar linea.");
                
                    }
                }
            }
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
            
            //Guardamos la igualdad
            this.equalskeys.add(this.equal);
            this.equals.add(this.equal);//borrar
            this.equal = new String();
        }
        
        //Debemos resetear el lastindex
        this.lastindex = 0;
    }
    /****
     * Comenzamos a revisar la declaracion de tokens
     * paramos hasta que encontremos un IGNORE
     */
    private void TokenDecl()
    {
        while (contL<gramatica.size() && !checkString("IGNORE",gramatica.get(contL).trim()) && !checkString("END",gramatica.get(contL).trim()))
        {
            //Debemos resetear el lastindex
            this.lastindex = 0;
            
            //Copiamos la proxima linea
            String temp = gramatica.get(contL);
            
            //Revisamos el ident
            if (!Ident(temp,false,false))
                this.errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Ident no reconocido.");
            else
            {
                //Revisamos el igual
                if (checkString("=",temp.substring(this.lastindex)))
                {
                    if (this.ids.contains(this.id) || this.idstokens.contains(this.id))
                    {
                        this.errores.add("El ident en la linea "+Integer.toString(getIndex(temp))+" ya ha sido usado anteriormente.");
                    }
                    //agregamos el ident a tokens
                    this.idstokens.add(this.id);
             
                    TokenExpr(temp);
                    //Revisamos EXCEPT KEYWORDS
                    if (checkString("EXCEPT KEYWORDS",temp.substring(this.lastindex)))
                    {
                        this.ExceptKeys.add(true);
                    }
                    else
                    {   
                        this.ExceptKeys.add(false);
                    }
                }
                else
                {
                    //agregamos el ident a tokens
                    this.idstokens.add(this.id);
                    
                    //Si no hubo igual revisamos si ha EXCEPT KEYWORDS
                    if (checkString("EXCEPT KEYWORDS",temp.substring(this.lastindex)))
                    {
                        this.ExceptKeys.add(true);
                    }
                    else
                        this.ExceptKeys.add(false);
                    
                    //Debemos buscar la igualdad del ident y agregarla
                    if (this.ids.contains(this.id))
                    {
                        this.equal += "四"+this.equals.get(ids.indexOf(id)-1)+"枼";
                    }
                    else
                        if (this.idstokens.contains(this.id))
                        {
                            this.equal += "四"+this.equalstokens.get(idstokens.indexOf(id))+"枼";
                        }
                        else
                            this.errores.add("El ident en la linea "+Integer.toString(getIndex(temp))+". No ha sido declarado." );
                    
                }
            
                //Revisamos el .
                if (!checkString(".",temp.substring(this.lastindex)))
                    errores.add("Error en Linea "+Integer.toString(getIndex(temp))+". Falto el . para terminar linea.");
            }
            
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
            
            //Debemos agregar a equalstokens la igualdad
            this.equalstokens.add(this.equal);
            this.equal = new String();
        }
        //Debemos resetear el lastindex
        this.lastindex = 0;
    }
    
    private void TokenExpr(String expr)
    {
        TokenTerm(expr);
        while (checkString("|",expr.substring(this.lastindex)))
        {
            this.equal += '少'; //ponemos el | a nuestra expresión
            TokenTerm(expr);
        }
    }
    
    private void TokenTerm(String expr)
    {
        TokenFactor(expr);
        boolean flag = true;
        while (flag && !checkString("|",expr.substring(this.lastindex)) && !checkString(".",expr.substring(this.lastindex)) && !checkString("}",expr.substring(this.lastindex)) && !checkString(")",expr.substring(this.lastindex)) && !checkString("]",expr.substring(this.lastindex)))
        {
            int index = this.lastindex;
            if (!checkString("EXCEPT KEYWORDS",expr.substring(this.lastindex)))
            {
                this.equal += '要';
                TokenFactor(expr);
            }
            else
            {
                flag = false;
                this.lastindex = index;
            }                
        }
        this.lastindex--;
    }
    
    private void TokenFactor(String expr)
    {
        if (checkString("(",expr.substring(this.lastindex)))
        {
            this.equal += '四';//ponemos el ( a nuestra expresión
            TokenExpr(expr);
            if (!checkString(")",expr.substring(this.lastindex)))
            {
                this.errores.add("Error en la Linea "+Integer.toString(getIndex(expr))+". Hace falta )");
            }
            else
                this.equal += '枼';//ponemos el ) a nuestra expresión
        }
        else
        {
            if (checkString("[",expr.substring(this.lastindex)))
            {
                this.equal += '四';//ponemos el ( a nuestra expresión
                this.equal += '四';//ponemos el ( a nuestra expresión
                TokenExpr(expr);
                if (!checkString("]",expr.substring(this.lastindex)))
                {
                    this.errores.add("Error en la Linea "+Integer.toString(getIndex(expr))+". Hace falta ]");
                }
                else
                {
                    this.equal += '枼';//ponemos el ) a nuestra expresión
                    this.equal += '少';//ponemos el | a nuestra expresión
                    this.equal += '永';//ponemos el epsilon a nuestra expresión
                    this.equal += '枼';//ponemos el ) a nuestra expresión
                }
            }
            else
                if (checkString("{",expr.substring(this.lastindex)))
                {
                    this.equal += '四';//ponemos el ( a nuestra expresión
                    TokenExpr(expr);
                    if (!checkString("}",expr.substring(this.lastindex)))
                    {
                        this.errores.add("Error en la Linea "+Integer.toString(getIndex(expr))+". Hace falta }");
                    }
                    else
                    {
                        this.equal += '枼';//ponemos el ) a nuestra expresión
                        this.equal += '砂';//ponemos el * a nuestra expresión
                    }
                }
                else
                {
                    if (!Symbol(expr))
                    {
                        this.errores.add("Error en Linea "+Integer.toString(getIndex(expr)));
                    }
                }
        }
    }
    
    private boolean Symbol(String expr)
    {
        int index = this.lastindex;
        
        if (Ident(expr,false,false))
        {
            if(this.ids.contains(this.id))
            {
                this.equal += "四"+this.equals.get(ids.indexOf(id)-1)+"枼";
                return true;
            }
            else
                if (this.idstokens.contains(this.id))
                {
                    this.equal += "四"+this.equalstokens.get(ids.indexOf(id))+"枼";
                    return true; 
                }
                else
                {
                    this.errores.add("El ident "+this.id+" no ha sido declarado.");
                    return true;
                }
        }
        else
        {
            this.lastindex = index;
            
            if(string(expr))
            {
                this.equal += saveString(expr.substring(index,this.lastindex-1));
                return true;
            }
            else 
            {
                this.lastindex = index;
                int indexequal = this.equal.length();
                if (caracter(expr))
                {
                    this.equal += saveCaracter(expr.substring(index,this.lastindex-1));
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }
    /****
     * Recibimos una expr que contine una linea de la gramatica
     * revisamos que contenga la palabra IGNORE y que no nos pasemos del numero de lineas
     * Buscaos Set.
     * 
     * @param expr 
     */
    private void WhiteSpaceDecl(String expr)
    {
        this.lastindex = 0;
        this.ignorar = false;
        while (contL<gramatica.size() && checkString("IGNORE",gramatica.get(contL).trim()))
        {
            expr = gramatica.get(this.contL);
            //Revisamos el Set
            this.equal = new String();
            Set(expr);
            
            this.equals.add(this.equal);
            this.ids.add("IGNORE");
            
            this.equalstokens.add(this.equal);
            this.idstokens.add("IGNORE");
            //Revisamos el .
            if (!checkString(".",expr.substring(this.lastindex)))
                errores.add("Error en Linea "+Integer.toString(getIndex(expr))+". Falto el . para terminar linea.");
            
            //Comenzamos una nueva linea
            this.lastindex = 0;
            contL++;
        }
    }
    
    /****
     * Recibimos una cadena string 
     * y metemos lo que se encuentra dentro de las comillas en parentesis
     * para guardarlo en equal
     * @param cadena
     * @return 
     */
    private String saveString(String cadena)
    {
        String temp = "四";
        int index = cadena.indexOf("\"");
        cadena = cadena.substring(index+1);
        for (int i=0;i<cadena.length();i++)
        {
            char c = cadena.charAt(i);
            if (c=='\\')
            {
                i++;
                temp+=cadena.charAt(i);
            }
            else
                temp+=c;
        }
        temp += "枼";
        return temp;
    }
    
    /****
     * Recibimos una cadena que contiene un solo caracter
     * y metemos este caracter dentro de parentesis 
     * para guardarlo en equal
     * @param cadena
     * @return 
     */
    private String saveCaracter(String cadena)
    {
        String temp = "四";
        int index = cadena.indexOf("'");
        cadena = cadena.substring(index+1);
        for (int i=0;i<cadena.length();i++)
        {
            char c = cadena.charAt(i);
            if (c=='\\')
            {
                i++;
                temp+=cadena.charAt(i);
            }
            else
                temp+=c;
        }
        temp += "枼";
        return temp;
    }
    
    /****
     * Recibimos una cadena que contiene un numero
     * este numero lo convertimos a char
     * ponemos este char entre parentesis para guardarlo en equal
     * @param cadena
     * @return 
     */
    private String saveCHR(String cadena)
    {
        String temp = "四";
        char c = (char)Integer.parseInt(cadena);
        temp += c+"枼";
        return temp;
    }
    
    /****
     * Recibimos una cadena que contiene dos caracteres 
     * estos dos caracteres se encuentran entre parentesis cada uno
     * los buscamos y agregamos todos los ascii en medio de estos
     * lo metemos entre parentesis, para guardarlo en equal
     * @param cadena
     * @return 
     */
    private String convertChar(String cadena)
    {
        String temp = "四";
        boolean flag=true;
        int index = -1;
        int index2 = -1;
        for (int i=0;i<cadena.length() && flag;i++)
        {
            char c = cadena.charAt(i);
            if(c == '枼' && index!=-1)
            {
                flag=false;
                index2 = i;
            }
            if(c == '四' && index == -1)
                index = i;
        }    
        
        char c1 = cadena.charAt(index+1);
        flag=true;
        int index3 = -1;
        for (int i=index2;i<cadena.length() && flag;i++)
        {
            char c = cadena.charAt(i);
            
            if(c == '四' && index3 == -1)
            {
                flag = false;
                index3 = i;
            }
        }
        char c2 = cadena.charAt(index3+1);
        
        for (int i = (int)c1;i<=(int)c2;i++)
            temp += (char)i;
        temp += "枼";
        
        return temp;
    }
    
    private String addOr(String cadena)
    {
        String temp = new String();
        
        for (int i=0;i<cadena.length();i++)
            if (cadena.charAt(i)!= '四' && cadena.charAt(i) != '少' && cadena.charAt(i) != '要' && cadena.charAt(i) != '枼' && i+1<cadena.length())
                if (cadena.charAt(i+1) != '枼'&& cadena.charAt(i+1) != '少' && cadena.charAt(i+1) != '要')
                    temp += cadena.charAt(i)+"少";
                else
                    temp+=cadena.charAt(i);
            else
                temp += cadena.charAt(i);
        
        return temp;
    }
    
    private void removeChar()
    {
        String temp = new String();
        String temp2 = new String();
        boolean flag = true;
        for (int i = this.equal.length()-1;i>0 && flag;i--)
        {
            if (this.equal.charAt(i) != '要')
                temp = this.equal.charAt(i)+temp;
            else
            {
                temp2 = this.equal.substring(0, i);
                flag= false;
            }  
        }
        String temp3 = new String();
        for (int i = 0; i<temp2.length();i++)
        {
            if (temp2.charAt(i) != '四' && temp2.charAt(i) != '枼')
                if(!temp.contains(temp2.substring(i,i+1)))
                    temp3 += temp2.charAt(i);
                else
                    temp3=temp3;
            else
                temp3 += temp2.charAt(i);
        }
        this.equal = temp3;
    }
}