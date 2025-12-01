package model;

import java.util.Comparator;

public class ProjektComparator implements Comparator<Projekt> {

	private String sort;
	
	public ProjektComparator(String sort) {
		this.sort = sort;
	}
	
	@Override
	public int compare(Projekt o1, Projekt o2) {
		int result = 0;
		
		if (sort.equals("name")) {
			result = o1.getName().compareTo(o2.getName());
		} else if (sort.equals("kunde")){
			result = o1.getKunde().compareTo(o2.getKunde());
		} else {
			result = Integer.valueOf(o1.getNr()).compareTo(Integer.valueOf(o2.getNr()));
		}
		
		return result;
	}

}
