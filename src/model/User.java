package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class defines User object, 
 * which contains information of itself, and an arrayList that
 * contains albums for each users. 
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 */
public class User implements Serializable {

    private String username;
    private ArrayList<Album> albumList = new ArrayList();
    
    public void addAlbum(Album album){
    	albumList.add(album);
    }
    
    public void deleteAlbum(Album album){
    	albumList.remove(album);
    }
    
    public ArrayList<Album> getAlbums(){
    	return this.albumList;
    }
    
    public String getUsername(){
        return this.username;
    }

    public User(String username){
        this.username = username;
    }
    

}
