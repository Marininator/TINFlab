
public class KodnaRijec {
	boolean[] kodnaRijec;
	int n;
	
	public KodnaRijec(String input) {
		kodnaRijec = new boolean[input.length()];
		this.n = input.length();
		for(int i = 0, n = input.length() ; i < n ; ++i) { 
			if(input.charAt(i) == '1') kodnaRijec[i] = true;
			else kodnaRijec[i] = false;
		}
	}
	
	public KodnaRijec(int n) {
		kodnaRijec = new boolean[n];
		this.n = n;
	}
	
	@Override
	public String toString() {
		String retString = "";
		for(int i = 0; i < n; ++i) {
			if(kodnaRijec[i]) retString += "1";
			else retString += "0";
		}
		return retString;
	}
	
	@SuppressWarnings("null")
	public KodnaRijec zbroji(KodnaRijec druga, int n) {
		KodnaRijec retRijec = new KodnaRijec(n);
		for(int i = 0; i < n; ++i) {
			retRijec.kodnaRijec[i] = this.kodnaRijec[i] ^ druga.kodnaRijec[i];
		}
		return retRijec;
	}
	
	public int usporediRijeci(KodnaRijec druga, int n) {
		int count = 0;
		for(int i = 0; i < n; ++i) {
			if(this.kodnaRijec[i] ^ druga.kodnaRijec[i]) ++count;
		}
		return count;
	}
	
	@Override
	public boolean equals(Object obj) {
		KodnaRijec druga = (KodnaRijec) obj;
		for(int i = 0; i < n; ++i) {
			if(this.kodnaRijec[i] != druga.kodnaRijec[i]) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	/*public int zbrojiJedinice(int n) {
		int count = 0;
		for(int i = 0; i < n; ++i) {
			if(kodnaRijec[i]) ++count;
		}
		return count;
	}
	*/
	
}
