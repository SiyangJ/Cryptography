package Cryptography;

public interface BlockCipher extends Cipher 
{
	public int[] encipherToBlocks(String str);
	public String decipherFromBlocks(int[] blocks);

}
