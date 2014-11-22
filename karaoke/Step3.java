package karaoke;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Step3 implements Initializable {
	
    @FXML
    private Button btnNext;

    @FXML
    private VBox content;
    
    @FXML
    void prev(ActionEvent event) {
    	Main.toStep(1);
    }
    @FXML
    void next(ActionEvent event) {
    	if(!isValid) return;
    	Main.step3();
    }

	static boolean isValid = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		content.getChildren().setAll(Main.step3Lines);
		sumRefresh();
		
		if(Main.vFields.size()>0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Main.vFields.get(0).requestFocus();
						}
					});
				}
			}).start();
		}

		content.setOnKeyReleased(e->{
			sumRefresh();
		});
	}
	public void sumRefresh() {
		boolean valid = true;
		for(int i=0; i<Main.step3Lines.length; i++) {
			if(Main.type[i]<3 || Main.vWonSum[i]==Main.vDokSum[i]) {
				Main.vWonSumLabel[i].setTextFill(Color.BLACK);
				Main.vDokSumLabel[i].setTextFill(Color.BLACK);
			} else {
				Main.vWonSumLabel[i].setTextFill(Color.RED);
				Main.vDokSumLabel[i].setTextFill(Color.RED);
				valid = false;
			}
		}
		isValid = valid;
		if(isValid)
			btnNext.setText("다음");
		else
			btnNext.setText("유효하지 않습니다.");
	}

}
