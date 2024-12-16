package Servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entidades.Prestamo;
import Negocio.PrestamoNeg;
import NegocioImpl.PrestamoNegImpl;
import DaoImpl.DaoPrestamoImpl;

@WebServlet("/PedirPrestamos")
public class PedirPrestamos extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PrestamoNeg prestamoNegocio = new PrestamoNegImpl();;

    public PedirPrestamos() {
    }

   
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Comienzo pedir préstamo GET");
        RequestDispatcher rd = request.getRequestDispatcher("/PedirPrestamos.jsp");
        rd.forward(request, response);
    }

  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Comienzo pedir préstamo POST");

        try {
           
            String dniCliente = request.getParameter("dniCliente");
            float importePedido = Float.parseFloat(request.getParameter("monto"));
            int cuotas = Integer.parseInt(request.getParameter("cuotas"));
            String plazoPago = request.getParameter("plazoPago");

            
            Prestamo prestamo = new Prestamo();
            prestamo.setDniCliente(dniCliente);
            prestamo.setImportePedido(importePedido);
            prestamo.setCuotas(cuotas);
            prestamo.setPlazoPago(plazoPago);
            prestamo.setFecha(new java.sql.Date(System.currentTimeMillis()));
            prestamo.setEstado(false); 

           
            boolean exito = prestamoNegocio.solicitarPrestamo(prestamo);

            if (exito) {
                request.setAttribute("mensajeExito", "el préstamo ha sido solicitado exitosamente.");
            } else {
                request.setAttribute("mensajeError", "no se pudo registrar la solicitud del préstamo.");
            }

        } catch (Exception e) {
           
            request.setAttribute("mensajeError", "ocurrio un error al procesar la solicitud: " + e.getMessage());
        }

       
        RequestDispatcher rd = request.getRequestDispatcher("/PedirPrestamos.jsp");
        rd.forward(request, response);
    }
}

