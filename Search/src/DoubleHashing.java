
public class DoubleHashing extends Hashing{

	
	DoubleHashing (int loadFactor){
		super(loadFactor);
	}
	
	//Both works until find an appropriate index
	
	public int hashSSF(String value) {
		long ssfValue = 0, hashTwo = 0;
		int index = 0, multiplier = 0, startIndex = 0;
		for(int i = 0; i < value.length(); i ++)//Hash value of word as SSF
			ssfValue += (int) value.charAt(i);
		index = (int) ((ssfValue + hashTwo)%table.length);
		startIndex = index;
		while(table[index] != null && !table[index].getValue().equals(value)) {//When collision
			multiplier++;
			collisions++;
			hashTwo = multiplier*(31-(startIndex%31));//Hash two
			index = (int) ((startIndex + hashTwo)%table.length);
		}
		return index;
	}
	
	public int hashPAF (String value) {
		long pafValue = 0, hashTwo = 0;
		int index = 0, multiplier = 0, startIndex = 0;
		for(int i =0; i < value.length(); i++) //Hash value of word as PAF
			pafValue += value.charAt(value.length()-1-i) * Math.pow(31, i);
		index = (int) ((pafValue + hashTwo)%table.length);
		startIndex = index;
		while(table[index] != null && !table[index].getValue().equals(value)) {//When collision
			multiplier++;
			collisions++;
			hashTwo = multiplier*(31-(startIndex%31));//Hash two
			index = (int) ((startIndex + hashTwo)%table.length);
		}
		return index;
	}
}


