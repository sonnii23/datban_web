package helper;

public class Helper {

	public static Integer convertToInt(String nrparam) {
		Integer nr = null;
		
		try {
			nr = Integer.valueOf(nrparam);
		} catch (NumberFormatException ex) {}
		
		return nr;
	}
	
}
