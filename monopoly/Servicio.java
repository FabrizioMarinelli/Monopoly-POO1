package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class Servicio extends Propiedad {

    public Servicio(String nombre, int posicion, float valor, float impuesto) {
        super(nombre, posicion, valor, impuesto);
    }

    @Override
    public boolean alquiler(Jugador jugador, int tirada) {
        // No paga: si hipotecada, dueño es null o es el mismo jugador
        if (hipotecada || duenho == null || duenho.equals(jugador)) return true;

        // Contar servicios del dueño
        int numServicios = 0;
        for (Casilla c : duenho.getPropiedades()) {
            if (c instanceof Servicio) numServicios++;
        }

        int multiplicador = (numServicios == 2) ? 10 : 4;
        float total = multiplicador * impuesto * tirada;

        jugador.sumarFortuna(-total);
        jugador.sumarGastos(total);
        duenho.sumarFortuna(total);
        // Estadísticas entre jugadores
        jugador.sumarPagoDeAlquileres(total);
        duenho.sumarCobroDeAlquileres(total);


        System.out.println(jugador.getNombre() + " paga " + total +
                "€ de servicio a " + duenho.getNombre());

        this.sumarDineroGenerado(total);
        return jugador.getFortuna() >= 0;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero,
                                  int tirada, ArrayList<Jugador> jugadores) {

        if (duenho == null || duenho.equals(banca)) {
            System.out.println(actual.getNombre() +
                    " ha caído en " + nombre + ", está en venta por " + valor + "€.");
            return true;
        }

        if (duenho.equals(actual)) {
            System.out.println(actual.getNombre() +
                    " ha caído en su propio servicio, no paga nada.");
            return true;
        }

        return alquiler(actual, tirada);
    }

    @Override
    public float valor() {
        return valor;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public String getTipo() {
        return "Servicio";
    }
}
