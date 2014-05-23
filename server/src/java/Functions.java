import java.util.Random;

/*
 * Defines all supported functions which can be accessible from JS nashorn 
 */
public class Functions {
	Random randomGenarator;
	Random selectGenarator;
	
	public Functions(long seed) {
		randomGenarator = new Random();
		selectGenarator = new Random(seed);
	}
	
	public int randomInt(int range) {
		return randomGenarator.nextInt(range);
	}
	
	public int selectInt(int range) {
		return selectGenarator.nextInt(range);
	}

	public double randomDouble(int range) {
		return randomGenarator.nextInt(range) + randomGenarator.nextDouble();
	}
	
	public double selectDouble(int range) {
		return selectGenarator.nextInt(range) + selectGenarator.nextDouble();
	}
	
	public boolean randomBoolean() {
		return randomGenarator.nextBoolean();
	}
	
	public boolean selectBoolean() {
		return selectGenarator.nextBoolean();
	}
	
	public String randomDigitString(int length) {
		String r = "" + (char) (randomInt(9) + 1 + '0');
		for (int i = 1; i < length; i++)
			r += (char) (randomInt(10) + '0');
		return r;
	}
	
	public String randomDigitString(int length, int digit, int times) {
		String r = "";
		String x = "";
		String y = "";
		for (int i = 0; i < times; i++)
			x += (char) (digit + '0');
		for (int i = times; i < length; i++)
			y += (char) (randomInt(10) + '0');
		int indexX = 0;
		int indexY = 0;
		while (indexX < x.length() && indexY < y.length()) {
			r += randomBoolean() ? x.charAt(indexX++) : y.charAt(indexY++);
		}
		while (indexX < x.length())
			r += x.charAt(indexX++);
		while (indexY < y.length())
			r += y.charAt(indexY++);
		while (r.charAt(0) == '0') {
			if (digit == 0)
				r = r.substring(1) + '0';
			else
				r = r.substring(1) + (char) (randomInt(10) + '0');
		}
		return r;
	}
	
	public String selectDigitString(int length) {
		String r = "" + (char) (selectInt(9) + 1 + '0');
		for (int i = 0; i < length; i++)
			r += (char) (selectInt(10) + '0');
		return r;
	}
	
	public String randomCharString(int length) {
		String r = "";
		for (int i = 0; i < length; i++) {
			int v = randomBoolean() ? (int) 'a' : (int) 'A';
			v += randomInt(26);
			r += (char) v;
		}
		return r;
	}
	
	public Object randomItem(Object... listItems) {
		int index = randomInt(listItems.length);
		return listItems[index];
	}
	
	public String randomItemToString(Object... listItems) {
		return randomItem(listItems).toString();
	}
	
	public Object selectItem(Object... listItems) {
		int index = selectInt(listItems.length);
		return listItems[index];
	}
	
	public String selectItemToString(Object... listItems) {
		return selectItem(listItems).toString();
	}
}