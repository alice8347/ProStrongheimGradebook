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
	private Connection conn;
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
		try {
			if (request.getParameter("weightSub") != null) {
				String homework = request.getParameter("homework");
				String quiz = request.getParameter("quiz");
				String test = request.getParameter("test");
				String project = request.getParameter("project");
				writeToWeights(homework, quiz, test, project);			
			}
			
			content = "";
			response.setContentType("text/html");
			String classId = "", stuId = "", type = "";
			
			if (request.getParameter("classID") != "") {
				classId = request.getParameter("classID");
			}
			
			if (request.getParameter("studentID") != "") {
				stuId = request.getParameter("studentID");
			}
			
			if (request.getParameter("type") != "") {
				type = request.getParameter("type");
			}
			
			if (request.getParameter("allAssi") != null) {
				String tBody = getSelectedTable(classId, stuId, type);
				content += "<table class=\"table table-striped\"><thead><tr><th>id</th><th>Class ID</th><th>Student ID</th><th>Assignment Name</th><th>Type</th><th>Date</th><th>Grade</th></tr></thead><tbody>"
						+ tBody + "</tbody></table>";
			}
			
			if (request.getParameter("average") != null) {
				String ave = getAve(classId, stuId, type);
				content += "<div class=\"jumbotron\"><h4>Average grade: </h4><p>" + ave + "</p></div>";
			}
			
			if (request.getParameter("maxMin") != null) {
				String value[] = getMaxMin(classId, stuId, type);
				content += "<div class=\"jumbotron\"><h4>Highest grade: </h4><p>" + value[0] + "</p><br /><h4>Lowest grade: </h4><p>" + value[1] + "</p></div>";
			}
			request.setAttribute("content", content);
			getServletContext().getRequestDispatcher("/RequestOutput.jsp").forward(request, response);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void writeToWeights(String homework, String quiz, String test, String project) {
		try {
			String url = "jdbc:oracle:thin:testuser/password@localhost";
			
			Properties props = new Properties();
			props.setProperty("user", "testdb");
			props.setProperty("password", "password");
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(url, props);
			
			String sql1 = "UPDATE weights SET id = 1, type = 'Homework', weight = " + homework + " WHERE id = 1";
			String sql2 = "UPDATE weights SET id = 2, type = 'Quiz', weight = " + quiz + " WHERE id = 2";
			String sql3 = "UPDATE weights SET id = 3, type = 'Test', weight = " + test + " WHERE id = 3";
			String sql4 = "UPDATE weights SET id = 4, type = 'Project', weight = " + project + " WHERE id = 4";
			
			PreparedStatement prePreStatement1 = conn.prepareStatement(sql1);
			PreparedStatement prePreStatement2 = conn.prepareStatement(sql2);
			PreparedStatement prePreStatement3 = conn.prepareStatement(sql3);
			PreparedStatement prePreStatement4 = conn.prepareStatement(sql4);
			prePreStatement1.executeQuery();
			prePreStatement2.executeQuery();
			prePreStatement3.executeQuery();
			prePreStatement4.executeQuery();
			
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String getSelectedTable(String classId, String stuId, String type) {
		String content = "";
		Connection conn = connectDB();

		String sql1 = "", sql2 = "";
		if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades WHERE class_id = '" + classId + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE class_id = '" + classId + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades WHERE stu_id = '" + stuId + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE stu_id = '" + stuId + "'GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades WHERE type = '" + type + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE type = '" + type + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))){
			sql1 = "SELECT * FROM grades WHERE class_id = '" + classId + "' AND type = '" + type + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE class_id = '" + classId + "' AND type = '" + type + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (!type.equals("Select Type"))) {
			sql1 = "SELECT * FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		} else {
			sql1 = "SELECT * FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' AND type = '" + "'";
			sql2 = "SELECT SUM(\"gpaSubSum\" * weights.weight) FROM (SELECT SUM(grades.grade) AS \"gpaSubSum\", grades.type AS \"assiType\" FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' AND type = '" + type + "' GROUP BY grades.type), weights WHERE \"assiType\" = weights.type";
		}

		try {
			// creating PreparedStatement object to execute query
			PreparedStatement preStatement1 = conn.prepareStatement(sql1);
			PreparedStatement preStatement2 = conn.prepareStatement(sql2);
			ResultSet result1 = preStatement1.executeQuery();
			ResultSet result2 = preStatement2.executeQuery();
	
			while (result1.next()) {
				content += "<tr><td>"
						+ Integer.parseInt(result1.getString("id"))
						+ "</td><td>" 
						+ result1.getString("class_id")
						+ "</td><td>"
						+ result1.getString("stu_id")
						+ "</td><td>"
						+ result1.getString("assignment")
						+ "</td><td>"
						+ result1.getString("type")
						+ "</td><td>"
						+ result1.getString("assi_date").substring(0, 10)
						+ "</td><td>"
						+ Double.parseDouble(result1.getString("grade"))
						+ "</td></tr>";
			}
			
			while (result2.next()) {
				content += "<tr><td></td><td></td><td></td><td></td><td></td><th>GPA: </th><td>" + result2.getString(1) + "</td></tr>";
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return content;
	}
	
	private String getAve(String classId, String stuId, String type) {
		String content = "";
		Connection conn = connectDB();

		String sql = "";
		if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades WHERE class_id = '" + classId + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades WHERE stu_id = '" + stuId + "'";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades WHERE type = '" + type + "'";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))){
			sql = "SELECT AVG(grade) FROM grades WHERE class_id = '" + classId + "' AND type = '" + type + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (!type.equals("Select Type"))) {
			sql = "SELECT AVG(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
		} else {
			sql = "SELECT AVG(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' AND type = '" + "'";
		}

		try {
			// creating PreparedStatement object to execute query
			PreparedStatement preStatement = conn.prepareStatement(sql);
			ResultSet result = preStatement.executeQuery();
			
			while (result.next()) {
				content += result.getString(1);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return content;
	}
	
	private String[] getMaxMin(String classId, String stuId, String type) {
		String[] content = new String[2];
		Connection conn = connectDB();
		
		String sql1 = "", sql2 = "";
		if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades";
			sql2 = "SELECT MIN(grade) FROM grades";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE class_id = '" + classId + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE class_id = '" + classId + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE stu_id = '" + stuId + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE stu_id = '" + stuId + "'";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == false) && (type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE type = '" + type + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE type = '" + type + "'";
		} else if ((classId.isEmpty() == false) && (stuId.isEmpty() == true) && (!type.equals("Select Type"))){
			sql1 = "SELECT MAX(grade) FROM grades WHERE class_id = '" + classId + "' AND type = '" + type + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE class_id = '" + classId + "' AND type = '" + type + "'";
		} else if ((classId.isEmpty() == true) && (stuId.isEmpty() == false) && (!type.equals("Select Type"))) {
			sql1 = "SELECT MAX(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE stu_id = '" + stuId + "' AND type = '" + type + "'";
		} else {
			sql1 = "SELECT MAX(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' AND type = '" + "'";
			sql2 = "SELECT MIN(grade) FROM grades WHERE class_id = '" + classId + "' AND stu_id = '" + stuId + "' AND type = '" + "'";
		}

		try {
			// creating PreparedStatement object to execute query
			PreparedStatement preStatement1 = conn.prepareStatement(sql1);
			PreparedStatement preStatement2 = conn.prepareStatement(sql2);
			ResultSet result1 = preStatement1.executeQuery();
			ResultSet result2 = preStatement2.executeQuery();
			
			while (result1.next() && result2.next()) {
				content[0] = result1.getString(1);
				content[1] = result2.getString(1);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return content;
	}

	private Connection connectDB() {
		try {
			// URL of Oracle database server
			String url = "jdbc:oracle:thin:testuser/password@localhost";

			// properties for creating connection to Oracle database
			Properties props = new Properties();
			props.setProperty("user", "testdb");
			props.setProperty("password", "password");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// creating connection to Oracle database using JDBC
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
