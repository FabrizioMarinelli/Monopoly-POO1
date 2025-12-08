package monopoly;

import partida.Jugador;

public class Casa extends Edificio {
    private static int contador = 1;

    public Casa(Jugador propietario, Casilla casilla, float coste) {
        super(propietario, casilla, coste);
    }

    public String generarId() {
        int var10000 = contador++;
        return "Casa-" + var10000;
    }

    public String getTipo() {
        return "casa";
    }
}