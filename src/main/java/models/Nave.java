package models;

public class Nave {
    private int id;
    private TipoNave tipoNave;
    private Boolean saltoHiperEspacio;
    private int misilesProtonicos;
    private String fechaCreacion;

    public Nave() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public void setTipoNave(TipoNave tipoNave) {
        this.tipoNave = tipoNave;
    }

    public Boolean getSaltoHiperEspacio() {
        return saltoHiperEspacio;
    }

    public void setSaltoHiperEspacio(Boolean saltoHiperEspacio) {
        this.saltoHiperEspacio = saltoHiperEspacio;
    }

    public int getMisilesProtonicos() {
        return misilesProtonicos;
    }

    public void setMisilesProtonicos(int misilesProtonicos) {
        this.misilesProtonicos = misilesProtonicos;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Nave{" +
                "id=" + id +
                ", tipoNave=" + tipoNave +
                ", saltoHiperEspacio=" + saltoHiperEspacio +
                ", misilesProtonicos=" + misilesProtonicos +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }
}



