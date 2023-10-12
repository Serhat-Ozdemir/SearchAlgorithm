import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*                          IMPORTANT!!!  
		 * You must enter the wanted load factor as integer when creating hashMap. If you want load factor 0.8 you should write 80
		 * Load factor can not be different then 80 or 50. Standard load factor is 80 for different values // Hashing => 10
		 * Do not forget to call the true hashfunction when adding ( Main => 40), searching, searchTime ( Main => 244),
		   rehashing (Hashing => 105), Mostrelevant (Main => 129) .  
		 */
		File folder = new File("sport");
		File[] listOfFiles = folder.listFiles();//Puts files in a list.
		if(listOfFiles == null || listOfFiles.length == 0) {//Control if folder exists.
			System.out.println("There is no folder named '" + folder.getName() + "' in directory eclipse-workspace or '" + folder.getName() + "' has no '.txt' file");
			System.exit(0);
		}
		File stopWordtxt = new File("stop_words_en.txt");//Taking stop words, clearing them and splitting into an array.
		String[] stopWords = fileReader(stopWordtxt).toLowerCase(Locale.ENGLISH).replaceAll("[\\p{Punct}&&[^']]+", " ").replaceAll("\\s+","#").split("#");
		
		//Different collsion handling with different load factors
		
		/*LineerProbing hashMap = new LineerProbing(50);
		LineerProbing hashMap = new LineerProbing(80);
		
		DoubleHashing hashMap = new DoubleHashing(80);*/
		DoubleHashing hashMap = new DoubleHashing(50);//Most efficient hashtable
		
		//Loading files and words into hastable
		//long startLoad = System.nanoTime();
		for (int i = 0; i < 100; i++) {//Loop for all files in array of files, Number (100) can be changed according to file count in folder
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")) {
				String text = fileReader(listOfFiles[i]);//All content of a file returning as a string
				text = textCleaner(text, stopWords);//Cleaning file
				String[] textWords = text.split("#");//Splitting all words in an array
				for(int j = 0; j < textWords.length; j++) {//Adding all words in file
					FileNode file = new FileNode(listOfFiles[i].getName(),0);
					//hashMap.add(hashMap.hashSSF(textWords[j]), textWords[j], file );
					hashMap.add(hashMap.hashPAF(textWords[j]), textWords[j], file );
				}
			}
		  
		}
		//long finishLoad = System.nanoTime();
		//float timeSpent = (float)(finishLoad-startLoad)/1000000000;//Indexing time
		//System.out.println(timeSpent);
		
		//Restarting collisions before searching all words in Search.txt
		/*hashMap.collisions = 0;
		searchTime(hashMap);
		hashMap.show();*/
				
		System.out.println("Note:\n*You can't search only punctuations except (')."
				+ "\n*The punctuation (') is not removed from texts to clear all stop words efficiently."
				+ "\n*Words with and without (') are different => players, players' and player's are count as different words"
				+ "\n*Texts are cleared from stop words and numbers so if you try to search a stop word or number you can't find it."
				+ "\n*Do not search a word more than 45 characters or table will be broken. Longest word in English has 45 characters."
				+ "\n*You are free to enter words as much as you want."
				+ "\n*If more than one file has the same relation first file of folder is selected.");
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		while(flag) {
			System.out.println("\nPlease enter the words to search: ");
			String words = sc.nextLine().toLowerCase(Locale.ENGLISH).replaceAll("[\\p{Punct}&&[^']]+"," ").replaceAll("\\s+"," ");//Input clearing
			while(words.length() == 0 || (words.charAt(0) == ' ' && words.length() == 1)) {//Checking inut wheter acceptable or not
				System.out.println("Please enter something to seaarch");
				words = sc.nextLine().toLowerCase(Locale.ENGLISH).replaceAll("[\\p{Punct}&&[^']]+"," ").replaceAll("\\s+"," ");
			}		
			if(words.charAt(0) == ' ')//Precaution for null char
				words = words.substring(1);

			mostRelevant(words, hashMap);//Calculating most relevant three files and then the most relevant one
			flag = false;
			System.out.println("\nWrite yes if you want to search something else");
			if(sc.nextLine().toLowerCase(Locale.ENGLISH).replaceAll("[\\p{Punct}]+","").replaceAll("\\s+","").equals("yes"))
					flag = true;
		}
		System.out.println("\nGood Bye!");
		sc.close();
		
	}
	
	public static String fileReader(File file) throws IOException  {//Converts a .txt file to a string
		String content = "";
		
		  BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("There is no file such named '" + file.getName() +"' in directory eclipse-workspace");
			System.exit(0);
		}
		  String line;//Reads all lines and sum them in a string
		  while((line = br.readLine()) != null)
		  	content +=line + " ";    
		  br.close();
		  content = content.toLowerCase(Locale.ENGLISH).replaceAll("[\\p{Punct}&&[^']]+"," ");
		  return content;
	}
	
	public static String textCleaner(String text, String[] stopWordsList) { // Cleans string from delimeters, numbers and stop words than returns it		
		for(int i = 0; i< stopWordsList.length; i++) {
			text = text.replaceAll(" "+stopWordsList[i]+" ","  ");
		}
		text = text.replaceAll("\\d"," ");//numbers
		text = text.replaceAll("\\s+","#");//Non visible characters and white spaces
		if(text.charAt(0) == '#')
			text = text.substring(1);
		
		return text;
	}

	public static void mostRelevant(String textToSearch, Hashing hashMap) {
		FileNode tempNode, comparedFile = null; //tempNode for traveling into file list, comparedFile for holding the data of file which compared
		FileNode[] filesOfWords; //List of files for every word [0][1][2]... = [word1 files][word2 files][word3 files]...
		String[] words = textToSearch.split(" ");//All words in Search.txt
		int sum = 0, relationMultiplier = 0;//For calculating most relevant file, Also sum is used for clearing multiple same words
		//Clearing multiple words from words array
		for(int i = 0; i < words.length; i++)//Checks one index with bigger indexes
			for(int j = i+1; j < words.length; j++)
				if(words[i] != null && words[i].equalsIgnoreCase(words[j])) {
					words[j] = null;
					sum ++;
				}
		String[] tempArray = new String[words.length-sum];
		sum = 0;
		for(int i = 0; i < words.length; i++) {//Fills temp array with not null values
			if(words[i] == null)
				sum++;
			if(words[i]!=null)
				tempArray[i-sum] = words[i];
		}
		words = tempArray;
		sum = 0;
		String [][] mostRelevants = new String[words.length + 3][4];//Top three most relevant files and their info
		//Initializing two dimensional array
		mostRelevants[words.length+1][1] = "0"; mostRelevants[words.length+1][2] = "0"; mostRelevants[words.length+1][3] = "0";//Initial sum
		mostRelevants[words.length+2][1] = "0"; mostRelevants[words.length+2][2] = "0"; mostRelevants[words.length+2][3] = "0";//Initil relation power
		for(int i =1; i< words.length+1; i++)//Puttinng words into two dimensional array
			mostRelevants[i][0] = words[i-1];
		mostRelevants[words.length+1][0] = "Total occurence";
		mostRelevants[words.length+2][0] = "Relation power";
		
		
		filesOfWords = new FileNode[words.length];
		for(int i = 0; i < words.length; i++) //Putting all files in array according to words
			filesOfWords[i] = hashMap.search(hashMap.hashPAF(words[i]), words[i]);
		//Looking one file for all words and summing their total occurence and relation power and than comparing them until all files checked
		/*For example => [0] file1, file3, file5, file7 
		                 [1] file1, file5, file7
		                 [2] file2, file5
		  starts from [0] file 1 and checks other bigger indexes if there is any other file1. If yes than deletes it to prevent recount
		  when [0] file1 is done, then starts for [0] file3
		  when index [0] is done starts same from next index [1]...
		 */
		for(int i = 0; i < filesOfWords.length; i++) {
			tempNode = filesOfWords[i];
			while(tempNode != null) {
				sum =0;
				relationMultiplier = 0;
				int[] wordCounts = new int[words.length+2];//Holds the count of words with same order with filesOfWords[], additionally sum and relation multiplier
				if(!tempNode.getFileName().equals("") ) {
					wordCounts[i] = tempNode.getCount();
					comparedFile = tempNode;
					sum += tempNode.getCount();
					relationMultiplier += 1;
				}
				for(int j = i+1; j < filesOfWords.length; j++) {
					FileNode tempNodeTwo = filesOfWords[j];
					while(tempNodeTwo != null) {
						if(tempNode.getFileName().equals(tempNodeTwo.getFileName()) && !tempNodeTwo.getFileName().equals("")) {
							wordCounts[j] = tempNodeTwo.getCount();
							sum += tempNodeTwo.getCount();
							relationMultiplier += 1;
							tempNodeTwo.setFileName("");
						}
						tempNodeTwo = tempNodeTwo.getNext();
					}
				}
				tempNode = tempNode.getNext();
				wordCounts[wordCounts.length - 2] = sum;
				wordCounts[wordCounts.length - 1] = sum*relationMultiplier;
				
				//Starts comparing from most relevant file 
				
				//If the file is more relative than existing firs relative shifts 1. and 2. as 2. and 3. than adds the file as 1.
				if(wordCounts[wordCounts.length-1] > Integer.valueOf( mostRelevants[words.length+2][1])) {
					for(int k = 3; k > 1; k--) {
						for(int p = wordCounts.length ; p >= 0; p-- )
							mostRelevants[p][k] = mostRelevants[p][k-1];
					}
					for(int k = 0; k < wordCounts.length + 1; k++) {
						if(k ==0)
							mostRelevants[k][1] = comparedFile.getFileName();
						else 
							mostRelevants[k][1] = String.valueOf(wordCounts[k-1]) ;
					}
					
				}
				//If file is between 1. and 3. shifts 2. as 3. than adds the file as 2.
				else if(wordCounts[wordCounts.length-1] > Integer.valueOf( mostRelevants[words.length+2][2])) {
					for(int k = wordCounts.length ; k >= 0; k--)
						mostRelevants[k][3] = mostRelevants[k][2];
					for(int k = 0; k < wordCounts.length + 1; k++) {
						if(k ==0)
							mostRelevants[k][2] = comparedFile.getFileName();
						else 
							mostRelevants[k][2] = String.valueOf(wordCounts[k-1]) ;
					}

				}
				//If the file is more relative than 3. adds the file as 3.
				else if(wordCounts[wordCounts.length-1] > Integer.valueOf( mostRelevants[words.length+2][3])) {
					for(int k = 0; k < wordCounts.length + 1; k++) {
						if(k ==0)
							mostRelevants[k][3] = comparedFile.getFileName();
						else 
							mostRelevants[k][3] = String.valueOf(wordCounts[k-1]) ;
					}

				}
			}
		}
		//Monitoring two dimensional array
		for(int i =0; i < words.length+3; i++) {
			for(int j = 0; j< 4; j++)
				if(i ==0) {
					if(j == 0)
						System.out.printf("%-44s", " ");
					else if(mostRelevants[i][j] != null)
						System.out.printf("%-46s" , mostRelevants[i][j] );
				}
				else if(mostRelevants[i][j] != null && mostRelevants[i][j] != "0")
					System.out.printf("%-46s" , mostRelevants[i][j] );
			System.out.println("\n");
		}
		if(mostRelevants[0][1] == null)
			System.out.println("There is no file which includes the word(s) you have searched!");
		else
			System.out.println("Most Relevant File Is => " + mostRelevants[0][1]);
		
	}
	
	public static  void searchTime  (Hashing hashMap) throws IOException {
		File search = new File("search.txt");
		String text = null;
		try{
			text = fileReader(search);//Takes file as a string
		}
		catch(FileNotFoundException e) {
			System.out.println("Please check whether you have 'search.txt' in directory eclipse-workspace\\Search than try again!");
			System.exit(0);
		}
		long minTime = 0;
		long maxTime = 0;
		long averageTime = 0;
		
		String [] searchWords = text.split(" ");//Puts all words in Search.txt in an array
		for(int i = 0; i < searchWords.length; i++) {//Searching all words in array
			long beforeSearch = System.nanoTime();
			//hashMap.search(hashMap.hashSSF(searchWords[i]),searchWords[i]);
			hashMap.search(hashMap.hashPAF(searchWords[i]),searchWords[i]);
			long afterSearch = System.nanoTime();
			long timeSpent = (afterSearch-beforeSearch);//Time calculation
			if(minTime == 0 && maxTime == 0) {//Initialize minTime and maxTime
				minTime = timeSpent;
				maxTime = timeSpent;
			}
			else if(timeSpent < minTime)//Minimum time comparision
				minTime = timeSpent;
			else if(timeSpent > maxTime)//Maximum time comparision
				maxTime = timeSpent;
			averageTime += timeSpent;
		}
		averageTime = averageTime / searchWords.length;//Average time
		System.out.println("minTime = " + minTime +  " maxTime = " + maxTime + " AverageTime = " + averageTime);
	}
}
