package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI extends JPanel implements MouseListener {

    /**
     * Modoficadas
     */

    private Tablero tablero;

    // Modificaciones dany
    private Tablero tableroDijkstra;
    private Tablero tableroAvara;
    private Tablero tableroAstar;

    private String tipotablero = "normal";
    //


    private Ventana panelDeJuego;
    private Casilla.Tipo click;


    private Color campo = new Color(43, 168, 71);
    private Color bosque = new Color(82, 80, 58);
    private Color rio = new Color(104, 151, 187);
    private Color montana = new Color(194, 194, 194);
    private Color entrada = new Color(255, 0, 255);
    private Color salida = new Color(255, 0, 0);
    private Color solucion = new Color(251, 249, 196);

    public GUI() {

        this.setLayout(new BorderLayout()); // Declaramos el nuevo layout
        this.tablero = new Tablero(4, 4); // numero inicial de casillas
        this.tableroAstar = tablero;
        this.tableroDijkstra = tablero;
        this.tableroAvara = tablero;
        this.panelDeJuego = new Ventana(tablero); // instanciamos el nuevo tablero

        panelDeJuego.addMouseListener(this); // añadimos la recogida de datos por raton

        click = Casilla.Tipo.CAMPO; // primera y ultima posicion

        /*
        * Declaramos los paneles que usaremos para las distintas vistas del juego
        * */
        JPanel dimensiones = new JPanel();
        JPanel modoEjecucion = new JPanel();
        JPanel modoAlgoritmo = new JPanel();
        JPanel utilidades = new JPanel();
        JPanel botonera = new JPanel();

        /*
        * Definimos el tamaño que tendran los paneles en filas y columnas
        * */
        dimensiones.setLayout(new GridLayout(5, 1));
        modoEjecucion.setLayout(new GridLayout(5, 1));
        modoAlgoritmo.setLayout(new GridLayout(4, 1));
        utilidades.setLayout(new GridLayout(3, 1));
        botonera.setLayout(new GridLayout(4, 1));

        /*
        * Declaramos todos los textos que vamos a mostrar en la interfaz
        * */
        JLabel labelDimensiones = new JLabel("Dimensiones del Mapa");
        JLabel labelAlgoritmo = new JLabel("Algorítmo de Búsqueda");
        JLabel labelEjecucion = new JLabel("Modo de Ejecución");
        JLabel labelUtilidades = new JLabel("Utilidades");
        JLabel labelAlto = new JLabel("Alto");
        JLabel labelAncho = new JLabel("Ancho");

        //JLabel labeltablero = new JLabel("Normal");
        /*
        * Declaramos las areas de introduccion del tamaño del tablero
        * */
        TextField textFieldAlto = new TextField();
        TextField textFieldAncho = new TextField();

        /*
        * Añadimos todos los componentes del area de dimensiones.
        * */
        dimensiones.add(labelDimensiones);
        dimensiones.add(labelAlto);
        dimensiones.add(textFieldAlto);
        dimensiones.add(labelAncho);
        dimensiones.add(textFieldAncho);
        //dimensiones.add(labeltablero);


        /*
        * Definimos los valores que se mostraran por defecto
        * */
        textFieldAlto.setText(Integer.toString(tablero.getHeight()));
        textFieldAncho.setText(Integer.toString(tablero.getWidth()));

        /*
        * Declaramos todos los botones necesarios para la ejecucion.
        * */

        Button botonBusquedaEstrella = new Button("Buscar A*");
        Button botonBusquedaDijkstra = new Button("Buscar Dijkstra");
        Button botonBusquedaVoraz = new Button("Buscar Avara");
        Button botonCrearTablero = new Button("Generar Tablero Nuevo");
        Button botonResetearTablero = new Button("Limpiar Mapa");
        Button botonAutomatico = new Button("Modo Automático");
        Button botonManual = new Button("Modo Manual");
        Button botonGenerarSucesores = new Button("Generar Sucesores");
        Button botonSiguiente = new Button("Siguiente");

        /*
        * Inicializamos los valores iniciales de los botones
        * */

        botonBusquedaEstrella.setEnabled(false);
        botonBusquedaDijkstra.setEnabled(false);
        botonBusquedaVoraz.setEnabled(false);
        botonCrearTablero.setForeground(Color.blue);
        botonResetearTablero.setEnabled(false);
        botonAutomatico.setEnabled(false);
        botonManual.setEnabled(false);
        botonGenerarSucesores.setEnabled(false);
        botonSiguiente.setEnabled(false);

        /*
        * Añadimos los elementos pertenecientes a los distintos modos de ejecucion
        * */
        modoEjecucion.add(labelEjecucion);
        modoEjecucion.add(botonAutomatico);
        modoEjecucion.add(botonManual);
        modoEjecucion.add(botonGenerarSucesores);
        modoEjecucion.add(botonSiguiente);

        /*
        * Añadimos los elementos pertenecientes a los distintos modos de algoritmos
        * */

        modoAlgoritmo.add(labelAlgoritmo);
        modoAlgoritmo.add(botonBusquedaEstrella);
        modoAlgoritmo.add(botonBusquedaDijkstra);
        modoAlgoritmo.add(botonBusquedaVoraz);

        /*
        * Añadimos los elementos pertenecientes a los distintos modos de utilidades
        * */
        utilidades.add(labelUtilidades);
        utilidades.add(botonCrearTablero);
        utilidades.add(botonResetearTablero);

        /*
        * Añadimos todos los elementos a la botonera
        * */
        botonera.add(dimensiones);
        botonera.add(utilidades);
        botonera.add(modoEjecucion);
        botonera.add(modoAlgoritmo);

        /*
        * Establecemos la posicion del area de juego y de la botonera
        * */
        this.add(panelDeJuego, BorderLayout.CENTER); // centramos el panel de juego
        this.add(botonera, BorderLayout.EAST); // Colocamos la botonera a la derecha de la pantalla de juego

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonBusquedaEstrella.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (botonGenerarSucesores.isEnabled() || botonSiguiente.isEnabled()) {
                    JOptionPane.showMessageDialog(null, "Pulse Botón de Generar Sucesores para empezar el algorítmo");

                    tipotablero = "astar";
                    panelDeJuego.setMalla("astar");
                    panelDeJuego.repaint();

                } else {

                    // Se utilizaria metodo automatico
                    if (tableroAstar.getSolucionEncontrada())
                        tableroAstar.reniciarTablero();
                    tipotablero = "astar";
                    tableroAstar.busquedaAEstrella();
                    panelDeJuego.setMalla("astar");
                    panelDeJuego.repaint();
                }
                //labeltablero.setText("estrella");

                botonBusquedaEstrella.setEnabled(true);
                botonBusquedaEstrella.setForeground(Color.blue);

                botonBusquedaDijkstra.setEnabled(true);
                botonBusquedaDijkstra.setForeground(Color.black);

                botonBusquedaVoraz.setEnabled(true);
                botonBusquedaVoraz.setForeground(Color.black);

            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonBusquedaDijkstra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (botonGenerarSucesores.isEnabled() || botonSiguiente.isEnabled()) {
                    JOptionPane.showMessageDialog(null, "Pulse Boton de Generar Sucesores para empezar el algoritmo");

                    tipotablero = "dijkstra";
                    panelDeJuego.setMalla("dijkstra");
                    panelDeJuego.repaint();
                } else {

                    // Pasariamos al metodo automatico
                    if (tableroDijkstra.getSolucionEncontrada())
                        tableroDijkstra.reniciarTablero();
                    tableroDijkstra.busquedaDijkstra();
                    tipotablero = "dijkstra";
                    panelDeJuego.setMalla("dijkstra");
                    panelDeJuego.repaint();
                }

                //labeltablero.setText("dijkstra");

                botonBusquedaEstrella.setEnabled(true);
                botonBusquedaEstrella.setForeground(Color.black);

                botonBusquedaDijkstra.setEnabled(true);
                botonBusquedaDijkstra.setForeground(Color.blue);

                botonBusquedaVoraz.setEnabled(true);
                botonBusquedaVoraz.setForeground(Color.black);

            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonBusquedaVoraz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (botonGenerarSucesores.isEnabled() || botonSiguiente.isEnabled()) {
                    JOptionPane.showMessageDialog(null, "Pulse Boton de Generar Sucesores para empezar el algoritmo");

                    tipotablero = "avara";
                    panelDeJuego.setMalla("avara");
                    panelDeJuego.repaint();
                } else {
                    if (tableroAvara.getSolucionEncontrada())
                        tableroAvara.reniciarTablero();
                    tipotablero = "avara";
                    tableroAvara.busquedaAvara();
                    panelDeJuego.setMalla("avara");
                    panelDeJuego.repaint();

                }
                //labeltablero.setText("avara");
                botonBusquedaEstrella.setEnabled(true);
                botonBusquedaEstrella.setForeground(Color.black);

                botonBusquedaDijkstra.setEnabled(true);
                botonBusquedaDijkstra.setForeground(Color.black);

                botonBusquedaVoraz.setEnabled(true);
                botonBusquedaVoraz.setForeground(Color.blue);
            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonCrearTablero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int alto = Integer.parseInt(textFieldAlto.getText());
                int ancho = Integer.parseInt(textFieldAncho.getText());
                if (alto > 10 || ancho > 10) {
                    JOptionPane.showMessageDialog(null, "El tamaño máximo de tablero es de 10X10");

                    botonCrearTablero.setEnabled(true);
                    botonCrearTablero.setForeground(Color.blue);

                    botonResetearTablero.setEnabled(false);
                    botonResetearTablero.setForeground(Color.gray);

                    botonAutomatico.setEnabled(false);
                    botonAutomatico.setForeground(Color.gray);

                    botonManual.setEnabled(false);
                    botonManual.setForeground(Color.gray);

                    botonGenerarSucesores.setEnabled(false);
                    botonGenerarSucesores.setForeground(Color.gray);

                    botonBusquedaEstrella.setEnabled(false);
                    botonBusquedaEstrella.setForeground(Color.gray);

                    botonBusquedaDijkstra.setEnabled(false);
                    botonBusquedaDijkstra.setForeground(Color.gray);

                    botonBusquedaVoraz.setEnabled(false);
                    botonBusquedaVoraz.setForeground(Color.gray);

                } else {
                    tablero = new Tablero(alto, ancho);
                    panelDeJuego.setMalla("normal");
                    panelDeJuego.repaint();

                    botonCrearTablero.setEnabled(false);
                    botonCrearTablero.setForeground(Color.gray);

                    botonResetearTablero.setEnabled(true);
                    botonResetearTablero.setForeground(Color.blue);

                    botonAutomatico.setEnabled(true);
                    botonAutomatico.setForeground(Color.blue);

                    botonManual.setEnabled(true);
                    botonManual.setForeground(Color.blue);

                    botonGenerarSucesores.setEnabled(false);
                    botonGenerarSucesores.setForeground(Color.gray);

                    botonBusquedaEstrella.setEnabled(false);
                    botonBusquedaEstrella.setForeground(Color.gray);

                    botonBusquedaDijkstra.setEnabled(false);
                    botonBusquedaDijkstra.setForeground(Color.gray);

                    botonBusquedaVoraz.setEnabled(false);
                    botonBusquedaVoraz.setForeground(Color.gray);

                }
            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonResetearTablero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                tablero.reniciarTablero();

                // Modificaciones Dany//

                tableroDijkstra.reniciarTablero();
                tableroAvara.reniciarTablero();
                tableroAstar.reniciarTablero();
                System.out.println("He reniciado todos los tableros");
                // Fin Modificaciones Dany//
                panelDeJuego.setMalla("normal");
                panelDeJuego.repaint();

                botonCrearTablero.setForeground(Color.blue);
                botonCrearTablero.setEnabled(true);

                botonResetearTablero.setEnabled(false);
                botonResetearTablero.setForeground(Color.gray);

                botonAutomatico.setEnabled(false);
                botonAutomatico.setForeground(Color.gray);

                botonManual.setEnabled(false);
                botonManual.setForeground(Color.gray);

                botonGenerarSucesores.setEnabled(false);
                botonGenerarSucesores.setForeground(Color.gray);

                botonBusquedaEstrella.setEnabled(false);
                botonBusquedaEstrella.setForeground(Color.gray);

                botonBusquedaDijkstra.setEnabled(false);
                botonBusquedaDijkstra.setForeground(Color.gray);

                botonBusquedaVoraz.setEnabled(false);
                botonBusquedaVoraz.setForeground(Color.gray);
            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonAutomatico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCrearTablero.setEnabled(false);
                botonCrearTablero.setForeground(Color.gray);

                botonResetearTablero.setEnabled(true);
                botonResetearTablero.setForeground(Color.blue);

                botonAutomatico.setEnabled(true);
                botonAutomatico.setForeground(Color.blue);

                botonManual.setEnabled(false);
                botonManual.setForeground(Color.gray);

                botonGenerarSucesores.setEnabled(false);
                botonGenerarSucesores.setForeground(Color.gray);

                botonBusquedaEstrella.setEnabled(true);
                botonBusquedaEstrella.setForeground(Color.blue);

                botonBusquedaDijkstra.setEnabled(true);
                botonBusquedaDijkstra.setForeground(Color.blue);

                botonBusquedaVoraz.setEnabled(true);
                botonBusquedaVoraz.setForeground(Color.blue);

                //Modificacion Dany: Se propaga el tablero actual a los otros 3 //

                tableroDijkstra = tablero;
                tableroDijkstra.setTipoBusqueda(Tablero.Busqueda.DIJKSTRA);
                tableroAvara = tablero;
                tableroAvara.setTipoBusqueda(Tablero.Busqueda.AVARA);
                tableroAstar = tablero;
                tableroAstar.setTipoBusqueda(Tablero.Busqueda.AESTRELLA);

                panelDeJuego.setMalla("normal");
                panelDeJuego.repaint();
                System.out.println("He propagado a todos los tableros");

                //Fin Modificacion Dany //
                JOptionPane.showMessageDialog(null, "Seleccione el Algorítmo de Búsqueda a Emplear");
            }
        });

        /*
        * Metodo para establecer la accion que supondra el hacer click sobre el boton
        * */
        botonManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                botonCrearTablero.setEnabled(false);
                botonCrearTablero.setForeground(Color.gray);

                botonResetearTablero.setEnabled(true);
                botonResetearTablero.setForeground(Color.blue);

                botonAutomatico.setEnabled(false);
                botonAutomatico.setForeground(Color.gray);

                botonManual.setEnabled(false);
                botonManual.setForeground(Color.gray);

                botonGenerarSucesores.setEnabled(true);
                botonGenerarSucesores.setForeground(Color.blue);

                botonBusquedaEstrella.setEnabled(true);
                botonBusquedaEstrella.setForeground(Color.blue);

                botonBusquedaDijkstra.setEnabled(true);
                botonBusquedaDijkstra.setForeground(Color.blue);

                botonBusquedaVoraz.setEnabled(true);
                botonBusquedaVoraz.setForeground(Color.blue);

                //Modificacion Dany: Se propaga el tablero actual a los otros 3 //


                tableroAvara = tablero;
//                tableroAvara.setTipoBusqueda(Tablero.Busqueda.AVARA);
                  tableroAvara.setCasillaActual(tableroAvara.iniciarBusquedaManual());

                tableroAstar = tablero;
//                tableroAstar.setTipoBusqueda(Tablero.Busqueda.AESTRELLA);
//                tableroAstar.iniciarBusquedaManual();

                tableroDijkstra = tablero;
//                tableroDijkstra.setTipoBusqueda(Tablero.Busqueda.DIJKSTRA);
//                tableroDijkstra.iniciarBusquedaManual();

                System.out.println("He propagado a todos los tableros");


                //Fin Modificacion Dany //

                JOptionPane.showMessageDialog(null, "Seleccione el Algorítmo de Búsqueda a Emplear");
            }
        });

        botonGenerarSucesores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonGenerarSucesores.setEnabled(false);
                botonGenerarSucesores.setForeground(Color.gray);
                botonSiguiente.setEnabled(true);
                botonSiguiente.setForeground(Color.blue);

                tableroAvara.setTipoBusqueda(Tablero.Busqueda.AVARA);
                tableroAvara.generarSucesoresManual(tableroAvara.getCasillaActual());

                //tableroDijkstra.setTipoBusqueda(Tablero.Busqueda.DIJKSTRA);
                //tableroDijkstra.generarSucesoresManual(tableroDijkstra.getCasillaActual());

                //tableroAstar.setTipoBusqueda(Tablero.Busqueda.AESTRELLA);
                //tableroAstar.generarSucesoresManual(tableroAstar.getCasillaActual());


                switch (tipotablero) {
                    case "avara":
                        panelDeJuego.setMalla("avara");
                        panelDeJuego.repaint();
                        break;

                    case "dijkstra":
                        panelDeJuego.setMalla("dijkstra");
                        panelDeJuego.repaint();
                        break;

                    case "astar":
                        panelDeJuego.setMalla("astar");
                        panelDeJuego.repaint();
                        break;

                    default:
                        break;
                }

            }
        });

        botonSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonSiguiente.setEnabled(false);
                botonSiguiente.setForeground(Color.gray);
                botonGenerarSucesores.setEnabled(true);
                botonGenerarSucesores.setForeground(Color.blue);

                tableroAvara.setTipoBusqueda(Tablero.Busqueda.AVARA);
                tableroAvara.setCasillaActual(tableroAvara.seleccionarActualManual());

                //tableroDijkstra.setTipoBusqueda(Tablero.Busqueda.DIJKSTRA);
                //tableroDijkstra.setCasillaActual(tableroDijkstra.seleccionarActualManual());

                //tableroAstar.setTipoBusqueda(Tablero.Busqueda.AESTRELLA);
                //tableroAstar.setCasillaActual(tableroAstar.seleccionarActualManual());


                switch (tipotablero) {
                    case "avara":
                        panelDeJuego.setMalla("avara");
                        panelDeJuego.repaint();
                        break;

                    case "dijkstra":
                        panelDeJuego.setMalla("dijkstra");
                        panelDeJuego.repaint();
                        break;

                    case "astar":
                        panelDeJuego.setMalla("astar");
                        panelDeJuego.repaint();
                        break;

                    default:
                        break;
                }


            }
        });
    }

    /*
    * Definimos el evento que pasara cuando hagamos click
    * */
    @Override
    public void mouseClicked(MouseEvent e) {

        panelDeJuego.requestFocus();

        if (tablero.getSolucionEncontrada())
            tablero.reniciarTablero();
        // Aqui tengo que hacer control de lo que voy a mostrar //
        Casilla casilla = tablero.getCasillas()[e.getY() / 70][e.getX() / 70];

       /* if (click.equals(Casilla.Tipo.ENTRADA)) {
            tablero.setInicio(casilla);
            click = Casilla.Tipo.CAMPO;

        } else if (click.equals(Casilla.Tipo.SALIDA)) {
            tablero.setObjetivo(casilla);
            click = Casilla.Tipo.CAMPO;

        } else {*/
        tablero.cambiaTipo(casilla);
        click = casilla.getTipo();
        //}

        panelDeJuego.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /*
    * Definimos la clase mediante la que vamos a crear la ventana de juego de la aplicacion
    * */
    public class Ventana extends JPanel {

        private Tablero malla; // nuestro tablero de juego

        public Ventana(Tablero tablero) {
            this.malla = tablero;
        }

        public void setMalla(String tipo) {

            switch (tipo) {
                case "avara":
                    this.malla = tableroAvara;
                    break;

                case "dijkstra":
                    this.malla = tableroDijkstra;
                    break;

                case "astar":
                    this.malla = tableroAstar;
                    break;

                default:
                    this.malla = tablero;
                    break;
            }

            this.malla = tablero;
        }

        /*
        * Metodo de pintado de tablero, se emleara para pintar y repintar los datos que nos entren.
        * */

        public void paintComponent(Graphics g) {

            int sqsize = 70;
            int yoffset;
            int xoffset;

            for (int i = 0; i < malla.getHeight(); i++) {
                for (int j = 0; j < malla.getWidth(); j++) {
                    Casilla casilla = malla.getCasillas()[i][j];
                    yoffset = sqsize * i;
                    xoffset = sqsize * j;

                    // Fill with colors
                    switch (casilla.getTipo()) {
                        case ENTRADA:
                            g.setColor(entrada);
                            // if (click.equals(Casilla.Tipo.ENTRADA))
                            //   g.setColor(Color.cyan);
                            break;
                        case SALIDA:
                            g.setColor(salida);
                            //if (click.equals(Casilla.Tipo.SALIDA))
                            //  g.setColor(Color.cyan);
                            break;
                        case MONTAÑA:
                            g.setColor(montana);
                            break;
                        case SOLUCION:
                            g.setColor(solucion);
                            break;
                        case RIO:
                            g.setColor(rio);
                            break;
                        case BOSQUE:
                            g.setColor(bosque);
                            break;
                        case CAMPO:
                            g.setColor(campo);
                            break;
                    }
                    g.fillRect(xoffset, yoffset, sqsize, sqsize);

                    // Draw a border
                    switch (casilla.getEstado()){

                        case VISITADO:
                            g.setColor(Color.GRAY);
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);
                        case SELECCIONADO:
                            g.setColor(Color.GREEN);
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);
                        case SUCESOR:
                            g.setColor(Color.YELLOW);
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);
                        default:
                            g.setColor(Color.BLACK);
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);



                    }


                    g.setColor(Color.black);
                    g.drawRect(xoffset, yoffset, sqsize, sqsize);

                    // Escribir posicion
                    g.drawString(casilla.toString(), xoffset + 36, yoffset + 15);

                    //if (casilla.getTipo().equals(Casilla.Tipo.MONTAÑA))
                    //continue;
                    // Escribir heuristica
                    g.drawString("H:" + casilla.getHn(), xoffset + 1, yoffset + 15);

                    // Escribir Coste
                    g.drawString("G:" + casilla.getGn(), xoffset + 1, yoffset + 65);
                    g.drawString("F:" + casilla.getFn(), xoffset + 40, yoffset + 65);


                    // Escribir padre
                    if (casilla.getPadre() != null) {
                        g.drawString("P:" + casilla.getPadre().toString(), xoffset + 12, yoffset + 40);
                    }

                }
            }
        }
    }
}

