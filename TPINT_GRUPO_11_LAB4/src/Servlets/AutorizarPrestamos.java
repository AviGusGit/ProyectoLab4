package Servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entidades.Prestamo;
import NegocioImpl.PrestamoNegImpl;

@WebServlet("/AutorizarPrestamos")
public class AutorizarPrestamos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AutorizarPrestamos() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrestamoNegImpl prestamoNeg = new PrestamoNegImpl();
		
		ArrayList<Prestamo> listaP = prestamoNeg.listarTodos();
		
		request.setAttribute("listaP", listaP);
		
        String numeroPres = request.getParameter("numeroPres");
        
        if (numeroPres != null && !numeroPres.isEmpty()) {
        	
            try {

            	int num = Integer.parseInt(numeroPres);

                request.setAttribute("numeroPres", num);
                
            } catch (NumberFormatException e) {
            	
                request.setAttribute("mensaje", "Formato de número inválido.");
            }
            
        }else {
            request.setAttribute("mensaje", "Seleccione un préstamo para cargar los datos.");
        }
		
		RequestDispatcher rd = request.getRequestDispatcher("/AutorizarPrestamos.jsp");   
        rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
