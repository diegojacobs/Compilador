/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs 13160
 */
public class Estado {
    private int num;
    private ArrayList<Transicion> enlaces = new ArrayList();
    private boolean inicial;
    private boolean fin;

    
    public Estado(int num)
    {
        this.num = num;
    }
    public Estado(int num, ArrayList<Transicion> enlaces) {
        this.num = num;
        this.enlaces = enlaces;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
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
    
    @Override
    public String toString()
    {
        return "{"+this.num+"}";
    }
    
    
}
