package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;


/**
 * This class is used to control caption interface.
 * Called when caption button is clicked inside AlbumPage.fxml.
 * Used to caption/recaption selected photo.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class captionController {
	
	
	@FXML Button enter;
	@FXML TextField caption;


	/**
	 * Caption is set to the text from caption TextField.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@FXML public void onEnterButtonClicked() throws ClassNotFoundException, IOException{
		
		ArrayList<User> userList = new ArrayList<>();
		userList = PhotoAlbum.read(userList);
		userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).setCaption(caption.getText());
		userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).setDate();
		
		PhotoAlbum.write(userList);
		FXMLLoader albumLoader = new FXMLLoader();
		albumLoader.setLocation(getClass().getResource("/view/AlbumPage.fxml"));

		Stage currentStage = (Stage)enter.getScene().getWindow();
		currentStage.close();	
		
	}

}
