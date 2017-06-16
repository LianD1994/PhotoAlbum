package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This method is called from the userSubsystem window.
 * Depends on the global variable createOrRename, either used to
 * rename an existing album or to create a new album.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class createController {
	
	@FXML TextField title;
	@FXML Button createAlbum;
	
	@FXML public void setRename(){
			createAlbum.setText("Rename");
	}
	
	/**
	 * This method is called when the create/rename button is clicked.
	 * Rename an existing album or create a new album.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@FXML public void onCreateButtonClicked() throws ClassNotFoundException, IOException{
		
		ArrayList<User> userList = new ArrayList();
		ArrayList<Album> albumList = new ArrayList();
		
		if(userSubsystemController.createOrRename.equals("create")){
			if(title.getText().isEmpty()){
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Error");
	            alert.setHeaderText(null);
	            alert.setContentText("Please enter a title!");
	            alert.showAndWait();
	            title.clear();
	            return;
			}
			userList = PhotoAlbum.read(userList);
        
			albumList = userList.get(Controller.count).getAlbums();
        
			String create = title.getText();
        
			int count = 0;
			boolean existingAlbum = false;
			if(albumList != null){
				while(count < albumList.size()){
					if(title.getText().equals(albumList.get(count).getTitle())){
						existingAlbum = true;
						Alert alert = new Alert(AlertType.INFORMATION);
		                alert.setTitle("Error");
		                alert.setHeaderText(null);
		                alert.setContentText("An album with same title already exists!");
		                alert.showAndWait();
						break;
					}
					count++;
				}
			}
        
			if(!existingAlbum){
				Album newAlbum = new Album(title.getText());
				userList.get(Controller.count).addAlbum(newAlbum);
				userSubsystemController.list.add(newAlbum.getTitle());
				new File("data/" + userList.get(Controller.count).getUsername() + "/" + title.getText()).mkdir();
			}

			PhotoAlbum.write(userList);

			FXMLLoader userPageLoader = new FXMLLoader();
			userPageLoader.setLocation(getClass().getResource("/view/UserSubsystem.fxml"));
			userSubsystemController userController = userPageLoader.getController();
     	
			Stage currentStage = (Stage)createAlbum.getScene().getWindow();
			currentStage.close();
		}else if(userSubsystemController.createOrRename.equals("rename")){

			userList = PhotoAlbum.read(userList);
			
			ArrayList<String> albumTitles = new ArrayList();
	    	albumTitles = new ArrayList<>();
	    	albumList = userList.get(Controller.count).getAlbums();    //get albumList
	        int count = 0;
	        if(albumList != null){                                     //get all titles in albumList
	        	while(count < albumList.size()){
	        		albumTitles.add(albumList.get(count).getTitle());
	        		count++;
	        	}
	        }
	    	
	    	int albumIndex = 0;
	    	if(albumTitles != null && title != null){                           //get index in the user album list
	    		while(albumIndex < albumTitles.size()){
	    			if(userSubsystemController.renameAlbum.equals(albumTitles.get(albumIndex))){
	    				break;
	    			}
	    			albumIndex++;
	    		}
	    	}
	    	
	    	int i = 0;
	    	boolean validName = true;
	    	while(i < albumTitles.size()){
	    		if(title.getText().equals(albumTitles.get(i)) && i != albumIndex){
	    			validName = false;
	    		}
	    		i++;
	    	}

	    	if(validName){
	    		String fileName = userList.get(Controller.count).getAlbums().get(albumIndex).getTitle();
	    		userSubsystemController.list.set(userSubsystemController.renameAlbumIndex, title.getText());
	    		userList.get(Controller.count).getAlbums().get(albumIndex).setTitle(title.getText());
	    		File folder = new File("data/" + userList.get(Controller.count).getUsername() + "/");
	    		File newFile = new File("data/" + userList.get(Controller.count).getUsername() + "/" + title.getText());
                File[] listOfFiles = folder.listFiles();
                i = 0;
                while(i < folder.listFiles().length){
                	if(listOfFiles[i].getName().equals(fileName)){
                		listOfFiles[i].renameTo(newFile);
                		break;
                	}
                	i++;
                }

				PhotoAlbum.write(userList);
	    		
	    		FXMLLoader userPageLoader = new FXMLLoader();
				userPageLoader.setLocation(getClass().getResource("/view/UserSubsystem.fxml"));
				userSubsystemController userController = userPageLoader.getController();
	     	
				Stage currentStage = (Stage)createAlbum.getScene().getWindow();
				currentStage.close();
	    	}else{
	    		Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An album with same title already exists!");
                alert.showAndWait();
                title.clear();
	    	}
		
		}
		
		
	}
	
}
