package admin;

import javafx.scene.image.Image;

public class Emp {
	public String id;
	public String name;
	public String job;
	public String email;
	public String pnum;

	private Image fp;
	Emp(){}
	Emp(String id,String name,String job,String email,String pnum){
		this.id=id;
		this.name=name;
		this.job=job;
		this.email=email;
		this.pnum=pnum;
	}
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getJob(){
		return job;
	}
	public String getEmail(){
		return email;
	}
	public String getPnum(){
		return pnum;
	}
	
}
