package com.tariqkirsten;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args)
	{
		ArrayList<Population> popList = loadInput("input.txt");
		Scanner scan = new Scanner(System.in);
		for(int i = 0;i<popList.size();i++)
		{
			System.out.println("Population "+(i+1));
			popList.get(i).run();
			System.out.println("Load the next population? Y/N");
			String input = scan.nextLine();
			if(input.equalsIgnoreCase("y"))
			{
				if(i==popList.size()-1)
				{
					System.out.println("End of input file... exiting");
					break;
				}
				else {
					continue;
					
				}
			}
			else
			{
				System.out.println("Exiting...");
				break;
			}
		}
		
	}
	
	public static ArrayList<Population> loadInput(String path) 
	{
		ArrayList<Population> population = new ArrayList<Population>(); 
		File inputFile = new File("input.txt");
		Scanner scan;
		Population currentPop = null;
		try {
			scan = new Scanner(inputFile);
		
		String current = scan.nextLine();
		//scan.skip("\n");
		while(scan.hasNextLine())
		{
			//if(scan.equals("\n")) continue;
			//
			if(current.equals("\n")|| current.equals(""))
			{
				current = scan.nextLine();
				continue;
			}
			else{
				
				if(current.equals("***")) //new population
				{
					scan.hasNextLine();
					int capacity = scan.nextInt();
					int quota = scan.nextInt();
					int size = scan.nextInt();
					population.add(new Population(capacity, quota, 10));
					currentPop = population.get(population.size()-1); // get latest population
					current = scan.nextLine();
				}
				else {
					currentPop.insert(current);
					current = scan.nextLine();
				}
				
				
			}
		}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("input data successfully loaded");
		return population;
		
	}
	
	
}
