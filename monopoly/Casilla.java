package monopoly;

import partida.Avatar;
import partida.Jugador;

import java.util.ArrayList;

/**
 * Clase raíz abstracta para todas las casillas.
 */
public abstract class Casilla {

    protected String nombre;
    protected int posicion;
    protected ArrayList<Avatar> avatares;
    // estadísticas básicas
    protected float dineroGenerado = 0f;
    protected int vecesPisada = 0;

    public Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
    }
    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    /** devuelve true si el avatar está en esta casilla (comparación por referencia) */
    public boolean estaAvatar(Avatar avatar) {
        return avatar != null && avatar.getLugar() == this;
    }

    /** devuelve cuántas veces se ha visitado la casilla */
    public int frecuenciaVisita() {
        return vecesPisada;
    }

    /** incrementar contador de visitas (debe llamarse cuando un avatar cae en la casilla) */
    public void incrementarVisita() {
        this.vecesPisada++;
    }

    /** sumar dinero generado por esta casilla (alquileres, pagos que recibe la casilla, etc) */
    public void sumarDineroGenerado(float cantidad) {
        this.dineroGenerado += cantidad;
    }

    public float getDineroGenerado() { return dineroGenerado; }
    public int getVecesPisada() { return vecesPisada; }

    public String getNombre() { return nombre; }
    public int getPosicion() { return posicion; }

    //cada subclase devolvera su tipo
    public abstract String getTipo();

    /** Cada subclase implementará qué ocurre cuando un jugador cae aquí.*/
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores);

    @Override
    public abstract String toString();

}
