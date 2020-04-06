package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
    //criando uma dependencia aq no departmentListcntroler
	private DepartmentService service; 
	@FXML
	private TableView<Department> tableViewDepartment;
	  
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> TableColumnName;
	
	@FXML
	private Button btNew;
	
	//carregar os department na obsList,
	 private ObservableList<Department> obsList;
	@FXML
	public void onBtNewAction () {
		System.out.println("teste new ");
	}
	//fazemos isso ao inves de fazer o new, fizemos uma inversao de controller
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
   //metodo  para iniciar componetes de minha tela
	private void initializeNodes() {
	      //padrao para iniciar o comportamento das colunas 
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		//para a tela do  table view ir ate o final,  mais um macete
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
     //metodo responsavel por acessar service, carregar e jogar os department na obsList
	public void updateTableView() {
		//fazendo  uma excesao para  programado nao esquecer
		//e significa  service esta instanciado
		if (service == null) {
			throw new IllegalStateException("Servicewas null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		
	}
}
