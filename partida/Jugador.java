package partida;

import java.util.ArrayList;

import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private boolean tiradaDisponible;
    private int tiradasRepetidas;
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private ArrayList<Casilla> hipotecas;
    private float dineroInvertido = 0;           // dinero gastado en comprar cosas
    private float pagoTasasEImpuestos = 0;       // dinero pagado por tasas/impuestos
    private float pagoDeAlquileres = 0;          // dinero pagado al caer en propiedades de otros
    private float cobroDeAlquileres = 0;         // dinero cobrado por sus propiedades
    private float pasarPorCasillaDeSalida = 0;   // dinero ganado al pasar por salida
    private float premiosInversionesOBote = 0;   // dinero ganado por premios o parking
    private int vecesEnLaCarcel = 0;             // veces que fue encarcelado
    //Creamos el ArrayList de edificios que tienen los jugadores
    private ArrayList<Edificio> edificios = new ArrayList<>();

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {

        this.nombre = "Banca";
        this.avatar = null; //La banca no tiene avatar
        this.fortuna = Float.MAX_VALUE; //la banca tiene dinero infinito
        this.enCarcel = false;
        this.gastos = 0;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.tiradaDisponible = true;
        this.tiradasRepetidas = 0;
    }

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados, ArrayList<Jugador> jugadoresCreados) {
        this.nombre = nombre;
        this.fortuna = 15000000; //Fortuna inicial 15000000
        this.enCarcel = false;
        this.gastos = 0;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new  ArrayList<>();
        this.edificios = new ArrayList<>();
        this.tiradaDisponible = true;
        this.tiradasRepetidas = 0;
        jugadoresCreados.add(this);
        //Por ultimo se crea el avatar

        this.avatar = new Avatar(tipoAvatar,this, inicio, avCreados);
    }

    //GETTERS Y SETTERS
    public boolean getTiradaDisponible(){return tiradaDisponible;}

    public void setTiradaDisponible(boolean valor){
        this.tiradaDisponible = valor;
    }

    public int getTiradasRepetidas(){return tiradasRepetidas;}

    public void setTiradasRepetidas(int valor){
        this.tiradasRepetidas = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public void setFortuna(float fortuna) {
        this.fortuna = fortuna;
    }

    public float getGastos() {
        return gastos;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public boolean isEnCarcel() {
        return this.enCarcel;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Casilla> getPropiedades() {
        return this.propiedades;
    }

    public ArrayList<Casilla> getHipotecas(){
        return this.hipotecas;
    }

    public void setPropiedades(ArrayList<Casilla> propiedades) {
        this.propiedades = propiedades;
    }

    public ArrayList<Edificio> getEdificios() {
        return this.edificios;
    }

    public float getDineroInvertido() {
        return dineroInvertido;
    }

    public void setDineroInvertido(float dineroInvertido) {
        this.dineroInvertido = dineroInvertido;
    }

    public int getVecesEnLaCarcel() {
        return vecesEnLaCarcel;
    }

    public void setVecesEnLaCarcel(int vecesEnLaCarcel) {
        this.vecesEnLaCarcel = vecesEnLaCarcel;
    }

    public float getPremiosInversionesOBote() {
        return premiosInversionesOBote;
    }

    public void setPremiosInversionesOBote(float premiosInversionesOBote) {
        this.premiosInversionesOBote = premiosInversionesOBote;
    }

    public float getPasarPorCasillaDeSalida() {
        return pasarPorCasillaDeSalida;
    }

    public void setPasarPorCasillaDeSalida(float pasarPorCasillaDeSalida) {
        this.pasarPorCasillaDeSalida = pasarPorCasillaDeSalida;
    }

    public float getCobroDeAlquileres() {
        return cobroDeAlquileres;
    }

    public void setCobroDeAlquileres(float cobroDeAlquileres) {
        this.cobroDeAlquileres = cobroDeAlquileres;
    }

    public float getPagoDeAlquileres() {
        return pagoDeAlquileres;
    }

    public void setPagoDeAlquileres(float pagoDeAlquileres) {
        this.pagoDeAlquileres = pagoDeAlquileres;
    }

    public float getPagoTasasEImpuestos() {
        return pagoTasasEImpuestos;
    }

    public void setPagoTasasEImpuestos(float pagoTasasEImpuestos) {
        this.pagoTasasEImpuestos = pagoTasasEImpuestos;
    }

    //Funciones para modificar los valores de las estadisticas
    public void sumarDineroInvertido(float cantidad) {
        dineroInvertido += cantidad;
    }

    public void sumarPagoTasasEImpuestos(float cantidad) {
        pagoTasasEImpuestos += cantidad;
    }

    public void sumarPagoDeAlquileres(float cantidad) {
        pagoDeAlquileres += cantidad;
    }

    public void sumarCobroDeAlquileres(float cantidad) {
        cobroDeAlquileres += cantidad;
    }

    public void sumarPasarPorCasillaDeSalida(float cantidad) {
        pasarPorCasillaDeSalida += cantidad;
    }

    public void sumarPremiosInversionesOBote(float cantidad) {
        premiosInversionesOBote += cantidad;
    }

    public void incrementarVecesEnCarcel() {
        vecesEnLaCarcel++;
    }


    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        //se comprueba que la casilla no pertenezca ya al jugador
        if(!propiedades.contains(casilla)){
            propiedades.add(casilla);
            casilla.    setDuenho(this);
        }

    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        //se comprueba que la propiedad pertenezca al jugador
        if(propiedades.contains(casilla)){
            propiedades.remove(casilla);
            casilla.setDuenho(null);
        }
    }

    public void anhadirHipoteca(Casilla casilla) {
        //se comprueba que la casilla no pertenezca ya al jugador
        if(!hipotecas.contains(casilla)){
            hipotecas.add(casilla);
        }

    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarHipoteca(Casilla casilla) {
        //se comprueba que la propiedad pertenezca al jugador
        hipotecas.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    /*Método para establecer al jugador en la cárcel.
     * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        Casilla carcel = null;
        //Se busca la casilla de la carcel en el tablero
        for(ArrayList<Casilla> lado : pos){
            for(Casilla c : lado){
                if(c.getNombre().equalsIgnoreCase("Carcel")){
                    //cuando se encuentra se guarda en la variable
                    carcel = c;
                    break;
                }
            }
            if(carcel != null) break;
        }

        if(carcel != null) {
            //elminamos al avatar de su casilla actual
            this.avatar.getLugar().getAvatares().remove(this.avatar);

            //Y lo movemos a la casilla de la carcel
            this.avatar.setLugar(carcel);
            carcel.getAvatares().add(this.avatar);

            //ponemos el valor de carcel a true dentro del jugador
            this.enCarcel = true;

            //Inicializamos el contador de tiradas en la carcel
            this.tiradasCarcel = 0;
            //Actualizamos las estadisticas del jugador encarcelado
            this.incrementarVecesEnCarcel();

            System.out.println(nombre + "ha sido enviado a la carcel.");

        }

    }

    //Añadimos el edificio que se acaba de construír al jugador
    public void anhadirEdificio(Edificio e) {
        edificios.add(e);
    }


    @Override
    public String toString() {
        return """
                {
                    nombre: %s
                    fortuna: %.2f
                    avatar: %s
                    propiedades: %s
                    edificios: %s
                    hipoteca: %s

                }
                """.formatted(this.nombre, this.fortuna, this.avatar.getId(), this.propiedades.isEmpty()? "-" : this.propiedades.toString(), this.edificios.isEmpty()? "-" : this.edificios.toString(),this.hipotecas.isEmpty()? "-" : this.hipotecas.toString());
    }
}