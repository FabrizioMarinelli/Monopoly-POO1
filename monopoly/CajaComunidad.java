package monopoly;

import partida.Jugador;
import java.util.ArrayList;

public class CajaComunidad extends Carta{
    public CajaComunidad(int id, String descripcion, String accion, float cantidad, String destino) {
        super(id, descripcion, accion, cantidad, destino);
    }

    @Override
    public void accion(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores) {
        System.out.println("Acción: " + descripcion);
        switch (accion.toUpperCase()) {
            case "COBRAR":
                //El jugador recibe una cantidad de dinero y se actualizan sus estadisticas
                jugador.sumarFortuna(this.cantidad);
                jugador.sumarPremiosInversionesOBote(this.cantidad);
                break;
            case "PAGAR":
                if (descripcion.contains("cada jugador")) {
                    // Pagar a cada jugador (excepto uno mismo)
                    for (Jugador j : jugadores) {
                        if (!j.equals(jugador)) {
                            //actualizamos estadistica de los que reciben el dinero
                            j.sumarFortuna(cantidad);
                            j.sumarPremiosInversionesOBote(cantidad);

                            //actualizamos estadisticas del jugador que paga
                            jugador.sumarFortuna(-cantidad);
                            jugador.sumarGastos(cantidad);
                            jugador.sumarPagoTasasEImpuestos(cantidad);
                        }
                    }
                } else {
                    // Carta normal de pagar al banco
                    jugador.sumarFortuna(-cantidad);
                    jugador.sumarGastos(cantidad);
                    jugador.sumarPagoTasasEImpuestos(cantidad);
                }
                break;
            case "MOVER":
                Casilla casillaDestino = tablero.encontrar_casilla(destino);
                // Creamos un tablero para calcular posiciones que tiene que avanzar el personaje
                ArrayList<Casilla> tableroJunto = new ArrayList<>();
                for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
                    tableroJunto.addAll(lado);
                }
                int posActual = tableroJunto.indexOf(jugador.getAvatar().getLugar());
                int posDestino = tableroJunto.indexOf(casillaDestino);
                // Calculamos cuántas casillas avanzar (considerando el ciclo del tablero)
                int avance = (posDestino - posActual + tableroJunto.size()) % tableroJunto.size();
                // Movemos usando moverAvatar, que ya gestiona pasar por Salida
                jugador.getAvatar().moverAvatar(tablero.getPosiciones(), avance);
                break;
            case "CARCEL":
                //Se mete al jugador en la carcel
                jugador.encarcelar(tablero.getPosiciones());
                break;
            case "RETROCEDER":
                //el jugador retrocede un numero de casillas
                jugador.getAvatar().moverAvatar(tablero.getPosiciones(), (int) this.cantidad);
                break;
        }
    }
}
