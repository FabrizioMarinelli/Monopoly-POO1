package monopoly;

import partida.Jugador;

public class PistaDeporte extends Edificio {
    private static int contador = 1;

    public PistaDeporte(Jugador propietario, Casilla casilla, float coste) {
        super(propietario, casilla, coste);
    }

    public String generarId() {
        int var10000 = contador++;
        return "PistaDeporte-" + var10000;
    }

    public String getTipo() {
        return "pistaDeporte";
    }
}