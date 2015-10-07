/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs
 */
public class LectorPrograma {
    private ArrayList<String> program = new ArrayList();
    private ArrayList<String> ids = new ArrayList();
    private ArrayList<String> exception = new ArrayList();
    private ArrayList<String> keys = new ArrayList();
  
    
    private ArrayList<Automata> AFNS = new ArrayList();
    private ArrayList<Automata> AFNSKeys = new ArrayList();
    private ArrayList<Integer> lasts = new ArrayList();
    private ArrayList<Boolean> flags = new ArrayList();
    
    
    private int contL;
    private int indexid;
    private String id;
    
    private ArrayList<String> res = new ArrayList();
    
    public LectorPrograma (ArrayList<String> archivo, ArrayList<String> equals, ArrayList<String> ids, ArrayList<String> key, ArrayList<String> ex, ArrayList<String> equalkey)
    {
        this.program = archivo;
        this.contL = 0;
        this.ids=ids;
        this.exception=ex;
        this.keys=key;
        
        for (String equal : equals) 
        {
            String post = myPostfix.infixToPostfix(equal);
            Thompson afn = new Thompson(post);
            afn.armar();
            this.AFNS.add(afn.getAuto());
        }
        
        for (String equal : equalkey) 
        {
            String post = myPostfix.infixToPostfix(equal);
            Thompson afn = new Thompson(post);
            afn.armar();
            this.AFNSKeys.add(afn.getAuto());
        }
        
        for (Automata AFNS1 : this.AFNS) 
        {
                this.lasts.add(-1);
                this.flags.add(false);
        }
    }

    public ArrayList<String> getProgram() {
        return program;
    }

    public void setProgram(ArrayList<String> program) {
        this.program = program;
    }

    public ArrayList<Automata> getAFNS() {
        return AFNS;
    }

    public void setAFNS(ArrayList<Automata> AFNS) {
        this.AFNS = AFNS;
    }

    public ArrayList<Integer> getLasts() {
        return lasts;
    }

    public void setLasts(ArrayList<Integer> lasts) {
        this.lasts = lasts;
    }

    public ArrayList<Boolean> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<Boolean> flags) {
        this.flags = flags;
    }

    public int getContL() {
        return contL;
    }

    public void setContL(int contL) {
        this.contL = contL;
    }

    public ArrayList<String> getRes() {
        return res;
    }

    public void setRes(ArrayList<String> res) {
        this.res = res;
    }
    
    public void revisar()
    {
        while (this.contL<this.program.size())
        {
            int index = 0; //Numero de caracter que llevamos revisado de la linea
            int index2 = 1;
            int contA = 0;
            boolean flag = false;
            
            //Debemos reiniciar los ArrayList
            int i=0;
            for (Automata AFNS1 : this.AFNS) 
            {
                this.lasts.set(i,-1);
                this.flags.set(i,false);
                i++;
            }
            String line = this.program.get(this.contL);
            String temp = line.substring(index, index2);
            
            while (index<line.length())
            {
                temp = line.substring(index, index2);
                
                while (contA < this.AFNS.size())
                {
                    Automata tempA = this.AFNS.get(contA);
                    SimulacionAFN simu = new SimulacionAFN(tempA,temp);
                    if (simu.Simular())
                    {
                        this.flags.set(contA, Boolean.TRUE);
                        this.lasts.set(contA,index2-1);
                    }
                    else
                    {
                        this.flags.set(contA, Boolean.FALSE);
                    }
                    contA++;
                }
                if (index2+1 <= line.length())
                    index2++;
                else
                {
                    //revisar si todos los flags son false
                    //si todos son false debo buscar el last mas grande
                    //y el index se vuelve el last mas 1 y el index2 se vuelve el index + 2
                    //si uno es true tomo ese como id
                    for (Boolean flag1 : flags) 
                        if (flag1)
                            flag=true;
                    
                    //Si ninguno logro simular toda la cadena buscamos cual fue el ultimo caracter simulado
                    if(!flag)
                    {
                        //Buscamos el automata que haya aceptado el mayor numero de caracteres
                        //Y buscamos el ident perteneciente a este automata
                        int mayor = -1;
                        String id = new String();
                        ArrayList<String> arrayid = new ArrayList();
                        for (i=0; i<this.AFNS.size();i++)
                        {
                            if (this.lasts.get(i) == mayor && this.lasts.get(i)>-1)
                            {
                                mayor = this.lasts.get(i);
                                id = this.ids.get(i);
                                arrayid.add(id);
                            }
                            if (this.lasts.get(i) > mayor)
                            {
                                arrayid.clear();
                                mayor = this.lasts.get(i);
                                id = this.ids.get(i);
                                arrayid.add(id);
                            }
                        }
                        
                        //Si no se encontro ningun identificador para la cadena aceptada
                        //Motramos la cadena como error
                        //Si no mostamos el ident encontrado
                        if (arrayid.isEmpty())
                        {
                            this.res.add(line.substring(index) + " = " + "Error.");
                            index=index2+1;
                        }
                        else
                        {
                            if (!arrayid.contains("IGNORE"))
                                for (String tempid:arrayid)
                                {
                                    this.id = tempid;
                                    temp=line.substring(index,mayor+1);
                                    this.res.add(temp + " = " + tempid);
                                }
                            index=mayor+1;
                            index2=index+1;
                        }
                        
                        //buscamos el index del id;
                        this.indexid=this.ids.indexOf(this.id);
                        //buscamos si tiene except keywords en la posicion del id seleccionado
                        if (!arrayid.contains("IGNORE") && this.exception.get(this.indexid).equals("true"))
                        {
                            //Simulamos lo aceptado en los automatas de keywords
                            //Si es aceptado en uno es cambiado el ident
                            //Si no solo se prosigue
                            contA = 0;
                            while (contA < this.AFNSKeys.size())
                            {
                                Automata tempA = this.AFNSKeys.get(contA);
                                SimulacionAFN simu = new SimulacionAFN(tempA,temp);
                                if (simu.Simular())
                                {
                                    this.res.remove(this.res.size()-1);
                                    this.res.add(temp+" = "+this.keys.get(contA));
                                    contA=this.AFNS.size();
                                }
                                else
                                    contA++;
                            }
                        }
                        
                        //Reseteamos nuestros valores de los ArrayList
                        i=0;
                        for (Automata AFNS1 : this.AFNS) 
                        {
                            this.lasts.set(i,-1);
                            this.flags.set(i,false);
                            i++;
                        }
                    }
                    //Si logro ser simulado copiamos al index inicial el ultimo index leido
                    else
                        index=index2;         
                }
                //Reseteamos contador de automatas
                contA=0;
            }
            
            if (flag)
            {
                //Buscamos que automata la reconocio
                int mayor = -1;
                String id = new String();
                ArrayList<String> arrayid = new ArrayList();
            
                for (i=0; i<this.AFNS.size();i++)
                {
                    if(this.flags.get(i))
                    {
                        if (this.lasts.get(i) == mayor && this.lasts.get(i)>-1)
                        {
                            mayor = this.lasts.get(i);
                            id = this.ids.get(i);
                            arrayid.add(id);
                        }
                        
                        if (this.lasts.get(i) > mayor)
                        {
                            arrayid.clear();
                            mayor = this.lasts.get(i);
                            id = this.ids.get(i);
                            arrayid.add(id);
                        }
                    }
                }   
                if (arrayid.isEmpty())
                    this.res.add(temp + " = " + "Error.");
                else
                {
                    if (!arrayid.contains("IGNORE"))
                        for (String tempid:arrayid)
                        {
                            this.id = tempid;
                            this.res.add(temp + " = " + tempid);
                        }
                }
                //buscamos el index del id;
                this.indexid=this.ids.indexOf(this.id);
                
                //buscamos si tiene except keywords en la posicion del id seleccionado
                if (!arrayid.contains("IGNORE") && this.exception.get(this.indexid).equals("true"))
                {
                    contA = 0;
                    while (contA < this.AFNSKeys.size())
                    {
                        Automata tempA = this.AFNSKeys.get(contA);
                        SimulacionAFN simu = new SimulacionAFN(tempA,temp);
                        if (simu.Simular())
                        {
                            this.res.remove(this.res.size()-1);
                            this.res.add(temp+" = "+this.keys.get(contA));
                            contA=this.AFNS.size();
                        }
                        else
                            contA++;
                    }
                }
            }
            this.contL++;
        }
    }
    
}