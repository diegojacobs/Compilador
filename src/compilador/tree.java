/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Diego Jacobs 13160
 */
public class tree {
    private Nodo raiz;
    private Nodo actual;
    private String hojas = new String();
    private Stack<Character> st = new Stack<Character>();
    List<Character> unarios = Arrays.asList('*');
    List<Character> binarios = Arrays.asList('|','.');
    List<Character> operadores = Arrays.asList('|','.','*');
    
    public void tree()
    {
        raiz = null;
    }

    public Nodo getRaiz() 
    {
        return raiz;
    }

    public void setRaiz(Nodo raiz) 
    {
        this.raiz = raiz;
    }

    public Nodo getActual() 
    {
        return actual;
    }

    public void setActual(Nodo actual) 
    {
        this.actual = actual;
    }
    
    public void llenarStack(String exp)
    {
        for (int k=0;k<exp.length();k++)
            {
                Character c3 = exp.charAt(k);
                st.push(c3);
            }
    }
    public String preOrder(Nodo d)
    {
        String res = "";
        if (d != null)
        {
            res += d.getCarac();
            res += preOrder(d.getLeft());
            res += preOrder(d.getRight());
            return res;
        }
        else
            return res;
    }
    
    public String postOrder(Nodo d)
    {
        String res = "";
        if (d != null)
        {            
            res += postOrder(d.getLeft());
            res += postOrder(d.getRight());
            res += d.getCarac();
            return res;
        }
        else
            return res;
    }
    
    public String inOrder(Nodo d)
    {
        String res = "";
        if (d != null)
        {            
            res += inOrder(d.getLeft());
            res += d.getCarac();
            res += inOrder(d.getRight());
            return res;
        }
        else
            return res;
    }
    
    public void armar(tree set)
    {        
        Character c = st.pop();
        
        if (binarios.contains(c))
            raiz= new Nodo(c, null);
        else
        {
            raiz = new Nodo(c, null);
            raiz.setBin(false);
            Nodo n = new Nodo('$', actual);
            raiz.setRight(n);
        }
        
        actual=raiz;
        
        while (st.size() > 0)
        {
            Character c1 = st.pop();
            
            if (binarios.contains(c1) && operadores.contains(c1))
            {   
                if (actual.getRight() == null)
                {
                    Nodo n = new Nodo(c1, actual);
                    actual.setRight(n);
                    actual = actual.getRight();
                }
                else
                    if (actual.getLeft() == null)
                    {
                        Nodo n = new Nodo(c1, actual);
                        actual.setLeft(n);
                        actual = actual.getLeft();
                    }
            }
            else
                if (unarios.contains(c1) && operadores.contains(c1))
                {
                    if (actual.getRight() == null)
                    {
                        Nodo n = new Nodo(c1, actual);
                    
                        actual.setRight(n);
                    
                        actual = actual.getRight();
                    
                        Nodo der = new Nodo('$', actual);
                    
                        actual.setRight(der);
                    }
                    else
                        if (actual.getLeft() == null)
                        {
                            Nodo n = new Nodo(c1, actual);
                            actual.setLeft(n);
                            actual = actual.getLeft();
                        
                            Nodo izq = new Nodo('$', actual);
                    
                            actual.setLeft(izq);
                            
                        } 
                }
                else
                    {
                        boolean flag = true;
                        if (actual.getRight() == null)
                        {
                            Nodo n = new Nodo(c1,actual);
                            actual.setRight(n);
                        }
                        else 
                            if (actual.getLeft() == null)
                            {
                                Nodo n = new Nodo(c1,actual);
                                actual.setLeft(n);    
                            }
                            else 
                            {
                        
                                actual = actual.getPrevious();
                                while (actual.getLeft()!=null)
                                {
                                    actual=actual.getPrevious();
                                }
                
                                Nodo n = new Nodo(c1,actual);
                                    
                                actual.setLeft(n);
                                
                                flag = false;
                            }
                        if (flag)
                        {
                                while (actual.getLeft()!=null && st.size()>0)
                                {
                                    actual=actual.getPrevious();
                                }
                        }
                    }
        }        
    }
    
    public String getHojas (Nodo n) {
        if (n != null) {
            if (n.getLeft() == null && n.getRight() == null) {
                hojas += n.getCarac();
            }
            getHojas(n.getLeft());
            getHojas(n.getRight());
        }
        return hojas;
    }
    
}
