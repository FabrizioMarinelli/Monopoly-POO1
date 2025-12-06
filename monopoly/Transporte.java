package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public final class Transporte extends Propiedad {
    public Transporte(String nombre, int posicion, float valor, float impuesto) {
        super(nombre, posicion, valor, impuesto);
    }

    @Override
    public boolean alquiler(Jugador jugador, int tirada) {

        // Si no tiene dueño o pertenece al mismo jugador, no paga
        if (hipotecada || duenho == null || duenho.equals(jugador)) {

            if (duenho == null) {
                System.out.println(jugador.getNombre() + " ha caído en el transporte " + nombre +
                        ". Está en venta por " + valor + "€.");
            } else if (duenho.equals(jugador)) {
                System.out.println(jugador.getNombre() + " ha caído en su propio transporte. No paga nada.");
            }
            return true;
        }

        // Calcular alquiler: suma del impuesto de todos los transportes del dueño
        float alquilerTotal = 0;
        int numTransportes = 0;

        for (Casilla c : duenho.getPropiedades()) {
            if (c instanceof Transporte) {
                alquilerTotal += ((Transporte) c).impuesto;
                numTransportes++;
            }
        }

        System.out.println(jugador.getNombre() + " ha caído en el transporte " + nombre +
                " de " + duenho.getNombre() + ". El propietario posee " +
                numTransportes + " transportes.");
        System.out.println("Debe pagar un alquiler total de " + alquilerTotal + "€.");

        // Actualizar fortunas
        jugador.sumarFortuna(-alquilerTotal);
        jugador.sumarGastos(alquilerTotal);
        duenho.sumarFortuna(alquilerTotal);

        // Estadísticas
        jugador.sumarPagoDeAlquileres(alquilerTotal);
        duenho.sumarCobroDeAlquileres(alquilerTotal);
        this.sumarDineroGenerado(alquilerTotal);

        return jugador.getFortuna() >= 0;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero,
                                  int tirada, ArrayList<Jugador> jugadores) {

        this.incrementarVisita();

        // Si no tiene dueño, se puede comprar
        if (duenho == null || duenho.equals(banca)) {
            System.out.println(actual.getNombre() + " ha caído en el transporte " + nombre +
                    ". Está en venta por " + valor + "€.");
            return true;
        }

        // Si es del propio jugador, no hay nada que hacer
        if (duenho.equals(actual)) {
            System.out.println(actual.getNombre() + " ha caído en su propio transporte. No paga nada.");
            return true;
        }

        // Si pertenece a otro jugador, pagar alquiler
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
        return "Transporte";
    }
}
