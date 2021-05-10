package in.games.jollygames.Model;

import com.google.gson.annotations.SerializedName;

public class SliderModel{

	@SerializedName("image")
	private String image;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	public String getImage(){
		return image;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getStatus(){
		return status;
	}
}