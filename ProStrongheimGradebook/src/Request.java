

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Request
 */
@WebServlet("/Request")
public class Request extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String content;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Request() {
        super();
        content = "";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		content = "";
		response.setContentType("text/html");
		String stuId = "", type = "";
		if (request.getParameter("studentID") != null) {
			stuId = request.getParameter("studentID");
		}
		if (request.getParameter("type") != null) {
			type = request.getParameter("type");
		}
		
		String tBody = getSelectedTable(stuId, type);
		if (request.getParameter("allAssi") != null) {
			content += "<table class=\"table table-striped\"><thead><tr><th>id</th><th>Student ID</th><th>Assignment Name</th><th>Type</th><th>Date</th><th>Grade</th></tr></thead><tbody>"
					+ tBody + "</tbody></table>";
		}
		request.setAttribute("content", content);
		getServletContext().getRequestDispatcher("/RequestOutput.jsp").forward(request, response);
	}
	
	private String getSelectedTable(String stuId, String type) {
		String content = "";
		try {
			// URL of Oracle database server
			String url = "jdbc:oracle:thin:testuser/password@localhost";

			// properties for creating connection to Oracle database
			Properties props = new Properties();
			props.setProperty("user", "testdb");
			props.setProperty("password", "password");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// creating connection to Oracle database using JDBC
			Connection conn = DriverManager.getConnection(url, props);

			String sql = "";
			if ((stuId.isEmpty() == true) && (type.isEmpty() == true)) {
				sql = "SELECT * FROM grades";
			} else if ((stuId.isEmpty() == false) && (type.isEmpty() == true)) {
				sql = "SELECT * FROM grades WHERE stu_id = '" + stuId + "'";
			} else if ((stuId.isEmpty() == true) && (type.isEmpty() == false)) {
				sql = "SELECT * FROM grades WHERE type = '" + type + "'";
			} else {
				sql = "SELECT * FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
			}

			// creating PreparedStatement object to execute query
			PreparedStatement preStatement = conn.prepareStatement(sql);
			ResultSet result = preStatement.executeQuery();

			while (result.next()) {
				content += "<tr><td>"
						+ Integer.parseInt(result.getString("id"))
						+ "</td><td>" 
						+ result.getString("stu_id")
						+ "</td><td>"
						+ result.getString("assignment")
						+ "</td><td>"
						+ result.getString("type")
						+ "</td><td>"
						+ result.getString("assi_date").substring(0, 10)
						+ "</td><td>"
						+ Double.parseDouble(result.getString("grade"))
						+ "</td></tr>";
			}
		} catch (SQLException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return content;
	}

}
