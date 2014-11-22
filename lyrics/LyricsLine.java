package lyrics;

public class LyricsLine {
	private String  won;
	private String  dok;
	private String  hae;
	private int     type;
	
	public LyricsLine() {
		this("","","",0);
	}
	public LyricsLine(String won, String dok, String hae, int type) {
		this.won  = won;
		this.dok  = dok;
		this.hae  = hae;
		this.type = type;
	}
	public String getWon() {
		return won;
	}
	public String getDok() {
		return dok;
	}
	public String getHae() {
		return hae;
	}
	public int getType() {
		return type;
	}	
}
