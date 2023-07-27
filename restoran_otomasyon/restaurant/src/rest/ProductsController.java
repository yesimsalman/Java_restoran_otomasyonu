package rest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.*;
import javafx.stage.Window;

public class ProductsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSave1;
    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button btnDelete;
    
    
    
    @FXML
    private ComboBox<String> cbCategories;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ComboBox<String> cbWeight;

    @FXML
    private TableColumn<Products, String> colName;

    @FXML
    private ImageView ivProduct;
    
    @FXML
    private TextField etDescription;

    @FXML
    private TextField etId;

    @FXML
    private TextField etPrice;

    @FXML
    private TextField etBarcode;
    
    @FXML
    private Button btnBrowse;
    
    @FXML
    private TableView<Products> tableProducts;
    
    @FXML
    private TableColumn<Products, String> colCategory;

    @FXML
    private TableColumn<Products, String> colDescription;

    @FXML
    private TableColumn<Products, Integer> colId;

    @FXML
    private TableColumn<Products, String> colPrice;

    @FXML
    private TableColumn<Products, String> colStatus;

    @FXML
    void actionAddCategory(ActionEvent event) {
    	 try {
             openModalWindow("Category.fxml", "Manage Categories");
         } catch (Exception ex) {

         }
    }
    
    @FXML
    void saveProduct(ActionEvent event) {

    	Connection conn = jdbc.getConnection();
        try {
            String barcode = etBarcode.getText();
            String description = etDescription.getText();
            String price = etPrice.getText();
            String category = cbCategories.getSelectionModel().getSelectedItem();
            String isweight = cbWeight.getSelectionModel().getSelectedItem();
            String status = cbStatus.getSelectionModel().getSelectedItem();
            Window owner = (Stage) etDescription.getScene().getWindow();
            if (barcode.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty()
                    || isweight.isEmpty() || status.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, owner, "Lutfen Formu Doldurunuz.", "Hata");
            } else {
                //resim secildi mi kontrolu
                if (file == null) {
                    showAlert(Alert.AlertType.ERROR, owner, "Lutfen bir resim seciniz.", "Hata");
                } else {
                    FileInputStream fin = new FileInputStream(file);
                    int len = (int) file.length();
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO products(barcode,description,price,category,weight,image,status) VALUES(?,?,?,?,?,?,?)");
                    ps.setString(1, barcode);
                    ps.setString(2, description);
                    ps.setString(3, price);
                    ps.setString(4, category);
                    ps.setString(5, isweight);
                    ps.setBinaryStream(6, fin, len);
                    ps.setString(7, status);

                    int res = ps.executeUpdate();
                    if (res > 0) {
                        showAlert(Alert.AlertType.INFORMATION, owner, "Urun basariyla kaydedildi.", "Tebrikler");

                        etBarcode.clear();
                        etDescription.clear();
                        etPrice.clear();

                        cbCategories.valueProperty().set(null);
                        cbWeight.valueProperty().set(null);
                        cbStatus.valueProperty().set(null);

                        ivProduct.setImage(null);
                        file = null;
                        showProducts();
                    } else {
                        showAlert(Alert.AlertType.ERROR, owner, "Islem sirasinda hata olustu.", "Hata");
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
        }
    	
    }
    
    @FXML
    void handleBrowseImage(ActionEvent event) {

    	try {
            FileChooser fc = new FileChooser();
            //kisitlama
            FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");

            fc.getExtensionFilters().addAll(ext1, ext2);

            file = fc.showOpenDialog(DashboardController.getPrimaryStage());
            BufferedImage bf;
            bf = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bf, null);
            ivProduct.setImage(image);

        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
        }
    	
    }

    JdbcDao jdbc;

    Scene fxmlFile;
    Parent root;
    Stage window;

    File file;
    
    @FXML
    void deleteEntry(ActionEvent event) {

    	Connection conn = jdbc.getConnection();
        try{
            Products product = tableProducts.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM products WHERE id = '" + product.getId() + "'";
            executeQuery(query);
            showProducts();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    	
    }

    @FXML
    void editEntry(ActionEvent event) {

    	 Connection conn = jdbc.getConnection();
         try{
             Products product = tableProducts.getSelectionModel().getSelectedItem();
             String query = "UPDATE products SET description = '" + etDescription.getText() + "', "
                     + "price = '" + etPrice.getText() +"', category = '" + 
                     cbCategories.getSelectionModel().getSelectedItem() + "', status = '" + cbStatus.getSelectionModel().getSelectedItem() + 
                     "' WHERE id = '" + product.getId() + "'";
             executeQuery(query);
             showProducts();
             
         }catch(Exception ex){
             System.out.println(ex.getMessage());
         }
    	
    }
    public void showProducts() {
        ObservableList<Products> list = getProductList();
        colId.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Products, String>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Products, String>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Products, String>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Products, String>("status"));
        
        tableProducts.setItems(list);
    }
    private void insertRecord() {
      /*
         * String name = tfTableName.getText();
         
        if(!name.isEmpty()){
            String query = "INSERT INTO `tbltables` (name) VALUES('" + name + "')";
            executeQuery(query);
            showProducts();
     */   
  }
    
    private ObservableList<Products> getProductList() {
        ObservableList<Products> productList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM products";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Products products;
            while (rs.next()) {
                products = new Products(rs.getInt("id"), rs.getString("barcode"), rs.getString("description"), rs.getString("price"), rs.getString("category"), rs.getBlob("image"), rs.getString("status"));
                productList.add(products);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return productList;
    }
    
    private void executeQuery(String query) {
        Connection conn = jdbc.getConnection();
        Statement st;
        System.out.println(query);
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println("Kayit eklenirken hata olustu.");
            ex.printStackTrace();
        }
    }
    private void openModalWindow(String resource, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

    private void populateCategories() {
        Connection conn = jdbc.getConnection();
        Statement st;
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
        	
            ResultSet rs = conn.createStatement().executeQuery("select * from categories");
            while (rs.next()) {
                list.add(rs.getString("name"));
            }

        } catch (Exception ex) {
            System.out.println("Insert islemi sirasinda hata olustu.");
            ex.printStackTrace();
        }

        cbCategories.setItems(null);
        cbCategories.setItems(list);

    }
    
    private void addListenerForTable() {
        tableProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnSave.setDisable(true);
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                etId.setText("" + newSelection.getId());
                etBarcode.setText(newSelection.getBarcode());
                etDescription.setText(newSelection.getDescription());
                etPrice.setText(newSelection.getPrice());
                
                cbCategories.getSelectionModel().select(newSelection.getCategory());
                cbStatus.getSelectionModel().select(newSelection.getStatus());
            } else {
                etBarcode.setText("");
                etDescription.setText("");
                etPrice.setText("");
                cbCategories.getSelectionModel().selectFirst();
                cbStatus.getSelectionModel().selectFirst();
                
                btnSave.setDisable(false);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }
    
    
    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.showAndWait();
    }
    
    @FXML
    void initialize() {
        
    	jdbc = new JdbcDao();
    	addListenerForTable();
        showProducts();
        populateCategories();
        
        cbStatus.getItems().add("MEVCUT");
        cbStatus.getItems().add("MEVCUT DEGIL");

        cbWeight.getItems().add("EVET");
        cbWeight.getItems().add("HAYIR");	
    }
}
