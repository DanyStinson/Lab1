package com;

import java.util.Comparator;

public class Casilla {

    // PROPIEDADES

    private String id;  // ID de la casilla
    private int x;      // Posicion X en el tablero
    private int y;      // Posicion Y en el tablero

    // Se genera una enumeracion donde se definen los distintos tipos de casilla

    private Tipo tipo;
    enum Tipo { ENTRADA, SALIDA, MONTAÑA, CAMPO, SOLUCION, RIO, BOSQUE}

    private Estado estado;
    enum Estado { SELECCIONADO, SUCESOR, VISITADO, NOEXPLORADO}

    // Para el cálulo de heurisiticas, costes y funciones de evaluaciones
    // necesitaremos saber la casilla del padre de ese nodo.

    private Casilla padre;

    private int hn;     // Heuristica h(n)
    private double gn;     // Coste g(n)
    private double fn;     // Funcion de evaluacion f(n)

    // CONSTRUCTOR DE CASILLA

    public Casilla(int x, int y, int hn, double gn, Tipo tipo) {
        this.x = x;
        this.y = y;
        this.gn = gn;
        this.hn = hn;
        this.tipo = tipo;
        this.fn = gn + hn;  // F(n) siempre sera G(n) + H(n)
        this.id = "[" + Integer.toString(x) + ", " + Integer.toString(y) + "]";
        this.estado = Estado.NOEXPLORADO;
    }

    // GETTERS Y SETTERS

    public String getId() { return id;}
    public int getX() { return x;}
    public int getY() { return y;}
    public Tipo getTipo() {return tipo;}
    public int getHn() { return hn;}
    public double getGn() { return gn;}
    public double getFn() {return fn;}
    public Casilla getPadre() {return padre;}
    public Estado getEstado() {return estado;}


    public void setId(String id) {this.id = id;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setTipo( Tipo tipo) {this.tipo = tipo;}
    public void setHn(int hn) {this.hn = hn;}
    public void setGn(double gn) {this.gn = gn;}
    public void setFn(double fn) {this.fn = fn;}
    public void setPadre(Casilla padre) {this.padre = padre;}
    public void setEstado(Estado estado) {this.estado = estado;}

    // METODOS DE CASILLA

    /*public void calculaFGH(Casilla fin){

        // Primero tengo que calcular el |Xini - Xfin|
        int valorx = this.getX() - fin.getX();
        int valory = this.getY() - fin.getY();

        // Tengo que pasar los valores a valor real
        valorx = Math.abs(valorx);
        valory = Math.abs(valory);

        // Calcular cual es el mayor y esa es mi h
        if (valorx > valory){this.setHn(valorx);}
        else{this.setHn(valory);}

        // Ahora ya tengo mi valor H, ahora tengo que calcular el G
        // actual.setGn(padre.getGn()+actual.getTipo());

    }*/

    @Override
    public boolean equals(Object obj) {
        Casilla posicion = (Casilla) obj;
        return posicion.getX() == this.getX() && posicion.getY() == this.getY();
    }

    @Override
    public String toString() { return getId();}


    public static Comparator<Casilla> ComparadorGn = new Comparator<Casilla>() {

        public int compare(Casilla pos1, Casilla pos2) {
            if (pos2.getGn() > pos1.getGn())
                return -1;
            return 1;
        }
    };

    public static Comparator<Casilla> ComparadorHn = new Comparator<Casilla>() {

        public int compare(Casilla pos1, Casilla pos2) {
            if (pos2.getHn() > pos1.getHn())
                return -1;
            return 1;
        }
    };

    public static Comparator<Casilla> ComparadorFn = new Comparator<Casilla>() {

        public int compare(Casilla pos1, Casilla pos2) {
            if (pos2.getFn() > pos1.getFn())
                return -1;
            return 1;
        }
    };

    public double returnValorTipo (){
        if (tipo == Tipo.MONTAÑA){return 0;}
        else if (tipo == Tipo.BOSQUE){return 1/0.5;}
        else if (tipo == Tipo.CAMPO){return 1;}
        else if (tipo == Tipo.RIO){return 1/0.25;}
        else return 1;
        //else if (tipo == Tipo.ENTRADA){}
        //else if (tipo == Tipo.SALIDA){}
        //else if (tipo == Tipo.SOLUCION){}
    }

}
