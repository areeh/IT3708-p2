package logic;

import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jfree.ui.RefineryUtilities;

import display.OptionsPanel;
import display.Plot;

public class Simulation {
	Population population;
	Settings set;
	int gens;
	boolean success;
	
	List<Double> bestFits;
	List<Double> stdDevs;
	List<Double> averages;
	List<Integer> bestPheno;
	int generationNr;
	private boolean stop;
	
	JFrame f;
	
	List<JPanel> currentPlots;

	public Simulation(Settings set) {
		this.set = set;
		population = new Population(set);
		
		this.gens = set.getNrOfGenerations();
		generationNr = 0;
		
		bestFits = new ArrayList<Double>();
		stdDevs = new ArrayList<Double>();
		averages = new ArrayList<Double>();
		
		stop = true;
		
		if (set.getAdultSelectionType() == "full") {
			set.setChildrenSize(set.getGenerationSize());
		}
	}
	
	public void EAStep() {
		generationNr += 1;
		
		population.createPhenotypes();
		//System.out.println("Created phenos");
		population.evalGeneration();
		//System.out.println("Got fitness");
		population.selectAdults();
		//System.out.println("Selected adults");
		population.reproduce();
		//System.out.println("Reproduced");
		log();
		//System.out.println("Logged");
		
				
			
	}
	
	public void runLoop() {
		if (stop) {
			System.out.println("Stopped run");
		} else {
			if (set.isToSuccess()) {
				while (!population.getSuccess() && generationNr < set.getUpperLimit()) {
					EAStep();
					gens--;
				}
				System.out.println("Got success");
			} else {
				while (gens > 0 && !population.getSuccess()) {
					EAStep();
					gens--;
				}			
			}	
		}
		
		
		plot();
		stop = true;
	}
	
	public void plot() {
		//System.out.println("ran plot");
		f = new JFrame("Plots");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLayout(new GridLayout(1, 0));
		
		f.add(new Plot(bestFits, "Progression of the best fitness", "Best fitness"));
		f.add(new Plot(stdDevs, "Progression of standard deviation", "Standard deviation"));
		f.add(new Plot(averages, "Progression of average fitness", "Average fitness"));
		
		f.pack();
		f.setVisible(true);
	}

	private void logIndividuals(List<Individual> inds) {
		for (Individual ind : inds) {
			System.out.println("Individual fitness: ");
			System.out.println(ind.getFitness());
			System.out.println("Genotype: ");
			System.out.println(ind.getGenotype());
			System.out.println("Phenotype: ");
			System.out.println(ind.getPhenotype());
		}
	}
		
	private void log() {
		//To get correct data, first calc stats
		population.calcStats();
		//System.out.println("Adults: ");
		//logIndividuals(population.adults);
		
		//Tries to get fitness before set
		
		/* 
		*	System.out.println("Children: ");
		*	logIndividuals(population.children);
		*/
		
		stdDevs.add(population.getStdDev());
		averages.add(population.getAvgFit());
		bestFits.add(population.getBestFit());
		
		System.out.println("Generation statistics: ");
		System.out.println("Generation number: " + generationNr);
		System.out.println("Best fitness: " + population.getBestFit());
		System.out.println("Average fitness: " + population.getAvgFit());
		System.out.println("Standard deviation: " + population.getStdDev());
		System.out.println("Best phenotype: " + population.getBestPheno());
		System.out.println("Best genotype: " + population.getBestGeno());
		
		System.out.println("Run had " + population.errorCount + " errors");
		System.out.println("Chose " + population.getGoodPairs() + " good pairs.");
		System.out.println("Chose " + population.getBadPairs() + " bad pairs.");
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public static void main(String[] args) {
		Simulation a = new Simulation(new Settings());
		a.runLoop();
	}

	public Settings getSettings() {
		return set;
	}

	public boolean isStopped() {
		return stop;
	}

	public void start() {
		reset();
		if (set.getFitnessFunc() == "surpriseqlocal" || set.getFitnessFunc() == "surpriseqglobal") {
			System.out.println("ran special population");
			population = new SeqPopulation(set);
		}			
		stop = false;
		
		/*
		if (currentPlots.size() > 0) {
			System.out.println("Current plots");
			
			f.setVisible(false);
			for (JPanel p : currentPlots) {
				f.remove(p);
			}
			
		}
		*/
		
		runLoop();
	}

	public void stop() {
		stop = true;
		reset();
	}
	
	public void reset() {
		bestFits = new ArrayList<Double>();
		stdDevs = new ArrayList<Double>();
		averages = new ArrayList<Double>();
		generationNr = 0	;
		population = new Population(set);
		
		if (set.getAdultSelectionType() == "full") {
			set.setChildrenSize(set.getGenerationSize());
		}
		
	}

}
