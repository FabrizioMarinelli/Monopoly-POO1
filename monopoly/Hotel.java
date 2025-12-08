package monopoly;

import partida.Jugador;

public final class Hotel extends Edificio {
    private static int contador = 1;

    public Hotel(Jugador propietario, Casilla casilla, float coste) {
        super(propietario, casilla, coste);
    }

    public String generarId() {
        int var10000 = contador++;
        return "Hotel-" + var10000;
    }

    public String getTipo() {
        return "hotel";
    }
}