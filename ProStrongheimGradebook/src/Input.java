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
 * Servlet implementation class Input
 */
@WebServlet("/Input")
public class Input extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Input() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		// Store to DB
		String stuId = request.getParameter("studentID");
		String assi = request.getParameter("assignment");
		String type = "";
		String date = request.getParameter("date");
		double grade = 0.0;
		try {
			type = request.getParameter("type");
			if (type.equalsIgnoreCase("Select Type")) {
				type = "";
			}
			grade = Double.parseDouble(request.getParameter("grade"));
			writeTo(stuId, assi, type, date, grade);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		getServletContext().getRequestDispatcher("/InputForm.jsp").forward(request, response);
	}

	private static void writeTo(String stuId, String assi, String type, String date, double grade) {
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

			String pre = "SELECT COUNT(*) FROM grades";
			PreparedStatement prePreStatement = conn.prepareStatement(pre);
			ResultSet preResult = prePreStatement.executeQuery();
			int line = 0;
			while (preResult.next()) {
				line = Integer.parseInt(preResult.getString(1)) + 1;
			}
			String sql = "INSERT INTO grades VALUES (" + line + ", '" + stuId + "', '" + assi + "', '" + type + "', TO_DATE('" + date + "', 'YYYY-MM-DD'), " + grade + ")";

			// creating PreparedStatement object to execute query
			PreparedStatement preStatement = conn.prepareStatement(sql);
			preStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
