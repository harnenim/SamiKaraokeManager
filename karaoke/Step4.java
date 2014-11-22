package karaoke;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Step4 implements Initializable {

    @FXML
    private VBox review;
    @FXML
    private TextArea output;

    @FXML
    private ColorPicker cWon1;
    @FXML
    private ColorPicker cWon2;
    @FXML
    private ColorPicker cDok2;
    @FXML
    private ColorPicker cDok1;
    @FXML
    private ColorPicker cHae;
    @FXML
    private RadioButton radio0;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;

    @FXML
    void refresh(ActionEvent event) {
    	refresh();
    }

    @FXML
    void set0(ActionEvent event) {
    	type = 0;
    	clearRadioButton();
    	refresh();
    }
    @FXML
    void set1(ActionEvent event) {
    	type = 1;
    	clearRadioButton();
    	refresh();
    }
    @FXML
    void set2(ActionEvent event) {
    	type = 2;
    	clearRadioButton();
    	refresh();
    }
    @FXML
    void set3(ActionEvent event) {
    	type = 3;
    	clearRadioButton();
    	refresh();
    }
    
    @FXML
    void prev(ActionEvent event) {
    	Main.toStep(2);
    }
    
    public void clearRadioButton() {
    	if(type!=0) radio0.setSelected(false);
    	if(type!=1) radio1.setSelected(false);
    	if(type!=2) radio2.setSelected(false);
    	if(type!=3) radio3.setSelected(false);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cWon1.setValue(Color.AQUA);
		cWon2.setValue(Color.BLUE);
		cDok1.setValue(Color.AQUA);
		cDok2.setValue(Color.BLUE);
		cHae .setValue(Color.WHITE);
		refresh();
	}
	
	static int type = 0;
	public void refresh() {
		Color cw1 = cWon1.getValue();
		Color cw2 = cWon2.getValue();
		Color cd1 = cDok1.getValue();
		Color cd2 = cDok2.getValue();
		Color ch  = cHae .getValue();
		
		KaraokeLine.preset(type);
		KaraokeLine.setColor(cw1, cw2, cd1, cd2, ch);

		review.getChildren().clear();
		StringBuilder outputString = new StringBuilder();
		for(int i=0; i<Main.step4Lines.length; i++) {
			if(Main.step4Lines[i].length()==0) {
				review.getChildren().add(new Label());
				outputString.append("&nbsp;\n");
				continue;
			}
			for(int j=1; j<=Main.step4Lines[i].length(); j++) {
				review.getChildren().add(Main.step4Lines[i].get(j));
				outputString.append(Main.step4Lines[i].getSAMI (j)).append('\n');
			}
		}
		output.setText(outputString.toString());
	}

}
