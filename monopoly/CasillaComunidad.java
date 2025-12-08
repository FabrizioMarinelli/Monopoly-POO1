package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class CasillaComunidad extends Accion {

    public CasillaComunidad(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        System.out.println(actual.getNombre() + " ha caído en una casilla de Comunidad.");
        CajaComunidad carta = tablero.siguienteCartaComunidad();
        carta.accion(actual, tablero, jugadores);
        return true;
    }

    @Override
    public String toString() {
        return "Caja de Comunidad: " + nombre;
    }

    @Override
    public String getTipo() {
        return "CajaComunidad";
    }
}
