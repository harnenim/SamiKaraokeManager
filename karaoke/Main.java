package karaoke;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lyrics.Lyrics;
import lyrics.Utf32;

public class Main extends Application {

	static Stage primaryStage;
	static BorderPane root = new BorderPane();
	static BorderPane[] scene = new BorderPane[4];

	static String step2Text1 = "", step2Text2 = "", step2Text3 = "";

	// 각각의 줄
	static VBox[] step3Lines; 
	static int[] type;
	static KaraokeLine[] karaokeLines;
	static int[][][] wonGroups;
	static int[][][] furiGroups;
	static int[][]  doks; 
	static String[] haes;
	// 음절 개수
	static int[][] vWons;
	static int[][] vDoks;
	// 각 줄의 음절 개수 합계
	static int[] vWonSum;
	static int[] vDokSum;
	static Label[] vWonSumLabel;
	static Label[] vDokSumLabel;
	// 초기 포커스를 위해 어느 게 첫 번째 TextField인지를 알기 위함
	static ArrayList<TextField> vFields = new ArrayList<TextField>();
	static KanjiList kl = new KanjiList(); // 변수를 쓰진 않지만 객체의 static 변수 초기화
	
	static int[] spaces = Utf32.encode(" 　");
	static int[] buhos  = Utf32.encode("`1234567890-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?‘１２３４５６７８９０－＝「」￥；’、。・～!＠＃＄％＾＆＊（）＿＋｛｝｜：”＜＞？"); 
	static int[]   taks = Utf32.encode(" ゙ ゚゛゜");
	static int[][] yoOn = new int[5][];
	static int[][] kana = new int[6][];
	{
		yoOn[0] = Utf32.encode("ぁァゃゎャヮ");
		yoOn[1] = Utf32.encode("ぃィ");
		yoOn[2] = Utf32.encode("ぅゥゅュ");
		yoOn[3] = Utf32.encode("ぇェ");
		yoOn[4] = Utf32.encode("ぉォょョ");
		kana[0] = Utf32.encode("ぁあかがさざただなはばぱまゃやらゎわゕゖァアカガサザタダナハバパマャヤラヮワヷヵヶ");
		kana[1] = Utf32.encode("ぃいきぎしじちぢにひびぴみりゐィイキギシジチヂニヒビピミリヰヸ");
		kana[2] = Utf32.encode("ぅうくぐすずつづぬふぶぷむゅゆるゔゥウクグスズツヅヌフブプムュユルヴ");
		kana[3] = Utf32.encode("ぇえけげせぜてでねへべぺめれゑェエケゲセゼテデネヘベペメレヱヹ");
		kana[4] = Utf32.encode("ぉおこごそぞとどのほぼぽもょよろをォオコゴソゾトドノホボポモョヨロヲヺ");
		kana[5] = Utf32.encode("っんッンゝゞゟーヽヾ");
	}
	static public boolean isSpace(int c) {
		for(int i=0; i<spaces.length; i++)
			if(c==spaces[i])
				return true;
		
		return false;
	}
	static public boolean isBuho(int c) {
		for(int i=0; i<buhos.length; i++)
			if(c==buhos[i])
				return true;
		
		return false;
	}
	static public boolean isKana(int c) {
		for(int i=0; i<6; i++)
			for(int j=0; j<kana[i].length; j++)
				if(c==kana[i][j])
					return true;
		return false;
	}
	// 앞 글자와 요음을 이룰 수 있는가 여부
	static public boolean isYoOn(int[] cs, int i) {
		if(i<1) return false; // 앞 글자가 없을 때
		
		int j=0;
		while(j<taks.length && cs[i] != taks[j]) j++;
		if(j<taks.length) // 탁점 문자
			// 앞 글자가 가나라면 true
			return isKana(cs[i-1]);
		
		j=0; int k=0;
		while(j<yoOn.length) {
			k = 0;
			while(k<yoOn[j].length && cs[i] != yoOn[j][k]) k++;
			if(k<yoOn[j].length) break;
			j++;
		}
		if(j<yoOn.length) { // 요음 문자
			// 반모음 성분도 있는 요음
			if(k>=2) return true;
			
			// 모음 성분만 있는 요음
			for(k=0; k<6; k++)
				for(int l=0; l<kana[k].length; l++)
					if(cs[i-1]==kana[k][l]) { // 앞 글자가 가나이면서
						if(j==k) return false; // 모음이 같으면 장음이므로 false
						return true;
					}
		}		
		return false;
	}
	static public boolean hasJong(int c) {
		if(c<(int)'가') return false;
		if(c>(int)'힣') return false;
		if((c-(int)'가')%28 > 0) return true;
		return false;
	}

	static KaraokeLine[] step4Lines;
	
	@Override
	public void start(Stage primaryStage) {
		try{
			scene[0] = FXMLLoader.load(getClass().getResource("Step1.fxml"));
			root.setCenter(scene[0]);
			primaryStage.setScene(new Scene(root, 400, 700));
			primaryStage.setTitle("노래방 자막 만들기");
			primaryStage.show();
			this.primaryStage = primaryStage; // 창 크기 변경이 가능하도록 주긴 했는데 warning

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	static public void step1(Lyrics lyrics) {
		try {
			StringBuilder won = new StringBuilder();
			StringBuilder dok = new StringBuilder();
			StringBuilder hae = new StringBuilder();
			for(int i=0; i<lyrics.size(); i++) {
				won.append(lyrics.get(i).getWon()).append('\n');
				dok.append(lyrics.get(i).getDok()).append('\n');
				hae.append(lyrics.get(i).getHae()).append('\n');
			}
			step2Text1 = won.toString();
			step2Text2 = dok.toString();
			step2Text3 = hae.toString();

			scene[1] = FXMLLoader.load(new Main().getClass().getResource("Step2.fxml"));
			toStep(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void step2(Lyrics lyrics) {
		try {
			step3Lines = new VBox[lyrics.size()];
			type = new int[lyrics.size()];
			
			karaokeLines = new KaraokeLine[lyrics.size()];
			wonGroups  = new int[lyrics.size()][][];
			furiGroups = new int[lyrics.size()][][];
			vWons = new int[lyrics.size()][];
			doks  = new int[lyrics.size()][];
			vDoks = new int[lyrics.size()][];
			haes  = new String[lyrics.size()];
			
			// 각 줄의 음절 개수 합계
			vWonSum      = new int  [lyrics.size()];
			vDokSum      = new int  [lyrics.size()];
			vWonSumLabel = new Label[lyrics.size()];
			vDokSumLabel = new Label[lyrics.size()];
			
			for(int i=0; i<lyrics.size(); i++) {
				step3Lines[i] = new VBox();
				type[i] = lyrics.get(i).getType();
				vWonSum[i] = 0;
				vDokSum[i] = 0;
				vWonSumLabel[i] = new Label();
				vWonSumLabel[i].setAlignment(Pos.CENTER);
				vWonSumLabel[i].setPrefHeight(30);
				vDokSumLabel[i] = new Label();
				vDokSumLabel[i].setAlignment(Pos.CENTER);
				vDokSumLabel[i].setPrefHeight(30);
				
				switch(type[i]) {
					case 0 :{
						wonGroups[i] = new int[0][];
						vWons[i] = new int[0];
						doks [i] = new int[0];
						vDoks[i] = new int[0];
						haes [i] = "";
						break;
					}
					case 1 :{
						HBox won = lineStaticWonInit(i, lyrics.get(i).getWon());
						haes[i] = lyrics.get(i).getHae();
						step3Lines[i].getChildren().addAll(won);
						
						break;
					}
					case 2 :{
						HBox won = lineStaticWonInit(i, lyrics.get(i).getWon());
						haes [i] = lyrics.get(i).getHae();
						step3Lines[i].getChildren().addAll(won, new Label(lyrics.get(i).getHae()));
						
						break;
					}
					case 3 :{
						HBox won = lineWonInit(i, lyrics.get(i).getWon());
						HBox dok = lineDokInit(i, lyrics.get(i).getDok());
						haes [i] = lyrics.get(i).getHae();
						step3Lines[i].getChildren().addAll(won, dok, new Label(lyrics.get(i).getHae()));
						
						break;
					}
				}
			}

			scene[2] = FXMLLoader.load(new Main().getClass().getResource("Step3.fxml"));
			toStep(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 독음이 없을 경우 -> 원문에 별다른 처리를 거치지 않음
	static private HBox lineStaticWonInit(int i, String line) {
		int[] cs = Utf32.encode(line);
		wonGroups[i] = new int[1][];
		wonGroups[i][0] = cs;
		furiGroups[i] = new int[1][0];
		vWons[i] = new int[cs.length];
		doks [i] = cs;
		vDoks[i] = vWons[i];
		
		for(int j=0;j<cs.length; j++) {
			vWons[i][j] = 1;
			for(int k=0; k<spaces.length; k++) {
				if(cs[j]==spaces[k]) {
					vWons[i][j] = 0;
					break;
				}
			}
		}
		return new HBox(new Label(line));
	}
	static private HBox lineWonInit(int i, String line) {
		GridPane result = new GridPane();
		String[] ganaGroups = line.split("\\[|\\]");
		int[][] cs = new int[ganaGroups.length][];
		int cs_length = 0;
		wonGroups[i] = new int[ganaGroups.length][];
		furiGroups[i] = new int[ganaGroups.length][];
		for(int j=0; j<ganaGroups.length; j++) {
			String[] t = ganaGroups[j].split("\\|");
			ganaGroups[j] = t[0];
			cs[j] = Utf32.encode(ganaGroups[j]);
			wonGroups[i][j] = cs[j];
			cs[j] = Utf32.encode(ganaGroups[j]);
			furiGroups[i][j] =  t.length>1 ? Utf32.encode(t[1]) : new int[0];
			cs_length += cs[j].length;
		}
		vWons[i] = new int[cs_length];
		int lastPos = 0;
		Label lastLabel = new Label();

		result.getRowConstraints().addAll(
				new RowConstraints(4),
				new RowConstraints(),
				new RowConstraints()
			);
		
		int pos = 0;
		for(int j=0; j<ganaGroups.length; j++) {
			Label furi = new Label(Utf32.decode(furiGroups[i][j]));
			furi.setFont(new Font(7));
			result.add(furi, pos, 0);
			if(furiGroups[i][j].length>0) {
				result.add(new Label(ganaGroups[j]), pos, 1);
				for(int k=0; k<cs[j].length-1; k++) {
					ColumnConstraints col = new ColumnConstraints();
					col.setHalignment(HPos.CENTER);
					result.getColumnConstraints().add(col);
					vWons[i][pos+k] = 0;
				}
				vWons[i][pos+cs[j].length-1] = furiGroups[i][j].length;
				result.add(new Label(furiGroups[i][j].length + ""), pos, 2);
				
			} else {
				for(int k=0; k<cs[j].length; k++) {
					ColumnConstraints col = new ColumnConstraints();
					col.setHalignment(HPos.CENTER);
					result.getColumnConstraints().add(col);

					if(isSpace(cs[j][k])) {
						// 공백문자는 0음절
						result.add(new Label(new String(Utf32.decode(cs[j][k]))), pos+k, 1);
						vWons[i][pos+k] = 0;

					} else if(isBuho(cs[j][k])) {
						// 부호문자는 다르게 처리할 수도 있음
						result.add(new Label(new String(Utf32.decode(cs[j][k]))), pos+k, 1);
						vWons[i][pos+k] = 1;
						TextField v = new TextField("1");
						v.setPadding(new Insets(1));
						v.setMaxWidth(12);
						int tI = i;
						int tJ = pos+k;
						v.setOnKeyReleased(e->{
							try { vWons[tI][tJ] = Integer.parseInt(((TextField)e.getSource()).getText()); }
							catch (NumberFormatException e2) { vWons[tI][tJ] = 0; }
							vWonSum[tI] = 0;
							for(int x : vWons[tI])
								vWonSum[tI] += x;
							vWonSumLabel[tI].setText(vWonSum[tI]+"");
						});
						vFields.add(v);
						result.add(v, pos+k, 2);
		
					} else if(cs[j][k] < 128) {
						// ASCII 문자는 1음절
						result.add(new Label(new String(Utf32.decode(cs[j][k]))), pos+k, 1);
						vWons[i][pos+k] = 1;
						result.add(new Label("-"), pos+k, 2);
						
					} else if(isKana(cs[j][k])) {
						// 일본어는 기본 1음절
						result.add(new Label(new String(Utf32.decode(cs[j][k]))), pos+k, 1);
						vWons[i][pos+k] = 1;
						
						// 요음일 경우엔 앞 글자 0음절
						if(isYoOn(cs[j], k)) {
							vWons[i][pos+k-1] = 0;
							result.getChildren().remove(lastLabel);
							result.add(lastLabel, lastPos, 2, pos+k-lastPos+1, 1);
							
						} else {
							lastPos = pos + k;
							lastLabel = new Label("-");
							result.add(lastLabel, lastPos, 2);
						}
		
					} else {
						int[] vKanji = KanjiList.vLength(cs[j], k);
						for(int l=0; l<vKanji.length; l++) {
							result.add(new Label(new String(Utf32.decode(cs[j][k]))), pos+k, 1);
							vWons[i][pos+k] = vKanji[l];
							TextField v = new TextField(""+vKanji[l]);
							v.setPadding(new Insets(1));
							v.setMaxWidth(12);
							int tI = i;
							int tJ = pos+k;
							v.setOnKeyReleased(e->{
								try { vWons[tI][tJ] = Integer.parseInt(((TextField)e.getSource()).getText()); }
								catch (NumberFormatException e2) { vWons[tI][tJ] = 0; }
								vWonSum[tI] = 0;
								for(int x : vWons[tI])
									vWonSum[tI] += x;
								vWonSumLabel[tI].setText(vWonSum[tI]+"");
							});
							vFields.add(v);
							result.add(v, pos+k, 2);
							k++;
						}
						k--;
					}
				}
				
			}
			pos += cs[j].length;
		}
		vWonSum[i] = 0;
		for(int x : vWons[i])
			vWonSum[i] += x;
		vWonSumLabel[i].setText(vWonSum[i]+"");
		result.add(vWonSumLabel[i], cs_length, 1, 1, 2);
		return new HBox(result);
	}
	static private HBox lineDokInit(int i, String line) {
		GridPane result = new GridPane();
		int[] cs = Utf32.encode(line);
		doks [i] = cs;
		vDoks[i] = new int[cs.length];
		int lastPos = 0;
		Label lastLabel = new Label();

		for(int j=0;j<cs.length; j++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			result.getColumnConstraints().add(col);
			
			result.add(new Label(new String(Utf32.decode(cs[j]))), j, 0);
			
			if(isSpace(cs[j])) {
				// 공백문자는 0음절
				vDoks[i][j] = 0;

			} else if(isBuho(cs[j])) {
				// 부호문자는 다르게 처리할 수도 있음
				vDoks[i][j] = 1;
				TextField v = new TextField("1");
				v.setPadding(new Insets(1));
				v.setMaxWidth(12);
				int tI = i;
				int tJ = j;
				v.setOnKeyReleased(e->{
					try { vDoks[tI][tJ] = Integer.parseInt(((TextField)e.getSource()).getText()); }
					catch (NumberFormatException e2) { vDoks[tI][tJ] = 0; }
					vDokSum[tI] = 0;
					for(int x : vDoks[tI])
						vDokSum[tI] += x;
					vDokSumLabel[tI].setText(vDokSum[tI]+"");
				});
				vFields.add(v);
				result.add(v, j, 1);

			} else if(cs[j] < 128) {
				// ASCII 문자는 1음절
				vDoks[i][j] = 1;
				result.add(new Label("-"), j, 1);
				
			} else if(isKana(cs[j])) {
				// 일본어는 기본 1음절
				vDoks[i][j] = 1;
				
				// 요음일 경우엔 앞 글자 0음절
				if(isYoOn(cs, j)) {
					vDoks[i][j-1] = 0;
					result.getChildren().remove(lastLabel);
					result.add(lastLabel, lastPos, 2, j-lastPos+1, 1);
					
				} else {
					lastPos = j;
					lastLabel = new Label("-");
					result.add(lastLabel, lastPos, 2);
				}

			} else if(cs[j]>='가' && cs[j]<='힣') {
				// 한글은 기본 1음절
				vDoks[i][j] = 1;
				
				// 종성이 있을 경우 음절 불확실
				if(hasJong(cs[j])) {
					TextField v = new TextField("1");
					v.setPadding(new Insets(1));
					v.setMaxWidth(12);
					int tI = i;
					int tJ = j;
					v.setOnKeyReleased(e->{
						try { vDoks[tI][tJ] = Integer.parseInt(((TextField)e.getSource()).getText()); }
						catch (NumberFormatException e2) { vDoks[tI][tJ] = 0; }
						vDokSum[tI] = 0;
						for(int x : vDoks[tI])
							vDokSum[tI] += x;
						vDokSumLabel[tI].setText(vDokSum[tI]+"");
					});
					vFields.add(v);
					result.add(v, j, 1);

				} else {
					result.add(new Label("-"), j, 1);
				}

			} else {
				vDoks[i][j] = 1;
				TextField v = new TextField("1");
				v.setPadding(new Insets(1));
				v.setMaxWidth(12);
				int tI = i;
				int tJ = j;
				v.setOnKeyReleased(e->{
					try { vDoks[tI][tJ] = Integer.parseInt(((TextField)e.getSource()).getText()); }
					catch (NumberFormatException e2) { vDoks[tI][tJ] = 0; }
					vDokSum[tI] = 0;
					for(int x : vDoks[tI])
						vDokSum[tI] += x;
					vDokSumLabel[tI].setText(vDokSum[tI]+"");
				});
				vFields.add(v);
				result.add(v, j, 1);
			}

		}
		vDokSum[i] = 0;
		for(int x : vDoks[i])
			vDokSum[i] += x;
		vDokSumLabel[i].setText(vDokSum[i]+"");
		result.add(vDokSumLabel[i], cs.length, 0, 1, 2);
		return new HBox(result);
	}
	
	static public void step3() {
		try {
			step4Lines = new KaraokeLine[type.length];
	    	for(int i=0; i<type.length; i++)
	    		step4Lines[i] = new KaraokeLine(
						wonGroups [i],
						furiGroups[i],
						vWons     [i],
						doks      [i],
						vDoks     [i],
						haes      [i],
						type      [i]
					);
			scene[3] = FXMLLoader.load(new Main().getClass().getResource("Step4.fxml"));
			toStep(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void toStep(int n) {
		root.setCenter(scene[n]);
		if(n==0) {
			primaryStage.setX(primaryStage.getX()+primaryStage.getWidth()/2-200);
			primaryStage.setWidth(400);
		} else if(n==1) {
			primaryStage.setX(primaryStage.getX()+primaryStage.getWidth()/2-400);
			primaryStage.setWidth(800);
		} else if(n==2) {
			primaryStage.setX(primaryStage.getX()+primaryStage.getWidth()/2-200);
			primaryStage.setWidth(400);
		} else if(n==3) {
			primaryStage.setX(primaryStage.getX()+primaryStage.getWidth()/2-200);
			primaryStage.setWidth(400);
		}
	}
}
