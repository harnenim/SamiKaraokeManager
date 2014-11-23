package karaoke;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lyrics.Utf32;

public class KaraokeLine {
	private WonGroup[] won;  // 원문을 후리가나 여부로 자른 그룹
	private int[]      dok;  // 독음 문자열 
	private int[]      vDok; // 독음 글자별 음절 수
	private String     hae;  // 번역문 - beon 같은 변수명은 이상함(?)
	private int length;
	private int type;
	public int getType() {
		return type;
	}

	private interface Preset {
		public HBox   get    (KaraokeLine line, int pos);
		public String getSAMI(KaraokeLine line, int pos);
	}
	private static Preset preset;
	private static Color cw1;
	private static Color cw2;
	private static Color cd1;
	private static Color cd2;
	private static Color ch;
	
	public KaraokeLine(int[][] wonGroups, int[][] furiGroups, int[] vWon, int[] dok, int[] vDok, String hae, int type) {
		WonGroup[] wonGroup = new WonGroup[wonGroups.length];
		int pos = 0;
		int length = 0;
		for(int j=0; j<wonGroups.length; j++) {
			if(furiGroups[j].length > 0) { // 후리가나 있음
				wonGroup[j] = new WonGroup(wonGroups[j], true, furiGroups[j]);
				length += furiGroups[j].length;
			} else {
				int[] vWonGroup = new int[wonGroups[j].length];
				for(int k=0; k<wonGroups[j].length; k++) {
					vWonGroup[k] = vWon[pos+k];
					length += vWonGroup[k];
				}
				wonGroup[j] = new WonGroup(wonGroups[j], false, vWonGroup);
			}
			pos += wonGroups[j].length;
		}

		this.won  = wonGroup;
		this.dok  = dok;
		this.vDok = vDok;
		this.hae  = hae;
		this.length = length;
		this.type = type;
	}

	public static void preset(int preset) {
		switch(preset) {
			case 0 : KaraokeLine.preset = new Preset() {
				@Override
				public HBox get(KaraokeLine line, int pos) {
					VBox result = new VBox();
					result.getChildren().add(line.getWon(pos));
					if(line.getType()>1) result.getChildren().add(line.getHae());
					return new HBox(result);
				}
				@Override
				public String getSAMI(KaraokeLine line, int pos) {
					StringBuilder result = new StringBuilder();
					result.append(line.getWonSAMI(pos));
					if(line.getType()>1) result.append("<br>").append(line.getHaeSAMI());
					return result.toString();
				}
			}; break;
			
			case 1 : KaraokeLine.preset = new Preset() {
				@Override
				public HBox   get    (KaraokeLine line, int pos) {
					VBox result = new VBox();
					result.getChildren().add(line.getWon(pos));
					if(line.getType()>1) result.getChildren().add(line.getHae(true));
					return new HBox(result);
				}
				@Override
				public String getSAMI(KaraokeLine line, int pos) {
					StringBuilder result = new StringBuilder();
					result.append(line.getWonSAMI(pos));
					if(line.getType()>1) result.append("<br>").append(line.getHaeSAMI(true));
					return result.toString();
				}
			}; break;
			
			case 2 : KaraokeLine.preset = new Preset() {
				@Override
				public HBox   get    (KaraokeLine line, int pos) {
					VBox result = new VBox();
					result.getChildren().add(line.getWon(pos));
					if(line.getType()>2) result.getChildren().add(line.getDok(pos));
					if(line.getType()>1) result.getChildren().add(line.getHae(true));
					return new HBox(result);
				}
				@Override
				public String getSAMI(KaraokeLine line, int pos) {
					StringBuilder result = new StringBuilder();
					result.append(line.getWonSAMI(pos));
					if(line.getType()>2) result.append("<br>").append(line.getDokSAMI(pos));
					if(line.getType()>1) result.append("<br>").append(line.getHaeSAMI(true));
					return result.toString();
				}
			}; break;
			
			case 3 : KaraokeLine.preset = new Preset() {
				@Override
				public HBox   get    (KaraokeLine line, int pos) {
					VBox result = new VBox();
					if(line.getType()>2) result.getChildren().add(line.getDok(pos, 7));
					result.getChildren().add(line.getWon(pos));
					if(line.getType()>1) result.getChildren().add(line.getHae(true));
					return new HBox(result);
				}
				@Override
				public String getSAMI(KaraokeLine line, int pos) {
					StringBuilder result = new StringBuilder();
					if(line.getType()>2) result.append("<RUBY>");
					result.append(line.getWonSAMI(pos));
					if(line.getType()>2) result.append("<RT><RP>(</RP>").append(line.getDokSAMI(pos)).append("<RP>)</RP></RT></RUBY>");
					if(line.getType()>1) result.append("<br>").append(line.getHaeSAMI(true));
					return result.toString();
				}
			}; break;
		}
	}
	public static void setColor(Color cw1, Color cw2, Color cd1, Color cd2, Color ch) {
		KaraokeLine.cw1 = cw1;
		KaraokeLine.cw2 = cw2;
		KaraokeLine.cd1 = cd1;
		KaraokeLine.cd2 = cd2;
		KaraokeLine.ch  = ch ;
	}
	
	public int length() {
		return length;
	}
	public int type() {
		return type;
	}

	public HBox get(int pos) {
		return preset.get(this, pos);
	}
	
	public HBox getWon(int pos) {
		if(this.length==0)
			return new HBox();

		GridPane result = new GridPane();
		int passed = 0; 
		for(int i=0; i<won.length; i++) {
			if(won[i].hasFurigana())
				result.add(won[i].getFurigana(pos-passed, cw1, cw2), i, 0);
			result.add(won[i].getWon(pos-passed, cw1, cw2), i, 1);
			passed += won[i].length();
		}
		return new HBox(result);
	}
	public HBox getDok(int pos) {
		if(this.length==0)
			return new HBox();
		return getChild(dok, vDok, pos, cw1, cw2);
	}
	public HBox getDok(int pos, int size) {
		if(this.length==0)
			return new HBox();
		return getChild(dok, vDok, pos, cd1, cd2, size);
	}
	public HBox getHae(boolean withColor) {
		Label result = new Label(hae);
		if(withColor)
			result.setTextFill(ch);
		else
			result.setTextFill(Color.WHITE);
		return new HBox(result);
	}
	public HBox getHae() {
		return getHae(false);
	}
	
	public static HBox getChild(int[] c, int[] v, int pos, Color c1, Color c2) {
		return getChild(c, v, pos, c1, c2, 12);
	}
	public static HBox getChild(int[] c, int[] v, int pos, Color c1, Color c2, int size) {
		HBox result = new HBox();
		result.setAlignment(Pos.CENTER);
		Label label = new Label();
		label.setFont(new Font(size));

		int length = 0;
		for(int x : v) length += x;

		if(pos <= 0) {
			label.setText(new String(Utf32.decode(c)));
			label.setTextFill(c2);
			result.getChildren().add(label);

		} else if(pos >= length) {
			label.setText(new String(Utf32.decode(c)));
			label.setTextFill(c1);
			result.getChildren().add(label);

		} else do {
			StringBuilder text = new StringBuilder();
			int count = 0;
			int i = -1;
			int j = 0;

			// 글자 색 이미 변한 것들 
			label.setTextFill(c1);
			for(;;j++) {
				if(v[j]>0 || Main.isSpace(c[j])) {
					if(count+v[j] > pos)
						break;
					count += v[j];
					while(i+1<=j)
						text.append(Utf32.decode(c[++i]));
				}
			}
			double passed = i + (double)(pos-count)*(j-i)/v[j];
			while(i+1<=passed)
				text.append(Utf32.decode(c[++i]));
			if(++i > 0) {
				label.setText(text.toString());
				result.getChildren().add(label);
				if(i>=c.length)
					break;
			}
			
			// 걸친 문자
			if(i<=j) {
				label = new Label();
				label.setFont(new Font(size));
				passed -= Math.floor(passed);
				label.setTextFill(CM.color(c1, c2, passed));
				label.setText(new String(Utf32.decode(c[i++])));
				result.getChildren().add(label);
				if(i>=c.length)
					break;
			}

			// 나머지 - 아직 변하기 전
			label = new Label();
			label.setFont(new Font(size));
			label.setTextFill(c2);
			text = new StringBuilder();
			for(;i<c.length;i++)
				text.append(Utf32.decode(c[i]));
			label.setText(text.toString());
			result.getChildren().add(label);
			
		} while(false);

		return result;
	}
	
	public String getSAMI(int pos) {
		return preset.getSAMI(this, pos);
	}

	public String getWonSAMI(int pos) {
		if(this.length==0)
			return "&nbsp;";

		StringBuilder result = new StringBuilder();
		int passed = 0;
		for(WonGroup x : won) {
			result.append(x.getSAMI(pos-passed, cw1, cw2));
			passed += x.length();
		}
		return result.toString();
	}
	public String getDokSAMI(int pos) {
		if(this.length==0)
			return "&nbsp;";
		return new StringBuilder(getChildSAMI(dok, vDok, pos, cd1, cd2)).toString();
	}
	public String getHaeSAMI(boolean withColor) {
		if(withColor)
			return new StringBuilder("<FONT color=\"#").append(CM.code(ch)).append("\">").append(this.hae).append("</FONT>").toString();
		return getHaeSAMI();
	}
	public String getHaeSAMI() {
		return this.hae;
	}

	public static String getChildSAMI(int[] c, int[] v, int pos, Color c1, Color c2) {
		StringBuilder result = new StringBuilder();
		
		int length = 0;
		for(int x : v) length += x;

		if(pos <= 0) {
			result.append("<FONT color=\"#").append(CM.code(c2)).append("\">")
			      .append(new String(Utf32.decode(c)))
			      .append("</FONT>");
			
		} else if(pos >= length) {
			result.append("<FONT color=\"#").append(CM.code(c1)).append("\">")
			      .append(new String(Utf32.decode(c)))
			      .append("</FONT>");
			
		} else do {
			int count = 0;
			int i = -1;
			int j = 0;

			// 글자 색 이미 변한 것들 
			result.append("<FONT color=\"#").append(CM.code(c1)).append("\">");
			for(;;j++) {
				if(v[j]>0 || Main.isSpace(c[j])) {
					if(count+v[j] > pos)
						break;
					count += v[j];
					while(i+1<=j)
						result.append(Utf32.decode(c[++i]));
				}
			}
			double passed = i + (double)(pos-count)*(j-i)/v[j];
			while(i+1<=passed)
				result.append(Utf32.decode(c[++i]));
			if(i++>=0) {
				result.append("</FONT>");
				if(i>=c.length)
					break;
			} else {
				result = new StringBuilder();
			}
			
			// 걸친 문자
			if(i<=j) {
				passed -= Math.floor(passed);
				result.append("<FONT color=\"#").append(CM.code(c1, c2, passed)).append("\">");
				result.append(Utf32.decode(c[i++]));
				result.append("</FONT>");
				if(i>=c.length)
					break;
			}

			// 나머지 - 아직 변하기 전
			result.append("<FONT color=\"#").append(CM.code(c2)).append("\">");
			for(;i<c.length;i++) {
				result.append(Utf32.decode(c[i]));
			}
			result.append("</FONT>");
			
		} while(false);
		
		return result.toString();
	}
}

class WonGroup {
	private int[] won;      // 문자열
	private int[] furigana; // 후리가나
	private int[] vWon;     // 글자별 음절 수
	private int length = 0; // 포함 음절 수

	public WonGroup(int[] won, boolean hasFurigana, int[] arg) {
		this.won = won;
		
		if(hasFurigana) {
			// 후리가나 없을 때 생성자 : arg -> 후리가나
			furigana = arg;
			length = furigana.length;
			vWon = new int[won.length];
			for(int i=0; i<won.length-1; i++)
				this.vWon[i] = 0;
			this.vWon[won.length-1] = this.length;
			
		} else {
			// 후리가나 없을 때 생성자 : arg -> 음절 수
			furigana = new int[0];
			vWon = arg;
			for(int x : vWon)
				length += x;
		}
	}
	public boolean hasFurigana() {
		return furigana.length>0 ? true : false ; 
	}
	public int length() {
		return length;
	}
	// 미리보기용
	public HBox getWon(int pos, Color c1, Color c2) {
		if(won.length==0)
			return new HBox();
		return KaraokeLine.getChild(won, vWon, pos, c1, c2);
	}
	public HBox getFurigana(int pos, Color c1, Color c2) {
		if(furigana.length==0)
			return new HBox();
		int[] vFuri = new int[furigana.length];
		for(int i=0; i<vFuri.length; i++)
			vFuri[i] = 1;
		return KaraokeLine.getChild(furigana, vFuri, pos, c1, c2, 7);
	}
	// HTML 소스 출력용
	public String getSAMI(int pos, Color c1, Color c2) {
		if(hasFurigana())
			return getSAMIWithFurigana(pos, c1, c2);
		
		return KaraokeLine.getChildSAMI(won, vWon, pos, c1, c2);
	}
	private String getSAMIWithFurigana(int pos, Color c1, Color c2) {
		StringBuilder result = new StringBuilder("<RUBY>");

		if(pos <= 0) {
			result.append("<FONT color=\"#").append(CM.code(c2)).append("\">")
			      .append(new String(Utf32.decode(won)))
			      .append("</FONT>")
			      .append("<RT><RP>(</RP>")
					  .append("<FONT color=\"#").append(CM.code(c2)).append("\">")
					  .append(new String(Utf32.decode(furigana)))
					  .append("</FONT>")
				  .append("<RP>)</RP></RT>");
			
		} else if(pos >= length) {
			result.append("<FONT color=\"#").append(CM.code(c1)).append("\">")
			      .append(new String(Utf32.decode(won)))
			      .append("</FONT>")
			      .append("<RT><RP>(</RP>")
					  .append("<FONT color=\"#").append(CM.code(c1)).append("\">")
					  .append(new String(Utf32.decode(furigana)))
					  .append("</FONT>")
				  .append("<RP>)</RP></RT>");
			
		} else {
			StringBuilder wonmun = new StringBuilder();
			StringBuilder furi   = new StringBuilder();
			do {
				wonmun.append("<FONT color=\"#").append(CM.code(c1)).append("\">");
				int i = 0;
				double passed = (double)won.length*pos/length;
				while(i+1<=passed)
					wonmun.append(Utf32.decode(won[i++]));
				if(i>0)
					wonmun.append("</FONT>");
				else
					wonmun = new StringBuilder();
				
				// 걸친 문자
				if(i<passed) {
					passed -= i;
					wonmun.append("<FONT color=\"#").append(CM.code(c1, c2, passed)).append("\">").append(Utf32.decode(won[i++])).append("</FONT>");
					if(i>=won.length)
						break;
				}
	
				// 나머지 - 아직 변하기 전
				wonmun.append("<FONT color=\"#").append(CM.code(c2)).append("\">");
				for(;i<won.length;i++)
					wonmun.append(Utf32.decode(won[i]));
			    wonmun.append("</FONT>");
			} while(false);
			    
			furi.append("<FONT color=\"#").append(CM.code(c1)).append("\">");
			int i = 0;
			for(; i<pos; i++)
				furi.append(Utf32.decode(furigana[i]));
			furi.append("</FONT>");
			furi.append("<FONT color=\"#").append(CM.code(c2)).append("\">");
			for(; i<furigana.length; i++)
				furi.append(Utf32.decode(furigana[i]));
			furi.append("</FONT>");
			result.append(wonmun).append("<RT><RP>(</RP>").append(furi).append("<RP>)</RP></RT>");
		}

	    result.append("</RUBY>");
		return result.toString();
	}
}

final class CM {
	public static String code(Color c) {
		StringBuilder result = new StringBuilder();
		int
		t = (int)(c.getRed  ()*255);
		result.append((char)(t/16<10 ? t/16+48 : t/16+55))
		      .append((char)(t%16<10 ? t%16+48 : t%16+55));
		t = (int)(c.getGreen()*255);
		result.append((char)(t/16<10 ? t/16+48 : t/16+55))
		      .append((char)(t%16<10 ? t%16+48 : t%16+55));
		t = (int)(c.getBlue ()*255);
		result.append((char)(t/16<10 ? t/16+48 : t/16+55))
		      .append((char)(t%16<10 ? t%16+48 : t%16+55));
		return result.toString();
	}
	public static String code(Color c1, Color c2, double pos) {
		return code(color(c1, c2, pos));
	}
	public static Color color(Color c1, Color c2, double pos) {
		if(pos<0) pos = 0;
		else if(pos>1) pos = 1;
		
		return new Color(
				c1.getRed  ()*pos + c2.getRed  ()*(1-pos),
				c1.getGreen()*pos + c2.getGreen()*(1-pos),
				c1.getBlue ()*pos + c2.getBlue ()*(1-pos),
				1
			);
	}
}
