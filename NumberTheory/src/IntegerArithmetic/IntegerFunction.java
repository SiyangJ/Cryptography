package IntegerArithmetic;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class IntegerFunction extends IntegerArithmetic 
{	
	
	static public void main(String[] args)
	{
		System.out.println(tau(12));
		System.out.println(sigma(12));
		System.out.println(phi(12));
		System.out.println(phi(300));
		System.out.println(phi(12345));
		System.out.println(miu(2*3*5*7));
		System.out.println(miu(2*3*5));
		System.out.println(miu(20));
		System.out.println(miu(17));

	}
	
	static public int tau(int p)
	{
		return summationOverDivisor(d->1).applyAsInt(p);
	}
	
	static public int sigma(int p)
	{
		return summationOverDivisor(d->d).applyAsInt(p);
	}
	
	static public int phi(int p)
	{
		return multiplicationOverPrimeFactorization(
				(q,k)->{
					if (q<=1||k<=0) throw new InvalidParameterException();
					return (q-1)*power(q,k-1);}
				).applyAsInt(p);
	}
	
	static public int miu(int p)
	{
		return multiplicationOverPrimeFactorization(
				(q,k)->{
					if (q<=1||k<=0) throw new InvalidParameterException();
					return k>1?0:-1;}
				).applyAsInt(p);
	}
	
	static public IntUnaryOperator MobiusInversion(IntUnaryOperator F)
	{
		return n->
			summationOverDivisor(
					d->miu(n/d)*F.applyAsInt(d)
					).applyAsInt(n)
		;
	}

	static public IntUnaryOperator summationOverDivisor(IntUnaryOperator F)
	{
		return n->
		{
			if (n<=0) throw new InvalidParameterException();
			int result=0;
			for (int i=1;i<=n;i++) if (n%i==0) result+=F.applyAsInt(i);
			return result;
		};
	}
	
	static public IntUnaryOperator multiplicationOverPrimeFactorization(IntBinaryOperator F)
	{
		return n->
		{
			if (n<=0) throw new InvalidParameterException();
			if (n==1) return 1;
			int result=1;
			List<int[]> decomp=primeDecomposition(n);
			for (int i=0;i<decomp.size();i++) 
				result*=F.applyAsInt(decomp.get(i)[0],decomp.get(i)[1]);
			return result;
		};
	}
	
}
