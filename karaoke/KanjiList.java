package karaoke;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import lyrics.Utf32;

public class KanjiList {
	
	private class Kanji {
		int[] k;
		int[] v;
		
		public Kanji(int[] k, int[] v) {
			this.k = k;
			this.v = v;
		}
	}

	private static Kanji[] kanjis;
	{
		try {
//			BufferedReader in = new BufferedReader(new FileReader("C:\\kanji\\list"));
			URL url = new URL("http://noitamina.moe/kanji.list");
			URLConnection urlConn = url.openConnection();
			if(urlConn!=null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
				String line;
				ArrayList<Kanji> list = new ArrayList<Kanji>();
				while((line=in.readLine())!=null) {
					int[] line32 = Utf32.encode(line);
					if(line32.length%2>0 || line32.length==0) continue;
					int div = line32.length/2;
					int[] ks = new int[div];
					int[] vs = new int[div];
					System.arraycopy(line32,  0, ks, 0, div);
					for(int i=0; i<div; i++)
						vs[i] = line32[div+i]-48;
					list.add(new Kanji(ks, vs));
				}
				in.close();
				kanjis = new Kanji[list.size()];
				for(int i=0; i<list.size(); i++)
					kanjis[i] = list.get(i);
			} else {
				kanjis = new Kanji[0];
				System.out.println("기본 한자 리스트 불러오기 실패");
			}

		} catch (Exception e) {
			kanjis = new Kanji[0];
			System.out.println("기본 한자 리스트 불러오기 실패");
		}
	}
	public static int vLength(int c) {
		for(Kanji x : kanjis) {
			if(x.k.length == 1 && x.k[0]==c)
				return x.v[0];
		}
		return 1;
	}
	public static int[] vLength(final int[] cs, final int pos) {
		for(Kanji x : kanjis) {
			int i=0;
			for(; i<x.k.length && pos+i<cs.length; i++)
				if(x.k[i] != cs[pos+i])
					break;
			if(i == x.k.length)
				return x.v;
		}
		return new int[]{1};
	}

}
