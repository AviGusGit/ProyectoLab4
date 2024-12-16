package NegocioImpl;

import java.sql.SQLException;
import java.util.ArrayList;

import Dao.DaoMovimiento;
import DaoImpl.daoMovimientoImpl;
import Entidades.Movimiento;
import Negocio.MovimientoNeg;

public class MovimientoNegImpl implements MovimientoNeg {

	private DaoMovimiento movimientoDao = new daoMovimientoImpl();
	
	@Override
	public void insertarMovimiento(Movimiento movimiento) throws SQLException {
		movimientoDao.insertarMovimiento(movimiento);
	}

	@Override
	public Movimiento obtenerMovimiento(int codigoMovimiento) throws SQLException {
		return movimientoDao.obtenerMovimiento(codigoMovimiento);
	}

	@Override
	public ArrayList<Movimiento> obtenerMovimientosPorCuenta(int numeroCuenta) {
		return movimientoDao.obtenerMovimientosPorCuenta(numeroCuenta);
	}

	@Override
	public ArrayList<Movimiento> obtenerMovimientosPorCliente(int dniCliente) {
		return movimientoDao.obtenerMovimientosPorCliente(dniCliente);
	}

}
