package monopoly;

import  java.util.ArrayList;
import partida.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
public class Juego {

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
    public Juego(String[] args){
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
                    if (casilla instanceof Solar solar) {
                        System.out.println(solar.infoCasilla(nombre));
                    }
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
        for (Casilla c : jugadorActual.getPropiedades()){
            if(c.getNombre().equalsIgnoreCase(nombre)){
                if (!(c instanceof Solar propiedadHipotecar)) {
                    System.out.println("Esta propiedad no es un solar");
                    return;
                }

                //Comprobar si tiene edificios
                if(!propiedadHipotecar.getEdificios().isEmpty()){
                    System.out.println("No se puede hipotecar una propiedad con edificios");
                    return;
                }

                //Comprobar si está hipotecada
                if (!propiedadHipotecar.getPropiedadHipotecada()){
                    propiedadHipotecar.setPropiedadHipotecada(true);
                    jugadorActual.anhadirHipoteca(propiedadHipotecar);
                    jugadorActual.sumarFortuna(propiedadHipotecar.getHipoteca());
                    System.out.println(jugadorActual.getNombre() + " recibe "+propiedadHipotecar.getHipoteca()+" por la hipoteca de " + propiedadHipotecar.getNombre() + ". No puede recibir alquileres ni edificar en el grupo " + propiedadHipotecar.getGrupo().getColorGrupo());
                    return;
                } else {
                    System.out.println(jugadorActual.getNombre()+ " no puede hipotecar " + propiedadHipotecar.getNombre()+". Ya está hipotecada");
                    return;
                }
            }
        }
        System.out.println(jugadorActual.getNombre()+ " no puede hipotecar " + nombre +". No es una propiedad que le pertenece");
    }
    private void deshipotecarPropiedad(String nombre){
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);

        for (Casilla c : jugadorActual.getPropiedades()){
            if(c.getNombre().equalsIgnoreCase(nombre)){
                if (!(c instanceof Solar propiedadHipotecar)) {
                    System.out.println("Esta propiedad no es un solar");
                    return;
                }

                if (propiedadHipotecar.getPropiedadHipotecada()){
                    if (jugadorActual.getFortuna() >= propiedadHipotecar.getHipoteca()){
                        propiedadHipotecar.setPropiedadHipotecada(false);
                        jugadorActual.eliminarHipoteca(propiedadHipotecar);
                        jugadorActual.sumarFortuna(-propiedadHipotecar.getHipoteca());
                        System.out.println(jugadorActual.getNombre() + " paga "+propiedadHipotecar.getHipoteca()+" por deshipotecar " + propiedadHipotecar.getNombre() + ". Ahora puede recibir alquileres y edificar en el grupo " + propiedadHipotecar.getGrupo().getColorGrupo());
                        return;
                    } else{
                        System.out.println("El jugador no posee suficiente dinero");
                        return;
                    }
                } else {
                    System.out.println(jugadorActual.getNombre() +" no puede deshipotecar "+propiedadHipotecar.getNombre() +". No está hipotecada");
                    return;
                }
            }
        }

        System.out.println(jugadorActual.getNombre() + " no puede deshipotecar " +nombre + ". No es una propiedad que le pertenece");
    }
    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);

        // Buscar la casilla en el tablero
        Casilla casillaComprar = tablero.encontrar_casilla(nombre);
        if (casillaComprar == null) {
            System.out.println("La casilla " + nombre + " no existe.");
            return;
        }

        // Verificamos que sea una propiedad comprable
        if (!(casillaComprar instanceof Propiedad propiedad)) {
            System.out.println("La casilla " + nombre + " no se puede comprar.");
            return;
        }

        // Verificamos que el jugador está en la casilla
        if (!jugadorActual.getAvatar().getLugar().equals(casillaComprar)) {
            System.out.println("No estás en la casilla " + nombre + ". Solo puedes comprar donde estás.");
            return;
        }

        // Delegamos la lógica de compra a la propia propiedad
        propiedad.comprar(jugadorActual, banca);
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
    // Listar todas las propiedades en venta
    private void listarVenta() {
        System.out.println("Propiedades en venta:");
        boolean estaEnVenta = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c instanceof Propiedad propiedad) { // Solo propiedades comprables
                    if (propiedad.getDuenho() == banca) { // Si pertenece a la banca
                        estaEnVenta = true;
                        System.out.println("{");
                        System.out.println("  nombre: " + propiedad.getNombre());
                        System.out.println("  tipo: " + propiedad.getTipo() + ",");
                        if (propiedad instanceof Solar solar) { // Solo los solares tienen grupo
                            System.out.println("  grupo: " + solar.getGrupo().getColorGrupo() + ",");
                        }
                        System.out.println("  valor: " + propiedad.getValor());
                        System.out.println("}");
                    }
                }
            }
        }

        if (!estaEnVenta) {
            System.out.println("No hay propiedades en venta actualmente.");
        }
    }

    // Listar propiedades en venta de un grupo específico
    private void listarVenta(String grupoNombre) {
        System.out.println("Propiedades en venta para grupo: " + grupoNombre);
        boolean estaEnVenta = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c instanceof Propiedad propiedad) {
                    if (propiedad.getDuenho() == banca) {
                        boolean perteneceAlGrupo = false;

                        if (propiedad instanceof Solar solar) {
                            if (solar.getGrupo().getColorGrupo().equalsIgnoreCase(grupoNombre)) {
                                perteneceAlGrupo = true;
                            }
                        } else {
                            // Transportes y servicios no tienen grupo, solo los mostramos si el nombre coincide
                            if (propiedad.getNombre().equalsIgnoreCase(grupoNombre)) {
                                perteneceAlGrupo = true;
                            }
                        }

                        if (perteneceAlGrupo) {
                            estaEnVenta = true;
                            System.out.println("{");
                            System.out.println("  nombre: " + propiedad.getNombre());
                            System.out.println("  tipo: " + propiedad.getTipo() + ",");
                            if (propiedad instanceof Solar solar) {
                                System.out.println("  grupo: " + solar.getGrupo().getColorGrupo() + ",");
                            }
                            System.out.println("  valor: " + propiedad.getValor());
                            System.out.println("}");
                        }
                    }
                }
            }
        }

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
    public void edificar(String tipo) {
        tipo = tipo.toLowerCase();
        Jugador propietario = jugadores.get(indiceJugadorActual);
        Casilla c = propietario.getAvatar().getLugar();

        if (!(c instanceof Solar casilla)) {
            System.out.println("No puedes construir en esta casilla. Solo se puede edificar en solares.");
            return;
        }

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
            case "pistaDeporte":
                coste = casilla.getValorPistaDeporte();
                break;
            default:
                System.out.println("Tipo de edificio no válido: " + tipo);
                return;
        }

        if (!casilla.posibleConstruir(tipo, propietario)) {
            System.out.println("No se cumplen los requesitos para construir el edificio que solicita en este solar\n");
        } else if (propietario.getFortuna() < coste) {
            System.out.println("La fortuna no es suficiente para construir el edificio\n");
        } else {
            if (tipo.equals("hotel")) {
                int casasEliminadas = 0;
                Iterator<Edificio> it = casilla.getEdificios().iterator();

                while(it.hasNext()) {
                    Edificio e = (Edificio)it.next();
                    if (e.getTipo().equals("casa")) {
                        it.remove();
                        propietario.eliminarEdificio(e);
                        ++casasEliminadas;
                        if (casasEliminadas == 4) {
                            break;
                        }
                    }
                }
            }

            propietario.setFortuna(propietario.getFortuna() - coste);
            propietario.sumarDineroInvertido(coste);
            Edificio nuevoEdificio;
            switch (tipo) {
                case "casa":
                    nuevoEdificio = new Casa(propietario, casilla, coste);
                    break;
                case "hotel":
                    nuevoEdificio = new Hotel(propietario, casilla, coste);
                    break;
                case "piscina":
                    nuevoEdificio = new Piscina(propietario, casilla, coste);
                    break;
                case "pistaDeporte":
                    nuevoEdificio = new PistaDeporte(propietario, casilla, coste);
                    break;
                default:
                    System.out.println("Tipo no válido.");
                    return;
            }

            propietario.anhadirEdificio(nuevoEdificio);
            casilla.anhadirEdificio(nuevoEdificio);
            System.out.printf("Nueva edificación: %s en el solar: %s. La fortuna de %s se reduce en %.2f€.%n", tipo, casilla.getNombre(), propietario.getNombre(), coste);
        }
    }

    //Método para vender los edificios
    private void venderEdificios(String tipo, String nombre, String cantidad) {
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        Casilla c = tablero.encontrar_casilla(nombre);

        if (!(c instanceof Solar casilla)) {
            System.out.println("No se puede vender edificios en esta casilla. Solo solares tienen edificios.");
            return;
        }

        // Comprobar que la casilla es propiedad del jugador
        if (casilla.getDuenho() != jugadorActual) {
            System.out.println("La casilla " + nombre + " no le pertenece al jugador.");
            return;
        }

        int cantidadPorVender = Integer.parseInt(cantidad);
        int eliminados = 0;

        // Convertir tipo plural a singular
        String tipoEdificio = switch (tipo.toLowerCase()) {
            case "casas" -> "casa";
            case "hoteles" -> "hotel";
            default -> tipo.toLowerCase();
        };

        Iterator<Edificio> iterador = jugadorActual.getEdificios().iterator();
        while (iterador.hasNext() && eliminados < cantidadPorVender) {
            Edificio e = iterador.next();
            if (e.getTipo().equalsIgnoreCase(tipoEdificio) && e.getCasilla() == casilla) {
                jugadorActual.sumarFortuna(e.getCoste());
                iterador.remove();
                casilla.eliminarEdificio(e);
                eliminados++;
            }
        }

        if (eliminados == 0) {
            System.out.println("No hay edificios de tipo " + tipo + " para vender en " + nombre + ".");
        } else {
            System.out.println("Se vendieron " + eliminados + " " + tipo + "(s) en " + nombre + ".");
        }
    }

    // Método para listar todos los edificios construidos en el tablero
    private void listarEdificios() {
        ArrayList<Edificio> todosEdificios = new ArrayList<>();

        // Recorremos todos los jugadores y acumulamos sus edificios
        for (Jugador j : jugadores) {
            todosEdificios.addAll(j.getEdificios());
        }

        if (todosEdificios.isEmpty()) {
            System.out.println("No hay edificios construidos todavía.");
            return;
        }

        // Mostramos la información de cada edificio
        for (Edificio e : todosEdificios) {
            String grupoColor = (e.getCasilla() instanceof Solar casilla && casilla.getGrupo() != null)
                    ? casilla.getGrupo().getColorGrupo()
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
                    e.getId(),
                    (e.getPropietario() != null ? e.getPropietario().getNombre() : "-"),
                    (e.getCasilla() != null ? e.getCasilla().getNombre() : "-"),
                    grupoColor,
                    e.getCoste()
            );
        }
    }

    // Método para listar todos los edificios de un grupo específico
    private void listarEdificiosGrupo(String colorGrupo) {
        colorGrupo = colorGrupo.toUpperCase();
        ArrayList<Solar> casillasGrupo = new ArrayList<>();

        // Recorremos todas las casillas del tablero y filtramos solo los solares del grupo
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c instanceof Solar casilla && casilla.getGrupo() != null && casilla.getGrupo().getColorGrupo().equals(colorGrupo)) {
                    casillasGrupo.add(casilla);
                }
            }
        }

        if (casillasGrupo.isEmpty()) {
            System.out.println("No existen edificios en este grupo o el grupo no existe.");
            return;
        }

        boolean posibleCasa = false;
        boolean posibleHotel = false;
        boolean posiblePiscina = false;
        boolean posiblePista = false;

        for (Solar casilla : casillasGrupo) {
            ArrayList<String> casa = new ArrayList<>();
            ArrayList<String> hotel = new ArrayList<>();
            ArrayList<String> piscina = new ArrayList<>();
            ArrayList<String> pista = new ArrayList<>();

            for (Edificio e : casilla.getEdificios()) {
                switch (e.getTipo().toLowerCase()) {
                    case "casa" -> casa.add(e.getId());
                    case "hotel" -> hotel.add(e.getId());
                    case "piscina" -> piscina.add(e.getId());
                    case "pista" -> pista.add(e.getId());
                }
            }

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
                    casilla.getNombre(),
                    hotel.isEmpty() ? "-" : hotel,
                    casa.isEmpty() ? "-" : casa,
                    piscina.isEmpty() ? "-" : piscina,
                    pista.isEmpty() ? "-" : pista,
                    casilla.getImpuesto()
            );

            if (casa.size() < 4) posibleCasa = true;
            if (casa.size() == 4 && hotel.isEmpty()) posibleHotel = true;
            if (hotel.size() == 1 && piscina.isEmpty()) posiblePiscina = true;
            if (hotel.size() == 1 && pista.isEmpty()) posiblePista = true;
        }

        ArrayList<String> siSePuede = new ArrayList<>();
        ArrayList<String> noSePuede = new ArrayList<>();

        if (posibleCasa) siSePuede.add("casas"); else noSePuede.add("casas");
        if (posibleHotel) siSePuede.add("hotel"); else noSePuede.add("hotel");
        if (posiblePiscina) siSePuede.add("piscina"); else noSePuede.add("piscinas");
        if (posiblePista) siSePuede.add("pistas de deporte"); else noSePuede.add("pistas de deporte");

        if (!siSePuede.isEmpty()) System.out.println("Se puede edificar " + String.join(" y ", siSePuede) + ".");
        if (!noSePuede.isEmpty()) System.out.println("No se pueden construir " + String.join(" ni ", noSePuede) + ".");
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
