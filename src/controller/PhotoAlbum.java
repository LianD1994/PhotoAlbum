package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.*;
import java.util.ArrayList;

/**
 * This class is the main class for the project.
 * Start the login page.
 * @author Tien-Hsueh Li
 * @author Liangyan Ding
 *
 */
public class PhotoAlbum extends Application {

	/**
	 * Start the login page when compiled.
	 */
    @Override
    public void start(Stage primaryStage) throws Exception{

        // Need FXMLLoader instance to get corresponding Controller
        FXMLLoader loginPageLoader = new FXMLLoader();
        loginPageLoader.setLocation(getClass().getResource("/view/loginPage.fxml"));

        AnchorPane loginFxml = loginPageLoader.load();
        Controller loginController = loginPageLoader.getController();
        loginController.setPrevStage(primaryStage);

        Scene loginPage = new Scene(loginFxml, 600, 400);

        primaryStage.setScene(loginPage);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * main method, lanuch the program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Read data from userList.tmp file under data directory, and save it in an arrayList.
     * @param userList List of User objects.
     * @return an arrayList that is read from userList.tmp file.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static ArrayList<User> read(ArrayList<User> userList) throws IOException, ClassNotFoundException {
        BufferedReader bf = new BufferedReader(new FileReader("data/userList.tmp"));
        // Check if the file is empty to avoid Exception
        if (bf.readLine() != null) {
            // Read song Object list from file, add it to userList
            FileInputStream fis = new FileInputStream("data/userList.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            userList = (ArrayList<User>) ois.readObject();
            ois.close();
            fis.close();
        }

        return userList;
    }
    
    /**
     * Write the userList into the userList.tmp under data directory.
     * @param userList an arrayList that contains User objects.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static void write(ArrayList<User> userList) throws IOException, ClassNotFoundException {
        // Write userObj to file
        FileOutputStream fos = new FileOutputStream("data/userList.tmp");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(userList);
        oos.close();
        fos.close();
    }
}
