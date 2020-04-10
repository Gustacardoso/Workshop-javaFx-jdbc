package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {
	// criando uma dependencia aq no departmentListcntroler
	private DepartmentService service;
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> TableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;
	@FXML
	private Button btNew;

	// carregar os department na obsList,
	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		// temor que pegar ocurrentStage e colocar dentro da variavel stage, la da
		// classe util
		Stage parentStage = Utils.currentStage(event);
		/*
		 * update, no button. como é um buton para cadastrar novo departemente ele vai
		 * iniciar vazio, ai temos que instanciar o department, assim temos que colocar
		 * um parametro a mais no createDialogForm.
		 */
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);

	}

	// fazemos isso ao inves de fazer o new, fizemos uma inversao de controller
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// ese
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// metodo para iniciar componetes de minha tela
	private void initializeNodes() {
		// padrao para iniciar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// para a tela do table view ir ate o final, mais um macete
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// metodo responsavel por acessar service, carregar e jogar os department na
	// obsList
	public void updateTableView() {
		// fazendo uma excesao para programado nao esquecer
		// e significa service esta instanciado
		if (service == null) {
			throw new IllegalStateException("Servicewas null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	// tem que instanciar Stage
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			// logica para abrir nossa janela de formulario
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			// estamos pegando uma referencia para o controlador
			DepartmentFormController controller = loader.getController();
			// injetando
			controller.setDepartament(obj);
			controller.setDepartmentService(new DepartmentService());
			// me inscrevendo para o evento
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// colocar uma tela na frente da outra
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("Io Exception", "Erre loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	// esse metodo coloca um botao de edição no departmentlistcontroller
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to del");
	   if (result.get() == ButtonType.OK) {
		 if(service == null) {
			 throw new IllegalStateException("Service was null");
		 }
		 try {
			service.remove(obj);
			updateTableView();
		} catch (DbIntegrityException e) {
			Alerts.showAlert("Error removing objec", null, e.getMessage(), AlertType.ERROR);
		}
	}
	}

}
