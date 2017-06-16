package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Album;
import model.Photo;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class is used to display a photo that is displayed in AlbumPage.fxml is double-clicked.
 * Shows one picture at a time in a large window with details.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
public class displayController{

    @FXML ImageView imageView;
    @FXML Text tags;
    @FXML Text date;
    @FXML Text caption;

    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<Photo> photoList = new ArrayList<>();
    private int currPhotoIndex;

    /**
     * Called when first entered from AlbumPage, 
     * display the photo that was double clicked.
     * @param displayImage	the image that was double-clicked.
     * @param photo			the photo type of the image that was double-clicked, used for displaying details.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void start(Image displayImage, Photo photo) throws IOException, ClassNotFoundException {

        // Read from userList.tmp
        userList = PhotoAlbum.read(userList);

        // Get the photoList
        photoList = userList.get(Controller.count).getAlbums().get(
                userSubsystemController.albumIndex).getPhotoList();

        // Get current Album
        Album currAlbum = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex);

        // Get the current photo index
        currPhotoIndex = currAlbum.getPhotoIndex(photo);
        
        Photo temp = currAlbum.getPhotoList().get(currPhotoIndex);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateS = formatter.format(temp.getDate());
        date.setText(dateS);
        if (temp.getCaption() == null) {
            caption.setText("No Caption Yet");
        } else {
            caption.setText(temp.getCaption());
        }
        if (temp.getTag().size() == 0) {
            tags.setText("No Tags Yet");
        } else {
            tags.setText(temp.getTag().toString());
        }

        // Set the image user just double clicked on to imageView
        imageView.setImage(displayImage);
    }

    /**
     * Called when prev button is clicked.
     * Go to the previous picture in the photo array, and display
     * its details.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onPrevButtonClicked() throws IOException, ClassNotFoundException {

        int prevPhotoIndex = currPhotoIndex - 1;

        if(prevPhotoIndex < 0){
            return;
        }

        currPhotoIndex = prevPhotoIndex;
        Album currAlbum = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex);
        Photo temp = currAlbum.getPhotoList().get(currPhotoIndex);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateS = formatter.format(temp.getDate());
        date.setText(dateS);
        if (temp.getCaption() == null) {
            caption.setText("No Caption Yet");
        } else {
            caption.setText(temp.getCaption());
        }
        if (temp.getTag().size() == 0) {
            tags.setText("No Tags Yet");
        } else {
            tags.setText(temp.getTag().toString());
        }

        Photo prevPhoto = photoList.get(prevPhotoIndex);

        File photoFile = new File(prevPhoto.getAddress());
        FileInputStream inputStream = new FileInputStream(photoFile);
        Image displayImage = new Image(inputStream, 690, 363, false, true);
        inputStream.close();

        imageView.setImage(displayImage);
    }

    /**
     * Called when next button is clicked.
     * Go to the next photo in the photto array.
     * @throws IOException
     */
    @FXML
    void onNextButtonClicked() throws IOException {

        int nextPhotoIndex = currPhotoIndex + 1;

        if(nextPhotoIndex >= photoList.size()){
            return;
        }

        currPhotoIndex = nextPhotoIndex;
        Album currAlbum = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex);
        Photo temp = currAlbum.getPhotoList().get(currPhotoIndex);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateS = formatter.format(temp.getDate());
        date.setText(dateS);
        if (temp.getCaption() == null) {
            caption.setText("No Caption Yet");
        } else {
            caption.setText(temp.getCaption());
        }
        if (temp.getTag().size() == 0) {
            tags.setText("No Tags Yet");
        } else {
            tags.setText(temp.getTag().toString());
        }

        Photo nextPhoto = photoList.get(nextPhotoIndex);

        File photoFile = new File(nextPhoto.getAddress());
        FileInputStream inputStream = new FileInputStream(photoFile);
        Image displayImage = new Image(inputStream, 690, 363, false, true);
        inputStream.close();

        imageView.setImage(displayImage);
    }
}
