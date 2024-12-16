package DaoImpl;
import Entidades.Movimiento;
import Entidades.Prestamo;
import Entidades.TipoMovimiento;

import java.sql.*;
import java.util.ArrayList;

import Dao.DaoMovimiento;

public class daoMovimientoImpl implements DaoMovimiento {

    private Conexion conexion;

    public daoMovimientoImpl() {
    }

    // Método para insertar un movimiento
    public void insertarMovimiento(Movimiento movimiento) throws SQLException {
    	
        String query = "INSERT INTO MOVIMIENTOS (numeroCuenta_M, fecha_M, detalle_M, importe_M, IDtipoMovimientos_M) VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement ps = conexion.getConnection().prepareStatement(query);
        
        ps.setInt(1, movimiento.getNumeroCuenta());
        ps.setDate(2, new java.sql.Date(movimiento.getFecha().getTime()));
        ps.setString(3, movimiento.getDetalle());
        ps.setDouble(4, movimiento.getImporte());
        ps.setInt(5, movimiento.getTipoMovimiento().getIdTipoMovimiento());

        ps.executeUpdate();
    }

    // Método para obtener un movimiento por su ID
    public Movimiento obtenerMovimiento(int codigoMovimiento) throws SQLException {
    	
        String query = "SELECT * FROM MOVIMIENTOS M JOIN TIPOMOVIMIENTOS T ON M.IDtipoMovimientos_M = T.ID_TipoMovimiento WHERE M.codigoMov_M = ?";
        
        PreparedStatement ps = conexion.getConnection().prepareStatement(query);
        ps.setInt(1, codigoMovimiento);
        
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            Movimiento movimiento = new Movimiento();
            movimiento.setCodigoMovimiento(resultSet.getInt("codigoMov_M"));
            movimiento.setNumeroCuenta(resultSet.getInt("numeroCuenta_M"));
            movimiento.setFecha(resultSet.getDate("fecha_M"));
            movimiento.setDetalle(resultSet.getString("detalle_M"));
            movimiento.setImporte(resultSet.getDouble("importe_M"));
            
            TipoMovimiento tipoMovimiento = new TipoMovimiento();
            tipoMovimiento.setIdTipoMovimiento(resultSet.getInt("ID_TipoMovimiento"));
            tipoMovimiento.setDescripcion(resultSet.getString("Descripcion"));

            movimiento.setTipoMovimiento(tipoMovimiento);
            
            return movimiento;
        }
        return null;
    }

	@Override
	public ArrayList<Movimiento> obtenerMovimientosPorCuenta(int numeroCuenta) {
		
		conexion = new Conexion();
		conexion.Open();
		
        ArrayList<Movimiento> list = new ArrayList<Movimiento>();
        
        try {
			String query = "SELECT codigoMov_M, numeroCuenta_M, fecha_M, detalle_M, importe_M, IDtipoMovimientos_M FROM MOVIMIENTOS WHERE numeroCuenta_M = ?";
        	
	        PreparedStatement ps = conexion.getConnection().prepareStatement(query);
	        ps.setInt(1, numeroCuenta); 
	        
	        ResultSet rs = ps.executeQuery();
			
            if (rs.next()) {
                Movimiento movimiento = new Movimiento();
                TipoMovimiento tipoMov = new TipoMovimiento();
                tipoMov.setIdTipoMovimiento(rs.getInt("IDtipoMovimientos_M"));
                
                movimiento.setCodigoMovimiento(rs.getInt("codigoMov_M"));
                movimiento.setNumeroCuenta(rs.getInt("numeroCuenta_M"));
                movimiento.setFecha(rs.getDate("fecha_M"));
                movimiento.setDetalle(rs.getString("detalle_M"));
                movimiento.setImporte(rs.getFloat("importe_M"));
                movimiento.setTipoMovimiento(tipoMov);
                
                list.add(movimiento);
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conexion.close();
		}

		return list;
	}

	@Override
	public ArrayList<Movimiento> obtenerMovimientosPorCliente(int dniCliente) {
		// TODO Auto-generated method stub
		return null;
	}

}
