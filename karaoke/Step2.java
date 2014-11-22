package karaoke;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lyrics.Lyrics;
import lyrics.LyricsLine;

public class Step2 implements Initializable {

    @FXML
    private TextArea step2Area1;

    @FXML
    private TextArea step2Area2;

    @FXML
    private TextArea step2Area3;

    @FXML
    void prev(ActionEvent event) {
    	Main.toStep(0);
    }
    @FXML
    void next(ActionEvent event) {
    	String[] wonLines = step2Area1.getText().split("\n");
    	String[] dokLines = step2Area2.getText().split("\n");
    	String[] haeLines = step2Area3.getText().split("\n");
    	int lineNum = wonLines.length<dokLines.length ? wonLines.length : dokLines.length<haeLines.length ? dokLines.length : haeLines.length ;
    	ArrayList<LyricsLine> result = new ArrayList<LyricsLine>();
    	
    	for(int i=0; i<lineNum; i++) {
    		int type;
    		if(wonLines[i].length()>0) {
    			if(wonLines[i].equals(dokLines[i])) {
    				if(wonLines[i].equals(haeLines[i])) {
    					type = 1;
    				} else {
    					type = 2;
    				}
    			} else {
					type = 3;
    			}
    		} else {
				type = 0;
    		}
    		result.add(new LyricsLine(wonLines[i], dokLines[i], haeLines[i], type));
    	}
    	
    	Main.step2(new Lyrics(result));
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		step2Area1.setText(Main.step2Text1);
		step2Area2.setText(Main.step2Text2);
		step2Area3.setText(Main.step2Text3);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						step2Area1.requestFocus();
					}
				});
			}
		}).start();
	}
}
