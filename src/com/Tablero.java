package com;

import java.util.ArrayList;
import java.util.Collections;

public class Tablero {

// PROPIEDADES DE TABLERO

    private Casilla[][] casillas;               // Array bidimensional de casillas que representa nuestro tablero
    private ArrayList<Casilla> frontera;        // Lista de nodos abiertos
    private ArrayList<Casilla> list_cerrados;   // Lista de nodes ya visitados

    private Casilla posInicio;       // Casilla de inicio
    private Casilla posObjetivo;     // Casilla de objetivo

    private int width;       // Número de casillas horizontales
    private int height;      // Número de casillas verticales

    private boolean solucionEncontrada = false;     // Control de la solucion encontrada


    private Casilla casillaActual;

    enum Busqueda {AVARA, DIJKSTRA, AESTRELLA}      // Tipos de busqueda

    private Busqueda tipoBusqueda;      // Variable que contiene el tipo de busqueda que se va a emplear

// CONSTRUCTOR DE TABLERO

    public Tablero(int height, int width) {
        this.width = width;                     // Inicializamos el ancho del tablero
        this.height = height;                   // Inicializamos el alto del tablero

        casillas = new Casilla[height][width];  // Inicializamos el array de casillas
        frontera = new ArrayList<>();           // Inicializamos la frontera
        list_cerrados = new ArrayList<>();      // Incializamos la lista de cerrados

        generarEstados();                       // Generamos el tablero inicial

        //setInicio(casillas[height/2][0]);                       // Fijamos la posicion de inicio por defecto
        setInicio(casillas[0][0]);                       // Fijamos la posicion de inicio por defecto
        //setObjetivo(casillas[height - 1/2][width - 1]);   // Fijamos la posicion de objetivo por defecto
        setObjetivo(casillas[height - 1][width - 1]);   // Fijamos la posicion de objetivo por defecto

    }

// GETTERS Y SETTERS

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public Casilla[][] getCasillas() {return casillas;}
    public Casilla getPosObjetivo() {return posObjetivo;}
    public Casilla getPosInicio() {return posInicio;}
    public void setTipoBusqueda(Busqueda busqueda){tipoBusqueda = busqueda;}
    public Busqueda getTipoBusqueda() {return tipoBusqueda;}
    public boolean getSolucionEncontrada() {return solucionEncontrada;}

    public Casilla getCasillaActual() {
        return casillaActual;
    }

    public void setCasillaActual(Casilla casillaActual) {
        this.casillaActual = casillaActual;
    }

// METODOS DE TABLERO

    /* Con la funcion generarEstados(), se rellenara el tablero con casillas que contienen
     los valores iniciales correspondientes.*/

    private void generarEstados() {
        // Recorrremos el tablero
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                // Estos serán los valores por defecto de cada casilla del tablero inicial
                casillas[i][j] = new Casilla(i, j, 0, 0, Casilla.Tipo.CAMPO);

    }

    /* Con las funciones setInicio y setObjetivo estableceremos las
     casillas por defecto de Inicio y Objetivo al crear el tablero inicial */

    public void setInicio(Casilla casilla) {

        if (casilla.getTipo().equals(Casilla.Tipo.CAMPO)) {

            //TODO: ENTENDER POR QUE COJONES HACE ESTO

            if (posInicio != null) posInicio.setTipo(Casilla.Tipo.CAMPO);

            casilla.setTipo(Casilla.Tipo.ENTRADA);  // Fijamos el tipo de casilla como ENTRADA
            posInicio = casilla; // Fijamos esta casilla como inicio de la busqueda
        }
    }

    public void setObjetivo(Casilla casilla) {

        if (casilla.getTipo().equals(Casilla.Tipo.CAMPO)) {

            //TODO: ENTENDER POR QUE COJONES HACE ESTO

            if (posObjetivo != null) posObjetivo.setTipo(Casilla.Tipo.CAMPO);

            casilla.setTipo(Casilla.Tipo.SALIDA);   // Fijamos el tipo de casilla a SALIDA
            posObjetivo = casilla;    // Fijamos la posicion de objetivo

            // Una vez que tenemos la casilla de objetivo ya podemos calcular
            // H(n) de todas nuestras casillas.

            calculaHntablero();
        }
    }

    /* La funcion calculaHntablero sirve para asignar la heuristica a cada casilla del
     tablero incial */

    private void calculaHntablero() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                casillas[i][j].setHn(calculaH(casillas[i][j], posObjetivo));   //Calculamos la h(n) de la casilla
                //TODO: QUITAR COMPROBACION Y CAMBIAR NOMBRE A FUNCION
                System.out.println("Casilla [" + i + "," + j + "] -> H(n): " + casillas[i][j].getHn());
            }
    }

    /* En caso de que el usuario decida limpiar el tablero, debemos iniciarlo de cero,
        Para ello se utiliza la funcion reniciarTablero()  */

    public void reniciarTablero() {

        frontera.clear();       // Vaciamos la frontera
        list_cerrados.clear();  // Vaciamos la lista de cerrados

        // Volvemos a fijar los valores por defecto en las casillas de nuestro tablero
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (casillas[i][j].getTipo().equals(Casilla.Tipo.CAMPO) || casillas[i][j].getTipo().equals(Casilla.Tipo.SOLUCION))
                    casillas[i][j] = new Casilla(i, j, 0, 0, Casilla.Tipo.CAMPO);
            }
        }
        // Finalmente volvemos a calcular las heuristicas de nuestras casillas respecto al objetivo
        calculaHntablero();

    }

// METODOS PARA LAS BUSQUEDAS

    public void busquedaAEstrella() {
        tipoBusqueda = Busqueda.AESTRELLA;
        iniciarBusqueda();
    }

    public void busquedaDijkstra() {
        tipoBusqueda = Busqueda.DIJKSTRA;
        iniciarBusqueda();    }

    public void busquedaAvara() {
        tipoBusqueda = Busqueda.AVARA;
        iniciarBusqueda();
    }

    // Una vez que ya hemos definido la busqueda a realizar, comienza el juego

    private void iniciarBusqueda() {

        // Se añade el nodo inicial a la frontera
        frontera.add(posInicio);

        // Hacemos cerrado vacio
        list_cerrados.clear();

        //BUCLE (Repetir el proceso mientras ABIERTO ≠ vacío)
        while (!frontera.isEmpty()) {

            System.out.println("Abierto (" + Integer.toString(frontera.size()) + "): " + frontera);
            System.out.println("Cerrado (" + Integer.toString(list_cerrados.size()) + "): " + list_cerrados);

            // 1. NODO-ACTUAL = EXTRAE-PRIMERO(ABIERTO)

            Casilla casillaActual = frontera.remove(0);

            // 2. Poner NODO-ACTUAL en *CERRADO*

            list_cerrados.add(casillaActual);

            /* 3. Si ES-ESTADO-FINAL(ESTADO(NODO-ACTUAL))
            devolver CAMINO(NODO-ACTUAL) */

            System.out.println();
            System.out.println("Exploramos " +
                    casillaActual + " H(n): " +
                    casillaActual.getHn() + " G(n): " +
                    casillaActual.getGn() + " Padre: " +
                    casillaActual.getPadre());

            if (esObjetivo(casillaActual)) {
                System.out.println("SOLUCION ENCONTRADA!");
                System.out.println();
                System.out.println("Camino encontrado: ");
                mostrarCamino(casillaActual);
                solucionEncontrada = true;
                return;
            }

            /* 4. Si no, FUNCION SUCESORES(NODO-ACTUAL) */
            else {
                explorarSucesores(casillaActual);
            }
        }
    }

    /* Funcion que utilizamos para saber si nuestra casilla actual es el objetivo,
     y por tanto haber encontrado la solucion.*/

    private boolean esObjetivo(Casilla casillaActual) {
        return casillaActual.getHn() == 0;
    }

    public void mostrarCamino(Casilla posicion) {

        if (posicion.getPadre() != null)
            mostrarCamino(posicion.getPadre());

        System.out.print(posicion);
        if (posicion.getTipo().equals(Casilla.Tipo.CAMPO) ||
                posicion.getTipo().equals(Casilla.Tipo.RIO) ||
                    posicion.getTipo().equals(Casilla.Tipo.BOSQUE) ||
                            posicion.getTipo().equals(Casilla.Tipo.MONTAÑA)
                )
            posicion.setTipo(Casilla.Tipo.SOLUCION);
    }

    // Se llama a la exploración de sucesores en funcion de la busqueda en la que estemos
    private void explorarSucesores(Casilla casillaActual) {

        switch (this.tipoBusqueda) {
            case AVARA:
                explorarSucesoresAvara(casillaActual);
                break;

            case DIJKSTRA:
                explorarSucesoresDijkstra(casillaActual);
                break;

            case AESTRELLA:
                explorarSucesoresAStar(casillaActual);
                break;

            default:
                break;
        }
    }

    // Exploracion de sucesores

    /*Debemos crear una funcion que nos devuelva los sucesores de la casilla actual,
    teniendo en cuenta los limites y  de nuestro tablero generado*/

    private ArrayList<Casilla> obtenerSucesores(Casilla casillaActual) {

        ArrayList<Casilla> sucesores = new ArrayList<>();
        Casilla sucesor;

        // Sucesor superior derecha

        //Primero comprobamos que la casilla actual no este en la ultima fila del tablero
        if (casillaActual.getX() > 0 && casillaActual.getY() < width - 1) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() - 1][casillaActual.getY() + 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() - 1,   // Posicion X
                        casillaActual.getY() + 1,       // Posicion Y
                        calculaH2(casillaActual.getX() - 1, casillaActual.getY() + 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() - 1][casillaActual.getY() + 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() - 1][casillaActual.getY() + 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor supd Tipo:" + sucesor.getTipo());
                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }

        // Sucesor derecha

        //Primero comprobamos que la casilla actual no este en la ultima columna del tablero
        if (casillaActual.getY() < width - 1) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX()][casillaActual.getY() + 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX(),    // Posicion X
                        casillaActual.getY() + 1, // Posicion Y
                        calculaH2(casillaActual.getX(), casillaActual.getY() + 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX()][casillaActual.getY() + 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX()][casillaActual.getY() + 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor dcha Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }


        // Sucesor inferior derecha

        //Primero comprobamos que la casilla actual no este en la primera fila del tablero
        if (casillaActual.getX() < height - 1 && casillaActual.getY() < width - 1) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() + 1][casillaActual.getY() + 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() + 1,   // Posicion X
                        casillaActual.getY() + 1,       // Posicion Y
                        calculaH2(casillaActual.getX() + 1, casillaActual.getY() + 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() + 1][casillaActual.getY() + 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() + 1][casillaActual.getY() + 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor infd Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }

        // Sucesor inferior

        //Primero comprobamos que la casilla actual no este en la primera fila del tablero
        if (casillaActual.getX() < height - 1) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() + 1][casillaActual.getY()].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() + 1,   // Posicion X
                        casillaActual.getY(),       // Posicion Y
                        calculaH2(casillaActual.getX() + 1, casillaActual.getY(), posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() + 1][casillaActual.getY()].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() + 1][casillaActual.getY()].getTipo()); // Tipo
                System.out.println("Nuevo sucesor inf Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }

        // Sucesor inferior izquierda

        //Primero comprobamos que la casilla actual no este en la primera fila del tablero
        if (casillaActual.getX() < height - 1 && casillaActual.getY() > 0) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() + 1][casillaActual.getY() - 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() + 1,   // Posicion X
                        casillaActual.getY() - 1,       // Posicion Y
                        calculaH2(casillaActual.getX() + 1, casillaActual.getY() - 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() + 1][casillaActual.getY() - 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() + 1][casillaActual.getY() - 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor infi Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }
        // IZQUIERDA
        //Primero comprobamos que la casilla actual no este en la primera fila del tablero
        if (casillaActual.getY() > 0) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX()][casillaActual.getY() - 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX(),    // Posicion X
                        casillaActual.getY() - 1, // Posicion Y
                        calculaH2(casillaActual.getX(), casillaActual.getY() - 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX()][casillaActual.getY() - 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX()][casillaActual.getY() - 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor izq Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }


        // Sucesor superior izquierda

        //Primero comprobamos que la casilla actual no este en la ultima fila del tablero
        if (casillaActual.getX() > 0 && casillaActual.getY() > 0) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() - 1][casillaActual.getY() - 1].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() - 1,   // Posicion X
                        casillaActual.getY() - 1,       // Posicion Y
                        calculaH2(casillaActual.getX() - 1, casillaActual.getY() - 1, posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() - 1][casillaActual.getY() - 1].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() - 1][casillaActual.getY() - 1].getTipo()); // Tipo
                System.out.println("Nuevo sucesor supi Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }

        // Sucesor superior

        //Primero comprobamos que la casilla actual no este en la ultima fila del tablero
        if (casillaActual.getX() > 0) {
            // En segundo lugar, nos aseguramos que el candidato a sucesor no sea de tipo montaña (Obstaculo)
            if (casillas[casillaActual.getX() - 1][casillaActual.getY()].getTipo() != Casilla.Tipo.MONTAÑA) {
                // Una vez hechas estas dos comprobaciones, generamos el sucesor superior
                sucesor = new Casilla(
                        casillaActual.getX() - 1,   // Posicion X
                        casillaActual.getY(),       // Posicion Y
                        calculaH2(casillaActual.getX() - 1, casillaActual.getY(), posObjetivo), // H(n)
                        casillaActual.getGn() + casillas[casillaActual.getX() - 1][casillaActual.getY()].returnValorTipo(), //G(n)
                        casillas[casillaActual.getX() - 1][casillaActual.getY()].getTipo()); // Tipo
                System.out.println("Nuevo sucesor sup Tipo:" + sucesor.getTipo());

                sucesor.setPadre(casillaActual);    // Definimos la casilla actual como el padre
                sucesores.add(sucesor);             // Añadimos el nuevo sucesor generado a la lista de sucesores de la casilla actual
            }
        }

        return sucesores;
    }

    // Exploracion de sucesores en A*

    private void explorarSucesoresAStar(Casilla casillaActual) {

        // Obtener los sucesores de la casilla actual
        ArrayList<Casilla> sucesores = obtenerSucesores(casillaActual);


        // Para cada sucesor
        for (Casilla sucesor : sucesores) {

            System.out.println("Sucesor generado: " + sucesor);
            int index;

            // Si SUCESOR ya está en *CERRADO*
            if ((index = list_cerrados.indexOf(sucesor)) != -1) {

                Casilla posicionAnterior = list_cerrados.get(index);

                if (sucesor.getFn() < posicionAnterior.getFn()) {
                    casillas[sucesor.getX()][sucesor.getY()] = sucesor;
                    frontera.add(sucesor);
                    list_cerrados.remove(index);
                }
            }

            // Si SUCESOR ya está en *ABIERTO*
            else if ((index = frontera.indexOf(sucesor)) != -1) {

                Casilla posicionAnterior = frontera.get(index);
                if (sucesor.getFn() < posicionAnterior.getFn()) {
                    casillas[sucesor.getX()][sucesor.getY()] = sucesor;
                    // Borrar el nodo en *ABIERTO*
                    frontera.remove(index);
                    // Insertar ordenadamente SUCESOR en *ABIERTO*
                    frontera.add(sucesor);
                }
            }

            else {
                casillas[sucesor.getX()][sucesor.getY()] = sucesor;
                frontera.add(sucesor);
            }

            // Ordenar abiertos en funcion de F(n). Esto podria hacerse mas eficientemente colocando el nuevo estado en su posicion correcta y no al final.
            Collections.sort(frontera, Casilla.ComparadorFn);

        }
    }

    // Exploración de sucesores en Dijkstra

    private void explorarSucesoresDijkstra(Casilla posicionActual) {

        // Buscamos los sucesores de la posicion actual. Comprobando los límites.
        ArrayList<Casilla> sucesores = obtenerSucesores(posicionActual);

        // Para cada sucesor
        for (Casilla sucesor : sucesores) {

            System.out.println("Sucesor generado: " + sucesor);
            int index;

            // Si el sucesor no esta en abierto ni en cerrado
            if (list_cerrados.indexOf(sucesor) == -1 && frontera.indexOf(sucesor) == -1) {

                // Añadir el sucesor a abierto
                frontera.add(sucesor);
                casillas[sucesor.getX()][sucesor.getY()] = sucesor;

                // Ordenar abiertos en funcion de G(n)
                Collections.sort(frontera, Casilla.ComparadorGn);
            }
        }
    }

    // Exploración de sucesores en Avara

    private void explorarSucesoresAvara(Casilla posicionActual) {

        // Buscamos los sucesores de la posicion actual. Comprobando los límites
        ArrayList<Casilla> sucesores = obtenerSucesores(posicionActual);

        // Para cada sucesor
        for (Casilla sucesor : sucesores) {

            System.out.println("Sucesor generado: " + sucesor);
            int index;

            // Si el sucesor no esta en abierto ni en cerrado
            if (list_cerrados.indexOf(sucesor) == -1 && frontera.indexOf(sucesor) == -1) {

                // Añadir el sucesor a abierto
                frontera.add(sucesor);
                casillas[sucesor.getX()][sucesor.getY()] = sucesor;

                // Ordenar abiertos en funcion de H(n)
                Collections.sort(frontera, Casilla.ComparadorHn);
            }
        }
    }

    // Metodos para calcular H(n)

    public int calculaH(Casilla actual, Casilla fin){

        // Primero tengo que calcular el |Xini - Xfin|
        int valorx = actual.getX() - fin.getX();
        int valory = actual.getY() - fin.getY();

        // Tengo que pasar los valores a valor real
        valorx = Math.abs(valorx);
        valory = Math.abs(valory);

        // Calcular cual es el mayor y esa es mi h
        if (valorx > valory){return valorx;}
        else{return valory;}

    }

    public int calculaH2(int actualX, int actualY, Casilla fin){

        // Primero tengo que calcular el |Xini - Xfin|
        int valorx = actualX - fin.getX();
        int valory = actualY - fin.getY();

        // Tengo que pasar los valores a valor real
        valorx = Math.abs(valorx);
        valory = Math.abs(valory);

        // Calcular cual es el mayor y esa es mi h
        if (valorx > valory){return valorx;}
        else{return valory;}

    }


    // Funcion para cambiar el tipo de obstaculos

    public void cambiaTipo(Casilla casilla) {
        if (!casilla.equals(posInicio) && !casilla.equals(posObjetivo)) {
            if (casilla.getTipo().equals(Casilla.Tipo.CAMPO))
                casilla.setTipo(Casilla.Tipo.BOSQUE);
            else if (casilla.getTipo().equals(Casilla.Tipo.BOSQUE))
                casilla.setTipo(Casilla.Tipo.RIO);
            else if (casilla.getTipo().equals(Casilla.Tipo.RIO))
                casilla.setTipo(Casilla.Tipo.MONTAÑA);
            else {
                casilla.setTipo(Casilla.Tipo.CAMPO);
            }

        }
    }


// Inciar busqueda explorada

public Casilla iniciarBusquedaManual(){
    System.out.println("Tablero: "+ this.getTipoBusqueda() );
    // Se añade el nodo inicial a la frontera
    frontera.add(posInicio);
    posInicio.setEstado(Casilla.Estado.SELECCIONADO);

    // Hacemos cerrado vacio
    list_cerrados.clear();

    System.out.println("Abierto (" + Integer.toString(frontera.size()) + "): " + frontera);
    System.out.println("Cerrado (" + Integer.toString(list_cerrados.size()) + "): " + list_cerrados);

    // 1. NODO-ACTUAL = EXTRAE-PRIMERO(ABIERTO)

    Casilla casillaActual = frontera.remove(0);

    // 2. Poner NODO-ACTUAL en *CERRADO*

    list_cerrados.add(casillaActual);
    casillaActual.setEstado(Casilla.Estado.VISITADO);
            /* 3. Si ES-ESTADO-FINAL(ESTADO(NODO-ACTUAL))
            devolver CAMINO(NODO-ACTUAL) */

    System.out.println();
    System.out.println("Exploramos " +
            casillaActual + " H(n): " +
            casillaActual.getHn() + " G(n): " +
            casillaActual.getGn() + " Padre: " +
            casillaActual.getPadre());

    if (esObjetivo(casillaActual)) {
        System.out.println("SOLUCION ENCONTRADA!");
        System.out.println();
        System.out.println("Camino encontrado: ");
        mostrarCamino(casillaActual);
        solucionEncontrada = true;
        return casillaActual;
    }

    return casillaActual;
}

public Casilla seleccionarActualManual() {
    if (!frontera.isEmpty()) {

        System.out.println("Abierto (" + Integer.toString(frontera.size()) + "): " + frontera);
        System.out.println("Cerrado (" + Integer.toString(list_cerrados.size()) + "): " + list_cerrados);

        // 1. NODO-ACTUAL = EXTRAE-PRIMERO(ABIERTO)

        Casilla casillaActual = frontera.remove(0);

        // 2. Poner NODO-ACTUAL en *CERRADO*

        list_cerrados.add(casillaActual);
        casillaActual.setEstado(Casilla.Estado.VISITADO);
            /* 3. Si ES-ESTADO-FINAL(ESTADO(NODO-ACTUAL))
            devolver CAMINO(NODO-ACTUAL) */

        System.out.println();
        System.out.println("Exploramos " +
                casillaActual + " H(n): " +
                casillaActual.getHn() + " G(n): " +
                casillaActual.getGn() + " Padre: " +
                casillaActual.getPadre());

        if (esObjetivo(casillaActual)) {
            System.out.println("SOLUCION ENCONTRADA!");
            System.out.println();
            System.out.println("Camino encontrado: ");
            mostrarCamino(casillaActual);
            solucionEncontrada = true;
            return casillaActual;
        }

        return casillaActual;
    }
    return casillaActual;
}

public void generarSucesoresManual(Casilla casillaActual){

    explorarSucesores(casillaActual);

}




}