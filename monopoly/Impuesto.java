package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class Impuesto extends Casilla {

    private float impuesto; // cantidad que se debe pagar al caer aquí

    public Impuesto(String nombre, int posicion, float impuesto) {
        super(nombre, posicion);
        this.impuesto = impuesto;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        System.out.println(actual.getNombre() + " ha caído en la casilla de impuesto: " + nombre + ".");
        System.out.println("Debe pagar " + impuesto + "€.");

        // Descontamos la fortuna del jugador y sumamos a los gastos
        actual.sumarFortuna(-impuesto);
        actual.sumarGastos(impuesto);

        // Se lo damos a la banca
        banca.sumarFortuna(impuesto);

        // Actualizamos estadísticas del jugador
        actual.sumarPagoTasasEImpuestos(impuesto);

        // Retornamos true si el jugador aún tiene fortuna suficiente
        return actual.getFortuna() >= 0;
    }

    @Override
    public String toString() {
        return "Impuesto: " + nombre + " (" + impuesto + "€)";
    }

    @Override
    public String getTipo() {
        return "Impuesto";
    }
}
