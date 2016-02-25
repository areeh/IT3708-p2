package logic;

import java.util.ArrayList;
import java.util.List;

public class SeqPopulation extends Population{
	private int bitSize;
	
	public SeqPopulation(Settings set) {
		super(set);
		
		bitSize = 4;
		
		children = new ArrayList<Individual>(set.getGenerationSize());
		for (int i=0; i<set.getGenerationSize(); i++) {
			children.add(new SeqIndividual(set.getGenotypeSize(), set.getSeqLength(), bitSize, set.getSymbolNr()));			
		}
		
		if (children.size() != set.getGenerationSize()) {
			System.err.println("Created wrong number of children");
		}
		
		
	}
	
	/*
	public void calcBitsPerSymbol(int symbolNr) {
		for (int i=0; i<7; i++) {
			if (Math.pow(2, i) >= symbolNr) {
				bitSize = i;
				set.setBitSize(bitSize);
				System.out.println("Set bitsize");
				System.out.println(bitSize);
				return;
			}
		}
		System.err.println("Didnt get bitsize");
		return;
	}
	
	public void calcGenotypeLength() {
		set.setGenotypeSize(set.getSeqLength()*bitSize);
		System.out.println("set genotypeSize");
		System.out.println(set.getGenotypeSize());
	}
	*/
	
	public void addCrossover(Pair<Individual, Individual> parents) {
		if (rng.nextDouble() > set.getCrossoverRate()) {
			children.add(parents.x);
			children.add(parents.y);
			return;
		} else {
			int split = rng.nextInt(set.getSeqLength());
			
			List<Integer> newPheno1 = new ArrayList<Integer>();
			newPheno1.addAll(parents.x.getPhenotype().subList(0, split));
			newPheno1.addAll(parents.y.getPhenotype().subList(split, set.getSeqLength()));
			List<Integer> newPheno2 = new ArrayList<Integer>();
			newPheno2.addAll(parents.y.getPhenotype().subList(0, split));
			newPheno2.addAll(parents.x.getPhenotype().subList(split, set.getSeqLength()));
			
			
			Individual child1 = new SeqIndividual(parents.x.getGenotype(), newPheno1, set.getBitSize(), set.getSymbolNr());
			Individual child2 = new SeqIndividual(parents.y.getGenotype(), newPheno2, set.getBitSize(), set.getSymbolNr());
			
			/*
			children.remove(parents.x);
			children.remove(parents.y);
			*/
			
			children.add(child1);
			children.add(child2);
			}
			
		}
	
	public static void main(String[] args) {
		Population pop = new SeqPopulation(new Settings());
		
		Individual a = new SeqIndividual(20, 5, 4, 2);
		a.mutate();
		Individual b = new SeqIndividual(20, 5, 4, 2);
		b.mutate();
		
		pop.children.add(a);
		pop.children.add(b);
		
		System.out.println("Printing children");
		for (Individual child : pop.children) {
			System.out.println(child.phenotype);
		}
		
		pop.addCrossover(new Pair<Individual, Individual>(pop.children.get(0), pop.children.get(1)));
		
		System.out.println("Printing children");
		for (Individual child : pop.children) {
			System.out.println(child.phenotype);
		}
		
		
	}

}
