/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Arrays;
import java.util.ArrayList;
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
    private ArrayList<Nodo> nodos = new ArrayList();
    private Stack<Character> st = new Stack<Character>();
    private List<Character> unarios = Arrays.asList('*');
    private List<Character> binarios = Arrays.asList('|','.');
    private List<Character> operadores = Arrays.asList('|','.','*');
    private int cont;
    
    public void tree()
    {
        raiz = null;
        cont = 1;
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

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(Nodo node) {
        this.nodos.add(node);
    }
    
    public boolean isHoja (Nodo n) {
        if (n != null) {
            if (n.getLeft() == null && n.getRight() == null) {
                return true;
            }
            getHojas(n.getLeft());
            getHojas(n.getRight());
        }
        return false;
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
        {
            raiz= new Nodo(c, null);
            this.setNodos(raiz);
        }
        else
        {
            raiz = new Nodo(c, null);
            raiz.setBin(false);
            Nodo n = new Nodo('$', actual);
            raiz.setRight(n);
            this.setNodos(raiz);
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
                    this.setNodos(actual);
                }
                else
                    if (actual.getLeft() == null)
                    {
                        Nodo n = new Nodo(c1, actual);
                        actual.setLeft(n);
                        actual = actual.getLeft();
                        this.setNodos(actual);
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
                        
                        this.setNodos(actual);
                    
                        Nodo der = new Nodo('$', actual);
                    
                        actual.setRight(der);
                    }
                    else
                        if (actual.getLeft() == null)
                        {
                            Nodo n = new Nodo(c1, actual);
                            actual.setLeft(n);
                            actual = actual.getLeft();
                            this.setNodos(actual);
                        
                            Nodo der = new Nodo('$', actual);
                    
                            actual.setRight(der);
                        } 
                }
                else
                    {
                        boolean flag = true;
                        if (actual.getRight() == null)
                        {
                            Nodo n = new Nodo(c1,actual);
                            actual.setRight(n);
                            this.setNodos(n);
                        }
                        else 
                            if (actual.getLeft() == null)
                            {
                                Nodo n = new Nodo(c1,actual);
                                actual.setLeft(n);
                                this.setNodos(n);
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
                                this.setNodos(n);
                                
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
    
    public void ponerNum(Nodo n)
    { 
        if (n != null) {
            if (n.getLeft() == null && n.getRight() == null && n.getCarac() != '$') {
                n.setId(++cont);
            }
            ponerNum(n.getLeft());
            ponerNum(n.getRight());
        }
    }
}
