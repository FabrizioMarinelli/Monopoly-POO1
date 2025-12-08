package monopoly;

import partida.Jugador;

public class Piscina extends Edificio {
    private static int contador = 1;

    public Piscina(Jugador propietario, Casilla casilla, float coste) {
        super(propietario, casilla, coste);
    }

    public String generarId() {
        int var10000 = contador++;
        return "Piscina-" + var10000;
    }

    public String getTipo() {
        return "piscina";
    }
}