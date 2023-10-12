
public class LineerProbing extends Hashing{
	
	LineerProbing(int loadFactor){
		super(loadFactor);
	}
	
	//Both works until find an appropriate index
	
	public int hashSSF(String value) {
		int index = 0, multiplier = 0, startIndex = 0;
		for(int i = 0; i < value.length(); i ++)//Hash value of word as SSF
			index += (int) value.charAt(i);
		index = (index % table.length) + multiplier;
		startIndex = index;
		while(table[index] != null && !table[index].getValue().equals(value)) {//When collision
			multiplier++;
			collisions++;
			index = startIndex + multiplier;
			if(index >= table.length)
				index = index - table.length;
		}
		return index;
	}
	public int hashPAF (String value) {
		long pafValue = 0;
		int index = 0, multiplier = 0, startIndex = 0;
		for(int i =0; i < value.length(); i++) //Hash value of word as PAF
			pafValue += value.charAt(value.length()-1-i) * Math.pow(31, i);
		index = (int) (pafValue % table.length) + multiplier;
		startIndex = index;
		while(table[index] != null && !table[index].getValue().equals(value)) {//When collision
			multiplier++;
			collisions++;
			index = startIndex + multiplier;
			if(index >= table.length)
				index = index - table.length;
		}
		return index;
	}
}
