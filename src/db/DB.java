package db;


import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DB {
	//criar um conetor com  banco de dados
	private static Connection conn = null;
	//metodo para gerar a conecão com o banco de dados, abrir a coneccao
	public static Connection getConnection() {
		if (conn == null) {//ou seja se nao tiver conecão
			try {
				Properties props = loadProperties();//chama a propriedade load que é o metodo a baixo
				String url = props.getProperty("dburl");// pegando o endereço url que esta no arquivo db.properties
				conn = DriverManager.getConnection(url,props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			
		}
		return conn;// o retorno caso  ja tenha a coneccao  ja existente
	}
	
	//gestaorh.poa@arploenix.com.br
	//agora vamos fechar a conecção
	//porqu criamos uma classe static?
	//porque e um metodo da classe, ou seja so  vamos usar ela aq na classe
	
	public static void closeConnection() {
		if (conn != null) {//se ele tiver estanciado
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
				// TODO: handle exception
			}
		}
	}
	
	//metodo para carregar os metodos que estão no arquivo db.properties
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
    
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
