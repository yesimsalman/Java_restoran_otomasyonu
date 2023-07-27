package rest;

public class Tables {

	 private int id;
	    private String name;

	    public Tables(int id, String name) {
	        this.id = id;
	        this.name = name;
	    }

	    //sadece getter
	    public int getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }
	
}
