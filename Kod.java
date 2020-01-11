import java.util.Iterator;
import java.util.LinkedHashSet;

public class Kod {
	int n, k, distanca, t;
	double vjerojatnostDekodiranja;
	double p_g = 0.5;
	boolean linearan;
	int M;
	
	LinkedHashSet<KodnaRijec> kodneRijeci;
	
	public Kod(LinkedHashSet<KodnaRijec> kodneRijeci, int n) {
		this.kodneRijeci = kodneRijeci;
		ispisiKod();
		
		M = kodneRijeci.size();
		System.out.println("Upisano je: " + M + " kodnih rijeci!");
		
		this.n = n;
		System.out.println("n: " + n);
		
		k = (int) Math.ceil(Math.log(kodneRijeci.size()) / Math.log(2));
		System.out.println("k: " + k);
	
		//Test linearnost
		linearan = testLin();
		
		if(linearan) {
			System.out.println("Kod je linearan!");
		}
		else {
			System.out.println("Kod nije linearan!");
		}
		//Racunanje distance
		distanca = nelinDistanca();
		System.out.println("Distanca je: " + distanca);
		
		//Racunanje ispravljivih pogresaka
		this.t = (this.distanca - 1)/2;
		System.out.println("Broj ispravljivih pogresaka: " + t);
	}
	
	private void ispisiKod() {
		for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
			KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
			System.out.println(kodnaRijec);
		}
	}
	
	@SuppressWarnings("null")
	private boolean testLin() {
		
		for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
			KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
			for (Iterator<KodnaRijec> iterator2 = kodneRijeci.iterator(); iterator2.hasNext();) {
				KodnaRijec kodnaRijec2 = (KodnaRijec) iterator2.next();
				KodnaRijec rez = kodnaRijec.zbroji(kodnaRijec2, n);
				//System.out.println("Rezultat zbrajanja " + kodnaRijec + " i " + kodnaRijec2 + " je: " + rez);
				if(!kodneRijeci.contains(rez)) return false;
			}
		}
		
		return true;
	}
	
	/*@SuppressWarnings("unused")
	private int linearnaDistanca() {
		int distanca = n;
		for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
			KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
			int temp = kodnaRijec.zbrojiJedinice(n);
			if(temp < distanca) distanca = temp;
		}
		return distanca;
	}*/
	
	
	private int nelinDistanca() {
		int distanca = n;
		int temp;
		
		for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
			KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
			for (Iterator<KodnaRijec> iterator2 = kodneRijeci.iterator(); iterator2.hasNext();) {
				KodnaRijec kodnaRijec2 = (KodnaRijec) iterator2.next();
				temp = kodnaRijec.usporediRijeci(kodnaRijec2, n);
				if(kodnaRijec != kodnaRijec2 && temp < distanca) distanca = temp;
			}
		}
		
		return distanca;
	}
}
