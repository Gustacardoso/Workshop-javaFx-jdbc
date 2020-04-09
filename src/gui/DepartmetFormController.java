package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmetFormController implements Initializable{
    
	private Department entity;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSave;
	@FXML
	private Button btDelete;
	
	public void setDepartament(Department entity) {
		this.entity = entity;
	}
	@FXML
	public void onBtSaveAction() {
		System.out.println("button save");
	}
	@FXML
	public void onBtCancelAction() {
		System.out.println("button cancel");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
	}
	private void initializeNodes() {
		//para fazer so  aceitar numeros inteiros
		Constraints.setTextFieldInteger(txtId);
		//limitando  a quantidade de caracteres
		Constraints.setTextFieldMaxLength(txtName, 30);
		
	}
	public  void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	

}
