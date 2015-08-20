import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Servlet implementation class Output
 */
@WebServlet("/Output")
public class Output extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Output() {
		super();
		message = "";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		message = "";
		String tBody = getTable();
		message += "<table class=\"table table-striped\"><thead><tr><th>id</th><th>Student ID</th><th>Assignment Name</th><th>Type</th><th>Date</th><th>Grade</th></tr></thead><tbody>"
				+ tBody + "</tbody></table>";
		request.setAttribute("message", message);
		getServletContext().getRequestDispatcher("/OutputTable.jsp").forward(
				request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	private String getTable() {
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

			String sql = "SELECT * FROM grades";

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
