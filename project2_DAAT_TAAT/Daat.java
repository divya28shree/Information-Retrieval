package divya.lucene;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

public class Daat {

	int daatAndCount=0;
	int daatOrCount=0;
	public void daatAND(String[] terms, Writer out) throws IOException
	{
		daatAndCount=0;
		LinkedList<Integer> andPostingList = new LinkedList<Integer>();
		andPostingList = Lucene.invertedIndexMap.get(terms[0]);
		for(String term:terms)
		{
			out.append(term + " ");
			andPostingList=intersectionDaat(andPostingList,Lucene.invertedIndexMap.get(term));
			out.append(andPostingList.toString());
			out.flush();
		}
		if(andPostingList.isEmpty())
			out.append("\nResults: empty");
		else
			out.append("\nResults: "+andPostingList.toString().replaceAll("[\\[,\\]]", ""));
		out.append("\nNumber of documents in results: "+andPostingList.size());
		out.append("\nNumber of comparisons: "+ daatAndCount);
		//	System.out.println("DAAT and: "+andPostingList);
	}

	private LinkedList<Integer> intersectionDaat(LinkedList<Integer> andPostingList, LinkedList<Integer> linkedList) {
		
		//skip pointer
		int sp1=(int) Math.sqrt(linkedList.size());
		int sp2 = (int) Math.sqrt(andPostingList.size());
		LinkedList<Integer> tempList = new LinkedList<Integer>();
		
		
		outerLoop:for(int i=0;i<linkedList.size();)
		{

			for(int j=0;j<andPostingList.size();)
			{
				
				if(i>=linkedList.size())
					break outerLoop;
				if(linkedList.get(i)==andPostingList.get(j))
				{
					daatAndCount++;
					tempList.add(linkedList.get(i));
					i++;
					j++;
				}					
				else if(linkedList.get(i)<andPostingList.get(j))
				{
					int x=i+sp1;
					daatAndCount++;
					if(x <linkedList.size() && linkedList.get(x)<=andPostingList.get(j))
					{
						
						while(x<linkedList.size() && linkedList.get(x)<=andPostingList.get(j))
						{
							x=x+sp1;
						}
						i=x-sp1;
					}
					else
						i++;
				}
				else
					{
						daatAndCount++;
					int y=j+sp2;
						if(y<andPostingList.size() && linkedList.get(y)<=andPostingList.get(j))
						{
							
							while(y<andPostingList.size() && andPostingList.get(y)<=linkedList.get(i))
							{
								y=y+sp2;
							}
							j=y-sp2;
						}
						else
							j++;
						}
					
					
			}
				
		}
		return tempList;
		
	}
	
	public void daatOR(String[] terms, Writer out) throws IOException
	{
		daatOrCount=0;
		LinkedList<Integer> orPostingList = new LinkedList<Integer>();
		orPostingList=Lucene.invertedIndexMap.get(terms[0]);
		for(String term:terms)
		{
			out.append(term + " ");
			orPostingList = union(orPostingList, Lucene.invertedIndexMap.get(term));
		}
		if(orPostingList.isEmpty())
			out.append("\nResults: empty");
		else
			out.append("\nResults: "+orPostingList.toString().replaceAll("[\\[,\\]]", ""));
		out.append("\nNumber of documents in results: "+orPostingList.size());
		out.append("\nNumber of comparisons: "+ daatOrCount+"\n");
	}

	private LinkedList<Integer> union(LinkedList<Integer> orPostingList, LinkedList<Integer> linkedList) {
		
		LinkedList<Integer> temp = new LinkedList<>();
		if(orPostingList.isEmpty())
			orPostingList=linkedList;
		else {
			
			int i=0, j=0;
			outerLoop:for(;i<linkedList.size();)
			{
				for(;j<orPostingList.size();)
				{
					
					if(orPostingList.get(j).intValue()==linkedList.get(i).intValue())
					{
						daatOrCount++;
						temp.add(orPostingList.get(j).intValue());
						i++;
						j++;
					}						
					else if(orPostingList.get(j).intValue()<linkedList.get(i).intValue())
					{
						daatOrCount++;
						temp.add(orPostingList.get(j).intValue());
						j++;
						}
					else
					{
						temp.add(linkedList.get(i).intValue());
						i++;
						daatOrCount++;
						if (i>=linkedList.size()) 			
							break;

					}
					
					if(i>=linkedList.size() || j>=orPostingList.size())
						break outerLoop;
				}
			}
		if(i<linkedList.size())
		{
			for(;i<linkedList.size();i++)
				temp.add(linkedList.get(i).intValue());
		}
		if(j<orPostingList.size())
		{
			for(;j<orPostingList.size();j++)
				temp.add(orPostingList.get(j).intValue());
		}
		}
		
		return temp;
	}
	
}
