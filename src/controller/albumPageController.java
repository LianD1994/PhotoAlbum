package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class is used to control albumPage.fxml, an interface that 
 * user can modify contents in a selected album.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
public class albumPageController{

	private ArrayList<User> userList = new ArrayList<>();
	public static ArrayList<Photo> albumList = new ArrayList();
	public static boolean canCreateNewAlbum = false;
	
    @FXML TextField dateTextField;
    @FXML TextField tagTextField;
    @FXML TilePane tilePane;
    @FXML Button quitButton;
    @FXML Button backButton;
    @FXML Button addButton;
    @FXML Button removeButton;
    @FXML Button tagButton;
    @FXML Button captionButton;
    @FXML Button tagSearch;
    @FXML Button dateSearch;
    @FXML Text titleText;
    @FXML Text date;
    @FXML Text tag;
    @FXML Text caption;
    
    private String path = null;

    // To check if the operation is Move
    static boolean isMove = false;

    // to keep track of the selected photo for further operation
    public static String selectedPhotoName;

    // Keep track of selectedImageView for selection effect
    private static ImageView selectedImageView;

    // ArrayList for unselected ImageViews for cancel previous selection effect
    private static ArrayList<ImageView> unSelectedImageView = new ArrayList<>();

    // id to assign to each imageView for comparison
    private static int id = 0;
    
    /**
     * Cancel the search result, display all pictures in the album.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML public void onCancelButtonClicked() throws ClassNotFoundException, IOException{
    	canCreateNewAlbum = false;
    	showPictures();
    	date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");
    }
    
    /**
     * Search through all photos within the directory between two dates formatted in 
     * YYYYMMDD YYYYMMDD input, and display the resulting photos.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML public void onDateSearchButtonClicked() throws ClassNotFoundException, IOException{
    	if(dateTextField.getText().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, cannot be empty!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	String[] dateInput;
    	dateInput = dateTextField.getText().split(" ");
    	
    	if(dateInput.length != 2){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, length error!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	String date1 = dateInput[0];
    	String date2 = dateInput[1]; 
    	
    	if(date1.length() > 8 || date2.length() > 8){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, date length error!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	int count = 0;
    	boolean good = true;
    	while(count < date1.length()){
    		if(!Character.isDigit(date1.charAt(count))){
    			good = false;
    			break;
    		}
    		count++;
    	}
    	while(count < date2.length()){
    		if(!Character.isDigit(date1.charAt(count))){
    			good = false;
    			break;
    		}
    		count++;
    	}
    	
    	if(!good){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, input has to be all digits!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	int month1 = Integer.parseInt(date1.substring(4, 5));
    	int month2 = Integer.parseInt(date2.substring(4, 5));
    	
    	if(month1 > 12 || month2 > 12){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, month error!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	int day1 = Integer.parseInt(date1.substring(6, 7));
    	int day2 = Integer.parseInt(date2.substring(6, 7));
    	
    	if(((month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) && day1 > 31) || ((month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) && day2 > 31)){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, month and day dont match!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	if(((month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) && day1 > 31) || ((month2 == 4 || month2 == 6 || month2 == 9 || month2 == 11) && day2 > 31)){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, month and day dont match!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	int oldDate = Integer.parseInt(date1);
    	int newDate = Integer.parseInt(date2);
    	if(oldDate > newDate){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter date range following hint format, please put the older date in the front!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}
    	
    	albumList.clear();
    	
    	ArrayList<User> userList = new ArrayList();
		userList = PhotoAlbum.read(userList);
    	count = 0;
    	while(count < userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().size()){
    		Format formatter = new SimpleDateFormat("yyyyMMdd");
            String dateS = formatter.format(userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(count).getDate());
            int thisDate = Integer.parseInt(dateS);
    		if(thisDate >= oldDate && thisDate <= newDate){
    			albumList.add(userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(count));
    		}
    		count++;
    	}
    	
    	tilePane.getChildren().clear();

        count = 0;
        while(count < albumList.size()){
                ImageView imageView;
                File file = new File(albumList.get(count).getAddress());
                imageView = createImageView(file);
                tilePane.getChildren().addAll(imageView);  
                count++;
        }
        date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");
        dateTextField.clear();
        canCreateNewAlbum = true;   	
    }
    
    /**
     * Search through the album for photos with a certain tag input, 
     * then display all resulting photos.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML public void onTagSearchButtonClicked() throws ClassNotFoundException, IOException{
    	albumList.clear();
    	if(tagTextField.getText().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a Tag!");
            alert.showAndWait();
            return;
    	}else{
    		String findTag = tagTextField.getText();
    		ArrayList<User> userList = new ArrayList();	
    		userList = PhotoAlbum.read(userList);
    		
    		int photoCount = 0;
    		int tagCount = 0;
    		while(photoCount < userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().size()){
    				tagCount = 0;
    			while(tagCount < userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(photoCount).getTag().size()){
    				if(findTag.equals(userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(photoCount).getTag().get(tagCount))){
    					albumList.add(userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(photoCount));
    					break;
    				}
    				tagCount++;
    			}
    			photoCount++;
    		}
    		
    		
    		
    		tilePane.getChildren().clear();

            int count = 0;
            while(count < albumList.size()){
                    ImageView imageView;
                    File file = new File(albumList.get(count).getAddress());
                    imageView = createImageView(file);
                    tilePane.getChildren().addAll(imageView);  
                    count++;
            }
            date.setText("Date");
            caption.setText("Caption");
            tag.setText("Tags");
            tagTextField.clear();
            canCreateNewAlbum = true;
    	}    	
    }
    
    /**
     * Called when user want to create a new album based on search result.
     * Pop a new window for user to enter desired album title.
     * @throws IOException
     */
    @FXML public void createSearchAlbum() throws IOException{
    	if(!canCreateNewAlbum){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There is no search result, please search first!");
            alert.showAndWait();
            dateTextField.clear();
            return;
    	}else{
    		Stage searchAlbumStage = new Stage();
    		FXMLLoader searchAlbumLoader = new FXMLLoader();
    		searchAlbumLoader.setLocation(getClass().getResource("/view/searchAlbum.fxml"));
    		AnchorPane searchAlbumFXML = searchAlbumLoader.load();
            Scene searchAlbum = new Scene(searchAlbumFXML, 230, 150);
    		searchAlbumStage.setScene(searchAlbum);
    		searchAlbumStage.showAndWait();
    	}
    }
    
    /**
     * Used to caption selected photo.
     * A caption window will pop.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    @FXML public void onCaptionButtonClicked() throws IOException, ClassNotFoundException, InterruptedException{
    	if(!date.getText().equals("Date")){
	    	Stage captionStage = new Stage();
			FXMLLoader captionLoader = new FXMLLoader();
			captionLoader.setLocation(getClass().getResource("/view/Caption.fxml"));
			AnchorPane captionFXML = captionLoader.load();

            Scene caption = new Scene(captionFXML, 230, 150);
			captionStage.setScene(caption);
			captionStage.showAndWait();	
			
    	}else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Photo!");
            alert.showAndWait();
    	}
    	
    	showPictures();
    	date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");
    }
    
    /**
     * Used to add/delete tags on selected photo.
     * Tag window will pop.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onTagButtonClicked() throws IOException, ClassNotFoundException{
    	
    	if(!date.getText().equals("Date")){
	    	Stage tagStage = new Stage();
			FXMLLoader tagLoader = new FXMLLoader();
			tagLoader.setLocation(getClass().getResource("/view/Tag.fxml"));
			AnchorPane tagFXML = tagLoader.load();

            Scene tag = new Scene(tagFXML, 230, 150);
			tagStage.setScene(tag);
			tagStage.showAndWait();	
			
    	}else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Photo!");
            alert.showAndWait();
    	}
    	
    	showPictures();
    	date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");
    	
    }
    
    /**
     * Used to display all photos in the selected album directory.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void showPictures() throws ClassNotFoundException, IOException{

    	tilePane.getChildren().clear();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null) {
            for (final File file : listOfFiles) {
                ImageView imageView;
                imageView = createImageView(file);
                tilePane.getChildren().addAll(imageView);
            }
        }
    }
    /**
     * Called when entering from userSubsystem page.
     * Display all pictures in the selected album directory.
     * @param album the selected album.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    void start(Album album) throws ClassNotFoundException, IOException {
    	

        // Read from userList.tmp
        userList = PhotoAlbum.read(userList);
        // Set album path
        path = "data/" + userList.get(Controller.count).getUsername() + "/" + album.getTitle() +"/";
        // Set album Title text
        titleText.setText(album.getTitle());
        // Display photos in the album
        showPictures();
        date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");
    }
    
    /**
     * Used to add new photo into the album.
     * If a photo with same name already exist will fail.
     * FileChooser is launched in this method.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onAddButtonClicked() throws IOException, ClassNotFoundException {

        userList = PhotoAlbum.read(userList);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        try{
        	File selectedFile = fileChooser.showOpenDialog(quitButton.getScene().getWindow());
        	
        	if (selectedFile.exists()){
                File readf = selectedFile.getAbsoluteFile();
                int count = 0;
                while(count < userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().size()){
                	if(selectedFile.getName().equals(userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhotoList().get(count).getName())){
                		Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("A photo with the same name already exists!");
                        alert.showAndWait();
                        date.setText("Date");
                        caption.setText("Caption");
                        tag.setText("Tags");
                        return;
                	}
                	count++;
                }
                BufferedImage image = ImageIO.read(readf);
                File temp = new File( path + readf.getName());
                ImageIO.write(image, "jpg", temp);

                Photo newPhoto = new Photo();
                newPhoto.setName(readf.getName());
                newPhoto.setAddress(temp.getPath());
                newPhoto.setDate();
                userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).addPhoto(newPhoto);

                PhotoAlbum.write(userList);

            }
        }
        catch(NullPointerException e){
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Picture has not been added!");
            alert.showAndWait();
        }
        
        showPictures();
        date.setText("Date");
        caption.setText("Caption");
        tag.setText("Tags");

    }

    /**
     * Used to remove the selected photo.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onRemoveButtonClicked() throws IOException, ClassNotFoundException  {
    	if(!date.getText().equals("Date")){
	        // Read from userList.tmp
	        userList = PhotoAlbum.read(userList);
	
	        // Get the selected photo
	        Photo selected = userList.get(Controller.count).getAlbums().get(
	                userSubsystemController.albumIndex).getPhoto(selectedPhotoName);
	
	        // Delete selected photo file
	        File photoFile = new File(selected.getAddress());
	        boolean isDeleted = photoFile.delete();
	        if(isDeleted){ //check if the file has been deleted to avoid async operation
	            // Delete it from album's photoList
	            userList.get(Controller.count).getAlbums().get(
	                    userSubsystemController.albumIndex).deletePhoto(selected.getName());
	        }
	
	        // Write to userList.tmp
	        PhotoAlbum.write(userList);
	        
	        showPictures();
	        date.setText("Date");
	        caption.setText("Caption");
	        tag.setText("Tags");
    	}else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Photo!");
            alert.showAndWait();
    	}
    }

    /**
     * Used when back button is clicked,
     * go back to userSubsystemPage.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onBackButtonClicked() throws IOException, ClassNotFoundException {

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

		Stage currentStage = (Stage)backButton.getScene().getWindow();
		
		currentStage.close();
		userStage.show();
    }

    /**
     * Called when quit button is clicked.
     * Quit the program.
     */
    @FXML public void onQuitButtonClicked(){

    	Stage currentStage = (Stage)quitButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Called when move button is clicked and only when a photo is selected.
     * Pop a window to let the user choose which album to move the photo to.
     * Photo in the current album will be deleted.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onMoveButtonClicked() throws IOException, ClassNotFoundException {

    	if(!date.getText().equals("Date")){
	        isMove = true;
	
	        // Read from userList.tmp
	        userList = PhotoAlbum.read(userList);
	
	        // Get the selected photo
	        Photo selected = userList.get(Controller.count).getAlbums().get(
	                userSubsystemController.albumIndex).getPhoto(selectedPhotoName);
	
	        // Display the destination album selection pane
	        // =============================================
	        Stage albumSelectStage = new Stage();
	        FXMLLoader albumSelectLoader = new FXMLLoader();
	        albumSelectLoader.setLocation(getClass().getResource("/view/move.fxml"));
	        AnchorPane albumSelectFXML = albumSelectLoader.load();
	        moveController selectController = albumSelectLoader.getController();
	        selectController.start(selected);
	        Scene selectPage = new Scene(albumSelectFXML, 224, 444);
	        albumSelectStage.setScene(selectPage);
	        albumSelectStage.showAndWait();
	        
	        tilePane.getChildren().clear();
	        showPictures();
	        date.setText("Date");
	        caption.setText("Caption");
	        tag.setText("Tags");
    	}else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Photo!");
            alert.showAndWait();
    	}
    		
    }
    
    /**
     * Called when move button is clicked and only when a photo is selected.
     * Pop a window to let the user choose which album to copy the photo to.
     * Photo in the current album will not be deleted.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    void onCopyButtonClicked() throws IOException, ClassNotFoundException {
    	if(!date.getText().equals("Date")){
	        isMove = false;
	
	        // Read from userList.tmp
	        userList = PhotoAlbum.read(userList);
	
	        // Get the selected photo
	        Photo selected = userList.get(Controller.count).getAlbums().get(
	                userSubsystemController.albumIndex).getPhoto(selectedPhotoName);
	
	        // Display the destination album selection pane
	        // =============================================
	        Stage albumSelectStage = new Stage();
	        FXMLLoader albumSelectLoader = new FXMLLoader();
	        albumSelectLoader.setLocation(getClass().getResource("/view/move.fxml"));
	        AnchorPane albumSelectFXML = albumSelectLoader.load();
	        moveController selectController = albumSelectLoader.getController();
	        selectController.start(selected);
	        Scene selectPage = new Scene(albumSelectFXML, 224, 444);
	        albumSelectStage.setScene(selectPage);
	        albumSelectStage.show();
	        
	        date.setText("Date");
	        caption.setText("Caption");
	        tag.setText("Tags");
	    }else{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Error");
	        alert.setHeaderText(null);
	        alert.setContentText("Please Select A Photo!");
	        alert.showAndWait();
		}

    }
    
    /**
     * Used to display all image in the album directory in tilePane. 
     * @param imageFile an imageFile that is displayed
     * @return imageView an imageView inside the tilePane.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private ImageView createImageView(final File imageFile) throws ClassNotFoundException, IOException {

        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing
        ImageView imageView;
        String name = imageFile.getName();

        FileInputStream inputStream = new FileInputStream(imageFile);
        final Image image = new Image(inputStream, 100, 80, true, true);
        inputStream.close();

        imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(80);
        imageView.setId(Integer.toString(id));
        id++;

        unSelectedImageView.add(imageView);

        // Read from userList.tmp
        userList = PhotoAlbum.read(userList);

        Photo temp = userList.get(Controller.count).getAlbums().get(userSubsystemController.albumIndex).getPhoto(name);

        imageView.setOnMouseClicked(mouseEvent -> {

            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                // Set the selectedPhotoName to the imageFile name
                selectedPhotoName = imageFile.getName();

                // Display photo info on single click
                if(mouseEvent.getClickCount() == 1) {


                    // Add effect to selected photo
                    // ===========================================================
                    // set the selectedImageView
                    selectedImageView = imageView;
                    // Add dropShadow effect to selectedImageView
                    DropShadow shadow = new DropShadow();
                    imageView.setEffect(shadow);
                    // Get rid of the effect on previously selectedImageView
                    // So only one image has effect
                    for(int i=0;i<unSelectedImageView.size();i++){
                        if(!unSelectedImageView.get(i).getId().equals(selectedImageView.getId())){
                            unSelectedImageView.get(i).setEffect(null);
                        }
                    }


                    Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String dateS = formatter.format(temp.getDate());
                    date.setText(dateS);
                    if (temp.getCaption() == null) {
                        caption.setText("No Caption Yet");
                    } else {
                        caption.setText(temp.getCaption());
                    }
                    if (temp.getTag().size() == 0) {
                        tag.setText("No Tags Yet");
                    } else {
                        tag.setText(temp.getTag().toString());
                    }
                }

                // Display the photo on mouse double click
                if(mouseEvent.getClickCount() == 2){

                    try {
                        Stage displayStage = new Stage();
                        FXMLLoader displayPageLoader = new FXMLLoader();
                        displayPageLoader.setLocation(getClass().getResource("/view/photoDisplay.fxml"));
                        AnchorPane displayPageFXML = null;
                        displayPageFXML = displayPageLoader.load();
                        displayController displayController = displayPageLoader.getController();

                        FileInputStream inputStream2 = new FileInputStream(imageFile);
                        Image displayImage = new Image(inputStream2, 690, 363, false, true);
                        inputStream2.close();

                        displayController.start(displayImage, temp);
                        Scene selectPage = new Scene(displayPageFXML, 795, 574);
                        displayStage.setScene(selectPage);
                        displayStage.show();

                    } catch (NullPointerException | ClassNotFoundException | IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        return imageView;
    }
}
