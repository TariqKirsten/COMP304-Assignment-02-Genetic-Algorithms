package com.tariqkirsten;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Population {

	private ArrayList<String> populationData;
	private ArrayList<Item> items;
	private  ArrayList<DNA> population;
	private int capacity;
	private int quota;
	private int maxNumberOfItems;
	private int populationSize;
	private int popMaxSize;
	private float mutationRate = 0.01f;
	
	public Population(int capacity, int quota,int size)
	{
		this.populationData = new ArrayList<String>();
		this.items = new ArrayList<Item>();
		this.population = new ArrayList<DNA>();
		this.populationSize = size;
		this.popMaxSize = 1000;
		this.capacity = capacity;
		this.quota = quota;
	//	this.maxNumberOfItems = maxNumberOfItems;
	}
	
	public void insert(String member)
	{
		populationData.add(member);
	}
	
	public void generate()
	{
		ArrayList<Integer> genes = new ArrayList<Integer>();
		for(int i = 0;i <populationData.size();i++)
		{
			String[] data = populationData.get(i).split(" ");
			ArrayList<String> dataList = new ArrayList<String>();
			for(String s : data)
			{
				dataList.add(s);
			}
			
			for(int j = 0;j<dataList.size();j++)
			{
				String current = dataList.get(j);
				if(current.equals(""))
				{
					dataList.remove(j);
				}
			}
			items.add(new Item(dataList.get(0),Integer.parseInt(dataList.get(1)) , Integer.parseInt(dataList.get(2))));
		}
		
		for(int i = 0;i <populationSize;i++)
		{
			for(int j = 0; j<populationData.size();j++)
			{
				int randomInt =  ThreadLocalRandom.current().nextInt(0,2);
				genes.add(randomInt);
			}
			
			DNA newMember = new DNA(genes);
			population.add(newMember);
			genes.clear();
		}
	
	}

	public void calculateFitness()
	{
		for(int i = 0;i<population.size();i++)
		{
			DNA selectedMember = population.get(i);
			int totalWeight = 0;
			int totalValue = 0;
			
			
			for(int j = 0;j<items.size();j++)
			{
				if(selectedMember.genes.get(j)==1)
				{
					totalWeight += items.get(j).weight;
					totalValue += items.get(j).value;
				}
			}
			if(totalWeight>capacity)
			{
				selectedMember.fitness = 0;
				//population.remove(i);
			}
			
			else{
				selectedMember.fitness = totalValue;
				selectedMember.weight = totalWeight;
			}
		}
		Collections.sort(population);
		
	}
	
	public void calculateFitness(DNA dna)
	{
		
		{
			DNA selectedMember = dna;
			int totalWeight = 0;
			int totalValue = 0;
			
			
			for(int j = 0;j<items.size();j++)
			{
				if(selectedMember.genes.get(j)==1)
				{
					totalWeight += items.get(j).weight;
					totalValue += items.get(j).value;
				}
			}
			if(totalWeight>capacity)
			{
				selectedMember.fitness = 0;
				
			}
			
			else{
				selectedMember.fitness = totalValue;
				selectedMember.weight = totalWeight;
			}
		}
		//Collections.sort(population);
		
	}

	public ArrayList<DNA> tournament(int n)
	{
		int numOfMembers = n;
		int startPos = 0;
		int numOfMembersCompeted = 0; //checking if all members competed... debug purposes
		ArrayList<DNA> roundWinners = new ArrayList<DNA>();
		ArrayList<DNA> tournamentParticipants = new ArrayList<DNA>();
		while(!(n>=population.size()))
		{
		for(int i =startPos;i<n;i++)
		{
			tournamentParticipants.add(population.get(i));
			numOfMembersCompeted++;
		}
		Collections.sort(tournamentParticipants);
		roundWinners.add(tournamentParticipants.get(0)); // member at 0 will be the most fit
		startPos = n;
		n+=numOfMembers;
		tournamentParticipants.clear();
		}
		
		for(int i =startPos;i<population.size();i++)
		{
			tournamentParticipants.add(population.get(i));
			numOfMembersCompeted++;
		}
		Collections.sort(tournamentParticipants);
		roundWinners.add(tournamentParticipants.get(0)); // member at 0 will be the most fit
		Collections.sort(roundWinners);
		ArrayList<DNA> topTwo = new ArrayList<DNA>();
		
		if(roundWinners.size()==1)
		{
			roundWinners.add(tournamentParticipants.get(1)); //because it is sorted the second participant will be the second most fit
		}
		else
		{
			topTwo.add(roundWinners.get(1));
		}
		topTwo.add(roundWinners.get(0));
		topTwo.add(roundWinners.get(1));
		
		return topTwo;
	}

	public ArrayList<DNA> roulette()
	{
		DNA parentA = null, parentB = null;
		
		parentA = population.get(ThreadLocalRandom.current().nextInt(0,population.size()));
	parentB = population.get(ThreadLocalRandom.current().nextInt(0,population.size()));
		
		
		
		ArrayList<DNA> parents = new ArrayList<DNA>();
		parents.add(parentA);
		parents.add(parentB);
		return parents;
	}
	
	public void reproduction()
	{
		populationSize = population.size();
		for(int i = 0;i<populationSize;i++)
		{
			ArrayList<DNA> parents = tournament(10);
			DNA child = parents.get(0).crossover(parents.get(1));
			child.mutate(mutationRate);
			calculateFitness(child);
			population.add(child);
		}
		
		//culling the population, lower half of population are going to be considered unfit for further reproduction
		if(population.size()>=5000) //
		{
			Collections.sort(population);
			for(int i = population.size()-1;i>population.size()/2;i--)
			{
				population.remove(i);
			}
			//population.trimToSize();
		}
		
		
		
		
	}
	
	public void clearAll()
	{
		population.clear();
		items.clear();
		
	}

	
	public void run()
	{
		generate();
		calculateFitness();
		/*while(population.get(0).fitness == 0 || population.get(1).fitness ==0)
		{
			System.out.println("Random gene generation produced poor results, doubling population size and regenerating");
			populationSize *=2;
			clearAll();
			generate();
			calculateFitness();
			
		}*/
		System.out.println("Evolving...");
		int fitnessAtCurrentGeneration = 0;
		int generation = 1;
		int generationCurrentRound = 1;
		int solutionFitness = 0;
		int solutionGeneration = 0;
		int maxGenerations = 500;
		int maxGenerationsPerRound = 200;
		DNA mostFit = population.get(0);
		HashMap<Integer,Integer> generations = new HashMap<Integer, Integer>();
		while(true)
		{
			calculateFitness();
			mostFit = population.get(0);
			reproduction();
			fitnessAtCurrentGeneration=  mostFit.fitness;
			if(generation%1 == 0)
			{
				System.out.println("Generation:"+generation + " Fittest: " +fitnessAtCurrentGeneration+" Population: "+population.size());
			}
			if(solutionFitness<fitnessAtCurrentGeneration)
			{
				solutionFitness = fitnessAtCurrentGeneration;
				solutionGeneration = generation;
			}
				
			generation++;
			generationCurrentRound++;
			if(generationCurrentRound==maxGenerationsPerRound)
			{
				if(mostFit.fitness<quota)
				{
					generationCurrentRound = 0;
				}
				
				if(generation==maxGenerations)
				{
					break;
				}
				else
				{
					break;
				}
				
				
			}
		}
		populationSize = population.size();
		Collections.sort(population);
		System.out.println("Total Generations: "+generation);
		System.out.println("Best Solution with given parameters");
		System.out.println("Solution Generation:"+solutionGeneration + "\nFitness:"+solutionFitness);
		System.out.println("Value: "+population.get(0).fitness);
		System.out.println("Weight: "+population.get(0).weight);
		System.out.print("Set: ");
		for(int i = 0;i< population.get(0).genes.size();i++)
		{
			if(population.get(0).genes.get(i) ==1)
			{
				System.out.print(items.get(i).label+' ');
			}
		
		}
		System.out.println();
		float capacityUsed = (population.get(0).weight/(float)capacity)*100;
		System.out.println(capacityUsed+"% of total capacity used");
		
	}
}
