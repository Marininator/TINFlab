import java.util.LinkedHashSet;
import java.util.Scanner;

public class Main {
	@SuppressWarnings({ "null", "unused" })
	public static void main(String[] args)  {
		System.out.println("Unesite kodne rijeci svog binarnog blok koda (Upisite x kada ste gotovi sa upisom)!");
		int n = 0;
		
		LinkedHashSet<KodnaRijec> rijeci = new LinkedHashSet<KodnaRijec>();
		Scanner sc = new Scanner(System.in);
		String input;
		
		boolean legitInput = true;
		KodnaRijec tempRijec;
		do {
			input = sc.next();
			tempRijec = new KodnaRijec(input);
			if(tempRijec == null) System.out.println("Krivi format kodne rijeci unesen, unesite ponovo:");
		}	while(tempRijec == null);
		
		n = input.length();
		rijeci.add(tempRijec);
		

		
		do {
			input = sc.next();
			tempRijec = new KodnaRijec(input);
			if(tempRijec == null) System.out.println("Krivi format kodne rijeci unesen, unesite ponovo:");
		}	while(tempRijec == null);
		
		while (!input.equals("x")) {
			 if(input.length() != n) {
				 legitInput = false;
				 break;
			 }
			 
			 rijeci.add(tempRijec);

			 do {
					input = sc.next();
					tempRijec = new KodnaRijec(input);
					if(tempRijec == null) System.out.println("Krivi format kodne rijeci unesen, unesite ponovo:");
				}	while(tempRijec == null);
		}
		//sc.close();
		
		if(!legitInput) {
			System.out.println("Kodne rijeci nisu iste duljine!");
			return;
		}
		
		Kod kod = new Kod(rijeci, n);
		return;
	}

	
}
