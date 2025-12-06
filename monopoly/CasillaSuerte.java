package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class CasillaSuerte extends Accion {

    public CasillaSuerte(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        System.out.println(actual.getNombre() + " ha caído en una casilla de Suerte.");
        Suerte carta = tablero.siguienteCartaSuerte();
        carta.accion(actual, tablero, jugadores);
        return true; // asumimos que la acción de la carta no quiebra al jugador directamente
    }

    @Override
    public String toString() {
        return "Suerte: " + nombre;
    }

    @Override
    public String getTipo() {
        return "Suerte";
    }
}
