/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Estado<T> {
    private T num;
    private ArrayList<Transicion> enlaces = new ArrayList();
    private boolean inicial;
    private boolean fin;
    private int id;

    public Estado()
    {
        
    }
    public Estado(T num)
    {
        this.num = num;
        this.id = -1;
    }
    public Estado(T num, ArrayList<Transicion> enlaces) {
        this.num = num;
        this.enlaces = enlaces;
        this.id = -1;
    }

    public T getNum() {
        return num;
    }

    public void setNum(T num) {
        this.num = num;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public ArrayList<Transicion> getEnlaces() {
        return enlaces;
    }

    public void setEnlace(Transicion enlace) {
        this.enlaces.add(enlace);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        if (id == -1)
            return "{"+this.num+"}";
        else
            return this.id+" = {"+this.num+"}";
    }
    
    
}
