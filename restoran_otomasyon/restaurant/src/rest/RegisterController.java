package rest;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import rest.Register;

public class RegisterController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Register, Integer> colId;

    @FXML
    private TableColumn<Register, String> colName;

    @FXML
    private TableColumn<Register, String> colPassword;

    @FXML
    private TableColumn<Register, String> colUsername;

    @FXML
    private TextField field_id;

    @FXML
    private TextField field_isim;

    @FXML
    private PasswordField field_password;

    @FXML
    private TextField field_username;

    @FXML
    private TableView<Register> tableTable;

    @FXML
    void btnDeleteAction(ActionEvent event) {

    	if(event.getSource() == btnDelete){
            deleteFunct(); 	
    	}
    	
    }

    @FXML
    void btnSaveAction(ActionEvent event) {

    	if(event.getSource() == btnSave){
            insertFunct();  
            
    	}
    	
    }

    @FXML
    void btnUpdateAction(ActionEvent event) {

    	if(event.getSource() == btnUpdate){
            updateFunct(); 	
    	}
    	
    }
    
    @FXML
    void tableTablePressed(MouseEvent event) {
    	
    	Register reg = tableTable.getSelectionModel().getSelectedItem();
    	field_id.setText("" + reg.getId());
    	field_isim.setText("" + reg.getFullname());
    	field_username.setText("" + reg.getUsername());
    	field_password.setText("" + reg.getPassword());
 
    }
    
    public Connection getConnection() {
    	Connection conn;
    	try {
    		conn = DriverManager.getConnection("jdbc:mysql://localhost/restaurant", "root", "mysql");
    		return conn;
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			return null;
		}
    }
    
    
    public ObservableList<Register>getregList(){
    	ObservableList<Register>regList = FXCollections.observableArrayList();
    	Connection conn = getConnection();
    	String sorgu = "SELECT * FROM users";
    	Statement st;
    	ResultSet rs;
    	try {
			st = conn.createStatement();
			rs = st.executeQuery(sorgu);
			Register getReg;
			while(rs.next()) {
				//constructer sýralamasýyla ayný olmalý
				getReg = new Register(rs.getInt("id"), rs.getString("fullname"), rs.getString("username"), rs.getString("password"));
				regList.add(getReg);				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	return regList;
    }
    
    private void executeQuery(String sorgu) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(sorgu);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    	public void showRec() {
    	ObservableList<Register>list = getregList();
    	
    	colId.setCellValueFactory(new PropertyValueFactory<Register, Integer>("id"));
    	colName.setCellValueFactory(new PropertyValueFactory<Register, String>("fullname"));
    	colUsername.setCellValueFactory(new PropertyValueFactory<Register, String>("username"));
    	colPassword.setCellValueFactory(new PropertyValueFactory<Register, String>("password"));	
    	
    	tableTable.setItems(list);
    }
    
    	private void insertFunct() {
    	
    	String sorgu = "INSERT INTO users VALUES(" 
    	+ field_id.getText()
    	+ ",'" + field_isim.getText() + "','"
    	+ field_username.getText() + "','"
    	+ field_password.getText().valueOf(md5(field_password.getText()))+ "')";
    	
    	executeQuery(sorgu);
    	showRec();
    }
    	
    	//id'ye gore sorgulama yapýldý; týrnaklara dikkat
    	private void updateFunct(){
        	String sorgu = "UPDATE  users SET fullname  = '" + field_isim.getText()
        	+ "', username = '" + field_username.getText()
        	+ "', password = '" + field_password.getText().valueOf(md5(field_password.getText())) + "' WHERE id = " + field_id.getText() + "";
           		    	
           executeQuery(sorgu);
           showRec();
        }
    	
    	private void deleteFunct(){
            String sorgu = "DELETE FROM users WHERE id =" + field_id.getText() + "";
            executeQuery(sorgu);
            showRec();
        }
    	
    	public static String md5(String pass) {
    		try {
    			
    			MessageDigest md5 = MessageDigest.getInstance("MD5");
    			byte[] encrypted = md5.digest(pass.getBytes());
    			BigInteger no = new BigInteger(1, encrypted);
    			String hashPass = no.toString(16);
    			while(hashPass.length() < 32) {
    				hashPass = "0" + hashPass;
    			}
    			return hashPass;
    		} catch (NoSuchAlgorithmException e) {
    			throw new RuntimeException(e);
    		}
    	}
    
    @FXML
    void initialize() {
        
        showRec();
    }

}
