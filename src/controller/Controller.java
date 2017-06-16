package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used to control LoginPage.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class Controller {

    @FXML TextField userName;
    @FXML Button login;
    
    static int count = 0;
    
	private Stage prevStage;

	// Keep track of the prev stage
	void setPrevStage(Stage stage){
		this.prevStage = stage;
	}
	
	/**
	 * When login button is clicked, 
	 * this method is called and it will examine to see to advance to adminPage, 
	 * to advance to userPage, or the user doesn't exist.
	 * Password is not included.
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    @FXML
    public void LoginButton() throws NullPointerException, IOException, ClassNotFoundException {
    	
    	ArrayList<User> userList = new ArrayList<>();
    	String user = null;

		// Read from userList.tmp
		userList = PhotoAlbum.read(userList);
        
        count = 0;
        while(count < userList.size()){
        	if(userName.getText().equals(userList.get(count).getUsername())){
        		user = userList.get(count).getUsername();
        		break;
        	}
        	count++;
        }

    	// Switches to the adminSubsystem
		// ===================================
		if(userName.getText().equals("admin")){
			Stage adminStage = new Stage();
			FXMLLoader adminPageLoader = new FXMLLoader();
			adminPageLoader.setLocation(getClass().getResource("/view/AdminSubsystem.fxml"));
			AnchorPane adminFXML = adminPageLoader.load();

			adminSubsystemController adminController = adminPageLoader.getController();

			Scene adminSubSystem = new Scene(adminFXML, 600, 400);
			adminStage.setScene(adminSubSystem);

			adminController.start();

			prevStage.close();
			adminStage.show();

		}

		// Switches to the userSubsystem
		// =================================
		if(user != null) {// TODO: CONDITION UNFINISHED
			Stage userStage = new Stage();

			// Need FXMLLoader instance to get corresponding Controller
			FXMLLoader userPageLoader = new FXMLLoader();
			userPageLoader.setLocation(getClass().getResource("/view/UserSubsystem.fxml"));

			AnchorPane userFXML = userPageLoader.load();

			// Getting the corresponding Controller using FXMLLoader instance
			userSubsystemController userController = userPageLoader.getController();
			
			// Calling the separated userSubsystem Controller
			userController.setAlbumListView();

			Scene userSubsystem = new Scene(userFXML, 600, 400);
			userStage.setScene(userSubsystem);

			prevStage.close();
			userStage.show();
		}
		if(user == null && !userName.getText().equals("admin")){
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Such user does not exist!");
            alert.showAndWait();
            userName.clear();
		}
    }
}
