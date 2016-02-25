package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SeqIndividual extends Individual{
	private int symbolNr;

	public SeqIndividual(int genSize, int seqLength, int bitSize, int symbolNr) {
		this.symbolNr = symbolNr;
		this.bitSize = bitSize;
		rng = new Random();
		genotype = "11";
		phenotype = new ArrayList<Integer>();
		for (int i=0; i<seqLength; i++) {
			phenotype.add(rng.nextInt(symbolNr));
		}
	}
	
	
	public SeqIndividual(String genotype, List<Integer> phenotype, int bitSize, int symbolNr) {
		this.bitSize = bitSize;
		this.symbolNr = symbolNr;
		this.phenotype = phenotype;
	}
	
	@Override
	public void mutate() {
		if (phenotype == null) {
			System.out.println("null pheno");
		}
		rng = new Random();
		
		int target = rng.nextInt(phenotype.size());
		Integer newInt =  rng.nextInt(symbolNr);
		
		phenotype.set(target, newInt);
		
		
		/*
		int chance = rng.nextInt(genotype.length()/bitSize);
		boolean success = false;
		
		StringBuilder temp = new StringBuilder(genotype.substring(chance*bitSize, chance*bitSize+bitSize));
		
		while (!success) {
			System.out.println("Ran mutate loop");
		int target = rng.nextInt(bitSize);
		
		
		//dumb bit flip
		temp.setCharAt(target, genotype.charAt(target) == '0' ? '1' : '0');
		if (Integer.valueOf(temp.toString(), 2) < symbolNr) {
			success = true;
		}
		}
		genotype = genotype.substring(0, chance*bitSize) + temp.toString() + genotype.substring(chance*bitSize+bitSize, genotype.length());
		*/
	}
	
	@Override
	public void develop() {
		
	}
	
	public static Map<String, Integer> createOccurenceMap(List<String> strs) {
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		for (String str : strs) {
			Integer oldCount = occurences.get(str);
			if ( oldCount == null) {
				oldCount = 0;
			}
			occurences.put(str, oldCount +1);
		}
		return occurences;
	}
	
	public static List<String> patternPairs(List<Integer> nums, int d) {
		List<String> res = new ArrayList<String>();
		for (int i=0; i<nums.size()-d-1; i++) {
			res.add(Integer.toString(nums.get(i)) + Integer.toString(nums.get(i+d+1)));
		}
		return res;
	}
	
	public static int createCount(Map<String, Integer> map) {
		int res = 0;
		for (String key : map.keySet()) {
			res += map.get(key) - 1;
		}
		return res;
	}
	
	public static int globalSeq(List<Integer> nums) {
		int res = 0;
		for (int i=0; i<nums.size(); i++) {
			res += createCount(createOccurenceMap(patternPairs(nums, i)));
		}
		return res;
	}
	
	public static int localSeq(List<Integer> nums) {
		return createCount(createOccurenceMap(patternPairs(nums, 0)));
	}
	
	public void calcFit(String type) {
		if (type == "global") {
			fitness = -globalSeq(phenotype);			
		} else if (type == "local") {
			fitness = -localSeq(phenotype);
		} else {
			System.err.println("no type used for surprising seq");
		}
	}
	
	public static void main(String[] args) {
		Individual a = new SeqIndividual(20, 5, 4, 2);
		System.out.println(a.phenotype);
		a.mutate();
		System.out.println(a.phenotype);
		
	}
}
