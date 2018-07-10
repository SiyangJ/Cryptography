package IntegerArithmetic;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Vector;
import java.util.function.BinaryOperator;

public class IntegerArithmetic
{
	
	static public void main(String[] args)
	{
		System.out.println(isPrime(191));
		System.out.println(isPrime(60000));
		System.out.println(isPrime(199));
		System.out.println(isPrime(9377747));
		System.out.println(isPrime(982451653));
		System.out.println(isPrime(2147483647));
	}
	
	static protected List<Integer> primeList=new Vector<Integer>();
	static protected enum isInList 
	{
		isPrime,isNotPrime,biggerThanList;
	}
	
	static 
	{
		if (primeList.isEmpty()){primeList.add(2);primeList.add(3);primeList.add(5);primeList.add(7);}
	}
	
	static public int times(int a,int b,int modulo)
	{return integerArithmetic(a,b,modulo,(x,y)->x*y);}
	
	static public int plus(int a,int b,int modulo)
	{return integerArithmetic(a,b,modulo,(x,y)->x+y);}
	
	static public int power(int a,int k)
	{
		if (k<0) throw new InvalidParameterException();
		int result=1;
		while (k-->0) result*=a;
		return result;
	}
	
	static public int power(int a,int k,int n)
	{
		// Need to improve;
		if (k<0) throw new InvalidParameterException();
		int result=1;
		while (k-->0) result=(result*a)%n;
		return result;
	}
	
	static public int gcd(int a,int b)
	{return gcdHelper(a,b);}
	
	static protected int gcdHelper(int a,int b)
	{return a%b==0?b:gcdHelper(b,a%b);}
	
	static public List<int[]> primeDecomposition(int p)
	{
		if (p<=1) throw new InvalidParameterException();
		List<int[]> result = new Vector<int[]>();
		int i=0;
		int curPower;
		int cur;
		while(true)
		{
			if (p==1||p==0) return result;
			if (i==primeList.size()) addNextPrime();
			cur=primeList.get(i);
			if (cur*cur>p) {result.add(new int[]{p,1});return result;}
			curPower=highestPower(p,cur);
			for (int j=0;j<curPower;j++) p/=cur;
			if (curPower!=0) result.add(new int[]{cur,curPower});
			i++;
		}
	}
	
	static public void printPrimeDecomposition(int n)
	{
		if (n==1||n==0)System.out.println(n);
		System.out.println("n = "+(n<0?"-":"")+decompositionString(n));
	}
	
	static protected String decompositionString(int n)
	{
		if (n<=1) throw new InvalidParameterException();
		List<int[]> decomp=primeDecomposition(n);
		String result="";
		for (int i=0;i<decomp.size();i++)
		{
			result+=(i==0?"":"*")+decomp.get(i)[0]+"^"+decomp.get(i)[1];
		}
		return result;
	}
	
	static protected int highestPower(int n,int k)
	{
		int result=0;
		while (n%k==0)
		{
			result++;n/=k;
		}
		return result;
	}
	
	static public int integerInverse(int k,int p)
	{
		if (gcd(k,p)!=1) throw new InvalidParameterException();
		int[] result=reverseEuclidean(k,p);
		assert(result.length==3);
		if (result[2]!=1) return 0;
		result[0]%=p;
		return result[0]<0?result[0]+p:result[0];
	}
	
	static protected int[] reverseEuclidean(int a,int b)
	{
		int q=a/b;
		int r=a%b;
		if (r==0) return new int[]{1,-q,b};
		if (r==1) return new int[]{1,-q,1};
		int[] last=reverseEuclidean(b,r);
		assert(last.length==3);
		r=last[0];
		last[0]=last[1];
		last[1]=r-q*last[1];
		return last;
	}
	
	static public boolean isPrime(int p)
	{
		if (p<=1) throw new InvalidParameterException();
		isInList inList=isInCurrentPrimeList(p);
		if (inList==isInList.isPrime) return true;
		else if (inList==isInList.isNotPrime) return false;
		else if (inList==isInList.biggerThanList)
		{
			findPrimeTill((int)(Math.sqrt(p)));
			return relativelyPrimeThroughList(p);
		}
		else throw new NullPointerException();
	}
	
	static protected void findPrimeTill(int n)
	{
		for (int i=primeList.get(primeList.size()-1);i<=n;i++)
		{
			if (relativelyPrimeThroughList(i)) primeList.add(i);
		}
	}
	
	static protected void addNextPrime()
	{
		for (int i=primeList.get(primeList.size()-1)+1;;i++)
		{
			if (relativelyPrimeThroughList(i)) 
			{
				primeList.add(i);return;
			}
		}
	}
	
	static protected boolean relativelyPrimeThroughList(int p)
	{
		if (p<=1) throw new InvalidParameterException();
		int cur;
		for (int i=0;i<primeList.size();i++)
		{
			cur=primeList.get(i);
			if (p%cur==0) return false;
			if (cur*cur>p) return true;
		}
		return true;
	}
	
	static protected isInList isInCurrentPrimeList(int p)
	{
		assert(!primeList.isEmpty());
		assert(p>=1);
		if (p>primeList.get(primeList.size()-1)) return isInList.biggerThanList;
		return isInListHelper(p,0,primeList.size()-1);
	}
	
	static protected isInList isInListHelper(int p,int left,int right)
	{
		if (left==right) return p==primeList.get(left)?isInList.isPrime:isInList.isNotPrime;
		if (left==right-1) return p==primeList.get(left)||p==primeList.get(right)?isInList.isPrime:isInList.isNotPrime;
		int center=(left+right)/2;
		return p==primeList.get(center)?isInList.isPrime:
			(p<primeList.get(center)?isInListHelper(p,left,center):isInListHelper(p,center,right));
	}
	
	static protected int integerArithmetic(int a,int b,int modulo,BinaryOperator<Integer> op)
	{
		if (modulo<=0) throw new InvalidParameterException();
		return op.apply(a,b)%modulo;
	}
	
	

}
