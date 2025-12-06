package monopoly;

import partida.Jugador;
import java.util.ArrayList;

/**
 * Clase Grupo: representa un grupo de propiedades del mismo color.
 * Es una casilla en el tablero, pero funciona como contenedor de propiedades.
 */
public final class Grupo extends Casilla {

    private String colorGrupo;
    private ArrayList<Propiedad> miembros; // Relación de composición

    public Grupo(String colorGrupo) {
        super("Grupo " + colorGrupo, -1); // Posición -1 porque un grupo no es casilla física en el tablero
        this.colorGrupo = colorGrupo;
        this.miembros = new ArrayList<>();
    }

    // Constructor con lista de propiedades
    public Grupo(String colorGrupo, ArrayList<Propiedad> propiedades) {
        super("Grupo " + colorGrupo, -1);
        this.colorGrupo = colorGrupo;
        this.miembros = new ArrayList<>(propiedades);
    }

    /** Añade una propiedad al grupo */
    public void anhadirPropiedad(Propiedad propiedad) {
        if (propiedad != null && !miembros.contains(propiedad)) {
            miembros.add(propiedad);
        }
    }

    /** Comprueba si el jugador posee todas las propiedades del grupo */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Propiedad p : miembros) {
            if (!p.perteneceAJugador(jugador)) return false;
        }
        return true;
    }

    /** Devuelve la lista de propiedades del grupo */
    public ArrayList<Propiedad> getPropiedades() {
        return miembros;
    }

    public String getColorGrupo() {
        return colorGrupo;
    }

    /** Devuelve el dinero total generado por todas las propiedades del grupo */
    public float getDineroGenerado() {
        float total = 0;
        for (Propiedad p : miembros) {
            total += p.getDineroGenerado();
        }
        return total;
    }
    //Getter de las casillas del grupo
    public ArrayList<Propiedad> getMiembros() {
        return miembros;
    }

    public void setMiembros(ArrayList<Propiedad> miembros) {
        this.miembros = miembros;
    }

    // Implementación del método abstracto de Casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {
        // Como un grupo no es una casilla concreta, no se paga nada al caer en él
        System.out.println(actual.getNombre() + " ha caído en el grupo " + colorGrupo + ". Esto solo representa un conjunto de propiedades.");
        return true;
    }

    @Override
    public String toString() {
        return "Grupo " + colorGrupo + " con " + miembros.size() + " propiedades.";
    }
    @Override
    public String getTipo() {
        return "Grupo";
    }
}
