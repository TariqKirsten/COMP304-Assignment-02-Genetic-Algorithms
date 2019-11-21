package com.tariqkirsten;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DNA implements Comparable<DNA> {

	
	public ArrayList<Integer> genes; // either 1 or 0... 1-in vehicle 0-not in vehicle 
	public int fitness;
	public int weight;
	public DNA(ArrayList<Integer> genes) 
	{
       this.genes =  new ArrayList<Integer>(genes);
	}
	public DNA()
	{
		this.genes = new ArrayList<Integer>();
		
	}
	
	public DNA crossover(DNA partner)
	{
		DNA child = new DNA();
		int midpoint = ThreadLocalRandom.current().nextInt(genes.size());
		if(ThreadLocalRandom.current().nextInt(1)<0.8) //ensures it will only happen at that rate
		{
			
		
		for(int i = 0;i<genes.size();i++)
		{
			if(i>midpoint)
			{
				child.genes.add(genes.get(i));
			}
			else
			{
				child.genes.add(partner.genes.get(i));
			}
		}
		}
		return child;
	}
	
	
	public void mutate(double mutationRate)
	{
		for(int i = 0;i<genes.size();i++)
		{
			if(ThreadLocalRandom.current().nextInt(1)<mutationRate) //ensures it will only happen at that rate
			{
				genes.set(i, ThreadLocalRandom.current().nextInt(0,2));
			}
		}
	}

	@Override
	public int compareTo(DNA o) {
		
		if(this.fitness<o.fitness)
		{
			return 1;
		}
		else if(this.fitness == o.fitness)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	
	
	
	
	
	
	
}
