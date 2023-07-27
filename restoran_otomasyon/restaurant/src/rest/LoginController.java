package rest;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private Button btnRegister;
    

    @FXML
    void btnRegisterAction(ActionEvent event) {

    	if(event.getSource() == btnRegister) {
    		
			try {
				Node node = (Node) event.getSource();
				Stage stage = (Stage) node.getScene().getWindow();
				stage.close();
				
				Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Register.fxml")));
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {
				System.err.println(e.getMessage());
		}
	}
    	
    	
    }
    
    @FXML
    void initialize() {
        
    }
    //bayt dizisi
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
    
    //login action
    @FXML
    private void actionLogin(ActionEvent event) {
    	Window owner = txtUsername.getScene().getWindow();
    	//baþka bir ekrana yönlendirmek için
    	System.out.println(txtUsername.getText());
    	System.out.println(txtPassword.getText());
    	//kullanýcý adý ve sifre kontrolu
    	if(txtUsername.getText().isEmpty()) {
    		showAlert(Alert.AlertType.ERROR, owner, "Gecerli Bir Kullanici Adi Gir", "Dogru Kullanici Adi Girilmedi");
    		return;
    	}
    	if(txtPassword.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Gecerli Bir Sifre Gir","Dogru Sifre Girilmedi");
            return;
        }
    	String username = txtUsername.getText();
        String password = txtPassword.getText();
        //sifrenin algoritmaya girisi
        password = password.valueOf(md5(password));
        
        JdbcDao jdbcDao = new JdbcDao();
        boolean flag = jdbcDao.validate(username, password);
        //hata durumu
        if(!flag){
            infoBox("Gecerli bir kullanici adi ve sifre gir", null,"Hata");
        }else{
            infoBox("Giris yapildi!", null, "Basarili");
            //baþarýlý olma durumu
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent root = fxmlLoader.load();
                
                Stage stage = new Stage();
                stage.setTitle("Restoran | Kontrol Paneli");
                stage.setScene(new Scene(root));
                DashboardController controller = (DashboardController) fxmlLoader.getController();
                controller.setUsername(username);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
                //hide ile gizliyoruz
            }
            //hata
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
            
   
    
    public static void infoBox(String infoMessage, String headerText, String title) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setContentText(infoMessage);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.showAndWait();
    }
    
    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
    	Alert alert = new Alert(alertType);
    	alert.setContentText(message);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.initOwner(owner);
    	alert.show();
    }
    
    

}
