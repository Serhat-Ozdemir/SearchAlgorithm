
public class HashNode {
	String value;
	FileNode firstFile;
	
	HashNode(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FileNode getFirstFile() {
		return firstFile;
	}

	public void setFirstFile(FileNode firstFile) {
		this.firstFile = firstFile;
	}
}
