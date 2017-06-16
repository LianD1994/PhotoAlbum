package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;

/**
 * This class is called when there's search occurred, and 
 * create album based on search result button is clicked in the albumPage window.
 * @author Tien-Hsueh Li
 *
 */
public class searchController {
	
	ArrayList<User> userList = new ArrayList();
	@FXML TextField title;

	/**
	 * When create button is clicked, pop a window for user to enter 
	 * new album name, and input all the photos from search results into this
	 * album directory.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@FXML public void onCreateButtonClicked() throws ClassNotFoundException, IOException{
		if(title.getText().isEmpty()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a title!");
            alert.showAndWait();
            title.clear();
            return;
		}else{
			userList = PhotoAlbum.read(userList);
			int count = 0;
			boolean albumExist = false;
			while(count < userList.get(Controller.count).getAlbums().size()){
				if(userList.get(Controller.count).getAlbums().get(count).getTitle().equals(title.getText())){
					albumExist = true;
					break;
				}
				count++;
			}
			
			if(albumExist){
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Error");
	            alert.setHeaderText(null);
	            alert.setContentText("An album with the same title already exists!");
	            alert.showAndWait();
	            title.clear();
	            return;
			}
			
			new File("data/" + userList.get(Controller.count).getUsername() + "/" + title.getText()).mkdir();
			
			Album temp = new Album(title.getText());
			
			count = 0;
			
			while(count < albumPageController.albumList.size()){
				
				temp.addPhoto(albumPageController.albumList.get(count));
				
				File readf = new File(albumPageController.albumList.get(count).getAddress());
	            BufferedImage image = ImageIO.read(readf);
	            File destination = new File("data/" + userList.get(Controller.count).getUsername() + "/" + title.getText() + "/" + albumPageController.albumList.get(count).getName());
	            ImageIO.write(image, "jpg", destination);
				count++;
			}
			
			userList.get(Controller.count).getAlbums().add(temp);
			PhotoAlbum.write(userList);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("An album with searched result has been created!");
            alert.showAndWait();
            FXMLLoader albumLoader = new FXMLLoader();
			albumLoader.setLocation(getClass().getResource("/view/albumPageControll.fxml"));

			albumPageController.canCreateNewAlbum = false;
     	
			Stage currentStage = (Stage)title.getScene().getWindow();
			currentStage.close();
            		
		}
	}
	
}
