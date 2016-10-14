/**
 * 
 */
package hashTagCounter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author   Sachin Edlabadkar
 *  
 */
public class HashTagCounter {
	private static final boolean HTCLOGS  = false;
	private static final String INPUTFILE  = "input.txt";
	//private static final String OUTPUTFILE = "output.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new HashTagCounter().start(args); 
	}
	
	public void start(String[] args){
		//The first argument is assumed to be the file name
		String filename, line;
		BufferedReader br;
		MaxFibonacciHeap<HashTag> maxFH = new MaxFibonacciHeap<HashTag>();
		long z = 0;
		//Default Filename -- TODO Later change this to exit if nothing is provided
		if (args.length == 0){
			filename = INPUTFILE;
			//System.exit(0);
		} else {
			filename = args[0];
		}
		//Initiate FibonnaciHeap
		try {
			br = new BufferedReader(new FileReader(filename));
			line = br.readLine();
			while (line != null){
				z++;
				if (line.startsWith("#")){
					//HashTag
					String ht = line.substring(1, line.indexOf(' '));
					int count = Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim());
					HashTag newHashTag = new HashTag(ht, count), currHT;
					if ((currHT = maxFH.contains(newHashTag)) != null){
						if(HTCLOGS)System.out.println("\n\nAlready Added: " + currHT.toString());
						newHashTag.count += currHT.count;
						if(HTCLOGS)System.out.println("\n\nIncrease Key to: " + newHashTag.toString());
						maxFH.increaseKey(newHashTag);
					} else {
						if(HTCLOGS)System.out.println("\n\nAdding: " + newHashTag.toString());
						maxFH.insert(newHashTag);
					}
				} else if (line.toLowerCase().trim().equals("stop")){
					//Stop
					System.out.println("Got Stop. Exiting");
					System.exit(0);
					
				} else {
					//Number
					if (HTCLOGS) System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>               " +Integer.parseInt(line.trim()) + "                  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					int numHashTagsToOutput = Integer.parseInt(line.trim());
					HashTag[] tempHashTag = new HashTag[numHashTagsToOutput];
					//Write to output file
					for (int i = 0; i < numHashTagsToOutput; i++){
						tempHashTag[i] = maxFH.removeMax();
						if(true)System.out.print(tempHashTag[i].toString() + ",");
					}
					System.out.println("");
					for (int i = 0; i < numHashTagsToOutput; i++){
						maxFH.insert(tempHashTag[i]);
					}
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException FNFE){
			System.out.println ("File Not Found." + filename);
			System.exit(-1);
		} catch (IOException IOE) {
			IOE.printStackTrace();
			System.exit(0);
		} catch (NumberFormatException NFE){
			System.out.println("Fault at line " + z);
		}
	}
	
	public HashTagCounter(){

	}
	
	class HashTag implements Comparable<HashTag>{
		String hashTag;
		Integer count;
		
		public HashTag(String hashTag, Integer count){
			this.hashTag = hashTag;
			this.count = count;
		}
		
		@Override
		public String toString() {
			//return "[HashTag=" + hashTag + ", Count=" + count + "]";
			return hashTag;
		}


		@Override
		//HashTag Object hashCode is same as the hashCode of the hashtag it holds - So basically we default to String hashcode method
		public int hashCode()
		{
			return hashTag.hashCode();
		}
		
		@Override
		//HashTag objects are equal if they have the same hashtag - So basically we default to String equals method
		public boolean equals( Object obj )
		{
			HashTag ht = (HashTag) obj;
			return hashTag.equals(ht.hashTag);
		}

		@Override
		public int compareTo(HashTag h){
			if (h == null) return 1;
			return this.count - h.count;
		}
	}
}
