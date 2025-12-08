package monopoly;

import partida.Jugador;

import java.util.ArrayList;

public final class Solar extends Propiedad {

    private Grupo grupo;
    private ArrayList<Edificio> edificios = new ArrayList<>();
    private float valorCasa, valorHotel, valorPiscina, valorPistaDeporte;
    private float alquilerCasa, alquilerHotel, alquilerPiscina, alquilerPistaDeporte, hipoteca;
    private int casas = 0;
    private boolean hotel = false;
    private boolean piscina = false;
    private boolean pista = false;
    private boolean propiedadHipotecada = false;

    public Solar(String nombre, int posicion, float valor, float impuesto,
                 float valorCasa, float valorHotel, float valorPiscina, float valorPistaDeporte,
                 float alquilerCasa, float alquilerHotel, float alquilerPiscina, float alquilerPistaDeporte, float hipoteca) {
        super(nombre, posicion, valor, impuesto);
        this.valorCasa = valorCasa;
        this.valorHotel = valorHotel;
        this.valorPiscina = valorPiscina;
        this.valorPistaDeporte = valorPistaDeporte;
        this.alquilerCasa = alquilerCasa;
        this.alquilerHotel = alquilerHotel;
        this.alquilerPiscina = alquilerPiscina;
        this.alquilerPistaDeporte = alquilerPistaDeporte;
        this.hipoteca = hipoteca;
    }

    public boolean getPropiedadHipotecada() {
        return propiedadHipotecada;
    }

    public void setPropiedadHipotecada(boolean propiedadHipotecada) {
        this.propiedadHipotecada = propiedadHipotecada;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public void setEdificios(ArrayList<Edificio> edificios) {
        this.edificios = edificios;
    }

    //Getters de los valores de los edificios
    public float getValorCasa() {
        return valorCasa;
    }

    public float getValorHotel() {
        return valorHotel;
    }

    public float getValorPiscina() {
        return valorPiscina;
    }

    public float getValorPistaDeporte() {
        return valorPistaDeporte;
    }


    @Override
    public boolean alquiler(Jugador jugador, int tirada) {
        if (hipotecada || duenho == null || duenho.equals(jugador)) return true; // no se paga porque la propiedad pertenece al jugador
        float total = impuesto + casas * alquilerCasa;
        if (hotel) total += alquilerHotel;
        if (piscina) total += alquilerPiscina;
        if (pista) total += alquilerPistaDeporte;

        jugador.sumarFortuna(-total);
        jugador.sumarGastos(total);
        duenho.sumarFortuna(total);
        // Estadísticas entre jugadores
        jugador.sumarPagoDeAlquileres(total);
        duenho.sumarCobroDeAlquileres(total);

        System.out.println(jugador.getNombre() + " paga " + total + "€ de alquiler a " + duenho.getNombre());
        this.sumarDineroGenerado(total);
        return jugador.getFortuna() >= 0;
    }

    @Override
    public float valor() {
        return valor;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, int tirada, java.util.ArrayList<Jugador> jugadores) {
        if (duenho == null || duenho.equals(banca)) {
            System.out.println(actual.getNombre() + " ha caído en " + nombre + ", está en venta por " + valor + "€.");
            return true;
        }
        if (duenho.equals(actual)) {
            System.out.println(actual.getNombre() + " ha caído en su propia propiedad, no paga nada.");
            return true;
        }
        return alquiler(actual, tirada);
    }

    // Métodos de construcción de edificios
    public final boolean posibleConstruir(String tipo, Jugador jugador) {
        if (getDuenho() == null || !getDuenho().equals(jugador)) {
            System.out.println("No puedes construir en una propiedad que no te pertenece.");
            return false;
        }
        if (hipotecada) {
            System.out.println("No puedes construir en una propiedad hipotecada.");
            return false;
        }

        int casas = 0, hoteles = 0, piscinas = 0, pistas = 0;
        for (Edificio e : edificios) {
            switch (e.getTipo().toLowerCase()) {
                case "casa": casas++; break;
                case "hotel": hoteles++; break;
                case "piscina": piscinas++; break;
                case "pista_deporte": pistas++; break;
            }
        }

        switch (tipo.toLowerCase()) {
            case "casa": return casas < 4;
            case "hotel": return casas >= 4 && hoteles == 0;
            case "piscina": return hoteles > 0 && piscinas == 0;
            case "pista_deporte": return hoteles > 0 && pistas == 0;
            default: return false;
        }
    }

    public boolean estaHipotecada() {
        return hipotecada;
    }

    public void hipotecar() {
        this.hipotecada = true;
    }


    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla(String nombre) {
        //Comprobamos que la casilla de la cual nos piden datos es un Solar
        //La función equalsIgnoreCase() funciona igual que equals() menos porque omite la diferencia entre mayúsculas y minúsculas
        //No es necesario que se realice ninguna modificación en la implementación de la funcion equalsIgnoreCase(),
        //pues en este caso solo estamos comparando texto, no estamos comparando objetos.

        StringBuilder sb = new StringBuilder();

        //Información del tipo, solo se imprime inforacion de los solares
        sb.append("Tipo: Solar").append("\n");

        //Información del grupo/color
        if(grupo != null){
            sb.append("Grupo: ").append(grupo.getColorGrupo()).append("\n");
        }else{
            sb.append("Grupo: No pertenece a un grupo\n");
        }

        //Información del propietario
        if(duenho != null){
            sb.append("Duenho: ").append(duenho.getNombre()).append("\n");
        }else{
            sb.append("Duenho: Banca\n");
        }

        //Información de valores varios
        sb.append("Valor: ").append(valor).append("\n");
        sb.append("Alquiler: ").append(impuesto).append("\n");
        sb.append("valor hotel: ").append(String.format("%.0f", valorHotel)).append(",\n");
        sb.append("valor casa: ").append(String.format("%.0f", valorCasa)).append(",\n");
        sb.append("valor piscina: ").append(String.format("%.0f", valorPiscina)).append(",\n");
        sb.append("valor pista de deporte: ").append(String.format("%.0f", valorPistaDeporte)).append(",\n");
        sb.append("alquiler casa: ").append(String.format("%.0f", alquilerCasa)).append(",\n");
        sb.append("alquiler hotel: ").append(String.format("%.0f", alquilerHotel)).append(",\n");
        sb.append("alquiler piscina: ").append(String.format("%.0f", alquilerPiscina)).append(",\n");
        sb.append("alquiler pista de deporte: ").append(String.format("%.0f", alquilerPistaDeporte)).append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public String getTipo() {
        return "Solar";
    }

    //Añadimos edificios
    public void anhadirEdificio(Edificio e) {
        edificios.add(e);
    }

    public void eliminarEdificio(Edificio e){
        edificios.remove(e);
    }



}
