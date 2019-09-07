package divya.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;


public class InverseIndex {

	public void createInverseIndex(String filePath)
	{		
		Path path = Paths.get(filePath);   
		Directory dir;
		
		try 
			{
			dir = FSDirectory.open(path);
			IndexReader indexReader = DirectoryReader.open(dir);
			Fields fields = MultiFields.getFields(indexReader);
			for(String field:fields)
			{ 
				Terms terms = fields.terms(field);
				TermsEnum termsEnum = terms.iterator();
				while(termsEnum.next()!=null)
				{
					BytesRef br = termsEnum.term();
					PostingsEnum post = termsEnum.postings(null,PostingsEnum.ALL);
					LinkedList<Integer> postinglist = new LinkedList<Integer>();
					int docId = post.nextDoc();
					while(docId !=PostingsEnum.NO_MORE_DOCS)
					{
						postinglist.add(docId);
						docId = post.nextDoc();
					}
					if(field.equalsIgnoreCase("id"))
						continue;
					Lucene.invertedIndexMap.put(br.utf8ToString(), postinglist);
				}
			}
					
			
			} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
