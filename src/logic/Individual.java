//TODO: Efficiency pass

package logic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual> {
	protected String genotype;
	protected String target = "1111011010110000110100110101001101011110";
	protected List<Integer> phenotype;
	protected double fitness;
	protected int bitSize;
	Random rng;
	
	//for testing
	public Individual() {
		
	}
	
	public Individual(String genotype, int bitSize) {
		this.genotype = genotype;
		this.bitSize = bitSize;
		fitness = -1;
	}
	
	public Individual(int genSize, int bitSize) {
		this.bitSize = bitSize;
		rng = new Random();
		BigInteger out = new BigInteger(genSize, rng);
		genotype = pad(out.toString(2), genSize);
	}
	
	public static String pad(String in, int digits) {
		StringBuffer temp = new StringBuffer(in);
		int remainingInserts = digits - in.length();
		
		while (remainingInserts-- > 0) {
			temp.insert(0, "0");
		}
		
		return temp.toString();
	}
	
	//Efficiency?
	public void develop() {
		phenotype = convXBit(genotype, bitSize);
	}
	
	public List<Integer> convXBit(String binary, int bitSize) {
		if (binary.length() % bitSize != 0) {
			System.err.println("Genotype size not divisible by bitsize");
			System.out.println(binary.length());
			System.out.println(bitSize);
			return null;
		}
		int len = binary.length()/bitSize;		
		List<Integer> res = new ArrayList<Integer>(len);
		for (int i=0; i<len; i++) {
			res.add(Integer.parseInt(binary.substring(i*bitSize, i*bitSize+bitSize), 2));			
		}
		return res;
	}	
	
	public void calcFit(String type) {
		boolean modifyTarget = false;
		double res = 0;
		if (modifyTarget) {
			for (int i=0; i<genotype.length(); i++) {
				res += genotype.charAt(i) == target.charAt(i) ? 1 : 0;
			}
		} else {
			for (int i=0; i<genotype.length(); i++) {
				res += genotype.charAt(i) == '1' ? 1 : 0;
			}	
		}
		fitness = res/genotype.length();
	}
	
	
	public void lolzFit(int z) {
		int res = 0;
		char lead = genotype.charAt(0);
		
		for (int i=0; i<genotype.length(); i++) {
			if (genotype.charAt(i)==lead) {
				res++;
			}
			else {
				if (lead == '0' && res > z) {
					fitness = z;
				} else {
					fitness = res;					
				}
				return;
			}
		}
		if (lead == 0 && res > z) {
			fitness = z;
		} else {
			fitness = res;					
		}
		
	}
	
	public String getGenotype() {
		return genotype;
	}
	
	public void mutate() {
		rng = new Random();
		int target = rng.nextInt(genotype.length());
		
		//dumb bit flip
		StringBuilder temp = new StringBuilder(genotype);
		temp.setCharAt(target, genotype.charAt(target) == '0' ? '1' : '0');
		
		genotype = temp.toString();
	}
	
	double getFitness() {
		return fitness;
	}

	@Override
	public int compareTo(Individual ind) {
		if (fitness > ind.getFitness()) {
			return -1;
		} else if (fitness == ind.getFitness()) {
			return 0;
		} else {
			return 1;
		}
	}

	public List<Integer> getPhenotype() {
		return phenotype;
	}

	public static void main(String[] args) {
		/*
		Individual a = new Individual(20, 4);
		a.develop();
		System.out.println(a.genotype);
		System.out.println(a.phenotype);
		a.calcFit("none");
		System.out.println(a.fitness);
		a.lolzFit(4);
		System.out.println(a.fitness);
		*/
		Random rng = new Random();
		BigInteger out = new BigInteger(40, rng);
		String g = pad(out.toString(2), 40);
		System.out.println(g);
	}

	


}


