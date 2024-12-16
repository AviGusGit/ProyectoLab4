package DaoImpl;

import java.sql.*;
import java.util.ArrayList;
import Dao.DaoPrestamo;
import Entidades.Cuenta;
import Entidades.Prestamo;

public class DaoPrestamoImpl implements DaoPrestamo {
	
    private Conexion conexion;

    
    public DaoPrestamoImpl() {
    }

    
    public DaoPrestamoImpl(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean solicitarPrestamo(Prestamo prestamo) {
    	
        if (prestamo.getImportePedido() <= 0) {
            System.out.println("el importe solicitado debe ser mayor a 0.");
            return false;
        }

        if (prestamo.getCuotas() <= 0) {
            System.out.println("el número de cuotas debe ser mayor a 0.");
            return false;
        }

 
        float montoMes = prestamo.getImportePedido() / prestamo.getCuotas();
        prestamo.setMontoMes(montoMes);

     
        prestamo.setEstado(false);
    	
        String query = "INSERT INTO prestamos (dni_cliente, fecha, importe_pagar, importe_pedido, plazo_pago, monto_mes, cuotas, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
        	PreparedStatement ps = conexion.getConnection().prepareStatement(query);
        	
            ps.setString(1, prestamo.getDniCliente());
            ps.setDate(2, prestamo.getFecha());
            ps.setFloat(3, prestamo.getImportePagar());
            ps.setFloat(4, prestamo.getImportePedido());
            ps.setString(5, prestamo.getPlazoPago());
            ps.setFloat(6, prestamo.getMontoMes());
            ps.setInt(7, prestamo.getCuotas());
            ps.setBoolean(8, false); 
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
        	
            e.printStackTrace();
            return false;
        }
        
    }

    @Override
    public boolean actualizarEstado(Prestamo prestamo) {

        if (prestamo == null) {
            System.out.println("el Préstamo no existe.");
            return false;
        }

        if (prestamo.isEstado()) {
            System.out.println("el Préstamo ya está aprobado.");
            return false;
        }
        
        String query = "UPDATE prestamos SET estado = ? WHERE numero_pres = ?";
        
        try {
        	PreparedStatement ps = conexion.getConnection().prepareStatement(query);
            ps.setBoolean(1, prestamo.isEstado());
            ps.setInt(2, prestamo.getNumeroPres());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
        	
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Prestamo obtenerPrestamoPorNumero(int numeroPres) {
    	
        String query = "SELECT * FROM prestamos WHERE numero_pres = ?";
        
        try {
        	PreparedStatement ps = conexion.getConnection().prepareStatement(query);
            ps.setInt(1, numeroPres);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setNumeroPres(rs.getInt("numero_pres"));
                prestamo.setDniCliente(rs.getString("dni_cliente"));
                prestamo.setFecha(rs.getDate("fecha"));
                prestamo.setImportePagar(rs.getFloat("importe_pagar"));
                prestamo.setImportePedido(rs.getFloat("importe_pedido"));
                prestamo.setPlazoPago(rs.getString("plazo_pago"));
                prestamo.setMontoMes(rs.getFloat("monto_mes"));
                prestamo.setCuotas(rs.getInt("cuotas"));
                prestamo.setEstado(rs.getBoolean("estado"));
                return prestamo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Prestamo> listarPrestamosPorCliente(String dniCliente) {
    	
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String query = "SELECT * FROM prestamos WHERE dni_cliente = ?";
        
        try {
        	PreparedStatement ps = conexion.getConnection().prepareStatement(query);
        	
            ps.setString(1, dniCliente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setNumeroPres(rs.getInt("numero_pres"));
                prestamo.setDniCliente(rs.getString("dni_cliente"));
                prestamo.setFecha(rs.getDate("fecha"));
                prestamo.setImportePagar(rs.getFloat("importe_pagar"));
                prestamo.setImportePedido(rs.getFloat("importe_pedido"));
                prestamo.setPlazoPago(rs.getString("plazo_pago"));
                prestamo.setMontoMes(rs.getFloat("monto_mes"));
                prestamo.setCuotas(rs.getInt("cuotas"));
                prestamo.setEstado(rs.getBoolean("estado"));
                prestamos.add(prestamo);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return prestamos;
    }

    @Override
    public ArrayList<Prestamo> listarTodos() {
    	
    	conexion = new Conexion();
		conexion.Open();
		
		ArrayList<Prestamo> list = new ArrayList<Prestamo>();
		
		try {
			ResultSet rs = conexion.query("SELECT numeroPres_P, dniCliente_P, fecha_P, importePagar_P, importePedido_P, plazoPago_P, montoPorMes_P, cuotas_P FROM PRESTAMOS WHERE estado_P = 1");
			
			while (rs.next()) {
				Prestamo prestamo = new Prestamo();
				prestamo.setNumeroPres(rs.getInt("numeroPres_P"));
				prestamo.setDniCliente(rs.getString("dniCliente_P"));
				prestamo.setFecha(rs.getDate("fecha_P"));
				prestamo.setImportePagar(rs.getFloat("importePagar_P"));
				prestamo.setImportePedido(rs.getFloat("importePedido_P"));
				prestamo.setPlazoPago(rs.getString("plazoPago_P"));
				prestamo.setMontoMes(rs.getFloat("montoPorMes_P"));
				prestamo.setCuotas(rs.getInt("cuotas_P"));

				list.add(prestamo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conexion.close();
		}

		return list;
    }

    @Override
    public boolean pagarCuota(int numeroPres, float monto, int cuentaAsociada) {
    	
        String query = "UPDATE prestamos SET cuotas = cuotas - 1, importe_pagar = importe_pagar - ? WHERE numero_pres = ? AND cuotas > 0 AND importe_pagar >= ?";
        try {
        	PreparedStatement ps = conexion.getConnection().prepareStatement(query);
        	
            ps.setFloat(1, monto);
            ps.setInt(2, numeroPres);
            ps.setFloat(3, monto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


	@Override
	public ArrayList<Prestamo> listarConFiltro(String filtro) {
		return null;
	}
}
