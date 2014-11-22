package lyrics;

public class Utf32 {
	
	public static int[] encode(String str) {
		return encode(str.toCharArray());
	}
	public static int[] encode(char[] c) {
		int[] result = new int[c.length];
		int i=0, j=0;
		while(i<c.length) {
			if(    (c[i]  &0b1111110000000000) == 0b1101100000000000) {
				if((c[i+1]&0b1111110000000000) != 0b1101110000000000) {
					i++; continue;
				}
				int zzzzz = ((c[i]>>6) & 0b1111) + 1;
				result[j]    = (zzzzz<<16) + ((c[i++]&0b111111)<<10);
				result[j++] += c[i++]&0b1111111111;
				
			} else {
				result[j++] = c[i++];
			}
		}
		if(j<c.length) {
			int[] temp = new int[j];
			System.arraycopy(result, 0, temp, 0, j);
			result = temp;
		}
		return result;
	}
	
	public static char[] decode(int i) {
		char[] c;
		if(i<65536) {
			c = new char[1];
			c[0] = (char)i;
		} else {
			int ZZZZ = ((i>>16) & 0b11111) - 1;
			if(ZZZZ > 0b1111) {
				return new char[]{' '};
			}
			c = new char[2];
			c[0] = (char)(0b1101100000000000 + (ZZZZ<<6) + ((i>>10)&0b111111));
			c[1] = (char)(0b1101110000000000 + (i&0b1111111111));
		}
		return c;
	}
	public static String decode(int[] i) {
		StringBuilder result = new StringBuilder(i.length);
		for(int x : i)
			result.append(decode(x));
		return result.toString();
	}
}
