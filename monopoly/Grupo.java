package monopoly;

import partida.*;
import java.util.ArrayList;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Constructor vacío.
    public Grupo() {
        miembros = new ArrayList<>();
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
     * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.miembros.add(cas1);
        this.miembros.add(cas2);
        this.numCasillas = 2;
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
     * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.miembros.add(cas1);
        this.miembros.add(cas2);
        this.miembros.add(cas3);
        this.numCasillas = 3;
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
     * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        if(miembro != null && miembros.contains(miembro)) {
            miembros.add(miembro);
            numCasillas++;
        }
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
     * Parámetro: jugador que se quiere evaluar.
     * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for(Casilla casilla : miembros) {
            if(casilla.getDuenho() == null ||  !casilla.getDuenho().equals(jugador)) {
                return false;
            }
        }
        return true;
    }

    //Getter del color del grupo
    public String getColorGrupo(){
        return colorGrupo;

    }

    //Getter de las casillas del grupo
    public ArrayList<Casilla> getMiembros() {
        return miembros;
    }

    //Método para saber el dinero generado por un grupo
    public float getDineroGenerado() {
        float total = 0;
        for (Casilla c : miembros) {
            total += c.getDineroGenerado();   // ya lo tienes implementado en Casilla
        }
        return total;
    }
}

