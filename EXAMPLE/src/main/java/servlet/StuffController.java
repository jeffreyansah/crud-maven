package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CrudDaoStuff;
import model.CrudStuff;

/**
 * Servlet implementation class StuffController
 */
public class StuffController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CrudDaoStuff stuff = CrudDaoStuff.getInstance();
	private static final Logger logger = Logger.getLogger(StuffController.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StuffController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action= req.getServletPath();
		try {
			switch(action) {
			case "new":
				showEditForm(req, resp);
				break;
			case "insert":
				insertStuff(req, resp);
				break;
			case "delete":
				deleteStuff(req, resp);
				break;
			case "edit":
				showEditForm(req, resp);
				break;
			case "update":
				updateStuff(req, resp);
				break;
				default:
				listStuff(req, resp);
				break;
			}
			
		} catch(SQLException e) {
			//for simplicity just log the exception
			logger.log(Level.SEVERE,"SQL  Error", e);
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	private void updateStuff(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id=Integer.parseInt(req.getParameter("id"));
		String name= req.getParameter("name");
		String description= req.getParameter("description");
		int quantity= Integer.parseInt(req.getParameter("quantity"));
		String location=req.getParameter("location");
		CrudStuff crudStuff= new CrudStuff(id,name, description, quantity, location);
		try {
			boolean stuffdao= CrudDaoStuff.getInstance().update(crudStuff);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.sendRedirect("list");
		
		
	}

	private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException {

	String id= req.getParameter("id");
	Optional<CrudStuff> existingStuff=CrudDaoStuff.getInstance().find(id);
	RequestDispatcher dispatcher= req.getRequestDispatcher("StuffFrom.jsp");
	existingStuff.ifPresent(s ->req.setAttribute("crudstuff", s));
	dispatcher.forward(req, resp);
	}

	private void deleteStuff(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException {
   
		int id= Integer.parseInt(req.getParameter("id"));
		
		CrudStuff crudStuff= new CrudStuff(id);
        boolean stuffdao=CrudDaoStuff.getInstance().delete(crudStuff);
        resp.sendRedirect("list");
		
	}

	private void insertStuff(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException {

		String name= req.getParameter("name");
		String description= req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		String location= req.getParameter("location");
		CrudStuff crudStuff= new CrudStuff(name, description, quantity, location);
		boolean stuffdao=CrudDaoStuff.getInstance().save(crudStuff);
		resp.sendRedirect("list");
	}

	private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException {

		RequestDispatcher dispatcher= req.getRequestDispatcher("StuffFrom.jsp");
		  dispatcher.forward(req, resp);
		
		
	}

	private void listStuff(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,SQLException{

		RequestDispatcher dispatcher= req.getRequestDispatcher("StuffList.jsp");
		List<CrudStuff> listStuff= CrudDaoStuff.getInstance().findAll();
		req.setAttribute("listStuff", listStuff);
	}
	
	public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {
request.setCharacterEncoding("UTF-8");
chain.doFilter(request, response);
}
}
