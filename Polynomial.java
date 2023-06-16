import java.util.List;
import javax.swing.JOptionPane;

public class Polynomial extends Object {
	private double[] coef;
	private double[] remainder;
	
	public Polynomial() {
		coef = new double[1];
		remainder = new double[0];
	}
	
	public Polynomial(String[] inArray) {
		int i, m = inArray.length;
		int n;
		if (m == 0) {
			coef = new double[1];
			coef[0] = 0;
			return;
		}
		
		for (i = 0; i < m; i++) 
			if (Double.valueOf(inArray[i]).doubleValue() != 0)
				break;
		
		n = Math.max(m - i, 1);
		coef = new double[n];
		for (i = 0; i < n; i++)
			coef[i] = Double.valueOf(inArray[m - 1 - i]).doubleValue();
		
		remainder = new double[0];
	}
	
	public Polynomial(double[] inArray) {
		coef = inArray.clone();
		remainder = new double[0];
	}
	
	public String toString() {
		String str = "";
		
		str = arrayString(coef.clone(), "\n");
		
		if (remainder.length > 0) {
			str += "\nR ";
			str += arrayString(remainder, " ");
		}
		return str;
	}
	
	private String arrayString(double[] array, String lineEnds) { // converts an array to a string with lineEnds at the end of each line
		String c;
		String str = "";
		int length = array.length;
		
		for (int i = length - 1; i >= 2; i--) {
			if (array[i] != 0) {
				str += lineFormat(array[i], "x^" + i + " +" + lineEnds);
			}
		}
		
		if (length >= 2) {
			if (array[1] != 0) {
				str += lineFormat(array[1], "x +" + lineEnds);
			}
		}
		str += lineFormat(array[0], "");
		return str;
	}
	
	private String lineFormat(double c, String base) {
		String str = "";
		if (c % 1 == 0) {
			str = String.valueOf((int) c);
		}
		if (c != 1 || base == "") {
			if (str.equals("") == true) {
				str += String.valueOf(c) + base;
			}
			else {
				str += base;
			}
		}
		else {
			str = base;
		}
		return str;
	}
	
	public double valueAtX(double x) {
		double value = 0;
		for (int i = 0; i < coef.length; i++) {
			value += coef[i] * Math.pow(x, i);
		}
		
		return value;
	}
	
	public Polynomial clone() {
		String[] arr = new String[coef.length];
		for (int i = coef.length - 1; i >= 0; i--) {
			int coefIndex = coef.length - (i + 1);
			arr[i] = String.valueOf(coef[coefIndex]);
		}
		
		Polynomial poly = new Polynomial(arr);
		return poly;
	}
	
	public int length() {
		return coef.length;
	}
	
	public double coefAt(int x) {
		return coef[x];
	}
	
	public double[] getCoef() {
		return coef;
	}
	
	private double[] reverse(double[] array) {
		double[] tempCoef = new double[array.length];
		int coefIndex = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			tempCoef[coefIndex] = array[i];
			coefIndex++;
		}
		array = tempCoef.clone();
		return array;
	}
	
	
	public void sum(Polynomial poly1, Polynomial poly2) { // poly1 is the smaller polynomial
		int finalLength = poly2.length();
		coef = new double[finalLength];
		
		for (int i = 0; i < finalLength; i++) {
			double resultCoef;
			try {
				double coef1 = poly1.coefAt(i);
				double coef2 = poly2.coefAt(i);
			
				resultCoef = coef1 + coef2;
			} catch(IndexOutOfBoundsException e) {
				resultCoef = poly2.coefAt(i);
			}
			coef[i] = resultCoef;
		}
	}
	
	
	public void diff(Polynomial poly1, Polynomial poly2) { // routes to a separate method that is also used in division
		coef = findDiff(poly1.getCoef().clone(), poly2.getCoef().clone());
	}
	
	private double[] findDiff(double[] poly1, double[] poly2) {
		Polynomial largePoly; // the larger of the two polynomials
		double[] tempCoef;
		int largePolyVal;
		int finalLength;
		if (poly1.length > poly2.length) {
			largePoly = new Polynomial(poly1);
			largePolyVal = 1;
			finalLength = poly1.length;
		}
		else {
			largePoly = new Polynomial(poly2);
			largePolyVal = 2;
			finalLength = poly2.length;
		}
		
		tempCoef = new double[finalLength];
		
		for (int i = 0; i < finalLength; i++) {
			double resultCoef;
			try {
				double coef1 = poly1[i];
				double coef2 = poly2[i];
			
				resultCoef = coef1 - coef2;
			} catch(IndexOutOfBoundsException e) {
				if (largePolyVal == 1) {
					resultCoef = largePoly.coefAt(i);
				}
				else {
					resultCoef = -1 * largePoly.coefAt(i);
				}
			}
			tempCoef[i] = resultCoef;
		}
		return tempCoef;
	}
	
	
	public void prod(Polynomial poly1, Polynomial poly2) { // poly1 is the smaller polynomial
		int finalLength = poly1.length() + poly2.length() - 1;
		int smallPolyLength = poly1.length();
		int largePolyLength = poly2.length();
		int largeIndex = 0;
		int numOfZeros;
		double[][] result = new double[smallPolyLength][finalLength];
		coef = new double[finalLength];
		
		for (int i = 0; i < smallPolyLength; i++) { // thanks Guillermo
			for (numOfZeros = 0; numOfZeros < i; numOfZeros++) {
				result[i][numOfZeros] = 0;
			}
					
			for (int j = numOfZeros; j < finalLength; j++) {
				if (j >= largePolyLength + numOfZeros) {
					result[i][j] = 0;
				}
				else {
					result[i][j] = poly1.coefAt(i) * poly2.coefAt(largeIndex++);
				}
			}
			largeIndex = 0;
		}
		
		for (int i = 0; i < finalLength; i++) {
			int finalCoef = 0;
			for (int j = 0; j < smallPolyLength; j++) {
				finalCoef += result[j][i];
			}
			coef[i] = finalCoef;
		}
	}
	
	
	public void quo(Polynomial poly1, Polynomial poly2) {
		double[][] quoRem = findQuo(poly1.getCoef().clone(), poly2.getCoef().clone());
		coef = reverse(quoRem[0]);
		remainder = reverse(quoRem[1]);
	}
	
	private double[][] findQuo(double[] poly1, double[] poly2) {
		int dividendLength = poly1.length;
		int divisorLength = poly2.length;
		double[] quo;
		double[] rem;
		
		if (dividendLength >= divisorLength) {
			quo = new double[(dividendLength - divisorLength) + 1];
			double[] tempDividend = poly1;
			double[] originalDivisor = poly2;
			double[] tempDivisor = new double[0];
			int i = 0;
			while (quo.length > i) {
				quo[i] = tempDividend[0] / originalDivisor[0];
				tempDivisor = new double[divisorLength];
				
				for (int divisorIndex = 0; divisorIndex < divisorLength; divisorIndex++) { // makes a temporary divisor to subtract from the temporary dividend
					tempDivisor[divisorIndex] = originalDivisor[divisorIndex] * quo[i];
				}
				
				tempDividend = findDiff(tempDividend, tempDivisor);
				if (tempDividend[0] == 0) {
					double[] tempArray = new double[tempDividend.length - 1];
					for (int divIndex = 0; divIndex < tempDividend.length - 1; divIndex++) {
						tempArray[divIndex] = tempDividend[divIndex + 1];
					}
					tempDividend = tempArray.clone();
				}
				
				i++;
			}
			rem = tempDividend.clone();
		}
		else {
			quo = new double[1];
			quo[0] = 0;
			rem = poly1;
		}
		
		double[][] quoRem; // holds the quotient and the remainder
		if (quo.length > rem.length) {
			quoRem = new double[2][quo.length];
		}
		else { 
			quoRem = new double[2][rem.length];
		}
		quoRem[0] = quo;
		quoRem[1] = rem;
		return quoRem;
	}
	
	public double[] getRoots() {
		final int LOWER_BOUND = -100000;
		final int UPPER_BOUND = 100000;
		double[] roots = new double[coef.length - 1];
		int rootNum = 0; // tracks number of roots found
		int i = LOWER_BOUND;
		double prevVal = 0;
		
		while (i <= UPPER_BOUND && rootNum < coef.length - 1) { // checks 100,000 units away from the origin in both directions for roots
			if (prevVal != 0 && valueAtX(i) / prevVal < 0) {
				roots[rootNum++] = rootSearch(i);
				prevVal = valueAtX(i);
			}
			else if (valueAtX(i) == 0) {
				prevVal = 0;
				roots[rootNum++] = i;
				i++;
			}
			else {
				prevVal = valueAtX(i);
				i++;
			}
		}
		
		for (int iRoots = rootNum; iRoots < roots.length; iRoots++) {
			roots[iRoots] = Double.NaN;
		}
		
		return roots;
	}
	
	private double rootSearch(int i) {
		final int MAX_SEARCHES = 20;
		double lowerBound = i - 1;
		double upperBound = i;
		boolean rootFound = false;
		int searchNum = 0; // tracks the number of searches performed 
		double root = 0;
		
		while (rootFound == false) {
			double halfPoint = upperBound - .5 * (upperBound - lowerBound);
			double halfPointVal = valueAtX(halfPoint);
			
			if (halfPointVal == 0) {
				rootFound = true;
				return halfPointVal;
			}
			else {
				if (searchNum > MAX_SEARCHES) {
					rootFound = true;
					root = halfPoint;
				}
				else {
					if (halfPointVal < 0) {
						if (valueAtX(lowerBound) < valueAtX(upperBound)) {
							lowerBound = halfPoint;
						}
						else {
							upperBound = halfPoint;
						}
					}
					else {
						if (valueAtX(lowerBound) < valueAtX(upperBound)) {
							upperBound = halfPoint;
						}
						else {
							lowerBound = halfPoint;
						}
					}
					searchNum++;
				}
			}
		}
		
		return root;
	}
}
