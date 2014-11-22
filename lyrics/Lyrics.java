package lyrics;

import java.util.ArrayList;

public class Lyrics {

	private ArrayList<LyricsLine> lines = new ArrayList<LyricsLine>();
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		if(lines.size()>0)
			result.append(lineToString(0));
		for(int i=1; i<lines.size(); i++) {
			result.append('\n');
			result.append(lineToString(i));
		}
		
		return result.toString();
	}
	private StringBuilder lineToString(int index) {
		StringBuilder result = new StringBuilder();
		if(lines.get(index).getType() > 0) {
			result.append(lines.get(index).getWon()).append('\n');
			if(lines.get(index).getType() > 1) {
				if(lines.get(index).getType() > 2)
					result.append(lines.get(index).getDok()).append('\n');
				result.append(lines.get(index).getHae()).append('\n');
			}
		}
		return result;
	}
	
	public Lyrics(String str) {
		this(str, 1);
	}
	public Lyrics(String str, int spacingLine) {
		if(spacingLine>1) {
			StringBuilder sl = new StringBuilder("\n");
			StringBuilder a = new StringBuilder("a");
			for(int i=0; i<spacingLine; i++) {
				sl.append('\n');
				a.append('a');
			}
			str = str.replace(sl, "\n\n");
			spacingLine = 1;
		}
		
		String[] lines = str.replace("\n\r",  "\n").split("\n");
		for(int i=0; i<lines.length; i++)
			lines[i] = lines[i].trim();
		for(int i=0; i<lines.length; i+=spacingLine) {
			if(lines[i].length()>1) {
				if(i+1<lines.length && lines[i+1].length()>1) {
					if(i+2<lines.length && lines[i+2].length()>1) {
						this.lines.add(new LyricsLine(lines[i], lines[i+1], lines[i+2], 3));
						i += 3;
						
					} else {
						this.lines.add(new LyricsLine(lines[i], lines[i], lines[i+1], 2));
						i += 2;
					}
				} else {
					this.lines.add(new LyricsLine(lines[i], lines[i], lines[i], 1));
					i++;
				}
			} else {
				this.lines.add(new LyricsLine());
				i += spacingLine;
			}
		}
	}
	public Lyrics(ArrayList<LyricsLine> lines) {
		this.lines = lines;
	}
	
	public LyricsLine get(int index) {
		return lines.get(index);
	}
	public int size() {
		return lines.size();
	}
}
