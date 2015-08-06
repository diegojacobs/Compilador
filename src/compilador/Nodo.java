/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Nodo {
    
    private Character carac;
    private Nodo right;
    private Nodo left;
    private Nodo previous;
    private boolean bin;
    
    public Nodo(Character c, Nodo n)
    {
        carac = c;
        right = null;
        left = null;
        previous = n;
        bin =true;
    }
    
    public void setCarac(Character a)
    {
        carac = a;
    }
    public void setLeft(Nodo next)
    {
        this.left = next;
    }
    public void setRight(Nodo next)
    {
        this.right = next;
    }
    
    public Character getCarac ()
    {
        return carac;
    }
    public Nodo getLeft()
    {
        return left;
    }
    public Nodo getRight()
    {
        return right;
    }

    public boolean getBin() 
    {
        return bin;
    }

    public void setBin(boolean bin) 
    {
        this.bin = bin;
    }

    public Nodo getPrevious() {
        return previous;
    }

    public void setPrevious(Nodo previous) {
        this.previous = previous;
    }
    
    

}
