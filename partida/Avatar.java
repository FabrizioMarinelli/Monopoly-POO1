package partida;

import monopoly.*;

import java.util.ArrayList;
import java.util.Random;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
        this.id = "";
        this.tipo = "";
        this.jugador = null;
        this.lugar= null;
    }

    /*Constructor principal. Requiere éstos parámetros:
     * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
     * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;

        generarId(avCreados);    //generamos el ID unico para cada avatar

        avCreados.add(this); //se añade el avatar a la lista de avatares creados

        //Se asigna al avatar la casilla en la que se encuentra
        if(this.lugar != null){
            this.lugar.getAvatares().add(this);

        }
    }
    //GETTERS Y SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
     * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
     * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
     * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        //Vamos a meter todo el tablero en una sola lista(ordenada)
        ArrayList<Casilla> tableroJunto = new ArrayList<>();
        for(ArrayList<Casilla> lado : casillas){
            tableroJunto.addAll(lado);
        }

        //declaramos las variables necesarias
        int totalCasillas = tableroJunto.size();
        int posicionActual = tableroJunto.indexOf(this.lugar);
        int posicionNueva = (posicionActual + valorTirada) % totalCasillas;
        Casilla nuevaCasilla = tableroJunto.get(posicionNueva);

        //Comprobamos que la posicion sea correcta
        if(posicionActual == -1 || posicionActual > 40){
            System.out.println("Error: el avatar se encuentra en una casilla inválida");
            return;
        }

        //Si el jugador pasa por la casilla de salida debe recibir 2 millones y sumar una vuelta a ese jugador
        if ( posicionActual + valorTirada >= totalCasillas){
            jugador.sumarFortuna(2000000);
            jugador.setVueltas(jugador.getVueltas() + 1);
            System.out.println(jugador.getNombre() + "ha pasado por la casilla de salida, recibe 2 millones de euros");
        }
        //Quitamos el avatar de la casilla actual para moverlo a la nueva casilla
        this.lugar.getAvatares().remove(this);

        //Movemos el avatar a la nueva casilla
        this.lugar = nuevaCasilla;

        //añadimos el avatar al registro de avatares de la casilla
        nuevaCasilla.getAvatares().add(this);

        //actualizamos las estadisticas de la partida
        nuevaCasilla.incrementarVecesPisada();
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
     * El ID generado será una letra mayúscula. Parámetros:
     * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random random =  new Random();
        char nuevoId;
        boolean repetido;
        //Vamos comprobando que el nuevo id no sea igual que el de ningun avatar ya creado
        do{
            nuevoId = (char) ('A' + random.nextInt(26));
            repetido = false;

            for(Avatar a : avCreados){
                if(a.getId().equals(String.valueOf(nuevoId))){
                    repetido = true;
                    break;
                }
            }
        }while(repetido);
        //Asignamos el nuevo valor al id del nuevo avatar
        this.id = String.valueOf(nuevoId);
    }
}
