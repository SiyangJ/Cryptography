package Cryptography;

public class RSACipher extends ExponentialCipher 
{
	public RSACipher(int p,int q, int k) 
	{
		super(p*q, k);
		super.m_j=integerInverse(m_k,(p-1)*(q-1));
	}
	
	static public void main(String[] args)
	{
		ExponentialCipher cipher=new RSACipher(43,61,869);
		int[] blocks=new int[] {1030,1511,744,1237,1719};
		for (int i=0;i<blocks.length;i++)
			System.out.println(blocks[i]);
		String text=cipher.decipherFromBlocks(blocks);
		System.out.println(text);
		blocks=cipher.encipherToBlocks(text);
		for (int i=0;i<blocks.length;i++)
			System.out.println(blocks[i]);
	}

}
