package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail1;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button btSave;
	@FXML
	private Button btDelete;

	private ObservableList<Department> obsList;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		// metodo que atualiza a lista
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDateChangeListeners();
			// fechar a janela
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.errors);
		} catch (DbException e) {
			Alerts.showAlert("Error saving dwwfobjtp", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDateChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Seller getFormData() {
		// criando um objeto vazio
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (txtEmail1.getText() == null || txtEmail1.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail1.getText());
		//verificar se  o campo da data esa vazio
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}else {
		//isso tudo  é como pegamos o  valor da data e colocamos no instant
		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
        obj.setBirthDate(Date.from(instant));
		}
        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
        	exception.addError("baseSalary", "Field can't be empty");
        }
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		// vamos varificar se realmente tem algum erro
        
        obj.setDepartment(comboBoxDepartment.getValue());
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// para fazer so aceitar numeros inteiros
		Constraints.setTextFieldInteger(txtId);
		// limitando a quantidade de caracteres
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail1, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
              
	    initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail1.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		// vamos fazer o sistema da data que esta cnfigurado no pc do cliente
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		//verificando se tem department nao esta null
		if(entity.getDepartment() == null) { 
			//se tiver null atribuimos  o primeiro elemento do combo department
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else 
			// caso tenha elementos associados, 
			comboBoxDepartment.setValue(entity.getDepartment());
	}

	// carregar os objetos associados
	public void loadAssociatedObjects() {
		// buscando os department associados
		List<Department> list = departmentService.findAll();
		// colota da lista
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		// verificando se contem
		/*if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}else {
			labelErrorName.setText("");
		}  vamos aprender a condição ternario*/
		labelErrorName.setText((fields.contains("name") ? errors.get("name"): ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email"): ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary"): ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate"): ""));
	
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
