package model.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.Dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("insert into department (Name) value ( ? )"
					 , Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
			//verificar as linha atingida
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0 ) {
				ResultSet rs = st.getGeneratedKeys();
			    if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			    DB.closeResultSet(rs);//fechando  o result
			}
			else {
				throw new DbException("Unexpected error! No rows affected");
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
			PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("update department set Name = ?  where Id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			//verificar as linha atingida
		   st.executeUpdate();
			
			
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
	PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("delete from department where Id = ?");
			st.setInt(1, id);
			
			//verificar as linha atingida
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected == 0 ) {
				throw new DbException("id nao Existente");
			}
		}catch (SQLException e) {
			throw new  DbException(e.getMessage());
			// TODO: handle exception
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from department where Id = ?");
			
			st.setInt(1, id);
			//vamos dar um executequery porque precisamos fazer uma pesquisa no banco de dados
			rs = st.executeQuery();
			
			//configurando o preenchimento da colunas
			if(rs.next()) {
				Department dep =  new Department();
				  dep.setId(rs.getInt("Id"));
				  dep.setName(rs.getString("Name"));
				return dep;
			}
			return null;
			
		} catch (SQLException  e) {
			throw new DbException(e.getMessage());
		}
		finally {
			//nao posso esquecer de fechar
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from department");
			
			List<Department> listDepartment = new ArrayList<>();
			//vamos dar um executequery porque precisamos fazer uma pesquisa no banco de dados
			rs = st.executeQuery();
			
			//configurando o preenchimento da colunas
			while(rs.next()) {
				Department dep =  new Department();
				  dep.setId(rs.getInt("Id"));
				  dep.setName(rs.getString("Name"));
				listDepartment.add(dep);
			}
			return listDepartment;
			
		} catch (SQLException  e) {
			throw new DbException(e.getMessage());
		}
		finally {
			//nao posso esquecer de fechar
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	
	

}
