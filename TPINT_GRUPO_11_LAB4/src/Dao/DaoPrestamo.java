package Dao;

import java.util.ArrayList;
import Entidades.Prestamo;

public interface DaoPrestamo {
    boolean solicitarPrestamo(Prestamo prestamo);
    boolean pagarCuota(int numeroPres, float monto, int cuentaAsociada);
    boolean actualizarEstado(Prestamo prestamo);
    Prestamo obtenerPrestamoPorNumero(int numeroPres);
    ArrayList<Prestamo> listarPrestamosPorCliente(String dniCliente);
    ArrayList<Prestamo> listarTodos();
    ArrayList<Prestamo> listarConFiltro(String filtro);
}

