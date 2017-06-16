package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is called from UserSubsystem, when the move button was
 * clicked and a photo was selected.
 * Pop a window for the user to decide where to move the photo to.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
public class moveController {
	
    private Photo selectedPhoto;
    private ArrayList<User> userList = new ArrayList<>();
    private ObservableList<String> displayList = FXCollections.observableArrayList();

    @FXML ListView<String> listView;

    /**
     * Pop a window for the user to select album directory.
     * @param selected
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void start(Photo selected) throws IOException, ClassNotFoundException {

        // keep track of the selected photo for other methods
        selectedPhoto = selected;

        // Read from userList.tmp
        userList = PhotoAlbum.read(userList);

        // Display the User's AlbumList in listView
        ArrayList<Album> albumNames = userList.get(Controller.count).getAlbums();
        for(int i=0;i<albumNames.size();i++){
            displayList.add(albumNames.get(i).getTitle());
        }
        listView.setItems(displayList);
    }
    
    /**
     * Called when confirm button is clicked
     * If an album is selected move the photo to the selected directory, 
     * else show alert window.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onConfirmButtonClicked() throws IOException, ClassNotFoundException {

        // Read from userList.tmp
        userList = PhotoAlbum.read(userList);

        File photoFile = new File(selectedPhoto.getAddress());

        // Get the selected Album
        String selectedAlbumName = listView.getSelectionModel().getSelectedItem();

        String destination = "data/" + userList.get(Controller.count).getUsername() + "/" + selectedAlbumName +"/";
        ArrayList<Album> albumList = userList.get(Controller.count).getAlbums();

        // Check if the same photo exists in the destination folder
        File checkFile = new File(destination + photoFile.getName());
        if(checkFile.exists()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("photo already exist!");
            alert.showAndWait();
            return;
        }

        // Find the selected albumList, add the photo
        for(int i=0;i<albumList.size();i++){
            if(albumList.get(i).getTitle().equals(selectedAlbumName)){
                // Set new address for moved photo
                selectedPhoto.setAddress(destination + photoFile.getName());
                // Add the photo to the selected album
                userList.get(Controller.count).getAlbums().get(i).getPhotoList().add(selectedPhoto);
            }
        }



        // Add the photo file to selected album folder
        File readf = photoFile.getAbsoluteFile();
        BufferedImage image = ImageIO.read(readf);
        File temp = new File( destination + readf.getName());

        if(selectedAlbumName == null){
            return;
        }
        ImageIO.write(image, "jpg", temp);

        if(albumPageController.isMove) {
            // Remove photo from the current Album
            userList.get(Controller.count).getAlbums().get(
                    userSubsystemController.albumIndex).deletePhoto(selectedPhoto.getName());

            // Remove photo file from the current folder
            photoFile.delete();
        }

        // Write to userList.tmp
        PhotoAlbum.write(userList);

        // Close the selection pane
        Stage currentStage = (Stage) listView.getScene().getWindow();
        currentStage.close();
    }
}
