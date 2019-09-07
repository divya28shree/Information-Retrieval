package divya.lucene;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

public class Taat {
	
	int taatAndCount=0;
	int taatOrCount=0;

	public void getPostings(String[] terms, Writer out) throws IOException
	{
		for(String term: terms)
		{
			out.write("GetPostings");
			out.write("\n"+ term);
			//System.out.println("Term Posting: " + term);
			//System.out.println("Postings: "+ Lucene.invertedIndexMap.get(term) );
			out.write("\nPostings list: " + Lucene.invertedIndexMap.get(term).toString().replaceAll("[\\[,\\]]", "")+"\n");
		}
	}
	
	public void getORPostings(String[] terms, Writer out) throws IOException
	{
		taatOrCount=0;
		LinkedList<Integer> orPostingList = new LinkedList<Integer>();
		
		for(String term: terms)
		{
			out.append(term + " ");
			LinkedList<Integer> termPostingList = new LinkedList<Integer>();
			termPostingList = Lucene.invertedIndexMap.get(term);
			orPostingList = union(termPostingList, orPostingList);
		}
		out.append("\nResults: "+orPostingList.toString().replaceAll("[\\[,\\]]", ""));
		out.append("\nNumber of documents in results: "+ orPostingList.size());
		out.append("\nNumber of comparisons: "+taatOrCount);
		//System.out.println(orPostingList);
	}

	private LinkedList<Integer> union(LinkedList<Integer> termPostingList, LinkedList<Integer> orPostingList)
	{
		
		LinkedList<Integer> unionPostingList = new LinkedList<Integer>();
		
		if(orPostingList.isEmpty())
			unionPostingList=termPostingList;
		else
		{
			unionPostingList=orPostingList;
			for(int posting: termPostingList)
			{
				taatOrCount++;
				unionPostingList.add(posting);
			}
		}
		
		//sort
		unionPostingList.sort((a,b)->a.compareTo(b));
		
		//remove duplicates
		LinkedList<Integer> finalList = new LinkedList<Integer>();
		int now=unionPostingList.get(0);
		finalList.add(now);
		for(int i=1;i<unionPostingList.size();i++)
		{
			int next=unionPostingList.get(i);
			if(next==now)
				continue;
			else
			{
				finalList.add(next);
				now=next;
			}
		}
		
		return finalList;
	}
	
	
	public void getANDPostings(String[] terms, Writer out) throws IOException
	{
		taatAndCount=0;
		//LinkedList<Integer> andPostingList = new LinkedList<Integer>();
		LinkedList<Integer> tempList = new LinkedList<Integer>();
		tempList=Lucene.invertedIndexMap.get(terms[0]);
		for(String term:terms)
		{
			out.append(term + " ");
			tempList = intersection(tempList,Lucene.invertedIndexMap.get(term));
		}
		if(tempList.isEmpty())
			out.append("\nResults: empty");
		else
			out.append("\nResults: "+tempList.toString().replaceAll("[\\[,\\]]", ""));
		out.append("\nNumber of documents in results: "+tempList.size());
		out.append("\nNumber of comparisons: "+ taatAndCount);
	}

	private LinkedList<Integer> intersection(LinkedList<Integer> tempList, LinkedList<Integer> linkedList) {
		
		LinkedList<Integer> andPostingList = new LinkedList<Integer>();
		for(int i=0;i<linkedList.size();i++)
		{
			taatAndCount++;
			for(int j=0;j<tempList.size();j++)
				if(tempList.get(j).intValue()==linkedList.get(i).intValue())
					andPostingList.add(tempList.get(j));
		}
		return andPostingList;
	}
}
