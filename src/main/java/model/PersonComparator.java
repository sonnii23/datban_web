package model;

import java.util.Comparator;

public class PersonComparator implements Comparator<Person> {
	
	private String sort;
	
	public PersonComparator(String sort) {
		this.sort = sort;
	}

	@Override
	public int compare(Person o1, Person o2) {
		
		int result = 0;
		
		if (sort.equals("nachname")) {
			result = o1.getNachname().compareTo(o2.getNachname());
		} else if  (sort.equals("vorname")) {
			result = o1.getVorname().compareTo(o2.getVorname());
		} else if (sort.equals("position")) {
			result = o1.getPosition().compareTo(o2.getPosition());
		} else {
			result = Integer.valueOf(o1.getNr()).compareTo(Integer.valueOf(o2.getNr()));
		}
		return result;
	}

}
