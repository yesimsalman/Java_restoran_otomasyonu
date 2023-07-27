package rest;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
public class CategoryController {

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
    private TableColumn<Category, Integer> colId;

    @FXML
    private TableColumn<Category, String> colName;

    @FXML
    private TableView<Category> tableCategory;

    @FXML
    private TextField tfCategoryName;

    JdbcDao jdbc;
    @FXML
    void deleteEntry(ActionEvent event) {
    	 
    	Connection conn = jdbc.getConnection();
         try{
             Category category = tableCategory.getSelectionModel().getSelectedItem();
             String query = "DELETE FROM categories WHERE id = '" + category.getId() + "'";
             executeQuery(query);
             showCategories();
             
         }catch(Exception ex){
             System.out.println(ex.getMessage());
         }
    }

    @FXML
    void editEntry(ActionEvent event) {

    	Connection conn = jdbc.getConnection();
        try{
            Category category = tableCategory.getSelectionModel().getSelectedItem();
            String query = "UPDATE categories SET name = '" + tfCategoryName.getText() + " ' WHERE id = '" + category.getId() + "'";
            executeQuery(query);
            showCategories();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    	
    }

    @FXML
    void saveCategory(ActionEvent event) {
    	insertRecord();
    }

    
    
    public void showCategories(){
        ObservableList<Category> list = getCategoryList();
        colId.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));        
        
        
        tableCategory.setItems(list);
    }
    
    private ObservableList<Category> getCategoryList() {
        ObservableList<Category> categoryList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Category category;
            while(rs.next()){
                category = new Category(rs.getInt("id"), rs.getString("name"));
                categoryList.add(category);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        return categoryList;
       }
    
    private void addListenerForTable(){
        tableCategory.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if(newSelection != null){
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);                
                tfCategoryName.setText(newSelection.getName());                
            }else{
                tfCategoryName.setText("");
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }
    
    private void insertRecord(){
        String name = tfCategoryName.getText();
        if(!name.isEmpty()){
            String query = "INSERT INTO `categories` (name) VALUES('" + name + "')";
            executeQuery(query);
            showCategories();
            tfCategoryName.setText("");
        }
    }
    
     private void executeQuery(String query) {
        Connection conn = jdbc.getConnection();
        Statement st;
        System.out.println(query);
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            System.out.println("kayit eklenirken hata olustu.");
            ex.printStackTrace();
        } 
    }
     
    
    @FXML
    void initialize() {
    	
    	addListenerForTable();
        jdbc = new JdbcDao();
        showCategories();

    }

}
