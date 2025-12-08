package monopoly;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
public class Tablero {
    // Códigos ANSI para colorear las casillas en la consola
    private static final String RESET = "\u001B[0m";
    private static final String ROJO = "\u001B[31m";
    private static final String VERDE = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String AZUL = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CIAN = "\u001B[36m";
    private static final String BLANCO = "\u001B[37m";
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists decasillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será elcolor del grupo).
    private Jugador banca; //Un jugador que será la banca.
    //atributos para las cartas
    private ArrayList<Suerte> cartasSuerte;
    private ArrayList<CajaComunidad> cartasComunidad;
    private int indiceSuerte = 0;
    private int indiceComunidad = 0;

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca; //Guarda una referencia al objeto jugador que actuará como la banca. Así se permite que el tablerosepa quien es la banca
        this.posiciones = new ArrayList<>(); //Guardará los cuatro lados del tablero
        this.grupos = new HashMap<>(); //Almacenará los diferentes gurpos de solares organizados según su color
        this.generarCasillas(); //Crea las 40 casillas llamando a las funciones insertar()
        crearGrupos();
//iniciamos las cartas
        cartasSuerte = new ArrayList<>();
        cartasComunidad = new ArrayList<>();
        cartasSuerte.add(new Suerte(1, "Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€", "MOVER", 0, "Solar19"));
        cartasSuerte.add(new Suerte(2, "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€. ", "CARCEL", 0, "Cárcel"));
        cartasSuerte.add(new Suerte(3, "¡Has ganado el bote de la lotería! Recibe 1.000.000€", "COBRAR", 1000000, null));
        cartasSuerte.add(new Suerte(4, "Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€. ", "PAGAR", 250000, null));
        cartasSuerte.add(new Suerte(5, "¡Hora punta de tráfico! Retrocede tres casillas.", "RETROCEDER", -3, null));
        cartasSuerte.add(new Suerte(6, "Te multan por usar el móvil mientras conduces. Paga 150.000€.", "PAGAR", 150000, null));
        cartasSuerte.add(new Suerte(7, "Avanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada.", "MOVER", 0, "Transporte"));
        cartasComunidad.add(new CajaComunidad(1, "Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.", "PAGAR", 500000, null));
        cartasComunidad.add(new CajaComunidad(2, "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.", "CARCEL", 0, "Cárcel"));
        cartasComunidad.add(new CajaComunidad(3, "Colócate en la casilla de Salida. Cobra 2.000.000€..", "MOVER", 0, "Salida"));
        cartasComunidad.add(new CajaComunidad(4, "Devolución de Hacienda. Cobra 500.000€.", "COBRAR", 500000, null));
        cartasComunidad.add(new CajaComunidad(5, "Retrocede hasta Solar1 para comprar antigüedades exóticas.", "RETROCEDER", 0, "Solar1"));
        cartasComunidad.add(new CajaComunidad(6, "Ve a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.", "MOVER", 0, "Solar20"));
    }

    //Funcion para inicializar los grupos
    private void crearGrupos() {
        // Creamos listas de propiedades para cada grupo
        ArrayList<Propiedad> blanco = new ArrayList<>();
        blanco.add((Propiedad) encontrar_casilla("Solar1"));
        blanco.add((Propiedad) encontrar_casilla("Solar2"));

        ArrayList<Propiedad> cian = new ArrayList<>();
        cian.add((Propiedad) encontrar_casilla("Solar3"));
        cian.add((Propiedad) encontrar_casilla("Solar4"));
        cian.add((Propiedad) encontrar_casilla("Solar5"));

        ArrayList<Propiedad> magenta = new ArrayList<>();
        magenta.add((Propiedad) encontrar_casilla("Solar6"));
        magenta.add((Propiedad) encontrar_casilla("Solar7"));
        magenta.add((Propiedad) encontrar_casilla("Solar8"));

        ArrayList<Propiedad> naranja = new ArrayList<>();
        naranja.add((Propiedad) encontrar_casilla("Solar9"));
        naranja.add((Propiedad) encontrar_casilla("Solar10"));
        naranja.add((Propiedad) encontrar_casilla("Solar11"));

        ArrayList<Propiedad> rojo = new ArrayList<>();
        rojo.add((Propiedad) encontrar_casilla("Solar12"));
        rojo.add((Propiedad) encontrar_casilla("Solar13"));
        rojo.add((Propiedad) encontrar_casilla("Solar14"));

        ArrayList<Propiedad> amarillo = new ArrayList<>();
        amarillo.add((Propiedad) encontrar_casilla("Solar15"));
        amarillo.add((Propiedad) encontrar_casilla("Solar16"));
        amarillo.add((Propiedad) encontrar_casilla("Solar17"));

        ArrayList<Propiedad> verde = new ArrayList<>();
        verde.add((Propiedad) encontrar_casilla("Solar18"));
        verde.add((Propiedad) encontrar_casilla("Solar19"));
        verde.add((Propiedad) encontrar_casilla("Solar20"));

        ArrayList<Propiedad> azul = new ArrayList<>();
        azul.add((Propiedad) encontrar_casilla("Solar21"));
        azul.add((Propiedad) encontrar_casilla("Solar22"));

        // Creamos los grupos pasando las listas de propiedades
        Grupo g1 = new Grupo("BLANCO", blanco);
        Grupo g2 = new Grupo("CIAN", cian);
        Grupo g3 = new Grupo("MAGENTA", magenta);
        Grupo g4 = new Grupo("NARANJA", naranja);
        Grupo g5 = new Grupo("ROJO", rojo);
        Grupo g6 = new Grupo("AMARILLO", amarillo);
        Grupo g7 = new Grupo("VERDE", verde);
        Grupo g8 = new Grupo("AZUL", azul);

        // Guardar en el HashMap
        grupos.put("BLANCO", g1);
        grupos.put("CIAN", g2);
        grupos.put("MAGENTA", g3);
        grupos.put("NARANJA", g4);
        grupos.put("ROJO", g5);
        grupos.put("AMARILLO", g6);
        grupos.put("VERDE", g7);
        grupos.put("AZUL", g8);

        // Asignar el grupo a cada propiedad
        for (Grupo g : grupos.values()) {
            for (Propiedad p : g.getPropiedades()) {
                p.setGrupo(g);
            }
        }
    }

    //Getter para la posiciones
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }
    //Getter para los grupos
    //Es un paquete privado, para no exponer la variable grupos que es privada
    ArrayList<Grupo> getGrupos() {
        return new ArrayList<>(grupos.values());
    }

    //Método para colorear las casillas
    private String colorearCasilla(Casilla c) {
        String color = RESET;
//En funcion de lo que obtenga la cadena será un color u otro
        if (c.getTipo().equalsIgnoreCase("solar")) {
//Como en solares en funcion del número de idendtificacion se utilia un color u otro utilizamos un switch
            switch (c.getNombre().toLowerCase()) {
                case "solar1":
                case "solar2":
                    color = BLANCO;
                    break;
                case "solar3":
                case "solar4":
                case "solar5":
                    color = CIAN;
                    break;
                case "solar6":
                case "solar7":
                case "solar8":
                    color = MAGENTA;
                    break;
                case "solar9":
                case "solar10":
                case "solar11":
                    color = "\u001B[38;5;208m"; //Naranja
                    break;
                case "solar12":
                case "solar13":
                case "solar14":
                    color = ROJO;
                    break;
                case "solar15":
                case "solar16":
                case "solar17":
                    color = AMARILLO;
                    break;
                case "solar18":
                case "solar19":
                case "solar20":
                    color = VERDE;
                    break;
                case "solar21":
                case "solar22":
                    color = AZUL;
                    break;
                default:
                    color = RESET;
            }
        } else if (c.getTipo().equalsIgnoreCase("suerte")) {
            color = BLANCO;
        } else if (c.getTipo().equalsIgnoreCase("comunidad")) {
            color = BLANCO;
        } else if (c.getTipo().equalsIgnoreCase("servicio")) {
            color = BLANCO;
        } else if (c.getTipo().equalsIgnoreCase("transporte")) {
            color = BLANCO;
        } else if (c.getTipo().equalsIgnoreCase("especial")) {
            color = BLANCO;
        }
        int tamano = c.getNombre().length(); //Lo usaremos para ajustar el espacio de todas las casillas al mismo tamaño
//Ponemos RESET porque no queremos que se siga escribiendo en el color que estamos utilizando en consola
        return color + c.getNombre() + " ".repeat(10 - tamano) + RESET;
    }

    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        ladoNorte.add(new Parking("Parking", 21, 0f));
        ladoNorte.add(new Solar("Solar12", 22, 2_200_000f, 180_000f, 1_500_000f, 1_500_000f, 300_000f, 300_000f, 2_000_000f, 10_500_000f, 2_100_000f,  2_100_000f,  1_100_000f));
        ladoNorte.add(new CasillaSuerte("Suerte2", 23));
        ladoNorte.add(new Solar("Solar13", 24, 2_200_000f, 180_000f, 1_500_000f, 1_500_000f, 300_000f, 600_000f, 2_200_000f, 10_500_000f, 2_100_000f, 2_100_000f, 1_100_000f));
        ladoNorte.add(new Solar("Solar14", 25, 2_400_000f, 200_000f, 1_500_000f, 1_500_000f, 300_000f, 600_000f, 2_325_000f, 11_000_000f, 2_200_000f, 2_200_000f, 1_200_000f));
        ladoNorte.add(new Transporte("Trans3", 26, 500_000f, 250_000f));
        ladoNorte.add(new Solar("Solar15", 27, 2_600_000f, 220_000f, 1_500_000f, 1_500_000f, 300_000f, 600_000f, 2_450_000f, 11_500_000f, 2_300_000f, 2_300_000f, 1_300_000f));
        ladoNorte.add(new Solar("Solar16", 28, 2_600_000f, 220_000f, 1_500_000f, 1_500_000f, 300_000f, 600_000f, 2_450_000f, 11_500_000f, 2_300_000f, 2_300_000f, 1_300_000f));
        ladoNorte.add(new Servicio("Serv2", 29, 500_000f, 50_000f));
        ladoNorte.add(new Solar("Solar17", 30, 2_800_000f, 240_000f, 1_500_000f, 1_500_000f, 300_000f, 600_000f, 2_600_000f, 12_000_000f, 2_400_000f, 2_400_000f, 1_400_000f));
        ladoNorte.add(new Especial("IrCarcel", 31));

        posiciones.add(ladoNorte);
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>();
        ladoSur.add(new Especial("Salida", 1));
        ladoSur.add(new Solar("Solar1", 2, 600_000f, 20_000f, 500_000f, 500_000f, 100_000f, 200_000f, 400_000f, 2_500_000f, 500_000f, 500_000f, 300_000f));
        ladoSur.add(new CasillaComunidad("Caja1", 3));
        ladoSur.add(new Solar("Solar2", 4, 600_000f, 40_000f, 500_000f, 500_000f, 100_000f, 200_000f, 800_000f, 4_500_000f, 900_000f, 900_000f, 300_000f));
        ladoSur.add(new Impuesto("Imp1", 5, 2_000_000f));
        ladoSur.add(new Transporte("Trans1", 6, 500_000f, 250_000f));
        ladoSur.add(new Solar("Solar3", 7, 1_000_000f, 60_000f, 500_000f, 500_000f, 100_000f, 200_000f, 1_000_000f, 5_500_000f, 1_100_000f, 1_100_000f, 500_000f));
        ladoSur.add(new CasillaSuerte("Suerte1", 8));
        ladoSur.add(new Solar("Solar4", 9, 60_000f, 500_000f, 500_000f, 500_000f, 100_000f, 200_000f, 1_000_000f, 5_500_000f, 1_100_000f, 1_100_000f, 500_000f));
        ladoSur.add(new Solar("Solar5", 10, 80_000f, 500_000f, 500_000f, 500_000f, 100_000f, 200_000f, 1_250_000f, 6_000_000f, 1_200_000f, 1_200_000f, 600_000f));
        ladoSur.add(new Especial("Carcel", 11));

        posiciones.add(ladoSur);
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        ladoOeste.add(new Solar("Solar6", 12, 1_400_000f, 100_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 1_500_000f, 7_500_000f, 1_500_000f, 1_500_000f, 700_000f));
        ladoOeste.add(new Solar("Solar7", 13, 1_400_000f, 100_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 1_500_000f, 7_500_000f, 1_500_000f, 1_500_000f, 700_000f));
        ladoOeste.add(new Servicio("Serv1", 14, 500_000f, 50_000f));
        ladoOeste.add(new Solar("Solar8", 15, 1_600_000f, 120_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 1_750_000f, 9_000_000f, 1_800_000f, 1_800_000f, 800_000f));
        ladoOeste.add(new Solar("Solar9", 16, 1_800_000f, 140_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 1_850_000f, 9_500_000f, 1_900_000f, 1_900_000f, 900_000f));
        ladoOeste.add(new Transporte("Trans2", 17, 500_000f, 250_000f));
        ladoOeste.add(new Solar("Solar10", 18, 1_800_000f, 140_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 1_850_000f, 9_500_000f, 1_900_000f, 1_900_000f, 900_000f));
        ladoOeste.add(new CasillaComunidad("Caja2", 19));
        ladoOeste.add(new Solar("Solar11", 20, 2_200_000f, 160_000f, 1_000_000f, 1_000_000f, 200_000f, 400_000f, 2_000_000f, 10_000_000f, 2_000_000f, 2_000_000f, 1_000_000f));
        posiciones.add(ladoOeste);
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();
        ladoEste.add(new Solar("Solar18", 32, 3_000_000f, 260_000f, 2_000_000f, 2_000_000f, 400_000f, 800_000f, 2_750_000f, 12_750_000f, 2_550_000f, 2_550_000f, 1_500_000f));
        ladoEste.add(new Solar("Solar19", 33, 3_000_000f, 260_000f, 2_000_000f, 2_000_000f, 400_000f, 800_000f, 2_750_000f, 12_750_000f, 2_550_000f, 2_550_000f, 1_500_000f));
        ladoEste.add(new CasillaSuerte("Caja3", 34));
        ladoEste.add(new Solar("Solar20", 35, 3_200_000f, 280_000f, 2_000_000f, 2_000_000f, 400_000f, 800_000f, 3_000_000f, 14_000_000f, 2_800_000f, 2_800_000f, 1_600_000f));
        ladoEste.add(new Transporte("Trans4", 36, 500_000f, 250_000f));
        ladoEste.add(new Solar("Solar21", 37, 3_500_000f, 350_000f, 2_000_000f, 2_000_000f, 400_000f, 800_000f, 3_250_000f, 17_000_000f, 3_400_000f, 3_400_000f, 1_750_000f));
        ladoEste.add(new CasillaSuerte("Suerte3", 38));
        ladoEste.add(new Impuesto("Imp2", 39, 2_000_000f));
        ladoEste.add(new Solar("Solar22", 40, 4_000_000f, 500_000f, 2_000_000f, 2_000_000f, 400_000f, 800_000f, 4_250_000f, 20_000_000f, 4_000_000f, 4_000_000f, 2_000_000f));
        posiciones.add(ladoEste);
    }

    //Método que devuelve un texto con el nombre de la casilla y la incial del jugador que se encuentra en ella
//La hacemos private porque solo la vamos a utilizar dentro de la clase en la que la acabamos de definir
    private String mostrarAvatares(Casilla c) {
        StringBuilder sb = new StringBuilder();
        // Si hay avatares en la casilla, mostramos sus iniciales junto un '&' como se muestra en el .pdf
        if (c.getAvatares() != null && !c.getAvatares().isEmpty()) {
            sb.append(" &");
            for (Avatar a : c.getAvatares()) {
                //Comprobamos que existan avatares en ese momento en la casilla
                if (c.getAvatares() != null && !c.getAvatares().isEmpty()) {
                    //Imprimimos el identificador del avatar
                    sb.append(a.getId());
                }
            }
        }
        //Establecemos una longitud fija
        String texto = sb.toString();
        int longitud = 6;
        if (texto.length() < longitud) {
//Rellenamos con espacios a la derecha
            texto += " ".repeat(longitud - texto.length());
        } else if (texto.length() > longitud) {
//Si se pasa de tamaño lo recortamos
            texto = texto.substring(0, longitud);
        }
        return texto;
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); //StringBuilder para construir de manera eficiente el texto

        //Obtenemos las listas de las casillas de los diferentes lados del tablero
        ArrayList<Casilla> norte = posiciones.get(2);
        ArrayList<Casilla> sur = posiciones.get(0);
        ArrayList<Casilla> este = posiciones.get(3);
        ArrayList<Casilla> oeste = posiciones.get(1);

        //Imprimimos el lado norte
        for(int i = 0; i < norte.size(); i++){
            //Imprimimos la casilla del lado izquiero con sus barras laterales
            sb.append("|" + colorearCasilla(norte.get(i)) + mostrarAvatares(norte.get(i)));
        }
        sb.append("|\n");   //Cuando se termine queremos que haga un salto de línea para que comience con los laterales

        //Imprimir los lados de los lados
        //En el for se imprimirá cada línea del medio del tablero
        for(int e = 0; e < este.size(); e++){
            int o = oeste.size() - 1 - e;
            //Imprimimos la casilla del lado izquiero con sus barras laterales
            sb.append("|" + colorearCasilla(oeste.get(o)) + mostrarAvatares(oeste.get(o)) + "|");

            //Metemos más espacios en el medio para que el tablero tenga forma rectangular
            sb.append(" ".repeat(17*9-1));

            //Imprimimos la casilla de la derecha
            if (e < este.size()){
                sb.append("|" + colorearCasilla(este.get(e)) + mostrarAvatares(este.get(e)) + "|");
            }
            //Imprimimos el salto de línea para comenzar a realizar la línea inferior
            sb.append("\n");
        }
        //Imprimimos el lado sur del revés para que se pueda cerrar el rectángulo
        //Si no las casillas saldrían en un orden inverso
        for(int i = sur.size() - 1; i >= 0 ; i--){
            //Imprimimos la casilla del lado izquiero con sus barras laterales
            sb.append("|" + colorearCasilla(sur.get(i)) + mostrarAvatares(sur.get(i)));
        }
        sb.append("|\n");
        return sb.toString();
    }

    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        //Recorremos las listas
        for (ArrayList<Casilla> lado : this.posiciones){
            for (Casilla c : lado){
                //Devolvemos cada casilla
                if (c.getNombre().equalsIgnoreCase(nombre)){
                    return c;
                }
            }
        }
        return null;
    }


    //Método para calcular la siguiente carta de Suerte
    public Suerte siguienteCartaSuerte() {
        Suerte carta = null;
        //Buscamos cual es el tipo de la carta para poder calcular el indice que corresponda
        carta = cartasSuerte.get(indiceSuerte);
        indiceSuerte = (indiceSuerte + 1) % cartasSuerte.size(); //de este modo avanza de manera circular

        //Devolvemos la carta
        return carta;
    }

    //Método para calcular la siguiente carta de Caja de Comunidad
    public CajaComunidad siguienteCartaComunidad() {
        CajaComunidad carta = null;
        //Buscamos cual es el tipo de la carta para poder calcular el indice que corresponda
        carta = cartasComunidad.get(indiceComunidad);
        indiceComunidad = (indiceComunidad + 1) % cartasComunidad.size(); //de este modo avanza de manera circular

        //Devolvemos la carta
        return carta;
    }

}

