package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.User;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is called when entered from login page with an existing username.
 * Display all the album directories under the username.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
public class userSubsystemController {
	
	static ObservableList<String> list;
	static String renameAlbum = null;
	static int renameAlbumIndex = 0;
	static String createOrRename = null;
	static int albumIndex = 0;
	static Stage userStage;
	
	private ArrayList<Album> albumList = new ArrayList<>();
	private ArrayList<String> albumTitles = new ArrayList<>();
	private ArrayList<User> userList = new ArrayList<>();
	boolean good = false;

	@FXML ListView<String> albumListView;
    @FXML Button logoutButton;
    @FXML Button createButton;
    @FXML Button deleteButton;
    @FXML Button renameButton;
    @FXML Text photoNumbers;
    @FXML Text latestPhoto;
    @FXML Text welcome;
    @FXML Text num;
    @FXML Text lastDate;
    @FXML Text dateRange;

    /**
     * This class is called when the open button is clicked and there is a selected
     * album.
     * Advance to the albumPage with the selected album.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
	void onOpenButtonClicked() throws IOException, ClassNotFoundException {
    	if(good){
			// Switches to Album Stage
			// ==========================
			Stage albumStage = new Stage();
			Stage currentStage = (Stage)logoutButton.getScene().getWindow();
			FXMLLoader albumPageLoader = new FXMLLoader();
			albumPageLoader.setLocation(getClass().getResource("/view/AlbumPage.fxml"));
			AnchorPane albumPageFXML = albumPageLoader.load();
			albumPageController albumController = albumPageLoader.getController();
			Scene album = new Scene(albumPageFXML, 709, 557);
			albumStage.setScene(album);
	
	
			// Find the matching album name
			// Display the corresponding album Info
			// ====================================
			String selectedAlbumName = albumListView.getSelectionModel().getSelectedItem();
	
			if(selectedAlbumName != null) {// Do the following if something is selected
				for (int i = 0; i < albumList.size(); i++) {// find the matching album
					if (albumList.get(i).getTitle().equals(selectedAlbumName)) {
						albumIndex = i;
						// Display corresponding info
						albumController.start(albumList.get(i));
					}
				}
				userStage = currentStage;
				// Close the current stage, open the album stage
				currentStage.close();
				albumStage.show();
			}
    	}else{
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an album!");
            alert.showAndWait();
    	}
	}


    /**
     * This method is called when logout button is clicked.
     * Go back to the login page.
     * @throws IOException
     */
    @FXML public void onLogoutButtonClicked() throws IOException {
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
     * This method is called when an album is being selected and delete button is clicked.
     * Delete the selected album direcotry.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onDeleteButtonClicked() throws IOException, ClassNotFoundException{
    	
    	if(good){

			userList = PhotoAlbum.read(userList);
	
	    	albumTitles = new ArrayList<>();
	    	albumList = userList.get(Controller.count).getAlbums();    //get albumList
	        int count = 0;
	        if(albumList != null){                                     //get all titles in albumList
	        	while(count < albumList.size()){
	        		albumTitles.add(albumList.get(count).getTitle());
	        		count++;
	        	}
	        }
	    	
	    	String title = albumListView.getSelectionModel().getSelectedItem(); //get selected album title
	    	int index = albumListView.getSelectionModel().getSelectedIndex();   //get index in list view
	    	albumIndex = 0;
	    	boolean deletable = false;
	    	if(albumTitles != null && title != null){                           //get index in the user album list
	    		while(albumIndex < albumTitles.size()){
	    			if(title.equals(albumTitles.get(albumIndex))){
	    				deletable = true;
	    				break;
	    			}
	    			albumIndex++;
	    		}
	    	}
	
	    	if(deletable){
	    		
	    		File folder = new File("data/" + userList.get(Controller.count).getUsername() + "/");
	            File[] listOfFiles = folder.listFiles();
	            int i = 0;
	            while(i < folder.listFiles().length){
	            	if(listOfFiles[i].getName().equals(title)){
	            		listOfFiles[i].delete();
	            		break;
	            	}
	            	i++;
	            }
	            
	    		Album deleteAlbum = albumList.get(albumIndex);
	    		userList.get(Controller.count).deleteAlbum(deleteAlbum);
	    		list.remove(index);
	
				PhotoAlbum.write(userList);
	    	}
	    	good = false;
	    	num.setText("");
	    	lastDate.setText("");
	    	dateRange.setText("Range of Dates: ");
    	}else{
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an album!");
            alert.showAndWait();
    	}
    }


    /**
     * This method is called when an album is selected and rename button is clicked.
     * Pop a new window to let the user type in new album name, and change the direcotry name
     * and userList accordingly.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML public void onRenameButtonClicked() throws IOException, ClassNotFoundException{
    	
    	if(good){
    	
	    	if(albumListView.getSelectionModel().getSelectedItem() != null){
	    	
	    		renameAlbum = albumListView.getSelectionModel().getSelectedItem();
	    		renameAlbumIndex = albumListView.getSelectionModel().getSelectedIndex();
				createOrRename = "rename";
	    		Stage createStage = new Stage();
				FXMLLoader createLoader = new FXMLLoader();
				createLoader.setLocation(getClass().getResource("/view/CreatePage.fxml"));
				AnchorPane createFXML = createLoader.load();
				
				createController createController = createLoader.getController();
				Scene create = new Scene(createFXML, 230, 150);
				createStage.setScene(create);
				createStage.show();
		    	createController.setRename();
		    	good = false;
		    	num.setText("");
		    	lastDate.setText("");
		    	dateRange.setText("Range of Dates: ");

	    	
	    	}
    	}else{
    		Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an album!");
            alert.showAndWait();
    	}
    }

    /**
     * This method is called when quit button is clicked.
     * Exit program.
     */
    @FXML public void onQuitButtonClicked(){

        Stage currentStage = (Stage)logoutButton.getScene().getWindow();
        currentStage.close();
    }


    /**
     * This method is used to get all directories under the username directory under data
     * directory, and display them in the view while setting welcome message with the username.
     * @throws ClassNotFoundException
     * @throws IOException
     */
	@FXML
	void setAlbumListView() throws ClassNotFoundException, IOException{

		userList = PhotoAlbum.read(userList);

        welcome.setText("Welcome! " + userList.get(Controller.count).getUsername());
        albumList = userList.get(Controller.count).getAlbums();
        int count = 0;
        if(albumList != null){
        	while(count < albumList.size()){
        		albumTitles.add(albumList.get(count).getTitle());
        		count++;
        	}
        }
        list = FXCollections.observableArrayList(albumTitles);
        albumListView.setItems(list);
	}

    /**
     * This method is used when create button is clicked.
     * Pop the Createpage window for user to create a new album.
     * @throws NullPointerException
     * @throws ClassNotFoundException
     * @throws IOException
     */
	@FXML public void createButtonClicked() throws NullPointerException,ClassNotFoundException, IOException{
		Stage createStage = new Stage();
		FXMLLoader createLoader = new FXMLLoader();
		createLoader.setLocation(getClass().getResource("/view/CreatePage.fxml"));
		AnchorPane createFXML = createLoader.load();

		Scene create = new Scene(createFXML, 230, 150);
		createStage.setScene(create);
		createStage.show();
		createOrRename = "create";
		good = false;
    	num.setText("");
    	lastDate.setText("");
    	dateRange.setText("Range of Dates: ");

	}

	/**
	 * This method is to control mouseClickMovement.
	 * Display number of photos, date range, last modified date, accordingly.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@FXML public void handleMouseClick() throws IOException, ClassNotFoundException{

		userList = PhotoAlbum.read(userList);


		albumTitles = new ArrayList<>();
    	albumList = userList.get(Controller.count).getAlbums();    //get albumList
        int count = 0;
        if(albumList != null){                                     //get all titles in albumList
        	while(count < albumList.size()){
        		albumTitles.add(albumList.get(count).getTitle());
        		count++;
        	}
        }
    	
    	String title = albumListView.getSelectionModel().getSelectedItem(); //get selected album title
		int albumIndex = 0;
    	if(albumTitles != null && title != null){                           //get index in the user album list
    		while(albumIndex < albumTitles.size()){
    			if(title.equals(albumTitles.get(albumIndex))){
    				break;
    			}
    			albumIndex++;
    		}
    	}
    	Album temp = userList.get(Controller.count).getAlbums().get(albumIndex);
    	int dateMinIndex;
    	int dateMaxIndex;
    	Date minDate;
    	Date maxDate;
    	if(temp.getPhotoList().size() > 0){
    		minDate = temp.getPhotoList().get(0).getDate();
    		maxDate = minDate;
    		dateMinIndex = 0;
    		dateMaxIndex = 0;
    		Format formatter = new SimpleDateFormat("yyyyMMdd");
    		String date1 = formatter.format(minDate);
    		String date2 = formatter.format(maxDate);
            
    		int i = 0;
    		while(i < temp.getPhotoList().size()){
        		String date3 = formatter.format(temp.getPhotoList().get(i).getDate());
        		if(Integer.parseInt(date3) > Integer.parseInt(date2)){
        			dateMaxIndex = i;
        			date2 = formatter.format(date3);
        		}
        		if(Integer.parseInt(date3) < Integer.parseInt(date1)){
        			dateMinIndex = i;
        			date1 = formatter.format(date3);
        		}
        		i++;
    		}
    		Format displayFormatter = new SimpleDateFormat("yyyy/MM/dd");
    		dateRange.setText("Range of Dates: " + displayFormatter.format(temp.getPhotoList().get(dateMinIndex).getDate()) + "~" + displayFormatter.format(temp.getPhotoList().get(dateMaxIndex).getDate()));
    		lastDate.setText(displayFormatter.format(temp.getPhotoList().get(dateMinIndex).getDate()));
    	}
		
    	
    	
    	num.setText(Integer.toString(temp.getPhotoNumber()));
    	
    	
		good = true;
	}

}

