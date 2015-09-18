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
public class LectorPrograma {
    ArrayList<String> program = new ArrayList();
    ArrayList<Automata> AFNS = new ArrayList();
    
    public LectorPrograma (ArrayList<String> archivo, ArrayList<String> equals)
    {
        this.program = archivo;
        for (String equal : equals) 
        {
            String post = myPostfix.infixToPostfix(equal);
            Thompson afn = new Thompson(post);
            afn.armar();
            this.AFNS.add(afn.getAuto());
        }
    }
    
}
