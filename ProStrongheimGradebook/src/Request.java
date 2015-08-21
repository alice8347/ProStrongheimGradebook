

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
		
		if (request.getParameter("allAssi") != null) {
			String tBody = getSelectedTable(stuId, type);
			content += "<table class=\"table table-striped\"><thead><tr><th>id</th><th>Student ID</th><th>Assignment Name</th><th>Type</th><th>Date</th><th>Grade</th></tr></thead><tbody>"
					+ tBody + "</tbody></table>";
		}
		
		if (request.getParameter("average") != null) {
			String ave = getAve(stuId, type);
			content += "<div class=\"jumbotron\"><h4>Average grade: </h4><p>" + ave + "</p></div>";
		}
		
		if (request.getParameter("maxMin") != null) {
			String value[] = getMaxMin(stuId, type);
			content += "<div class=\"jumbotron\"><h4>Highest grade: </h4><p>" + value[0] + "</p><br /><h4>Lowest grade: </h4><p>" + value[1] + "</p></div>";
		}
		request.setAttribute("content", content);
		getServletContext().getRequestDispatcher("/RequestOutput.jsp").forward(request, response);
	}

	private String getSelectedTable(String stuId, String type) {
		String content = "";
		Connection conn = connectDB();

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
		return content;
	}
	
	private String getAve(String stuId, String type) {
		String content = "";
		Connection conn = connectDB();

		String sql = "";
		if ((stuId.isEmpty() == true) && (type.isEmpty() == true)) {
			sql = "SELECT AVG(grade) FROM grades";
		} else if ((stuId.isEmpty() == false) && (type.isEmpty() == true)) {
			sql = "SELECT AVG(grade) FROM grades WHERE stu_id = '" + stuId + "'";
		} else if ((stuId.isEmpty() == true) && (type.isEmpty() == false)) {
			sql = "SELECT AVG(grade) FROM grades WHERE type = '" + type + "'";
		} else {
			sql = "SELECT AVG(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
		}

		// creating PreparedStatement object to execute query
		PreparedStatement preStatement = conn.prepareStatement(sql);
		ResultSet result = preStatement.executeQuery();
		
		while (result.next()) {
			content += result.getString(1);
		}
		return content;
	}
	
	private String[] getMaxMin() {
		String[] content = new String[2];
		Connection conn = connectDB();
		
		String sql1 = "", sql2 = "";
		if ((stuId.isEmpty() == true) && (type.isEmpty() == true)) {
			sql1 = "SELECT MAX(grade) FROM grades";
			sql2 = "SELECT MIN(grade) FROM grades";
		} else if ((stuId.isEmpty() == false) && (type.isEmpty() == true)) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE stu_id = '" + stuId + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE stu_id = '" + stuId + "'";
		} else if ((stuId.isEmpty() == true) && (type.isEmpty() == false)) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE type = '" + type + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE type = '" + type + "'";
		} else {
			sql1 = "SELECT MAX(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
		}

		// creating PreparedStatement object to execute query
		PreparedStatement preStatement1 = conn.prepareStatement(sql1);
		PreparedStatement preStatement2 = conn.prepareStatement(sql2);
		ResultSet result1 = preStatement1.executeQuery();
		ResultSet result2 = preStatement2.executeQuery();
		
		while (result1.next() && result2.next()) {
			content[0] += result1.getString(1);
			content[1] += result2.getString(1);
		}
		return content;
	}

	private static Connection connectDB() {
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
		} catch (SQLException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
