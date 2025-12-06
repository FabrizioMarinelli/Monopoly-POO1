package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class Especial extends Casilla {

    public Especial(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        switch (nombre.toLowerCase()) {
            case "ircarcel":
                System.out.println(actual.getNombre() + " ha caído en 'Ir a la cárcel'.");
                // No movemos aquí porque no tenemos el tablero, el controlador del turno se encargará
                actual.setEnCarcel(true);
                return true;

            case "carcel":
                System.out.println(actual.getNombre() + " está de visita en la cárcel.");
                return true;

            case "salida":
                System.out.println(actual.getNombre() + " ha pasado por la salida. ¡Recibe 2.000.000€!");
                actual.sumarFortuna(2_000_000f);
                actual.sumarPasarPorCasillaDeSalida(2_000_000f);
                return true;

            default:
                System.out.println(actual.getNombre() + " ha caído en una casilla especial: " + nombre);
                return true;
        }
    }

    @Override
    public String toString() {
        return "Especial: " + nombre;
    }

    @Override
    public String getTipo() {
        return "Especial";
    }
}
