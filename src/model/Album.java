package model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class defines the Album object.
 * It saves photos in an arrayList with several informations.
 * Belongs to the User object.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class Album implements Serializable{
	
	private String title;
	private ArrayList<Photo> photoList = new ArrayList<>();
	
	public String getTitle(){
		return this.title;
	}

	public void setTitle(String title){
		this.title = title;
	}
	
	public Album(String title){
		this.title = title;
	}
	
	public int getPhotoNumber(){
		return photoList.size();
	}
	
	public void addPhoto(Photo photo){
		this.photoList.add(photo);
	}

	public ArrayList<Photo> getPhotoList(){
		return this.photoList;
	}
	
	public Photo getPhoto(String title){
		
		int count = 0;
		while(count < photoList.size()){
			if(title.equals(photoList.get(count).getName())){
				return photoList.get(count);
			}
			count++;
		}
		return null;		
	}
	
	public void deletePhoto(String title){
		int count = 0;
		while(count < photoList.size()){
			if(title.equals(photoList.get(count).getName())){
				photoList.remove(count);
			}
			count++;
		}		
	}
	
	public int getPhotoIndex(Photo photo){

		for(int i=0;i< photoList.size();i++){
			if(photoList.get(i).getName().equals(photo.getName())){
				return i;
			}
		}

		return -1;
	}

}
