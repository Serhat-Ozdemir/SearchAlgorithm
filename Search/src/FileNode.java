
public class FileNode {
	String fileName;
	int count;
	
	FileNode nextNode;
	
	public FileNode(String fileName, int count) {
		this.fileName = fileName;
		this.count = count;
	}
	
	
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setNext(FileNode nextNode) {
		this.nextNode = nextNode;
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public int getCount() {
		return count;
	}
	public FileNode getNext() {
		return nextNode;
	}

}
