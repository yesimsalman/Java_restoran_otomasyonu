package rest;

public class Register {

		private int id;
	    private String fullname;
	    private String username;
	    private String password;
	    
	    //constructure
		public Register(int id, String fullname, String username, String password) {
			super();
			this.id = id;
			this.fullname = fullname;
			this.username = username;
			this.password = password;
		}
		//getters and setters
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getFullname() {
			return fullname;
		}
		public void setFullname(String fullname) {
			this.fullname = fullname;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
}
