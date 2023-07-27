package rest;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public class TablesController {

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
    private TableColumn<Tables, Integer> colId;

    @FXML
    private TableColumn<Tables, String> colName;

    @FXML
    private TableView<Tables> tableTables;

    @FXML
    private void saveTable(ActionEvent event) {
            insertRecord();  
    }
    
    @FXML
    void deleteEntry(ActionEvent event) {

    	Connection conn = jdbc.getConnection();
        try{
            Tables table = tableTables.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM tbltables WHERE id = '" + table.getId() + "'";
            executeQuery(query);
            showTable();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    	
    

    @FXML
    void editEntry(ActionEvent event) {

    	//jdbc, JdbcDao.java sýnýfýna baglý
    	Connection conn = jdbc.getConnection();
        try{
            Tables table = tableTables.getSelectionModel().getSelectedItem();
            String query = "UPDATE tbltables SET name = '" + tfTableName.getText() + " ' WHERE id = '" + table.getId() + "'";
            executeQuery(query);
            showTable();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    	
    }
    
    @FXML
    private TextField tfTableName;

    JdbcDao jdbc;
    @FXML
    void initialize() {
    	
    	addListenerForTable();
    	//nesne turetme, turetilen: jdbc
        jdbc = new JdbcDao();
        showTable();    	
    }
    public void showTable(){
        ObservableList<Tables> list = getTableList();
        colId.setCellValueFactory(new PropertyValueFactory<Tables, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Tables, String>("name"));        
        tableTables.setItems(list);
    }
    
    private ObservableList<Tables> getTableList() {
        ObservableList<Tables> tableList = FXCollections.observableArrayList();
        Connection conn = jdbc.getConnection();
        String query = "SELECT * FROM tbltables";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Tables tables;
            while(rs.next()){
                tables = new Tables(rs.getInt("id"), rs.getString("name"));
                tableList.add(tables);
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        return tableList;
       }
    
    private void insertRecord(){
        String name = tfTableName.getText();
        if(!name.isEmpty()){
            String query = "INSERT INTO `tbltables` (name) VALUES('" + name + "')";
            executeQuery(query);
            showTable();
            tfTableName.setText("");
        }
    }
    
    //güncelle ve sil butonlarýnýn aktif pasif durumu
    private void addListenerForTable(){
        tableTables.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if(newSelection != null){
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);                
                tfTableName.setText(newSelection.getName());                
            }else{
                tfTableName.setText("");
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }
    
    private void executeQuery(String query) {
        Connection conn = jdbc.getConnection();
        Statement st;
        System.out.println(query);
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            System.out.println("Hata");
            ex.printStackTrace();
        } 
    }
}
