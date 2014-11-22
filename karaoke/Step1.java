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
//		step1Area.setText("掴摑😁🙏🚀😂🈳\n이게 뭐시여\n\n\n吐き出したその[生命|いのち]は\n하키다시타 소노 이노치와\n내뱉어진 그 생명은\n\n\n\n\nでしょ？\n데쇼?\n그렇지?\n\n\n\n\nねえ　愛したら誰もが\n네에 아이시타라 다레모가\n얘, 사랑하면 누구나\n\n\nこんな孤独になるの？\n코은나 코도쿠니 나루노?\n이렇게 고독해지는 거니?\n\n\nねえ　暗闇よりも深い苦しみ\nねえ　くらやみよりもふかいくるしみ\n얘, 어둠보다도 깊은 괴로움\n\n\n抱きしめてるの？\n다키시메테루노?\n끌어안고 있니?\n\n\n何もかもが二人輝くため　きっと\n나니모카모가 후타리 카가야쿠 타메 키잇토\n모든 것이 두 사람을 빛내기 위해서야, 분명\n\n\n君を君を愛してる\n키미오 키미오 아이시테루\n그대를 그대를 사랑해\n\n\n心で見つめている\n코코로데 미츠메테이루\n마음으로 바라보고 있어\n\n\n君を君を信じてる\n키미오 키미오 시은지테루\n그대를 그대를 믿고 있어\n\n\n寒い夜も\n사무이 요루모\n추운 밤에도\n\n\n\n\n淚で今　呼びかける\n나미다데 이마 요비카케루\n눈물로 지금 그대를 불러\n\n\n約束などいらない\n야쿠소쿠나도 이라나이\n약속 따윈 필요 없어\n\n\n君がくれた大切な強さだから\n키미가 쿠레타 타이세츠나 츠요사다카라\n그대가 준 소중한 힘이니까\n\n\n");
//		step1Area.setText("掴摑😁🙏🚀😂🈳\n이게 뭐시여\n\n\n\n\n吐き出したその[生命|いのち]は\n하키다시타 소노 이노치와\n내뱉어진 그 생명은\n\n\n吐き出したその生命は\n하키다시타 소노 이노치와\n내뱉어진 그 생명은");
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
