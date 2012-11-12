import java.sql.*;

public class DBTest
{
	public static void main(String[] argv) 
	{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Couldn't find the driver!");
			System.out.println("Let's print a stack trace, and exit.");
			cnfe.printStackTrace();
			System.exit(1);
		}

		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:postgresql://localhost/transmem","postgres", "postgres");
		} catch (SQLException se) {
			System.out.println("Couldn't connect: print out a stack trace and exit.");
			se.printStackTrace();
			System.exit(1);
		}
		
		PreparedStatement ps = null;
		try {
			String sql = "select F_Name from T_LangCodes where F_Code=?";
			ps = c.prepareStatement(sql);
			ps.setString(1,"EN");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String name = rs.getString(1);
				assert "English".equals(name);
			}
		} catch (SQLException e) {
			System.out.println("select from T_LangCodes error");
			System.out.println(e.getMessage());
		} finally {
			try { ps.close(); } catch (SQLException x) {}
		}
		
		//create a table for testing
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String sql = "create table t_roles(id serial not null,name varchar(10),level int,primary key (id))";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("create table error");
			System.out.println(e.toString());
		} finally {
			try { stmt.close(); } catch (SQLException x) {}
		}
		//insert some rows into T_Roles
		try {
			stmt = c.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setFetchSize(1);
			ResultSet rs = stmt.executeQuery("select * from T_Roles where id=0");
			for (int i=0; i<5; i++) {
				rs.moveToInsertRow();
				//char ch = (char)('A'+i);
				//rs.updateString(1, i);
				rs.updateString(2, String.valueOf(i));
				rs.updateInt(3, 100+i);
				rs.insertRow();
			}
		} catch (SQLException e) {
			System.out.println("insert through select for update error");
			System.out.println(e.toString());
		} finally {
			try { stmt.close(); } catch (SQLException x) {}
		}

		try {
			String sql = "SELECT * FROM T_Roles WHERE id=1";
			ps = c.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			//other types: ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.TYPE_SCROLL_INSENSITIVE
			ps.setFetchSize(2);
			ResultSet uprs = ps.executeQuery();
			uprs.next();
			uprs.updateInt("Level", 10);
			uprs.updateRow();
		} catch (SQLException e) {
			System.out.println("update by select error");
			System.out.println(e.getMessage());
		} finally {
			try { ps.close(); } catch (SQLException x) {}
		}

		//FINAL: drop the testing table and clean up connection!
		try {
			stmt = c.createStatement();
			stmt.executeUpdate("drop table t_roles");
			stmt.close();
			c.close();
		} catch (SQLException e) {
		}
	}
}
