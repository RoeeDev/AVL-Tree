
public class Item{
	
	private int key;
	private String info;
	
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	//this method returns item's key
	public int getKey(){
		return key;
	}
	//this method returns item's info
	public String getInfo(){
		return info;
	}
}