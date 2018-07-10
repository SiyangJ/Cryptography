package IntegerArithmetic;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Vector;

public class AdvancedInteger extends IntegerFunction 
{
	static public void main(String[] args)
	{
//		System.out.println(order(2,17));
//		System.out.println(order(3,17));
//		System.out.println(order(5,17));
//		System.out.println(order(2,19));
//		System.out.println(order(3,19));
		System.out.println(LegendreSymbol(200,419));

	}
	
	public static int LegendreSymbol(int a,int p)
	{
		if (p<3||!isPrime(p)) throw new InvalidParameterException("p is not odd prime");
		if (a%p==0) throw new InvalidParameterException("a is divisible by p");
		if (a<0) return p%4==1?LegendreSymbol(-a,p):-LegendreSymbol(-a,p);
		if (a>p) return LegendreSymbol(a%p,p);
		if (a==1) return 1;
		if (a==2) return p%8==1||p%8==7?1:-1;
		if (isPrime(a)) return (a%4==1||p%4==1?1:-1)*LegendreSymbol(p,a);
		List<int[]> primeDec=primeDecomposition(a);
		int result=1;
		for (int[] cur:primeDec)
		{
			if (cur[1]%2==1) result*=LegendreSymbol(cur[0],p);
		}
		return result;
	}
	
	public static List<Integer> getDivisors(int n)
	{
		n=Math.abs(n);
		if (n==0) throw new InvalidParameterException();
		List<Integer> result=new Vector<Integer>();
		for (int i=1;i<=n;i++) if (n%i==0) result.add(i);
		return result;
		
	}
	
	public static void printPrimitiveRoots(int n)
	{
		if (n<=1) throw new InvalidParameterException();
		List<Integer> roots=getPrimitiveRoots(n);
		if (roots.isEmpty()) {System.out.println("No primitive roots found.");return;}
		for (int i=0;i<roots.size();i++) System.out.println(roots.get(i));
	}
	
	public static List<Integer> getPrimitiveRoots(int n)
	{
		if (n<=1) throw new InvalidParameterException();
		List<Integer> result=new Vector<Integer>(),candidates=getRelativelyPrimes(n);
		int phi=phi(n);
		for (int i=0;i<candidates.size();i++)
			if (order(candidates.get(i),n)==phi) result.add(candidates.get(i));
		return result;
	}
	
	public static List<Integer> getRelativelyPrimes(int n)
	{
		if (n<=1) throw new InvalidParameterException();
		List<Integer> result=new Vector<Integer>();
		for (int i=2;i<n;i++)
			if (gcd(i,n)==1) result.add(i);
		return result;
	}
	
	public static int order(int a,int n)
	{
		if (a==0||n<=1||gcd(a,n)!=1) throw new InvalidParameterException();
		List<Integer> divisors=getDivisors(phi(n));
		for (int i=0;i<divisors.size();i++) 
			if (power(a,divisors.get(i),n)==1)
				return divisors.get(i);
		throw new InvalidParameterException();
	}

}
