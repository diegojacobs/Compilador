package compilador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Diego Jacobs 13160
 */
public class Construccion_Directa {
    private tree arbol;
    private AutomataFD AFD;
    
    public Construccion_Directa(tree set)
    {
        this.arbol = set;
    }

    public tree getArbol() {
        return arbol;
    }

    public void setArbol(tree arbol) {
        this.arbol = arbol;
    }

    public AutomataFD getAFD() {
        return AFD;
    }

    public void setAFD(AutomataFD AFD) {
        this.AFD = AFD;
    }
    
    public void agregarHastag()
    {
        Nodo node = new Nodo('.',null);
        Nodo node2 = new Nodo('#',node);
        arbol.getRaiz().setPrevious(node);
        node.setLeft(arbol.getRaiz());
        node.setRight(node2);
        arbol.setRaiz(node);
    }
    
    
}
