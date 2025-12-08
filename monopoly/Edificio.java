package monopoly;

import java.util.Objects;
import partida.Jugador;

public abstract class Edificio {
    protected String id;
    protected Jugador propietario;
    protected Casilla casilla;
    protected float coste;
    protected Grupo grupo;

    public Edificio(Jugador propietario, Casilla casilla, float coste) {
        this.propietario = propietario;
        this.casilla = casilla;
        this.coste = coste;
        if (casilla instanceof Solar s) {   //tenemos que comprobar que la casilla sea un solar para poder asignarle un grupo, si no este sera null
            this.grupo = s.getGrupo();
        } else {
            this.grupo = null;
        }
        this.id = this.generarId();
    }

    protected abstract String generarId();

    public abstract String getTipo();

    public String getId() {
        return this.id;
    }

    public Jugador getPropietario() {
        return this.propietario;
    }

    public Casilla getCasilla() {
        return this.casilla;
    }

    public float getCoste() {
        return this.coste;
    }

    public Grupo getGrupo() {
        return this.grupo;
    }

    public void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    public String toString() {
        return "id-edificio: %s, propietario: %s, casilla: %s, grupo: %s, coste: %.2f".formatted(this.id, this.propietario.getNombre(), this.casilla.getNombre(), this.grupo != null ? this.grupo.getColorGrupo() : "-", this.coste);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Edificio edificio = (Edificio)o;
            return Objects.equals(this.id, edificio.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }
}