
public abstract class Hashing {
	protected int initialSize = 2477;
	protected HashNode [] table = new HashNode[initialSize]; //Using nodes to store words for reaching files and counts
	
	protected int entries = 0;
	protected int collisions = 0;
	protected int rehashCount = 0;
	protected int loadFactor;
	Hashing(int loadFactor){
		if(loadFactor != 50)// Standard loadFactor
			this.loadFactor = 80;
		else
			this.loadFactor = loadFactor;
	}

	
	public void add(int key, String value, FileNode file){
		int position = key;
		HashNode node = new HashNode(value);
		if(table[position] == null) {//Adding if position returned by hash function is empty
			table[position] = node;
			entries++;
		}
		
		else if(node.getValue().equals(table[position].getValue())) {}//If same value is added.(Stays for readability)
		
		fileAdder(table[position], file);//Arranges files
		
		if(entries == (table.length * loadFactor)/100) {//Rehash condition
			rehashCount++;
			rehash(table.length);
		}		
		
	}

	public FileNode search(int key, String value) {
		int position = key;
		if(table[position] == null) {//Means that there now word
			System.out.println("There is no word such = " + value);
			return null;
		}
		else if(table[position].getValue().equals(value)) {}//If same value is added.(Stays for readability)
		return table[position].getFirstFile();
			
	}
	//To make sure other collision handling hash classes write their own hash function
	public abstract int hashPAF (String value);
	
	public abstract int hashSSF (String value);
	
	public void fileAdder(HashNode node, FileNode file) {
		if(node.getFirstFile() == null) { //First time adding into index
			node.setFirstFile(file);
			if(file.getCount() == 0)//prevention for unwanted increase of first file when rehashing
				file.setCount(file.getCount()+1);
		}
		else {
			if(file.getFileName().equals(node.getFirstFile().getFileName()) ) //If there is multiple same words in first file
				node.getFirstFile().setCount(node.getFirstFile().getCount()+1);
			else {//Adding a new file or if there is multiple same words in files except first file
				FileNode tempNode = node.getFirstFile();
				while(tempNode.getNext() != null && !tempNode.getNext().getFileName().equals(file.getFileName()))//Getting next node until next node is null or already existing file
					tempNode = tempNode.getNext();
				if(tempNode.getNext() != null)//If there is a file which already existing
					tempNode.getNext().setCount(tempNode.getNext().getCount()+1);
				else{//Adding file first time
					file.setCount(file.getCount()+1);
					tempNode.setNext(file);
					
				}
			}

		}
	}

	public void rehash(int number) {
		int primeNumber = (number * 2) + 1; //Start number of searcing lowest prime number bigger than table size
		boolean isNotPrime;//Flag for loop
		//Entry and collision counts starts from begining because all entries will be added again
		entries = 0;
		collisions = 0;
		//Calculating prime number
		if(primeNumber == 0 || primeNumber == 1)
			System.out.println("Not possible");
		do {
			isNotPrime = false;
			for(int i=2;i <= primeNumber/2 ;i++){      
			    if(primeNumber % i == 0) {
			    	isNotPrime = true;
			    	break;
			    }
			   }
			primeNumber ++;
			
		}
			while(isNotPrime);
		primeNumber --;
		//Recreating table
		HashNode[] tempTable = table;
		table = new HashNode[primeNumber];
		//Adding all entries into new table
		for(int i = 0; i< tempTable.length; i++){
			if(tempTable[i] != null) {
				add(hashPAF(tempTable[i].getValue()),tempTable[i].getValue(),tempTable[i].getFirstFile());
				//add(hashSSF(tempTable[i].getValue()),tempTable[i].getValue(),tempTable[i].getFirstFile());
			}
		}
	}
	
	public void show() {
		//Writing hash table and some other things
		for(int i = 0 ; i< table.length; i++)
			if(table[i] != null) {
				System.out.print(table[i].getValue() + " ");
				FileNode tempNode = table[i].getFirstFile();
				while(tempNode.getNext()!= null) {
					System.out.print(tempNode.getFileName() + " " + tempNode.getCount() + " ");
					tempNode = tempNode.getNext();
				}
				System.out.println(tempNode.getFileName() + " " + tempNode.getCount());
				
			}
		System.out.println("entry count = "  + entries + " /t/n rehash count = " + rehashCount + "   table lengthh = " + table.length + "  collisions = " + collisions);
	}
}
