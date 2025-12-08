package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public abstract class Accion extends Casilla {

    public Accion(String nombre, int posicion) {
        super(nombre, posicion);
    }

    // Cada acción concreta definirá lo que pasa cuando un jugador cae en ella
    @Override
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores);

}
