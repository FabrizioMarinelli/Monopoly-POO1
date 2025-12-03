package monopoly;

import partida.Jugador;

import java.util.ArrayList;

public abstract class Carta {
    protected int id;
    protected String descripcion;
    protected float cantidad;
    protected String destino;
    protected String accion;  // nombre de la casilla destino (si aplica)
    public Carta(int id, String descripcion, String accion, float cantidad, String destino) {
        this.id = id;
        this.descripcion = descripcion;
        this.accion = accion;
        this.cantidad = cantidad;
        this.destino = destino;
    }
    // getters
    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getAccion() { return accion; }
    public float getCantidad() { return cantidad; }
    public String getDestino() { return destino; }
    public abstract void accion(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores);
}