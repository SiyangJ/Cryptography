package Cryptography;

public class AlphabeticalCiphering implements Cipher
{
	
	private String m_key;
	
	public AlphabeticalCiphering(String key)
	{
		m_key=key;
	}
	
	public String encipher(String str)
	{
		return replaceString(str,1);
	}
	
	public String decipher(String str)
	{
		return replaceString(str,-1);
	}
	
	private String replaceString(String str,int sign)
	{
		assert(sign==-1 || sign==1);
		assert(str!=null);
		String result="";
		int keyCount=0;
		char curChar;
		for (int i=0;i<str.length();i++)
		{
			curChar=str.charAt(i);
			if (Character.isLetter(curChar))
				curChar=letterChange(curChar,sign*letterToNumber(toKeyChar(keyCount++)));
			result+=curChar;
		}
		return result;
	}
	
	private int letterToNumber(char letter)
	{
		assert(Character.isLetter(letter));
		return Character.toLowerCase(letter)-'a';
	}
	
	private int positiveModulo26(int i)
	{
		i=i%26;
		assert(Math.abs(i)<26);
		return i<0?i+26:i;
	}
	
	private char letterChange(char letter,int d)
	{
		assert (Character.isLetter(letter));
		return (char)((Character.isLowerCase(letter)?'a':'A')+
				positiveModulo26(letterToNumber(letter)+d));
	}
	
	private char toKeyChar(int i)
	{
		assert(i>=0);
		assert(m_key!=null);
		return m_key.charAt(i%m_key.length());
	}
	
	public static void main(String[] args)
	{
		Cipher cipher = new AlphabeticalCiphering("YES");
		System.out.println(cipher.decipher("BS FMX KFSGR JAPWL"));
	}

}
