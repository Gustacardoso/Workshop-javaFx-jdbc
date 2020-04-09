package gui.util;



import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	//funçao de palco principal
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node) event.getSource()).getScene().getWindow();
	}
	//fizemos esse processo para facilitar, para nnao ler o dadod de uma caixa que é numero  inteiro.
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException e ) {
			return null;
		}
	}

}
