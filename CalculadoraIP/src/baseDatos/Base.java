package baseDatos;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
public class Base {
	private static Base DB = new Base();
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String driverDB = "org.postgresql.Driver";
	private String dbName = "IP";
	private String urlDB = "jdbc:postgresql://localhost:5432/" + this.dbName;
	private String userDB = "postgres";
	private String passDB = "jose22";
	//constructor...
	private Base(){
		try {
			Class.forName(driverDB);
			this.conn=DriverManager.getConnection(urlDB,userDB,passDB);
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
	}
	public static Base getInstances() {
		return DB;
	}
	//imprimo registros...
	public void Imprime(String query, DefaultTableModel p) {
		try {
			Object data[]= new Object[2];
			this.stmt=this.conn.createStatement();
			this.rs=this.stmt.executeQuery(query);
			while(rs.next()) {
				for(int i=0;i<2;i++) {
					data[i]=rs.getString(i+1);
				}
				p.addRow(data);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//ingreso los datos
	public void IngresoDB(String query, String[] obj){
		try {
			this.pstmt=this.conn.prepareStatement(query);
			//ingreso datos..
			for(int i=1;i<obj.length+1;i++)
				this.pstmt.setString(i, (String)obj[i-1]);
			//ejecuto...
			this.pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				this.pstmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void dbClose() {
		try {
			this.conn.close();
			System.out.println("Conexion cerrada");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
