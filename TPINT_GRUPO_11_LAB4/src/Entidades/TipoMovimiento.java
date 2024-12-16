package Entidades;

public class TipoMovimiento {
    private int idTipoMovimiento;
    private String descripcion;

    // Constructor vac�o
    public TipoMovimiento() {}

    // Constructor con par�metros
    public TipoMovimiento(int idTipoMovimiento, String descripcion) {
        this.idTipoMovimiento = idTipoMovimiento;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(int idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
