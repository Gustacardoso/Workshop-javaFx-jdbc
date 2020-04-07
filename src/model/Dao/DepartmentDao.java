package model.Dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
       
	 void insert(Department obj);
	 void update (Department obj);
	 void deleteById(Integer id);
	 Department findById(Integer id );//busca pelo id
	  List<Department> findAll();//lista todos 
	  
	
}
