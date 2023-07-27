package rest;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class DashboardController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lblUsername;

    @FXML
    private Button btnManageTable;
    
    Scene fxmlFile;
    Parent root;
    Stage window;

    @FXML
    private Button btnLookup;
    

    @FXML
    private TableColumn<Products, String> colCategory;

    @FXML
    private TableColumn<Products, String> colDescription;

    @FXML
    private TableColumn<Products, Integer> colId;

    @FXML
    private TableColumn<Products, String> colPrice;

    @FXML
    private TableView<Products> tableProducts;
    
    @FXML
    private TableColumn<Products, String> colStatus;
    
    JdbcDao jdbc;
    
    @FXML
    void btnExit(ActionEvent event) {

    }
    
    
    
    @FXML
    void handleKeyPressed(KeyEvent event) {
    	
    	    	
    }

    
    private static Stage pStage;
    
    @FXML
    private void actionManageProduct(ActionEvent event) {

    	try {
            openModalWindow("Products.fxml", "Ürün Yönetimi");
        } catch (Exception ex) {
        	
        }
    	
    }

    @FXML
    private void manageTable(ActionEvent event) {

    	try {
            openModalWindow("Tables.fxml", "Tablo YÃ¶netimi");
        } catch (Exception ex) {
        	System.out.println("" + ex.getMessage());
            ex.printStackTrace();
        }
    	
    }
    public static Stage getPrimaryStage() {
        return pStage;
    }
    
    //yeni pencere
    private void openModalWindow(String resource, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        window.setTitle(title);
        setPrimaryStage(window);
        window.showAndWait();
    }
    
    //yeni pencere
    private void setPrimaryStage(Stage pStage) {
        DashboardController.pStage = pStage;
    }
    
    @FXML
    void initialize() {
    	jdbc = new JdbcDao();
        showProducts();

    }
    
    //veri tabanýndaki verileri görüntüleme
    public void showProducts() {
        ObservableList<Products> list = getProductList();
        colId.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Products, String>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Products, String>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Products, String>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Products, String>("status"));

        tableProducts.setItems(list);
    }

    private ObservableList<Products> getProductList() {
        ObservableList<Products> productList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM products";
        //veri tabanýnda ki içerik tablosunu getiriyor
        Statement st;
        ResultSet rs;
        //sql ile baðlantýlý bir yapý
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Products products;
            while (rs.next()) {
            	//buraya da deðin: constructure sýralamasýyla ayný olmalý:: Products.java 
                products = new Products(rs.getInt("id"), rs.getString("barcode"), rs.getString("description"), rs.getString("price"), rs.getString("category"), rs.getBlob("image"), rs.getString("status"));
                productList.add(products);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return productList;
    }
    
    public void setUsername(String username) {
    	lblUsername.setText(username);
    }
}
