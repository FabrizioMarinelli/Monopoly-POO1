package monopoly;

import  java.util.ArrayList;
import partida.*;
import java.util.Scanner;
import java.io.BufferedReader;
import partida.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Iterator;
public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    //Añadimos una nueva clase indice para saber cual es el jugador actual
    private int indiceJugadorActual;
    private boolean posible;    //Boolean que creamos para almacenar si es posible construir el ejercicio o no
    // Método para inciar una partida: crea los jugadores y avatares.


    private void iniciarPartida() {
        Scanner myObj = new Scanner(System.in);
        // Lectura inicial
        String linea;
        if (myObj.hasNextLine()) {
            linea = myObj.nextLine().trim().toLowerCase();
        } else {
            return;
        }
        // Lectura mientras la entrada sea != "fin" o vacio
        while (!linea.equals("fin")) {
            System.out.println(" ");
            analizarComando(linea);

            // Siguiente lectura para la próxima iteración
            if (!myObj.hasNextLine()) break;
            linea = myObj.nextLine().trim().toLowerCase();
        }
    }
    // Metodo para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida(BufferedReader br) {
        //Leer archivo
        try {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.toLowerCase();
                System.out.println(" ");
                System.out.println(linea);
                analizarComando(linea);
            }

        }catch (java.io.IOException e){
            System.err.println("Error leyendo el archivo: " + e.getMessage());

        }
        //Pasar a modo interactivo
        iniciarPartida();

    }
    public Menu(String[] args){
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        indiceJugadorActual = 0;
        if (args.length > 0) {
            String ruta = args[0];
            try (BufferedReader br = Files.newBufferedReader(Paths.get(ruta), StandardCharsets.UTF_8);){
                //modo lectura
                iniciarPartida(br);

            } catch (IOException e) {
                System.err.println("No pude abrir/leer el archivo: " + ruta + " -> " + e.getMessage());
                return;
            }
        } else {
            // Modo interactivo (terminal)
            System.out.println("Entrando sin documento: ");
            iniciarPartida();

        }
    }

    /*Método que interpreta el comando introducido y toma la accion correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {
        String[] comandoSplit;
        comandoSplit = comando.split("[\\s]");
        switch (comandoSplit[0]) {
            case "crear":
                agregarJugador(comandoSplit);
                break;
            case "jugador":
                imprimirJugadorActual();
                break;
            case "listar":
                if (comandoSplit.length > 3) {
                    System.out.println("comando invalido");
                }else{
                    if (comandoSplit[1].equals("jugadores")) {
                        listarJugadores();
                    } else if (comandoSplit[1].equals("enventa")) {
                        listarVenta();
                    }
                    else if (comandoSplit[1].equals("edificios")) {
                        if(comandoSplit.length == 2){
                            listarEdificios();
                        } else if (comandoSplit.length == 3) {
                            listarEdificiosGrupo(comandoSplit[2]);
                        }

                    }
                }
                break;
            case "lanzar":
                if (jugadores.size() < 2) {
                    System.out.println("Todavia no hay jugadores suficientes");
                } else {
                    if (comandoSplit.length == 2) {
                        lanzarDados(0,0);
                    } else if (comandoSplit.length == 3) {
                        String[] splitDados = comandoSplit[2].split("\\+");
                        lanzarDados(Integer.parseInt(splitDados[0].trim()), Integer.parseInt(splitDados[1].trim()));
                    } else {
                        System.out.println("comando invalido");
                    }
                }
                break;
            case "acabar":
                acabarTurno();
                break;
            case "salir":
                if(jugadores.size() < 2){
                    System.out.println("Todavia no hay jugadores suficientes");
                } else {
                    salirCarcel(jugadores.get(indiceJugadorActual));
                }
                break;
            case "describir":
                if(comandoSplit.length != 3 && comandoSplit.length != 2){
                    System.out.println("Comando invalido");
                } else if (comandoSplit[1].equals("jugador")) {
                    desJugador(comandoSplit);
                } else {
                    descCasilla(comandoSplit[1]);
                }
                break;
            case "comprar":
                if (jugadores.size() < 2) {
                    System.out.println("Todavia no hay jugadores suficientes");
                } else {
                    comprar(comandoSplit[1]);
                }
                break;
            case "ver":
                verTablero();
                break;
            case "hipotecar":
                if (jugadores.size() < 2) {
                    System.out.println("Todavia no hay jugadores suficientes");
                } else if (comandoSplit.length != 2) {
                    System.out.println("Comando invalido");
                } else {
                    hipotecarPropiedad(comandoSplit[1]);
                }
                break;
            case "deshipotecar":
                if (jugadores.size() < 2) {
                    System.out.println("Todavia no hay jugadores suficientes");
                } else if (comandoSplit.length != 2) {
                    System.out.println("Comando invalido");
                } else {
                    deshipotecarPropiedad(comandoSplit[1]);
                }
                break;

            case "edificar":
                edificar(comandoSplit[1]);
                break;
            case "vender":
                if (comandoSplit.length != 4) {
                    System.out.println("Comando invalido");
                    return;
                }
                venderEdificios(comandoSplit[1],comandoSplit[2],comandoSplit[3]);
                break;
            case "estadisticas":
                if (comandoSplit.length == 1) {
                    // Imprime estadísticas de la partida completa
                    estadisticasJuego();
                } else if (comandoSplit.length == 2) {
                    // Comando para las estadisticas de un jugador
                    estadisticasJugador(comandoSplit[1]);
                } else {
                    System.out.println("Comando invalido");
                }
                break;

            default:
                System.out.println("Comando invalido");
                break;
        }

    }
    // ---------------------------------------------------
    //         metodos para analizador de comandos
    // ---------------------------------------------------
    private void agregarJugador(String[] comandoSplit){
        if (jugadores.size()==4){
            System.out.println("cantidad de jugadores maxima alcanzada");
            return;
        }
        if ((comandoSplit.length == 4) && (comandoSplit[3].equals("coche")||comandoSplit[3].equals("esfinge")||comandoSplit[3].equals("sombrero")||comandoSplit[3].equals("pelota") ) ){
            //comprobacion lista de jugadores
            if (!jugadores.isEmpty()){
                for (Jugador jugador : jugadores) {
                    if (comandoSplit[2].equals(jugador.getNombre())){
                        System.out.println("Ese nombre ya esta en uso");
                        return;
                    }
                }
            }
            //comprobacion lista de avatares
            if (!avatares.isEmpty()){
                for (Avatar avatar : avatares) {
                    if (comandoSplit[3].equals(avatar.getTipo())){
                        System.out.println("Ese avatar ya esta en uso");
                        return;
                    }
                }
            }
            Jugador nuevoJugador = new  Jugador(comandoSplit[2],comandoSplit[3],tablero.encontrar_casilla("Salida"),avatares,jugadores);
            System.out.println("""
                            {
                                nombre: %s,
                                avatar: %s
                            }
                            """.formatted(nuevoJugador.getNombre(),nuevoJugador.getAvatar().getId()));
        } else {
            System.out.println("comando invalido");
        }
    }
    private void imprimirJugadorActual(){
        if(!jugadores.isEmpty()) {
            System.out.println("""
                            {
                                nombre: %s,
                                avatar: %s
                            }
                            """.formatted(jugadores.get(indiceJugadorActual).getNombre(), jugadores.get(indiceJugadorActual).getAvatar().getId()));
        } else {
            System.out.println("Todavia no hay jugadores");
        }
    }
    private void listarJugadores() {
        if (!jugadores.isEmpty()) {
            for (Jugador jugador : jugadores) {
                desJugador(jugador);
            }
        } else {
            System.out.println("Todavia no hay jugadores");
        }
    }
    private void desJugador(Jugador jugador) {
        System.out.println(jugador.toString());
    }
    private void desJugador(String[] partes){
        if (!jugadores.isEmpty()) {
            for (Jugador jugador : jugadores) {
                if (partes[2].equals(jugador.getNombre())) {
                    desJugador(jugador);
                    return;
                }
            }
            System.out.println("El jugador " + partes[2] + " no existe");
        } else {
            System.out.println("Todavia no hay jugadores");
        }
    }
    private void descCasilla(String nombre) {
        ArrayList <ArrayList <Casilla>> casillas = tablero.getPosiciones();
        for (ArrayList <Casilla> ladoCasilla : casillas) {
            for (Casilla casilla : ladoCasilla) {
                if (casilla.getNombre().equalsIgnoreCase(nombre)) {
                    System.out.println(casilla.infoCasilla(nombre));
                }
            }
        }

    }
    private void lanzarDados(int d1, int d2) {
        //Creamos los dados con los que realizaremos las tiradas
        Dado dado1 = new Dado();
        Dado dado2 = new Dado();
        int tiradaDado1;
        int tiradaDado2;

        //Declaramos al jugador actual
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        Avatar avatar = jugadorActual.getAvatar();

        //Comprobar si el jugador tiene tiradas disponibles
        if(!jugadorActual.getTiradaDisponible()){
            System.out.println("El jugador actual no tiene tiradas disponibles");
            return;
        }

        //Declaramos variable para contar las veces que se sacaron dobles y seteamos a false tirada disponible
        jugadorActual.setTiradaDisponible(false);

        //Hacemos las tiradas
        if (d1!= 0 && d2!=0){
            tiradaDado1 = d1;
            tiradaDado2 = d2;
        } else {
            tiradaDado1 = dado1.hacerTirada();
            tiradaDado2 = dado2.hacerTirada();
        }

        //calculamos el valor total
        int valorTirada = tiradaDado1 + tiradaDado2;
        System.out.println("Dados lanzados " + tiradaDado1 + "+" + tiradaDado2);

        //comprobamos si se sacaron dobles
        if (tiradaDado1 == tiradaDado2) {
            jugadorActual.setTiradasRepetidas(jugadorActual.getTiradasRepetidas() + 1);
            //Comprobamos que el contador de dobles no sea igual a 3, de ser a si encarcelamos al jugador
            if (jugadorActual.getTiradasRepetidas() == 3) {
                System.out.println(jugadorActual.getNombre() + " ha sacado tres dobles seguidos. Va a la cárcel.");
                jugadorActual.encarcelar(tablero.getPosiciones());
                return;
            } else if (jugadorActual.isEnCarcel()){
                salirCarcel(jugadorActual,1);
                jugadorActual.setTiradasRepetidas(jugadorActual.getTiradasRepetidas() - 1);
            }
            jugadorActual.setTiradaDisponible(true);
            System.out.println("Has sacado dobles vuelves a tirar.");
        }

        //Movemos al avatar el numero de posiciones que le corresponda
        if (!jugadorActual.isEnCarcel()) {
            avatar.moverAvatar(tablero.getPosiciones(), valorTirada);
            System.out.println("El jugador avanza " + valorTirada + " posiciones.");
        } else{
            jugadorActual.setTiradasCarcel(jugadorActual.getTiradasCarcel() + 1);
            if (jugadorActual.getTiradasCarcel() ==3){
                salirCarcel(jugadorActual);
            } else {
                System.out.println("El jugador esta en la carcel, no puede avanzar");
            }
        }
        //Declaramos el lugar actual del avatar para poder evaluar su posicion
        Casilla casillaActual = avatar.getLugar();
        boolean sigueEnJuego = casillaActual.evaluarCasilla(jugadorActual, banca, tablero,valorTirada, jugadores);

        //Si el jugador se queda sin dinero suficiente debe declararse en bancarota
        if (!sigueEnJuego) {
            System.out.println(jugadorActual.getNombre() + " no tiene suficiente dinero. Debe hipotecar o declararse en bancarrota.");
            return;
        }
        if (jugadorActual.isEnCarcel()){
            return;
        }
    }
    private void hipotecarPropiedad(String nombre){
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        Casilla propiedadHipotecar;

        //Recorrer cada propiedad del jugador
        for (Casilla propiedadJugador: jugadorActual.getPropiedades()){
            //Comprobar si coincide con la propiedad solicitada
            if(propiedadJugador.getNombre().equalsIgnoreCase(nombre)){
                propiedadHipotecar = propiedadJugador;

                //Comprobar tipo
                if (!propiedadHipotecar.getTipo().equalsIgnoreCase("solar")){
                    System.out.println("Esta propiedad no es un solar");
                    return;
                }
                //Comprobar si tiene edificios
                if(!propiedadHipotecar.getEdificios().isEmpty()){
                    System.out.println("No se puede hipotecar una propiedad con edificios");
                    return;
                }

                //Comprobar si esta hipotecada
                if (!propiedadHipotecar.getPropiedadHipotecada()){

                    //Setear hipotecada a true, otorgar dinero y avisar por mensaje
                    propiedadHipotecar.setPropiedadHipotecada(true);
                    jugadorActual.anhadirHipoteca(propiedadHipotecar);
                    jugadorActual.sumarFortuna(propiedadHipotecar.getHipoteca());
                    System.out.println(jugadorActual.getNombre() + " recibe "+propiedadHipotecar.getHipoteca()+" por la hipoteca de " + propiedadHipotecar.getNombre() + ". No puede recibir alquileres ni edificar en el grupo " +propiedadHipotecar.getGrupo().getColorGrupo());
                    return;

                }else {
                    System.out.println(jugadorActual.getNombre()+ " no puede hipotecar " + propiedadHipotecar.getNombre()+". Ya esta hipotecada");
                    return;
                }
            }
        }
        //Si no es de su propiedad avisa al jugador
        System.out.println(jugadorActual.getNombre()+ " no puede hipotecar " + nombre +". No es una propiedad que le pertenece");
    }
    private void deshipotecarPropiedad(String nombre){
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        Casilla propiedadHipotecar;

        //Recorrer cada propiedad del jugador
        for (Casilla propiedadJugador: jugadorActual.getPropiedades()){
            //Comprobar si coincide con la propiedad solicitada
            if(propiedadJugador.getNombre().equalsIgnoreCase(nombre)){
                propiedadHipotecar = propiedadJugador;

                //Comprobar tipo
                if (!propiedadHipotecar.getTipo().equalsIgnoreCase("solar")){
                    System.out.println("Esta propiedad no es un solar");
                }
                //Comprobar si esta hipotecada
                if (propiedadHipotecar.getPropiedadHipotecada()){

                    //Comprobar si el jugador tiene suficiente dinero
                    if (jugadorActual.getFortuna() > propiedadHipotecar.getHipoteca()){
                        //Setear hipotecada a false, cobrar dinero y avisar por mensaje
                        propiedadHipotecar.setPropiedadHipotecada(false);
                        jugadorActual.eliminarHipoteca(propiedadHipotecar);
                        jugadorActual.sumarFortuna(-(propiedadHipotecar.getHipoteca()));
                        System.out.println(jugadorActual.getNombre() + " paga "+propiedadHipotecar.getHipoteca()+" por deshipotecar " + propiedadHipotecar.getNombre() + ". Ahora puede recibir alquileres y edificar en el grupo " +propiedadHipotecar.getGrupo().getColorGrupo());
                        return;
                    } else{
                        System.out.println("El jugador no posee suficiente dinero");
                        return;
                    }


                }else {
                    System.out.println(jugadorActual.getNombre() +" no puede deshipotecar "+propiedadJugador.getNombre() +". No esta hipotecada");
                    return;
                }
            }
        }

        //Si no es de su propiedad avisa al jugador
        System.out.println(jugadorActual.getNombre() + " no puede deshipotecar " +nombre + ". No es una propiedad que le pertenece");

    }
    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        //  Establecemos el jugador actual
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);

        // Declaramos una variable para buscar la casilla que queremos comprar
        Casilla casillaComprar = tablero.encontrar_casilla(nombre);

        // Declaramos el resto de varibles necesarias
        float precio = casillaComprar.getValor();
        String tipo = casillaComprar.getTipo().toLowerCase();

        // Comprobamos si el tipo de la casilla que queremos comprar es correcta
        if (!(tipo.equals("solar") || tipo.equals("transporte") || tipo.equals("servicios"))) {
            System.out.println("La casilla " + nombre + " no se puede comprar.");
            return;
        }
        // Comprobamos si la casilla pertenece a otro jugador o a la banca
        if (!casillaComprar.getDuenho().equals(banca)) {
            System.out.println("La casilla " + nombre + " pertenece a " + casillaComprar.getDuenho().getNombre() + ".");
            return;
        }

        // Comprobamos que el jugador tiene dienero suficiente para comprar la propiedad
        if (jugadorActual.getFortuna() < precio) {
            System.out.println("No tienes suficiente dinero para comprar " + nombre + ". Precio: " + precio);
            return;
        }

        // Comprobamos que el jugador este en la casilla para poder comprarla
        if (!jugadorActual.getAvatar().getLugar().equals(casillaComprar)) {
            System.out.println("No estás en la casilla " + nombre + ". Solo puedes comprar donde estás.");
            return;
        }

        // Permitimos que el jugador compre la casilla actual
        casillaComprar.comprarCasilla(jugadorActual, banca);
        //System.out.println(jugadorActual.getNombre() + " ha comprado la casilla '" + nombre + "' por " + precio + ".");

    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
     * Parámetro: id del avatar a describir.
     */
    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.



    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel(Jugador jugador) {
        if(!jugador.isEnCarcel()){
            System.out.println("El jugador no se encuentra encarcelado actualmente");
            return;
        }
        if(jugador.getFortuna() >= 500_000f){
            jugador.setFortuna(jugador.getFortuna() - 500_000f);
            System.out.println(jugador.getNombre() + " ha pagado 500.000 y ha salido de la cárcel.");
            jugador.setTiradasCarcel(0);
            jugador.setEnCarcel(false);
            System.out.println(jugador.getNombre() + " ha salido de la cárcel.");
        }else{
            //ver si tiene propiedades para vender o hipotecar o vender edificios
            //jugador.setEliminado(true);
        }
    }
    private void salirCarcel(Jugador jugador, int i) {
        jugador.setTiradasCarcel(0);
        jugador.setEnCarcel(false);
        System.out.println(jugador.getNombre() + " ha salido de la cárcel.");
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        System.out.println("Propiedades en venta:");

        // Declaramos una variable para saber si la propiedad estan en venta
        boolean estaEnVenta = false;

        // Recorremos las casillas del tablero y comprobamos cuales estan disponibles para vender
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                // Solo nos interesan las que pueden ser compradas es decir, los solares, las casillas de transporte y los servicios
                if (c.getTipo().equalsIgnoreCase("Solar") ||
                        c.getTipo().equalsIgnoreCase("Transporte") ||
                        c.getTipo().equalsIgnoreCase("Servicios")) {

                    // Si la casilla aún pertenece a la banca, está en venta, por lo que si se imprimira
                    if (c.getDuenho() == banca) {
                        estaEnVenta = true; //Establecemos la casilla de propiedades en venta en verdadero
                        System.out.println("{");
                        System.out.println("  nombre: " +c.getNombre());
                        System.out.println("  tipo: " + c.getTipo() + ","); //Imprimimos el tipo de casilla
                        if (c.getTipo().equalsIgnoreCase("Solar")) {    //Si es un solar imprimimos el grupo al que pertenece
                            System.out.println("  grupo: " + c.getGrupo().getColorGrupo() + ",");
                        }
                        System.out.println("  valor: " + c.getValor());   //Se imprime el valor de la casilla
                        System.out.println("}");
                    }
                }
            }
        }

        //Si no hay ninguna propiedad para comprar se imprime por pantalla
        if (!estaEnVenta) {
            System.out.println("No hay propiedades en venta actualmente.");
        }

    }

    private void ListarVenta(String grupo){
        System.out.println("Propiedades en venta para grupo: "+grupo);

        // Declaramos una variable para saber si la propiedad estan en venta
        boolean estaEnVenta = false;
        boolean esGrupo = false;
        // Recorremos las casillas del tablero y comprobamos cuales estan disponibles para vender
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                // Solo nos interesan las que pueden ser compradas es decir, los solares, las casillas de transporte y los servicios
                if (c.getTipo().equalsIgnoreCase("Solar") ||
                        c.getTipo().equalsIgnoreCase("Transporte") ||
                        c.getTipo().equalsIgnoreCase("Servicios")) {
                    esGrupo = false;
                    // Si la casilla aún pertenece a la banca, está en venta, por lo que si se imprimira
                    if (c.getDuenho() == banca) {
                        ArrayList<Casilla> casillasg = c.getGrupo().getMiembros();
                        for (Casilla casilla : casillasg) {
                            if (casilla.getNombre().equalsIgnoreCase(grupo)) {
                                esGrupo = true;
                                break;
                            }
                        }
                        if (esGrupo) {
                            estaEnVenta = true; //Establecemos la casilla de propiedades en venta en verdadero
                            System.out.println("{");
                            System.out.println("  nombre: " + c.getNombre());
                            System.out.println("  tipo: " + c.getTipo() + ","); //Imprimimos el tipo de casilla
                            if (c.getTipo().equalsIgnoreCase("Solar")) {    //Si es un solar imprimimos el grupo al que pertenece
                                //System.out.println("  grupo: " + c.getGrupo().getColorGrupo() + ",");
                            }
                            System.out.println("  valor: " + c.getValor());   //Se imprime el valor de la casilla
                            System.out.println("}");
                        }
                    }
                }
            }
        }

        //Si no hay ninguna propiedad para comprar se imprime por pantalla
        if (!estaEnVenta) {
            System.out.println("No hay propiedades en venta actualmente.");
        }
    }
    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }


    private void acabarTurno() {
        if(jugadores.size()<2){
            System.out.println("Todavia no hay jugadores suficientes");
            return;
        }
        indiceJugadorActual = (indiceJugadorActual+1)%jugadores.size();
        Jugador siguiente = jugadores.get(indiceJugadorActual);
        siguiente.setTiradaDisponible(true);
        siguiente.setTiradasRepetidas(0);
        System.out.println("Turno del jugador: " + siguiente.getNombre());
    }

    //Método que inicializa y muestra por pantalla el tablero
    public void iniciarTablero() {
        System.out.println("Bienvenido al Monopoly creado por Carolina, Fabrizio y Maria");
        System.out.println(tablero.toString());
    }

    //Método que nos permite ver el tablero durante la partida
    public void verTablero(){
        System.out.println("Estado actual del tablero:\n\n");
        System.out.println(tablero.toString());

    }

    //Método para edificar
    public void edificar(String tipo){
        tipo = tipo.toLowerCase();
        Jugador propietario = jugadores.get(indiceJugadorActual);
        Casilla casilla = propietario.getAvatar().getLugar();

        float coste;

        switch(tipo){
            case "casa":
                coste = casilla.getValorCasa();
                break;
            case "hotel":
                coste = casilla.getValorHotel();
                break;
            case "piscina":
                coste = casilla.getValorPiscina();
                break;
            case "pista_deporte":
                coste = casilla.getValorPistaDeporte();
                break;
            default:
                System.out.println("Tipo de edificio no válido: " + tipo);
                return;
        }

        if(!casilla.posibleConstruir(tipo, propietario)){
            System.out.println("No se cumplen los requesitos para construir el edificio que solicita en este solar\n");
            return;
        }
        if(propietario.getFortuna() < coste){
            System.out.println("La fortuna no es suficiente para construir el edificio\n");
            return;
        }

        //Comprobamos el caso de que tengamos 4 casas y vayamos a contruir un hotel
        //Pues se deben eliminar las 4 casas
        if(tipo.equals("hotel")) {
            int casasEliminadas = 0;

            //Eliminamos las casas del jugador y de la casilla
            Iterator<Edificio> it = casilla.getEdificios().iterator();
            while (it.hasNext()) {
                Edificio e = it.next();
                if (e.getTipo().equals("casa")) {
                    it.remove();                      //Quitamos las casas de la casilla
                    propietario.eliminarEdificio(e);  //Quitamos las casas del jugador
                    casasEliminadas++;

                    if (casasEliminadas == 4) break;
                }
            }
        }
        //Modificamos la fortuna del jugador y actualizamos sus estadisticas
        propietario.setFortuna(propietario.getFortuna() - coste);
        propietario.sumarDineroInvertido(coste);

        //Creamos el nuevo edificio
        Edificio nuevoEdificio = new Edificio(tipo, propietario, casilla, coste);

        //Le asignamos al jugador su nuevo edificio
        propietario.anhadirEdificio(nuevoEdificio);

        //Le asignamos a la casilla su nuevo edificio
        casilla.anhadirEdificio(nuevoEdificio);

        System.out.printf("Nueva edificación: %s en el solar: %s. La fortuna de %s se reduce en %.2f€.%n", tipo, casilla.getNombre(), propietario.getNombre(), coste);
    }
    private void venderEdificios(String tipo, String nombre, String cantidad) {
        //Obtener jugador actual
        Jugador jugadorActual =  jugadores.get(indiceJugadorActual);

        //Buscar casilla
        Casilla casilla = tablero.encontrar_casilla(nombre);
        if (casilla == null){
            System.out.println("La casilla " + nombre + " no se encuentra en el tablero.");
            return;
        }
        //Comprobar que la casilla es propiedad del jugador
        if (!casilla.getDuenho().getNombre().equalsIgnoreCase(jugadorActual.getNombre())){
            System.out.println("La casilla " + nombre + " no le pertenece al jugador.");
            return;
        }
        //Operatoria cantidad vendida
        int cantidadPorVender = Integer.parseInt(cantidad);
        int eliminados = 0;

        //Convertir tipo plural a singular
        String tipoEdificio = tipo;
        if(tipoEdificio.equalsIgnoreCase("casas")){
            tipoEdificio = "casa";
        } else if(tipoEdificio.equalsIgnoreCase("hoteles")){
            tipoEdificio = "hotel";
        }
        Iterator<Edificio> iterador = jugadorActual.getEdificios().iterator();
        while (iterador.hasNext() && eliminados < cantidadPorVender){
            Edificio e = iterador.next();
            if(e.getTipo().equalsIgnoreCase(tipoEdificio) && e.getCasilla().getNombre().equalsIgnoreCase(nombre)){
                jugadorActual.sumarFortuna(e.getCoste());
                iterador.remove();
                e.getCasilla().eliminarEdificio(e);
                eliminados++;
            }
        }
        if (eliminados == 0) {
            System.out.println("No hay edificios de tipo " + tipo + " para vender en " + nombre + ".");
        } else {
            System.out.println("Se vendieron " + eliminados + " " + tipo + "(s) en " + nombre + ".");
        }
    }

    //Método para listar todos los edificios construidos en el juego
    private void listarEdificios(){
        ArrayList<Edificio> todosEdificios = new ArrayList<>();

        //Recorremos todos los jugadores y acumulamos sus edificios
        for(Jugador j : jugadores){
            todosEdificios.addAll(j.getEdificios());
        }

        //Caso de que aún no se hayan construído edificios
        if(todosEdificios.isEmpty()){
            System.out.println("No hay edificios construidos todavía.");
            return;
        }

        //Mostramos la información de cada edificio añadido al ArrayList
        for(Edificio e : todosEdificios){
            String grupoColor = (e.getCasilla() != null && e.getCasilla().getGrupo() != null)
                    ? e.getCasilla().getGrupo().getColorGrupo()
                    : "-";

            System.out.printf("""
                {
                    id: %s,
                    propietario: %s,
                    casilla: %s,
                    grupo: %s,
                    coste: %.0f
                },
                """,
                    e.getId(), (e.getPropietario() != null ? e.getPropietario().getNombre() : "-"), (e.getCasilla() != null ? e.getCasilla().getNombre() : "-"), grupoColor, e.getCoste()
            );
        }
    }

    //Método para mostrar todos los edificios de un grupo
    private void listarEdificiosGrupo(String colorGrupo){
        //Pasamos todo a mayúsculas
        colorGrupo = colorGrupo.toUpperCase();

        //Creamos un arraylist donde almacenaremos todas las casillas del grupo solicitado por el usuario
        ArrayList<Casilla> casillasGrupo = new ArrayList<>();
        //Recorremos todas las casillas del tablero
        for(ArrayList<Casilla> lado : tablero.getPosiciones()){
            for(Casilla c : lado){
                //Comprobamos que la casilla tenga un grupo asignado y lo comparamos por el grupo pasado como parámetro
                if(c.getGrupo() != null && c.getGrupo().getColorGrupo().equals(colorGrupo)){
                    //En caso de que sea igual que el grupo pasado por parámetro lo añadimos al ArrayList
                    casillasGrupo.add(c);
                }
            }
        }
        //Caso en el que el ArrayList está vacío, puede ser por dos motivcs, notificados por salida de pantalla
        if(casillasGrupo.isEmpty()){
            System.out.println("No existe edificios en este grupo o no existe este grupo, comprueba el comando escrito");
        }
        //Variables para saber que es posible construír
        boolean posibleCasa = false;
        boolean posibleHotel = false;
        boolean posiblePiscina = false;
        boolean posiblePista = false;

        //Recorremos cada casilla del ArrayList
        for(Casilla c : casillasGrupo){
            //Creamos nuevos ArrayList para almacenar lo que tenemos construído en el grupo
            ArrayList<String> casa = new ArrayList<>();
            ArrayList<String> hotel = new ArrayList<>();
            ArrayList<String> piscina = new ArrayList<>();
            ArrayList<String> pista = new ArrayList<>();
            //Recorremos todos los edificios del grupo y los añadimos a su correspondiente ArrayList para trabajar
            //con ellos en un futuro y ver que se puede edificar a mayores
            for(Edificio e : c.getEdificios()){
                switch(e.getTipo()){
                    case "casa":
                        casa.add(e.getId());
                        break;
                    case "hotel":
                        hotel.add(e.getId());
                        break;
                    case "piscina":
                        piscina.add(e.getId());
                        break;
                    case "pista":
                        pista.add(e.getId());
                        break;
                    default:
                }
            }
            //Imprimimos los edificios pertenecientes al grupo
            System.out.printf("""
                {
                    propiedad: %s,
                    hoteles: %s,
                    casas: %s,
                    piscinas: %s,
                    pistasDeDeporte: %s,
                    alquiler: %.0f
                },
                """,
                    c.getNombre(),
                    hotel.isEmpty() ? "-" : hotel,
                    casa.isEmpty() ? "-" : casa,
                    piscina.isEmpty() ? "-" : piscina,
                    pista.isEmpty() ? "-" : pista,
                    c.getImpuesto()
            );

            //Comprobamos que podemos edificar en nuestro grupo tal como está su situación actual
            if(casa.size() < 4){
                posibleCasa = true;
            }
            if(casa.size() == 4 && hotel.isEmpty()){
                posibleHotel = true;
            }
            if(hotel.size() == 1 && piscina.isEmpty()){
                posiblePiscina = true;
            }
            if(hotel.size() == 1 && pista.isEmpty()){
                posiblePista = true;
            }
        }

        //Imprimimos que se puede construír finalmente en el grupo
        //Creamos dos nuevos ArrayList para almacenar el tipo de propiedades que se pueden y no se pueden cosntruír
        ArrayList<String> siSePuede = new ArrayList<>();
        ArrayList<String> noSePuede = new ArrayList<>();

        if(posibleCasa){
            siSePuede.add("casas");
        }else{
            noSePuede.add("casas");
        }
        if(posibleHotel){
            siSePuede.add("hotel");
        }else{
            noSePuede.add("hotel");
        }
        if(posiblePiscina){
            siSePuede.add("piscina");
        }else{
            noSePuede.add("piscinas");
        }
        if(posiblePista){
            siSePuede.add("pistas de deporte");
        }else{
            noSePuede.add("pistas de deporte");
        }

        //Comprobamos que no sean vacías y si no lo son imprimimos su contentido
        if (!siSePuede.isEmpty())
            System.out.println("Se puede edificar " + String.join(" y ", siSePuede) + ".");

        if (!noSePuede.isEmpty())
            System.out.println("No se pueden construir " + String.join(" ni ", noSePuede) + ".");
    }

    //Método para imprimir todas las estadisticas de un jugador
    private void estadisticasJugador(String nombreJugador){
        Jugador objetivo = null;

        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombreJugador)) {
                objetivo = j;
                break;
            }
        }

        if (objetivo == null) {
            System.out.println("El jugador " + nombreJugador + " no existe.");
            return;
        }
        System.out.println("{");
        System.out.println("  dineroInvertido: " + objetivo.getDineroInvertido() + ",");
        System.out.println("  pagoTasasEImpuestos: " + objetivo.getPagoTasasEImpuestos() + ",");
        System.out.println("  pagoDeAlquileres: " + objetivo.getPagoDeAlquileres() + ",");
        System.out.println("  cobroDeAlquileres: " + objetivo.getCobroDeAlquileres() + ",");
        System.out.println("  pasarPorCasillaDeSalida: " + objetivo.getPasarPorCasillaDeSalida() + ",");
        System.out.println("  premiosInversionesOBote: " + objetivo.getPremiosInversionesOBote() + ",");
        System.out.println("  vecesEnLaCarcel: " + objetivo.getVecesEnLaCarcel());
        System.out.println("}");

    }

    // Método pra imprimir las estadisticas de la partida
    private void estadisticasJuego(){
        //Definimos todas las variables necesarias
        Casilla casillaMasRentable = null;
        float max = -1f;

        Grupo grupoMasRentable = null;
        float max2 = -1f;

        Casilla casillaMasFrecuentada = null;
        int max3 = -1;

        Jugador jugadorMasVueltas = null;
        int max4 = -1;

        Jugador jugadorEnCabeza = null;
        float maxFortuna = -1f;

        //Calculamos cual es la casilla mas rentable
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getDineroGenerado() > max) {
                    max = c.getDineroGenerado();
                    casillaMasRentable = c;
                }
            }
        }

        //Calculamos cual es el grupo mas rentable
        for (Grupo g : tablero.getGrupos()) {
            float generado = g.getDineroGenerado();
            if (generado > max2) {
                max2 = generado;
                grupoMasRentable = g;
            }
        }

        //Calculamos la casilla mas frecuentada
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getVecesPisada() > max3) {
                    max3 = c.getVecesPisada();
                    casillaMasFrecuentada = c;
                }
            }
        }

        //Calculamos cual es el jugador que lleva mas vueltas
        for (Jugador j : jugadores) {
            if (j.getVueltas() > max4) {
                max4 = j.getVueltas();
                jugadorMasVueltas = j;
            }
        }

        //Calculamos cual es el jugador que va en cabeza segun su fortuna
        for (Jugador j : jugadores) {
            // Fortuna total = dinero en mano + valor de propiedades y edificaciones
            float totalFortuna = j.getFortuna() + j.getDineroInvertido();
            if (totalFortuna > maxFortuna) {
                maxFortuna = totalFortuna;
                jugadorEnCabeza = j;
            }
        }

        //Imprimimos todas las estadisticas
        System.out.println("{");
        System.out.println("    casillaMasRentable: " + (casillaMasRentable != null ? casillaMasRentable.getNombre() : "Ninguna") + ",");
        System.out.println("    grupoMasRentable: " + (grupoMasRentable != null ? grupoMasRentable.getColorGrupo() : "Ninguna") + ",");
        System.out.println("    casillaMasFrecuentada: " + (casillaMasFrecuentada != null ? casillaMasFrecuentada.getNombre() : "Ninguna") + ",");
        System.out.println("    jugadorMasVueltas: " + (jugadorMasVueltas != null ? jugadorMasVueltas.getNombre() : "Ninguno") + ",");
        System.out.println("    jugadorEnCabeza: " + (jugadorEnCabeza != null ? jugadorEnCabeza.getNombre() : "NInguno"));
        System.out.println("}");


    }

}
