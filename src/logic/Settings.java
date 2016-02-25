package logic;

public class Settings {
	
	private double mutateRate;
	private int genotypeSize;
	private int nrOfGenerations;
	private String fitnessFunc;
	private String adultSelectionType;
	private String mateSelectionType;
	private double crossoverRate;
	private boolean toSuccess;
	private int generationSize;
	private int childrenSize;
	private int K;
	private double eChance;
	private boolean stop;
	private int z;
	private int upperLimit;
	private int seqLength;
	private int symbolNr;
	private int bitSize;
	private boolean elite;
	private double minRank;
	private double maxRank;
	
	

	public Settings() {
		upperLimit = 1000;
		crossoverRate = 0.9;
		mutateRate = 0.09;
		genotypeSize = 40;
		nrOfGenerations = 500;
		
		//for full generational replacement
		elite = false;
		
		//onemax, surpriseqlocal, surpriseqglobal, lolz
		fitnessFunc = "surpriseqlocal";
		
		bitSize = 4; //Default
		
		
		generationSize = 100;
		//For mixing and overproduction
		childrenSize = 510;
		
		//For tournament selection
		K = 4;
		eChance = 0.1d; //0-1
		
		//for lolz prefix
		z = 4;
		
		//for rank selection
		minRank = 0.5;
		maxRank = 1.5;
		
		//For surprising seq
		seqLength = 26;
		symbolNr = 10;
		
		//full, overprod, mix || full generational replacement, over-production, generational mixing
		adultSelectionType = "full";
		//fitprop, sigsca, toursel, ranksel || fitness proportionate, sigma scaling, tournament selection, rank selection
		mateSelectionType = "fitprop";
		toSuccess = true;
	}

	public double getMutateRate() {
		return mutateRate;
	}

	public void setMutateRate(double mutateRate) {
		this.mutateRate = mutateRate;
	}

	public int getGenerationSize() {
		return generationSize;
	}

	public void setGenerationSize(int generationSize) {
		this.generationSize = generationSize;
	}

	public int getGenotypeSize() {
		return genotypeSize;
	}

	public void setGenotypeSize(int genotypeSize) {
		this.genotypeSize = genotypeSize;
	}

	public String getFitnessFunc() {
		return fitnessFunc;
	}

	public void setFitnessFunc(String fitnessFunc) {
		this.fitnessFunc = fitnessFunc;
	}

	public String getAdultSelectionType() {
		return adultSelectionType;
	}

	public void setAdultSelectionType(String adultSelectionType) {
		this.adultSelectionType = adultSelectionType;
	}

	public String getMateSelectionType() {
		return mateSelectionType;
	}

	public void setMateSelectionType(String mateSelectionType) {
		this.mateSelectionType = mateSelectionType;
	}

	public int getNrOfGenerations() {
		return nrOfGenerations;
	}

	public void setNrOfGenerations(int nrOfGenerations) {
		this.nrOfGenerations = nrOfGenerations;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public boolean isToSuccess() {
		return toSuccess;
	}

	public void setToSuccess(boolean toSuccess) {
		this.toSuccess = toSuccess;
	}

	public int getChildrenSize() {
		return childrenSize;
	}

	public void setChildrenSize(int childrenSize) {
		this.childrenSize = childrenSize;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public void setAdultSelection(String name) {
		this.adultSelectionType = name;
	}

	public void setMateSelection(String name) {
		this.mateSelectionType = name;
	}

	public double getEChance() {
		return eChance;
	}

	public void setEChance(double eChance) {
		this.eChance = eChance;
	}

	public void setCrossoverRate(double doubleValue) {
		this.crossoverRate = doubleValue;
		
	}

	public int getZLength() {
		return z;
	}

	public void setZLength(int intValue) {
		z = intValue;		
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public int getSeqLength() {
		return seqLength;
		
	}
	
	public int getSymbolNr() {
		return symbolNr;
	}

	public int getBitSize() {
		return bitSize;
	}
	
	public void setBitSize(int bitSize) {
		this.bitSize = bitSize;
	}

	public void setSeqLength(int intValue) {
		seqLength = intValue;		
	}

	public void setSymbolNr(int intValue) {
		symbolNr = intValue;
		
	}

	public void setElitism(boolean selected) {
		elite = selected;
		
	}

	public boolean isElitism() {
		return elite;
	}

	public double getMinRank() {
		return minRank;
	}

	public double getMaxRank() {
		return maxRank;
	}

}
