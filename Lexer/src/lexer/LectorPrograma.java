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
    ArrayList<String> program = new ArrayList();
    ArrayList<String> ids = new ArrayList();
    
    ArrayList<Automata> AFNS = new ArrayList();
    ArrayList<Integer> lasts = new ArrayList();
    ArrayList<Boolean> flags = new ArrayList();
    
    
    int contL;
    
    ArrayList<String> res = new ArrayList();
    
    public LectorPrograma (ArrayList<String> archivo, ArrayList<String> equals, ArrayList<String> ids)
    {
        this.program = archivo;
        this.contL = 0;
        this.ids=ids;
        
        for (String equal : equals) 
        {
            String post = myPostfix.infixToPostfix(equal);
            Thompson afn = new Thompson(post);
            afn.armar();
            this.AFNS.add(afn.getAuto());
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
                    
                    if(!flag)
                    {
                        int mayor = -1;
                        String id = new String();
                        ArrayList<String> arrayid = new ArrayList();
                        for (i=0; i<this.AFNS.size();i++)
                        {
                            if (this.lasts.get(i) == mayor && this.lasts.get(i)>-1)
                            {
                                mayor = this.lasts.get(i);
                                id = this.ids.get(i+1);
                                arrayid.add(id);
                            }
                            if (this.lasts.get(i) > mayor)
                            {
                                arrayid.clear();
                                mayor = this.lasts.get(i);
                                id = this.ids.get(i+1);
                                arrayid.add(id);
                            }
                        }
                        if (arrayid.isEmpty())
                        {
                            this.res.add(line.substring(index) + " = " + "Error.");
                            index=index2+1;
                        }
                        else
                        {
                            if (!arrayid.contains("IGNORE"))
                                for (String tempid:arrayid)
                                    this.res.add(line.substring(index,mayor+1) + " = " + tempid);
                            index=mayor+1;
                            index2=index+1;
                        }
                        i=0;
                        for (Automata AFNS1 : this.AFNS) 
                        {
                            this.lasts.set(i,-1);
                            this.flags.set(i,false);
                            i++;
                        }
                    }  
                    else
                        index=index2;         
                }
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
                                id = this.ids.get(i+1);
                                arrayid.add(id);
                            }
                            if (this.lasts.get(i) > mayor)
                            {
                                arrayid.clear();
                                mayor = this.lasts.get(i);
                                id = this.ids.get(i+1);
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
                            this.res.add(temp + " = " + tempid);
                }
            }
            this.contL++;
        }
    }
    
}