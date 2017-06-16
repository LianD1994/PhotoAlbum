package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

/**
 * This class is called from albumPage window.
 * Used to add/delete tags of the selected photo.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class tagController {
	
	ArrayList<User> userList;
	
	@FXML Button add;
	@FXML Button delete;
	@FXML TextField tagText;
	@FXML Text existingTagsText;
	@FXML Text tagsText;
	
	/**
	 * Called when add button is clicked.
	 * Used to add a new tag.
	 * If the tag already exists, pop alert message.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@FXML public void onAddButtonClicked() throws ClassNotFoundException, IOException{
		
		userList = PhotoAlbum.read(userList);
		ArrayList<String> tags = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).getTag();
		boolean tagExist = false;
		int count = 0;
		if(tags != null){
			while(count < tags.size()){
				if(tagText.getText().equals(tags.get(count))){
					tagExist = true;
					break;
				}
				count++;
			}
		}
		
		if(!tagExist){
			userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).addTag(tagText.getText());;
			userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).setDate();
			PhotoAlbum.write(userList);
			
			FXMLLoader albumPageLoader = new FXMLLoader();
			albumPageLoader.setLocation(getClass().getResource("/view/AlbumPage.fxml"));
			albumPageController albumController = albumPageLoader.getController();
     	
			Stage currentStage = (Stage)add.getScene().getWindow();
			currentStage.close();
		}else{
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("This Tag already Exist!");
            alert.showAndWait();
            tagText.clear();
		}
		
	}
	
	/**
	 * Called when delete button is clicked.
	 * Delete tag in the photo that is the same as input from the textField.
	 * If the tag does not exist, pop an alert message.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@FXML public void onDeleteButtonClicked() throws ClassNotFoundException, IOException{
		
		userList = PhotoAlbum.read(userList);
		ArrayList<String> tags = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(albumPageController.selectedPhotoName).getTag();
		boolean tagExist = false;
		int count = 0;
		if(tags != null){
			while(count < tags.size()){
				if(tagText.getText().equals(tags.get(count))){
					tagExist = true;
					break;
				}
				count++;
			}
		}
		
		if(tagExist){
			tags.remove(count);
			PhotoAlbum.write(userList);
			FXMLLoader albumPageLoader = new FXMLLoader();
			albumPageLoader.setLocation(getClass().getResource("/view/AlbumPage.fxml"));
			albumPageController albumController = albumPageLoader.getController();
     	
			Stage currentStage = (Stage)add.getScene().getWindow();
			currentStage.close();
			
		}else{
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("This Tag Does Not Exist!");
            alert.showAndWait();
            tagText.clear();
		}
		
	}

}
