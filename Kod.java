import java.util.Base64.Decoder;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Kod {
	int n, k, distanca, t;
	double vjerojatnostDekodiranja;
	double p_g;
	boolean linearan;
	int M;
	int brojVekPogreske;
	
	
	
	LinkedHashSet<KodnaRijec> kodneRijeci;
	KodnaRijec[][] standardniNiz;
	
	public Kod(LinkedHashSet<KodnaRijec> kodneRijeci, int n){
		this.kodneRijeci = kodneRijeci;
		ispisiKod();		
		
		M = kodneRijeci.size();
		System.out.println("Upisano je: " + M + " kodnih rijeci!");
		
		
		this.n = n;
		System.out.println("n: " + n);
		
		k = (int) Math.ceil(Math.log(kodneRijeci.size()) / Math.log(2));
		System.out.println("k: " + k);
	
		boolean testNulVektor = false;
		for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
			KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
			if(kodnaRijec.nulVektor()) testNulVektor = true;
		}
		
		if(!testNulVektor) {
			System.out.println("Kod nema nul vektor pa stoga nije linearan!");
			linearan = false;
		}
		
		else {
			
			//Test linearnosti
			linearan = testLin();
		
			if(linearan) {
				System.out.println("Kod je linearan!");
			}
			else {
				System.out.println("Kod nije linearan!");
			}
		}
		//Racunanje distance
		distanca = nelinDistanca();
		System.out.println("Distanca je: " + distanca);
		
		//Racunanje ispravljivih pogresaka
		this.t = (this.distanca - 1)/2;
		System.out.println("Broj ispravljivih pogresaka: " + t);
		
		if(linearan) {
			
			LinkedList<KodnaRijec> listaKodnihRijeci = new LinkedList<KodnaRijec>();
			listaKodnihRijeci.add(KodnaRijec.nula(n));
			for (Iterator<KodnaRijec> iterator = kodneRijeci.iterator(); iterator.hasNext();) {
				KodnaRijec kodnaRijec = (KodnaRijec) iterator.next();
				if(!kodnaRijec.nulVektor()) listaKodnihRijeci.add(kodnaRijec);
			}
			LinkedList<KodnaRijec[]> vektoriPogreske = dohvatiVektorePogreske(n, t);
			brojVekPogreske = this.brojVektoraPogreske();
			standardniNiz = new KodnaRijec[brojVekPogreske + 1][M];
		
			//System.out.println("Broj vektora pogreske je: " + brojVekPogreske);
		
			listaKodnihRijeci.toArray(standardniNiz[0]);
		
		
			int pos = 1;
			for(int i = 1; i <= this.t; ++i) {
				KodnaRijec[] temp = vektoriPogreske.get(i - 1);
				for (int j = 0, size = temp.length; j < size; j++) {
					standardniNiz[pos][0] = temp[j];
					++pos;
				}
			}
		
			for(int i = 1; i < M; ++i) {
				for(int j = 1; j <= brojVekPogreske; ++j) {
					standardniNiz[j][i] = standardniNiz[0][i].zbroji(standardniNiz[j][0], this.n);
				}
			}
		
			System.out.println("STANDARDNI NIZ");
		
			for(int j = 0; j <= brojVekPogreske; ++j) {
				for(int i = 0; i < M; ++i) {
					System.out.print(standardniNiz[j][i] + " ");
				}
				System.out.print("\n");
			}
			Scanner scan = new Scanner(System.in);
			System.out.println("Unesite vjerojatnost pogreske (izmedu 0 i 1) (decimalni zarez, ne tocka): ");
			p_g = scan.nextDouble();
			while(p_g < 0 || p_g > 1) {
				System.out.println("Vjerojatnost pogreske mora biti izmedu 0 i 1, unesite ponovo: ");
				p_g = scan.nextDouble();
			}
			//scan.close();
		
			izrVjerojatnost();
		
			System.out.println("Unesite kodnu rijec koju zelite dekodirati: ");
			String inputString = scan.next();
			while(inputString.length() != this.n) {
				System.out.println("Trazena rijec mora imati duljinu " + this.n + ", unesite ponovo:");
				inputString = scan.next();
			}
			KodnaRijec inputRijec = new KodnaRijec(inputString);
			dekoder(inputRijec);
		}
		
		else {
			System.out.println("Kod nije linearan pa nije moguce dekodirati standardnim nizom!");
		}
		
		
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
	
	private static int binomCoef(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomCoef(n - 1, k) + binomCoef(n - 1, k - 1);
    }
	
	private int brojVektoraPogreske() {
		int count = 0;
		for(int i = 1; i <= t; ++i) {
			count += binomCoef(this.n, i);
		}
		return count;
	}
	
	private static LinkedList<KodnaRijec[]> dohvatiVektorePogreske(int n, int t){
		LinkedList<KodnaRijec[]> retList = new LinkedList<KodnaRijec[]>();
		LinkedList<String[]> stringList = new LinkedList<String[]>(dohvatiStringVektorePogreske(n, t));
		for (Iterator<String[]> iterator = stringList.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			KodnaRijec[] temp = new KodnaRijec[strings.length];
			for (int i = 0; i < strings.length; i++) {
				temp[i] = new KodnaRijec(strings[i]);
			}
			retList.add(temp);
		}
		return retList;
	}
	
	private void dekoder(KodnaRijec trazenaRijec) {
		if(trazenaRijec.nulVektor()) {
			System.out.println("Unesena rijec dekodirana glasi: " + trazenaRijec + " sa vektorom pogreske: " + trazenaRijec);
			return;
		}
		int count = 0;
		int row = 0;
		int column = 0;
		
		for(int i = 0; i <= this.brojVekPogreske; ++i) {
			for(int j = 1; j < M; ++j) {
				if(standardniNiz[i][j].equals(trazenaRijec)) {
					row = i;
					column = j;
					++count;
				}
			}
		}
		if(count == 0) {
			System.out.println("Rijec " + trazenaRijec + " nije moguce ispraviti/dekodirati!");
		}
		
		else if(count == 1) {
			System.out.println("Rijec " + trazenaRijec + " dekodirana glasi: " + standardniNiz[0][column] + " sa vektorom"
					+ " pogreske: " + standardniNiz[row][0]);
		}
		else {
			System.out.println("Rijec " + trazenaRijec + " nije moguce ispraviti/dekodirati!");
		}
		
	}
	
	private static LinkedList<String[]> dohvatiStringVektorePogreske(int n, int t) {
		LinkedList<String[]> list = new LinkedList<String[]>();
		for(int i = 1; i <= t; ++i) {
			list.add(i - 1, generirajVektorePogreske(n, i));
		}
		return list;
	}
	
	private static String[] generirajVektorePogreske(int n, int k) {
		String[] save = new String[(int) binomCoef(n, k)];
		int p = 0;
	    for (int i = 0; i < Math.pow(2, n); i++) {
	        if (Integer.bitCount(i) != k) {
	            continue;
	        }

	        String binary = Integer.toBinaryString(i);

	        if (binary.length() < n) {
	        	save[p++] = String.format("%0" + (n - binary.length()) + "d%s", 0, binary);
	        } else {
	        	save[p++] = binary;
	        }
	    }
	    return save;
	}
	
	private void izrVjerojatnost() {
		double vjerojatnost = Math.pow(( 1 - p_g ), this.n);
		for(int i = 1; i < this.t; ++i) {
			vjerojatnost += binomCoef(n, i) * Math.pow(this.p_g, i) * Math.pow(( 1 - this.p_g), n-i);
		}
		System.out.println("Vjerojatnost ispravnog dekodiranja je: " + vjerojatnost);
	}
	
}
