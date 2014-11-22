package karaoke;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lyrics.Lyrics;

public class Step1 implements Initializable {
	
    @FXML
    private TextField spacingLine;

    @FXML
    private TextArea step1Area;

    @FXML
    void next(ActionEvent event) {
    	int sl;
    	try {
    		sl = Integer.parseInt(spacingLine.getText());
    	} catch(NumberFormatException e) {
    		sl = 2;
    	}
    	Lyrics lyrics = new Lyrics(step1Area.getText(), sl);
    	
    	Main.step1(lyrics);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						step1Area.requestFocus();
					}
				});
			}
		}).start();
	}
}
