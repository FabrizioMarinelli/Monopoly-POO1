package monopoly;

import partida.Jugador;

import java.util.Objects;


public class Edificio {
    private static int contadorGlobal = 1;  //Nos permitirá generar ids unicos para los edificios
    private String id;          //Identificador del edificio/hotel/...
    private String tipo;        //Posibilidades: "casa", "hotel", "piscina", "pista_deporte"
    private Jugador propietario;
    private Casilla casilla;
    private float coste;
    private Grupo grupo;


    //Constructor
    public Edificio(String tipo, Jugador propietario, Casilla casilla, float coste){
        this.tipo = tipo;
        this.propietario = propietario;
        this.casilla = casilla;
        this.grupo = (casilla != null) ? casilla.getGrupo() : null;
        this.coste = coste;
        //Utilizaremos la variable contador para diferenciar las casas, hoteles, piscinas y pistas de otras
        //mediante su nombre + un número identificador
        this.id = tipo + "-" + contadorGlobal++;
    }

    // Getters
    public String getId(){
        return id;
    }
    public String getTipo(){
        return tipo;
    }
    public Jugador getPropietario(){
        return propietario;
    }
    public Casilla getCasilla() {
        return casilla;
    }

    public float getCoste(){
        return coste;
    }

    @Override
    public String toString() {
        return "id-edificio: %s, propietario: %s, casilla: %s, grupo: %s, coste: %.2f".formatted(this.id, this.propietario.getNombre(), this.casilla.getNombre(), (this.grupo != null) ? this.grupo.getColorGrupo() : "-", this.coste);
    }


    @Override
    public boolean equals(Object o) {
        // 1) misma referencia
        if (this == o) return true;
        // 2) null o clase distinta
        if (o == null || getClass() != o.getClass()) return false;
        // 3) casteo y comparación por id
        Edificio edificio = (Edificio) o;
        return Objects.equals(id, edificio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}