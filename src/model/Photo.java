package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class defines Photo object.
 * It is used to save album details, and photo in the 
 * album in an arrayList.
 * Belongs to userList.
 * @author Liangyan Ding
 * @author Tien-Hsueh Li
 *
 */
public class Photo implements Serializable{
	
	private String name;
	private String address;
	private Date date;
	private String caption;
	private ArrayList<String> tag = new ArrayList();
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return this.address;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public void setDate(){
		Calendar c = Calendar.getInstance();
		this.date = c.getTime();
	}
	
	public void setCaption(String caption){
		this.caption = caption;
	}
	
	public String getCaption(){
		return this.caption;
	}
	
	public void addTag(String tag){
		this.tag.add(tag);
	}
	
	public void removeTag(String tag){
		this.tag.remove(tag);
	}
	
	public ArrayList<String> getTag(){
		return this.tag;
	}

}
