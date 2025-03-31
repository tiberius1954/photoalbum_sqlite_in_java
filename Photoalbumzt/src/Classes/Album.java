package Classes;

public class Album {
	int aid;
	String name;
	String path;

	public Album(int aid, String name, String path) {
		this.aid = aid;
		this.name = name;
		this.path = path;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}


}
