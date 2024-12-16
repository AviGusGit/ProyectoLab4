package DaoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Dao.DaoCuenta;
import Entidades.Cuenta;

public class DaoCuentaImpl implements DaoCuenta {
	
	private Conexion cn;

	public DaoCuentaImpl() {
		
	}
	
	@Override
	public ArrayList<Cuenta> obtenerCuentas() {
		
    	cn = new Conexion();
		cn.Open();
		
		ArrayList<Cuenta> list = new ArrayList<Cuenta>();
		
		try {
			ResultSet rs = cn.query("SELECT numeroCuenta_CU, dniCliente_CU, fechaCreacion_CU, IDtipoCuenta_CU, CBU_CU, saldo_CU FROM CUENTAS WHERE estado_CU = 1");
			
			// "SELECT numeroCuenta_CU, dniCliente_CU, fechaCreacion_CU, nombre as tipoCuenta, CBU_CU, saldo_CU FROM CUENTAS INNER JOIN TIPOCUENTA WHERE IDtipoCuenta_CU = IDtipoCuenta"
			
			while (rs.next()) {
				Cuenta cuenta = new Cuenta();
				cuenta.setNumeroCuenta(rs.getInt("numeroCuenta_CU"));
				cuenta.setDniCliente(rs.getString("dniCliente_CU"));
				cuenta.setFechaCreacion(rs.getDate("fechaCreacion_CU"));
				
				cuenta.setTipoCuenta(rs.getInt("numeroCuenta_CU"));
				
				cuenta.setCbu(rs.getString("CBU_CU"));
				cuenta.setSaldo(rs.getFloat("saldo_CU"));

				list.add(cuenta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cn.close();
		}

		return list;
	}

	@Override
	public Cuenta obtenerUno(int numeroCuenta) {
	    cn = new Conexion();
	    cn.Open();

	    Cuenta cuenta = null;

	    try {
	        String query = "SELECT numeroCuenta_CU, dniCliente_CU, fechaCreacion_CU, IDtipoCuenta_CU, CBU_CU, saldo_CU " +
	                       "FROM CUENTAS WHERE estado_CU = 1 AND numeroCuenta_CU = ?";
	        
	        PreparedStatement ps = cn.getConnection().prepareStatement(query);
	        ps.setInt(1, numeroCuenta); 
	        
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            cuenta = new Cuenta();
				
	            cuenta.setNumeroCuenta(rs.getInt("numeroCuenta_CU"));
	            cuenta.setDniCliente(rs.getString("dniCliente_CU"));
	            cuenta.setFechaCreacion(rs.getDate("fechaCreacion_CU"));
	            
	            cuenta.setTipoCuenta(rs.getInt("numeroCuenta_CU"));
				
	            cuenta.setCbu(rs.getString("CBU_CU"));
	            cuenta.setSaldo(rs.getFloat("saldo_CU"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        cn.close(); 
	    }

	    return cuenta;
	}
	
	

	@Override
	public boolean agregarCuenta(Cuenta cuenta) {
		
		if(maxCuentas(cuenta.getDniCliente())) {
			return false;
		}
		
		boolean estado=true;

		cn = new Conexion();
		cn.Open();	

		String query = "INSERT INTO CUENTAS (dniCliente_CU, fechaCreacion_CU, IDtipoCuenta_CU, CBU_CU, saldo_CU, estado_CU) VALUES ('"
	              + cuenta.getDniCliente() + "', '"
	              + cuenta.getFechaCreacion() + "', "
	              + cuenta.getTipoCuenta() + ", '"
	              + cuenta.getCbu() + "', 10000.00, 1)";

		System.out.println(query);
		try
		 {
			estado=cn.execute(query);
		 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cn.close();
		}
		
		return estado;
	}

	@Override
	public boolean modificarCuenta(Cuenta cuenta) {
		
		if(maxCuentas(cuenta.getDniCliente())) {
			return false;
		}
		
	    boolean estado = true;

	    cn = new Conexion();
	    cn.Open();

	    String query = "UPDATE CUENTAS SET "
	                 + "dniCliente_CU = '" + cuenta.getDniCliente() + "', "
	                 + "fechaCreacion_CU = '" + cuenta.getFechaCreacion() + "', "
	                 + "IDtipoCuenta_CU = " + cuenta.getTipoCuenta() + ", "
	                 + "CBU_CU = '" + cuenta.getCbu() + "', "
	                 + "saldo_CU = " + cuenta.getSaldo()
	                 + " WHERE numeroCuenta_CU = " + cuenta.getNumeroCuenta();

	    try {
	        estado = cn.execute(query);
	    } catch (Exception e) {
	        e.printStackTrace();
	        estado = false;
	    } finally {
	        cn.close();
	    }

	    return estado;
	}
	
	@Override
	public boolean eliminarCuenta(int numeroCuenta) {
		
		boolean estado = true;
		
		cn = new Conexion();
		cn.Open();		 
		
		String query = "UPDATE CUENTAS SET estado_CU=0 WHERE numeroCuenta_CU="+numeroCuenta;
		try
		 {
			estado=cn.execute(query);
		 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cn.close();
		}
		return estado;
	}
	
	
    public ArrayList<Integer> obtenerNumCuenta() {
    	cn = new Conexion();
        cn.Open();

        ArrayList<Integer> List = new ArrayList<>();
        
        try {
            ResultSet rs = cn.query("SELECT numeroCuenta_CU FROM cuentas WHERE estado_CU = 1");
            
            while (rs.next()) {
            	
                List.add(rs.getInt("numeroCuenta_CU"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cn.close();
        }

        return List;
    }

	@Override
	public ArrayList<Integer> obtenerCuentasCliente(String dniCliente) {
		cn = new Conexion();
		cn.Open();

		ArrayList<Integer> list = new ArrayList<Integer>();

		try {
		    String query = "SELECT numeroCuenta_CU FROM CUENTAS WHERE estado_CU=1 AND dniCliente_CU = ?";
		    
		    PreparedStatement ps = cn.getConnection().prepareStatement(query);
		    
		    ps.setString(1, dniCliente); 
		    
		    ResultSet rs = ps.executeQuery();
		    
		    while (rs.next()) {

		        list.add(rs.getInt("numeroCuenta_CU"));
		    }
		    
		    rs.close();
		    ps.close();

		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    cn.close();
		}

		return list;

	}
	@Override
	public double obtenerTotal(String dniCliente) {
		
		cn = new Conexion();
		cn.Open();

	    double total = 0.0;
	    System.out.println("entro a la fx " + total);
	    try {
	        String query = "SELECT SUM(saldo_CU) AS saldoTotal FROM CUENTAS WHERE dniCliente_CU = ? AND estado_CU = 1;";

	        // Usamos PreparedStatement con parámetro
	        PreparedStatement ps = cn.getConnection().prepareStatement(query);
	        ps.setString(1, dniCliente);  // Usamos el parámetro 'dniCliente' en la consulta
	        ResultSet rs = ps.executeQuery();

	        // Verificamos si hay resultados
	        if (rs.next()) {
	            System.out.println("intentando agarrar el valor");
	            // Usamos getObject para obtener el saldo total como un objeto
	            Object saldoTotalObj = rs.getObject("saldoTotal");

	            if (saldoTotalObj != null) {
	                // Convertimos el objeto a Double
	                total = ((Number) saldoTotalObj).doubleValue();
	                System.out.println("El saldo es: " + total);
	            }
	        }
	        rs.close();
	        ps.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            cn.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    return total;
	}

	
	@Override
	public boolean maxCuentas(String dniCliente) {
		
		int cantCuentas = obtenerCuentasCliente(dniCliente).size();
		
		if(cantCuentas >= 3) {
			return true;
		}
		
		return false;
	}

	@Override
	public ArrayList<String> obtenerCbuTransf(int cuentaExcluida) {
		
	    Conexion cn = new Conexion();
	    ArrayList<String> cbuList = new ArrayList<>();

	    try {
	        cn.Open();
	        String query = "SELECT CBU_CU FROM cuentas WHERE estado_CU = 1 AND numeroCuenta_CU != ?";
	        PreparedStatement ps = cn.getConnection().prepareStatement(query);
	        ps.setInt(1, cuentaExcluida);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            cbuList.add(rs.getString("CBU_CU"));
	        }

	        rs.close();
	        ps.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        cn.close();
	    }

	    return cbuList;
	}
}
