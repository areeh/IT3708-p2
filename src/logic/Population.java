package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;

//TODO: Efficiency pass

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sun.font.CreatedFontTracker;

public class Population {
	
	List<Individual> children;
	List<Individual> adults;
	Settings set;
	Random rng;
	int bitSize;
	
	double avgFit;
	double stdDev;
	double bestFit;
	List<Integer> bestPheno;
	String bestGeno;
	int errorCount;
	private boolean success;
	
	private int badPairs;
	private int goodPairs;
	
	private double targetFit;

	public Population(Settings set) {
		this.set = set;
		success = false;
		rng = new Random();
		
		bitSize = set.getBitSize();
		
		children = new ArrayList<Individual>(set.getGenerationSize());
		for (int i=0; i<set.getGenerationSize(); i++) {
			children.add(new Individual(set.getGenotypeSize(), set.getBitSize()));			
		}
		
		if (children.size() != set.getGenerationSize()) {
			System.err.println("Created wrong number of children");
		}
		
		if (set.getFitnessFunc() == "onemax") {
			targetFit = 1;
		} else if (set.getFitnessFunc() == "lolz") {
			targetFit = set.getGenotypeSize();
		} else if (set.getFitnessFunc() == "surpriseqlocal" || set.getFitnessFunc() == "surpriseqglobal") {
				targetFit = 0;
		} else {
			System.err.println("No problem type chosen");
		}
	}
	
	public void createPhenotypes() {
		for (int i=0; i<children.size(); i++) {
			children.get(i).develop();
		}
	}
	
	public void selectAdults(){
		if (set.getAdultSelectionType() == "full") {
			fullGenRepl();			
		} else if (set.getAdultSelectionType() == "overprod") {
			overProdRepl();
		} else if (set.getAdultSelectionType() == "mix") {
			mixRepl();
		} else {
			System.err.println("No adult selection chosen");
		}
	}
	

	public void reproduce() {
		if (set.getMateSelectionType() == "fitprop" || set.getMateSelectionType() == "sigsca" || set.getMateSelectionType() == "ranksel") {
			reprodGlobalSelection();
		} else if (set.getMateSelectionType() == "toursel") {
			reprodTourSel();
		} else {
			System.err.println("No mate selection");
		}
	}
	

	private void reprodSigSca() {
		children = new ArrayList<Individual>(set.getGenerationSize());
		int repros = set.getGenerationSize()/2;
		List<Pair<Pair<Individual, Individual>, Double>> ranges = createRangesDoubles(adults);
		
		
		while (repros > 0) {
			Pair<Individual, Individual> parents = selectParentPair(ranges);
			if (parents.x.getFitness() + parents.y.getFitness() >= 1) {
				goodPairs++;				
			} else {
				badPairs++;
			}
			Pair<Individual, Individual> kidPair = crossover(parents);
			children.add(kidPair.x);
			children.add(kidPair.y);
			repros--;
		}
		mutateAll(children);
	}

	public void evalGeneration() {
		if (set.getFitnessFunc() == "onemax") {
			oneMax("onemax");
		} else if (set.getFitnessFunc() == "surpriseqlocal") {
			surpriSeqLocal();
		}else if (set.getFitnessFunc() == "surpriseqglobal") {
			surpriSeqGlobal();
		} else if (set.getFitnessFunc() == "lolz") {
			lolz();
		}
		else {
			System.err.println("No fitness func");			
		}
	}
	
	public void oneMax(String type) {
		for (Individual ind : children) {
			ind.calcFit(type);			
		}
	}
	
	public void surpriSeqLocal() {
		for (Individual ind : children) {
			ind.calcFit("local");	
		}
	}
	
	public void surpriSeqGlobal() {
		for (Individual ind : children) {
			ind.calcFit("global");	
		}
	}
	
	public void lolz() {
		for (Individual ind : children) {
			ind.lolzFit(set.getZLength());
		}
	}
	
	public void fullGenRepl() {
		adults = children;
		if (adults != null && set.isElitism()) {
			System.out.println("Ran elitism");
			adults.remove(rng.nextInt(adults.size()));
			Collections.sort(adults);
			Individual elite = adults.get(0);			
			adults.add(elite);
		}
		children = null;
		
		if (adults.size() != set.getGenerationSize()) {
			System.err.println("Wrong adults size");
		}
	}
	
	private Pair<Individual, Individual> crossover(Pair<Individual, Individual> parents) {
		if (rng.nextDouble() > set.getCrossoverRate()) {
			return parents;
		}
		
		int split = rng.nextInt(set.getGenotypeSize());
		
		Individual child1 = new Individual(
				parents.x.getGenotype().substring(0, split) + parents.y.getGenotype().substring(split, set.getGenotypeSize()),
				set.getBitSize());
		Individual child2 = new Individual(
				parents.y.getGenotype().substring(0, split) + parents.x.getGenotype().substring(split, set.getGenotypeSize()),
				set.getBitSize());

		
		return new Pair<Individual, Individual>(child1, child2);	
	}
	
	//Efficiency?
	private void mutateAll(List<Individual> inds) {
		for (Individual ind : inds) {
			double chance = rng.nextDouble();
			if (chance < set.getMutateRate()) {
				ind.mutate();
			}
			
		}
		
	}
	
	private List<Pair<Individual, Pair<Double, Double>>> createRangesSingles(List<Individual> participants) {
		double totFit = 0;
		List<Pair<Individual, Pair<Double, Double>>> res = new ArrayList<Pair<Individual, Pair<Double, Double>>>(participants.size());
		for (Individual ind : participants) {
			totFit += ind.getFitness(); 
		}
		
		double current = 0;
		
		for (int i=0; i<participants.size(); i++) {
			double next = participants.get(i).getFitness()/totFit;
			res.add(new Pair(participants.get(i), new Pair(current, next)));
			current = next;
		}
		
		return res;
	}
	
	private List<Pair<Pair<Individual, Individual>, BigDecimal>> createRangesDoublesBig(List<Individual> participants) {
		
		List<Pair<Pair<Individual, Individual>, BigDecimal>> res = new ArrayList<Pair<Pair<Individual, Individual>, BigDecimal>>(participants.size()/2);
		List<BigDecimal> tempVals = new ArrayList<BigDecimal>(participants.size()/2);
		
		Collections.sort(participants);
		BigDecimal min = BigDecimal.valueOf(participants.get(participants.size()-1).getFitness());
		BigDecimal max = BigDecimal.valueOf(participants.get(0).getFitness());
		
		for (int i=0; i<participants.size()/2; i++) {
			BigDecimal pairValue = BigDecimal.valueOf(participants.get(i*2).getFitness() + participants.get((i*2)+1).getFitness());
			BigDecimal scaledPairVal = SupportAlgs.scale(pairValue, min, max, BigDecimal.valueOf(0), BigDecimal.valueOf(1));
			tempVals.add(scaledPairVal);
		}
		
		BigDecimal totFit = new BigDecimal("0");
		
		for (int i=0; i<tempVals.size(); i++) {
			totFit = totFit.add(tempVals.get(i));
		}
		
		for (int i=0; i<tempVals.size(); i++) {
			tempVals.set(i, tempVals.get(i).divide(totFit, 10, RoundingMode.HALF_UP));
		}
		
		BigDecimal next = new BigDecimal("0");
		for (int i=0; i<tempVals.size(); i++) {
			next = next.add(tempVals.get(i));
			res.add(new Pair<Pair<Individual, Individual>, BigDecimal>(new Pair<Individual, Individual>(participants.get(i*2), participants.get(i*2+1)), next));
		}
		
		if (res.get(res.size()-1).y.compareTo(new BigDecimal("1.001")) >= 0 || res.get(res.size()-1).y.compareTo(new BigDecimal("0.99")) <= 0) {
			System.out.println("Got incorrect total: ");
			System.out.println(res.get(res.size()-1).y);
			errorCount++;
		}
		
		for (Pair p : res) {
			System.out.println(p.y);
		}
		
		return res;
	}
	
	private List<Pair<Pair<Individual, Individual>, Double>> createRangesDoubles(List<Individual> participants) {
		
		List<Pair<Pair<Individual, Individual>, Double>> res = new ArrayList<Pair<Pair<Individual, Individual>, Double>>(participants.size()/2);
		List<Double> tempVals = new ArrayList<Double>(participants.size()/2);
		
		Collections.sort(participants);
		double min = participants.get(participants.size()-1).getFitness();
		double max = participants.get(0).getFitness();
		
		for (int i=0; i<participants.size()/2; i++) {
			double pairValue = participants.get(i*2).getFitness() + participants.get((i*2)+1).getFitness();
			double scaledPairVal = SupportAlgs.scale(pairValue, min, max, 0.0d, 1.0d);
			tempVals.add(scaledPairVal);
		}
		
		double totFit = 0;
		
		for (int i=0; i<tempVals.size(); i++) {
			totFit+=tempVals.get(i);
		}
		
		for (Double d : tempVals) {
			System.out.println(d);
		}
		
		for (int i=0; i<tempVals.size(); i++) {
			tempVals.set(i, tempVals.get(i)/totFit);
		}
		
		double next = 0;
		for (int i=0; i<tempVals.size(); i++) {
			next += tempVals.get(i);
			res.add(new Pair<Pair<Individual, Individual>, Double>(new Pair<Individual, Individual>(participants.get(i*2), participants.get(i*2+1)), next));
		}
		
		if ( res.get(res.size()-1).y >= 1.001 || res.get(res.size()-1).y <= 0.99 ) {
			System.out.println("Got incorrect total: ");
			System.out.println(res.get(res.size()-1).y);
			errorCount++;
		}
		
		for (Pair p : res) {
			System.out.println(p.y);
		}
		
		return res;
	}
	
	private List<Pair<Pair<Individual, Individual>, Double>> createRangesSigma(List<Individual> participants) {
		calcStats();
		double totSigma = 0;
		List<Double> sigmaScaled = new ArrayList<Double>(participants.size());
		List<Pair<Pair<Individual, Individual>, Double>> res = new ArrayList<Pair<Pair<Individual, Individual>, Double>>(participants.size());
		for (Individual ind : participants) {
			double temp = 1.0d + (ind.getFitness()-avgFit)/stdDev;
			totSigma+=temp;
			sigmaScaled.add(temp);
		}
		
		double next = 0;
		
		for (int i=0; i<participants.size()/2; i++) {
			if (i==(participants.size()/2)-1)  {
				next = 1;
			} else {
				next += (sigmaScaled.get(i*2)+sigmaScaled.get(i*2+1))/totSigma;				
			}
			if (next > 1.001) {
				errorCount++;
			}
			res.add(new Pair(new Pair(participants.get(i*2), participants.get(i*2+1)), next));
			}
		return res;
	}
	
	private List<Pair<Pair<Individual, Individual>, Double>> createRangesRankSel(List<Individual> participants) {
		calcStats();
		double totRank = 0;
		List<Double> rankScaled = new ArrayList<Double>(participants.size());
		List<Pair<Pair<Individual, Individual>, Double>> res = new ArrayList<Pair<Pair<Individual, Individual>, Double>>(participants.size());
		Collections.sort(participants);
		int rank = participants.size();
		for (Individual ind : participants) {
			double temp = set.getMinRank() + (set.getMaxRank()-set.getMinRank())*(rank-1)/(participants.size()-1);
			rank--;
			totRank+=temp;
			rankScaled.add(temp);
		}
		
		double next = 0;
		
		for (int i=0; i<participants.size()/2; i++) {
			if (i==(participants.size()/2)-1)  {
				next = 1;
			} else {
				next += (rankScaled.get(i*2)+rankScaled.get(i*2+1))/totRank;				
			}
			if (next > 1.001) {
				errorCount++;
			}
			res.add(new Pair(new Pair(participants.get(i*2), participants.get(i*2+1)), next));
			}
		return res;
	}
	
	public Pair<Individual, Individual> selectParentPair(List<Pair<Pair<Individual, Individual>, Double>> ranges) {
		double chance = rng.nextDouble();
		
		for (int i=0; i<ranges.size(); i++) {
			if (chance < ranges.get(i).y) {
				return ranges.get(i).x;
			}
		}
		
		System.err.println("No range found");
		return null;
		
	}
	
	public void reprodGlobalSelection() {
		children = new ArrayList<Individual>(set.getGenerationSize());
		int repros = set.getChildrenSize()/2;
		List<Pair<Pair<Individual, Individual>, Double>> ranges;
		
		
		if (set.getMateSelectionType() == "fitprop") {
			ranges = createRangesDoubles(adults);			
		} else if (set.getMateSelectionType() == "sigsca") {
			ranges = createRangesSigma(adults);
		} else if (set.getMateSelectionType() == "ranksel") {
			ranges = createRangesRankSel(adults);
		} else {
			System.err.println("Mate selection type not set");
			ranges = null;
		}
		
		
		while (repros > 0) {
			Pair<Individual, Individual> parents = selectParentPair(ranges);
			if (parents.x.getFitness() + parents.y.getFitness() >= 1) {
				goodPairs++;				
			} else {
				badPairs++;
			}
			addCrossover(parents);
			repros--;
		}
		mutateAll(children);
	}
	
	public void addCrossover(Pair<Individual, Individual> parents) {
		Pair<Individual, Individual> kidPair = crossover(parents);
		children.add(kidPair.x);
		children.add(kidPair.y);
	}
	
	private void mixRepl() {
		if (adults != null) {
			children.addAll(adults);			
		}
		Collections.sort(children);
		adults = children.subList(0, set.getGenerationSize());
		
	}
	
	private void overProdRepl() {
		Collections.sort(children);
		if (adults != null) {
			Collections.sort(adults);
			Individual elite = adults.get(0);
			adults.add(elite);			
		}
		adults = children.subList(0, set.getGenerationSize()-1);
		
	}
	
	
	//Efficiency?
	public void calcStats() {
		double mean = 0;
		double variance = 0;
		double size = adults.size();
		
		for (int i=0; i<size; i++) {
			mean += adults.get(i).getFitness();
			/*
			if (adults.get(i).getFitness() == -1) {
				System.err.println("Fitness not set");
			}
			*/
		}
		avgFit = mean/size;
		
		for (int i=0; i<size; i++) {
			variance += Math.pow((adults.get(i).getFitness() - avgFit), 2);
		}
		stdDev = Math.sqrt(variance/size);
		
		Collections.sort(adults);
		bestPheno = adults.get(0).getPhenotype();
		bestGeno = adults.get(0).getGenotype();
		bestFit = adults.get(0).getFitness();
		if (bestFit >= targetFit) {
			success = true;
		}
	}
	
	private void reprodTourSel() {
		children = new ArrayList<Individual>(set.getGenerationSize());
		int repros = set.getChildrenSize()/2;
		List<List<Individual>> groups = createTournamentGroups();
		
		while (repros > 0) {
			Pair<Individual, Individual> parents = selectWinnerPair(selectGroup(groups));
			if (parents.x.getFitness() + parents.y.getFitness() >= 1) {
				goodPairs++;				
			} else {
				badPairs++;
			}
			Pair<Individual, Individual> kidPair = crossover(parents);
			children.add(kidPair.x);
			children.add(kidPair.y);
			repros--;
		}
		mutateAll(children);	
	}
	
	private List<List<Individual>> createTournamentGroups() {
		int N = set.getGenerationSize()/set.getK(); 
		List<List<Individual>> res = new ArrayList<List<Individual>>(N);
		
		for (int i=0; i<N; i++) {
			res.add(adults.subList(i*set.getK(), i*set.getK()+set.getK()));			
		}
		
		return res;
	}
	
	private List<Individual> selectGroup(List<List<Individual>> groups) {
		int chance = rng.nextInt(groups.size());
		
		return groups.get(chance);
	}
	
	private Pair<Individual, Individual> selectWinnerPair(List<Individual> group) {
		return new Pair<Individual, Individual>(selectWinner(group), selectWinner(group));
	}
	
	private Individual selectWinner(List<Individual> group) {
		Individual res;
		double chance = rng.nextDouble();
		
		if (chance > set.getEChance()) {
			Collections.sort(group);
			res = group.get(0);
		} else {
			int rand = rng.nextInt(group.size());
			res = group.get(rand);
		}
		
		return res;
	}
	
	public double getAvgFit() {
		return avgFit;
	}
	
	public double getStdDev() {
		return stdDev;
	}
	

	public double getBestFit() {
		return bestFit;
	}

	public List<Integer> getBestPheno() {
		return bestPheno;
	}

	public String getBestGeno() {
		return bestGeno;
	}

	public boolean getSuccess() {
		return success;
	}

	public int getGoodPairs() {
		return goodPairs;
	}
	
	public int getBadPairs() {
		return badPairs;
	}

	public static void main(String[] args) {
		Population pop = new Population(new Settings());
		System.out.println(pop.children.get(0).getFitness());
		pop.evalGeneration();
		System.out.println(pop.children.get(0).getFitness());
		Collections.sort(pop.children);
		System.out.println(pop.children.get(0).getFitness());
		
		
		pop.createRangesDoubles(pop.children);
	}
}
