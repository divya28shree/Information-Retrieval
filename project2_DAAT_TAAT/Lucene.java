package divya.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Lucene {
	public static Map<String, LinkedList<Integer>> invertedIndexMap = new HashMap<String, LinkedList<Integer>>();
	public static void main(String[] args) throws IOException {
		
			
		InverseIndex inverseIndex = new InverseIndex();
		inverseIndex.createInverseIndex(args[0]);
		
		URL path = Lucene.class.getResource(args[2]);
		//File fileIn = new File(path.getFile());
		File fileIn= new File(args[2]);
		File fileOut = new File(args[1]);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn),"UTF8"));
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut,true), "UTF8"));
        String readLine = "";        
        while ((readLine = in.readLine()) != null) {
            String[] terms = readLine.split(" ");
            Taat taat = new Taat();
            taat.getPostings(terms,out);
            out.append("TaatAnd\n");
            taat.getANDPostings(terms,out);
            out.append("\nTaatOr\n");
           taat.getORPostings(terms,out);
            Daat daat = new Daat();
            out.append("\nDaatAnd\n");
            daat.daatAND(terms,out);
            out.append("\nDaatOr\n");
            daat.daatOR(terms,out);
        }
        out.flush();
		out.close();
		
	}

}
