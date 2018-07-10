package Cryptography;


public class ExponentialCipher implements BlockCipher
{
	protected int m_prime,m_k;
	protected int m_blockLength=4;
	protected int m_j;
	
	public static void main(String[] args)
	{
		ExponentialCipher cipher=new ExponentialCipher(2561,3);
		int[] blocks=cipher.encipherToBlocks("GOLD MEDAL");
		for (int i=0;i<blocks.length;i++)
			System.out.println(blocks[i]);
		System.out.println(cipher.decipherFromBlocks(blocks));
	}
	
	public ExponentialCipher(int prime,int k)
	{
		assert(prime>0&&k>0);
		m_prime=prime;m_k=k;
		assert(m_blockLength%2==0);
		m_j=integerInverse(m_k,m_prime-1);
	}

	@Override
	public String encipher(String str) 
	{
		return blocksToString(encipherToBlocks(str));
	}

	@Override
	public String decipher(String str) 
	{
		return decipherFromBlocks(stringToBlocks(str));
	}
	
	public int[] encipherToBlocks(String str) 
	{
		int[] blocks=stringToBlocks(str);
		for (int i=0;i<blocks.length;i++)
			blocks[i]=encipherOneBlock(blocks[i]);
		return blocks;
	}

	protected int encipherOneBlock(int num)
	{
		assert(num>=0&&num<=m_prime);
		return integerPower(num,m_k,m_prime);
	}
	
	protected int integerPower(int base,int exp,int mod)
	{
		if (exp==1) return base%mod;
		return (base*integerPower(base,exp-1,mod))%mod;
	}
	
	public String decipherFromBlocks(int[] blocks) 
	{
		assert(blocks!=null);
		for (int i=0;i<blocks.length;i++)
		{
			blocks[i]=decipherOneBlock(blocks[i]);
		}
		return blocksToString(blocks);
	}
	
	protected int decipherOneBlock(int num)
	{
		return integerPower(num,m_j,m_prime);
	}
	
	protected int integerInverse(int k,int p)
	{
		int result=reverseEuclidean(k,p)[0];
		result%=p;
		return result<0?result+p:result;
	}
	
	protected int[] reverseEuclidean(int a,int b)
	{
		int q=a/b;
		int r=a%b;
		if (r==1) return new int[]{1,-q};
		int[] last=reverseEuclidean(b,r);
		assert(last.length==2);
		r=last[0];
		last[0]=last[1];
		last[1]=r-q*last[1];
		return last;
	}
	
	protected int[] stringToBlocks(String str)
	{
		return numbersToBlocks(stringToNumbers(str));
	}
	
	protected String blocksToString(int[] blocks)
	{
		return numbersToString(digitsToNumbers(blocksToDigits(blocks)));
	}
	
	protected int[] stringToNumbers(String str)
	{
		assert(str!=null);
		int[] result=new int[2*str.length()];
		int curNum;
		for (int i=0;i<str.length();i++)
		{
			curNum=charToNumber(str.charAt(i));
			result[2*i]=curNum/10;
			result[2*i+1]=curNum%10;
		}
		return result;
	}
	
	protected int charToNumber(char c)
	{
		assert(Character.isLetter(c)||c==' ');
		return Character.isLetter(c)?letterToNumber(c):26;
	}
	
	protected char numberToChar(int i)
	{
		return i==26?' ':numberToLetter(i,true);
	}
	
	protected int[] numbersToBlocks(int[] numbers)
	{
		assert(numbers!=null);
		for (int i=0;i<numbers.length;i++)
			assert(numbers[i]>=0);
		int[] result=new int[numbers.length%m_blockLength==0?
				numbers.length/m_blockLength:numbers.length/m_blockLength+1];
		int i=-1;
		int is2=0;;
		while (++i<result.length)
		{
			result[i]=0;
			for (int j=0;j<m_blockLength;j++)
			{
				result[i]=result[i]*10+
						((i*m_blockLength+j)<numbers.length?
								numbers[i*m_blockLength+j]:
									(is2++%2==0?2:3));
			}
		}
		return result;
	}
	
	
	
	protected int[] blocksToDigits(int[] blocks)
	{
		assert(blocks!=null);
		assert(blocks.length*m_blockLength%2==0);
		int[] result=new int[blocks.length*m_blockLength];
		int curNum;
		for (int blockIndex=0;blockIndex<blocks.length;blockIndex++)
		{
			curNum=blocks[blockIndex];
			for (int i=m_blockLength-1;i>=0;i--)
			{
				result[blockIndex*m_blockLength+i]=curNum%10;
				curNum/=10;
			}
		}
		return result;
	}
	
	protected int[] digitsToNumbers(int[] digits)
	{
		assert(digits.length%2==0);
		for (int i=0;i<digits.length;i++)
			assert(digits[i]>=0&&digits[i]<10);
		int[] result=new int[digits.length/2];
		for (int i=0;i<result.length;i++)
		{
			result[i]=10*digits[2*i]+digits[2*i+1];
		}
		return result;
	}
	
	protected String numbersToString(int[] numbers)
	{
		String result="";
		for (int i=0;i<numbers.length;i++)
		{
			result+=numberToChar(numbers[i]);
		}
		return result;
	}

	
	protected char numberToLetter(int num,boolean isUpperCase)
	{
		assert(num>=0&&num<26);
		return (char)((isUpperCase?'A':'a')+num);
	}
	
	protected int letterToNumber(char letter)
	{
		assert(Character.isLetter(letter));
		return Character.toLowerCase(letter)-'a';
	}
	
	protected int positiveModulo26(int i)
	{
		i=i%26;
		assert(Math.abs(i)<26);
		return i<0?i+26:i;
	}
	
	protected char letterChange(char letter,int d)
	{
		assert (Character.isLetter(letter));
		return (char)((Character.isLowerCase(letter)?'a':'A')+
				positiveModulo26(letterToNumber(letter)+d));
	}

}
