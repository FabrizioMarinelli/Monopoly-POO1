package monopoly;

import partida.Avatar;
import partida.Jugador;

import java.util.ArrayList;

public abstract class Propiedad extends Casilla {

    protected Jugador duenho;
    protected float valor;
    protected float impuesto; // lo usamos como alquiler base
    protected boolean hipotecada = false;
    protected Grupo grupo;

    public Propiedad(String nombre, int posicion, float valor, float impuesto) {
        super(nombre, posicion);
        this.valor = valor;
        this.impuesto = impuesto;
        this.duenho = null; // al principio propiedad de la banca
    }

    public float getValor() {
        return valor;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    // Setter para asignar grupo
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    // Getter para obtener el grupo
    public Grupo getGrupo() {
        return grupo;
    }

    // métodos obligatorios
    public boolean perteneceAJugador(Jugador jugador) {
        return this.duenho != null && this.duenho.equals(jugador);
    }

    public void comprar(Jugador jugador, Jugador banca) {
        if (duenho == null || duenho.equals(banca)) {
            if (jugador.getFortuna() >= valor) {
                jugador.sumarFortuna(-valor);
                jugador.sumarGastos(valor);
                banca.sumarFortuna(valor);
                jugador.sumarDineroInvertido(valor);

                this.duenho = jugador;
                jugador.anhadirPropiedad(this);

                System.out.println(jugador.getNombre() + " ha comprado " + nombre + " por " + valor + "€.");
            } else {
                System.out.println(jugador.getNombre() + " no tiene suficiente dinero para comprar " + nombre + ".");
            }
        } else {
            System.out.println(nombre + " ya tiene dueño: " + duenho.getNombre());
        }
    }

    // cada subclase implementará el alquiler y valor real
    public abstract boolean alquiler(Jugador jugador, int tirada);
    public abstract float valor();

    public Jugador getDuenho() {
        return duenho;
    }

    public boolean isHipotecada() {
        return hipotecada;
    }

    public void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
}
