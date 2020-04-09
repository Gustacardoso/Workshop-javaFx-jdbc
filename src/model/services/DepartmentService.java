package model.services;

import java.util.ArrayList;
import java.util.List;

import model.Dao.DaoFactory;
import model.Dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
     
	public List<Department> findAll(){
		return dao.findAll();   
	}
	
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
       
}
