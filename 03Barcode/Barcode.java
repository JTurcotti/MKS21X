import java.util.List;
import java.util.ArrayList;

public class Barcode implements Comparable<Barcode> {

    String zip;
    static final int LEN = 5;

    public Barcode(String zip) {
	this.zip = zip;
	int sum = 0;
	for (int i=0; i<LEN; i++) {
	    try {
		sum+=Integer.parseInt(""+zip.charAt(0));
		zip=zip.substring(1);
	    } catch (NumberFormatException e) {
		throw new RuntimeException("Zip code contaisn non-digits");
	    } catch (IndexOutOfBoundsException e) {
		throw new RuntimeException("Zip code too short");
	    }
	}
	if (zip.length()>0) throw new RuntimeException(" Zip code too long");
	this.zip=this.zip+sum%10;
    }

    public Barcode clone() {
	return new Barcode(zip.substring(0, LEN));
    }

    public String toString() {
	String out = zip + " ";
	for (int i=0; i<LEN; i++) {
	    String val=":::||";
	    switch (zip.charAt(i)) {
	    case '0': val="||:::"; break;
	    case '1': val=":::||"; break;
	    case '2': val="::|:|"; break;
	    case '3': val="::||:"; break;
	    case '4': val=":|::|"; break;
	    case '5': val=":|:|:"; break;
	    case '6': val=":||::"; break;
	    case '7': val="|:::|"; break;
	    case '8': val="|::|:"; break;
	    case '9': val="|:|::"; break;
	    }
	    out+=val;
	}
	return out;
    }

    public int compareTo(Barcode other) {
	return Integer.parseInt(zip)-Integer.parseInt(other.zip);
    }

    public static void main(String[] args) {

	List<String> cases = new ArrayList<String>();
	cases.add("17535");
	cases.add("58395");
	cases.add("93624");
	cases.add("00000");
	cases.add("aaaaa");
	cases.add("hfu48");
	cases.add("4b5ig");
	cases.add("");
	cases.add("1");
	cases.add("000000");
	cases.add("a");
	cases.add("aaaaaa");
	cases.replaceAll((String zip) -> {
		try {
		    Barcode b = new Barcode(zip);
		    Barcode c = b.clone();
		    return zip + " " + c.toString();
		} catch (RuntimeException e) {
		    return zip + " " + e.getMessage();
		}
	    });
	    System.out.println(cases);
	}    
}
		