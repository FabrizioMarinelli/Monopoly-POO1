package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class Parking extends Accion {

    private float bote; // Valor del bote que puede incrementarse con impuestos

    public Parking(String nombre, int posicion, float bote) {
        super(nombre, posicion);
        this.bote = bote;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        System.out.println(actual.getNombre() + " ha caído en Parking. Recibe " + bote + "€ del bote.");
        actual.sumarFortuna(bote);
        actual.sumarPremiosInversionesOBote(bote);
        this.bote = 0; // bote se vacía
        return true;
    }

    @Override
    public String toString() {
        return "Parking: " + nombre;
    }

    public void agregarAlBote(float cantidad) {
        this.bote += cantidad;
    }

    public float getBote() {
        return bote;
    }

    @Override
    public String getTipo() {
        return "Parking";
    }
}
