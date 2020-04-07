package model.Dao;

import db.DB;
import model.Dao.impl.DepartmentDaoJDBC;
import model.Dao.impl.SellerDaoJDBC;

public class DaoFactory {
   
	//classe tera operção static para instanciar os daos
	// internamento ela implemetar um interfaces retornando uma classe de implementação
	 public static SellerDao createSellerDao() {
		 return new SellerDaoJDBC(DB.getConnection());
	 }
	 
	 public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
		 
	 }
}
