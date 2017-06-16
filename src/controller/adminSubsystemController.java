package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
/**
 * 
 * This class is used to control AdminSubsystem.fxml, an interface that administrator
 * can create/delete users.
 *
 */
public class adminSubsystemController implements Serializable {

    @FXML ListView<String> userListView;
    @FXML Button logoutButton;
    @FXML Button quitButton;
    @FXML TextField userNameTextField;

    ObservableList<String> displayList = FXCollections.observableArrayList();
    ArrayList<User> userList = new ArrayList<>();

    void start() throws IOException, ClassNotFoundException {

        userList = PhotoAlbum.read(userList);

        for (User anUserList : userList) {
            displayList.add(anUserList.getUsername());
        }

        userListView.setItems(displayList);
    }
    /**
     * This method is used to create new user.
     * Username will be added to the ArrayList<User> userList,
     * and a directory will be created for the user under data file.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onCreateButtonClicked() throws IOException, ClassNotFoundException {
    	
    	if(!userNameTextField.getText().equals("")){
    	
    	int count = 0;
    	boolean canCreate = true;
    	while(count < userList.size()){
    		if(userNameTextField.getText().equals(userList.get(count).getUsername())){
    			canCreate = false;
    		}
    		count++;
    	}
    	
    	if(canCreate){
		    User addUser = new User(userNameTextField.getText());
		    userList.add(addUser);
		    new File("data/" + userNameTextField.getText()).mkdir();

			PhotoAlbum.write(userList);

		    displayList.add(addUser.getUsername());
		    userListView.setItems(displayList);
    	}else{
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An user with same username already exists!");
            alert.showAndWait();
    		
    	}
    	
    	userNameTextField.clear();
    	}else{
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an username!");
            alert.showAndWait();
            userNameTextField.clear();
            return;
    	}
    	
    }
    
    /**
     * This method is used to delete an existing user.
     * User will be deleted from the userList, and the directory 
     * under data directory will also be deleted.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onDeleteButtonClicked() throws IOException, ClassNotFoundException {
    	if(userListView.getSelectionModel().getSelectedItem() != null){

	        for(int i=0;i<userList.size();i++){
	            if(userList.get(i).getUsername().equals(userListView.getSelectionModel().getSelectedItem())){

					String toBeDeleted = userListView.getSelectionModel().getSelectedItem();
	                userList.remove(i);
	                displayList.remove(userListView.getSelectionModel().getSelectedIndex());
	                
	                File folder = new File("data/");
	                File[] listOfFiles = folder.listFiles();
	                int count = 0;
	                while(count < folder.listFiles().length){
	                	if(listOfFiles[count].getName().equals(toBeDeleted)){
	                		listOfFiles[count].delete();
	                		break;
	                	}
	                	count++;
	                }
	            }
	        }
	
	       PhotoAlbum.write(userList);
	
	        userListView.setItems(displayList);
    	}
    }

    /**
     * This is method is called when the log out button is clicked.
     * The program will go back to the log in stage.
     * @throws IOException
     */
    @FXML
    void onLogoutButtonClicked() throws IOException {
        Stage loginStage = new Stage();
        FXMLLoader loginPageLoader = new FXMLLoader();
        loginPageLoader.setLocation(getClass().getResource("/view/loginPage.fxml"));

        AnchorPane loginFXML = loginPageLoader.load();
        Controller loginController = loginPageLoader.getController();

        // Set the previous stage to the login page before switch to different scene
        loginController.setPrevStage(loginStage);

        Scene loginPage = new Scene(loginFXML, 600, 400);
        loginStage.setScene(loginPage);

        Stage currentStage = (Stage)logoutButton.getScene().getWindow();
        currentStage.close();
        loginStage.show();
    }
    /**
     * This method is used to quit the program when quit button is clicked.
     */
    @FXML
    void onQuitButtonClicked(){

        Stage currentStage = (Stage)logoutButton.getScene().getWindow();
        currentStage.close();
    }
}
