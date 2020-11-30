package data;

public class Employee {

	private String name;
	private String lastConnectionDate;
	
	public Employee(String name, String lastConnectionDate) {
		super();
		this.name = name;
		this.lastConnectionDate = lastConnectionDate;
		if(lastConnectionDate != null) {
			//change this string to a more appropriate format (from yyyy-mm-dd to dd/mm/yyyy)
			String substr[] = lastConnectionDate.split("-");
			if(substr.length == 3) {
				this.lastConnectionDate = substr[2] + "/" + substr[1] + "/" + substr[0];
			}
		}
	}
	
	public Employee(String name) {
		this(name, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastConnectionDate() {
		return lastConnectionDate;
	}

	public void setLastConnectionDate(String lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
	}	
}
