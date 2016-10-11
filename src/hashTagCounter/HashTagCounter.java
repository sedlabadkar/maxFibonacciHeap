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
 * UFID:     70647958 
 */
public class HashTagCounter {
	
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
				if (line.startsWith("#")){
					//HashTag
					String ht = line.substring(1, line.indexOf(' '));
					int count = Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim());
					HashTag newHashTag = new HashTag(ht, count);
					if (maxFH.contains(newHashTag)){
						System.out.println("Already Added: " + newHashTag.toString());
						maxFH.increaseKey(newHashTag);
					} else {
						System.out.println("Adding: " + newHashTag.toString());
						maxFH.insert(newHashTag);
					}
				} else if (line.toLowerCase().trim().equals("stop")){
					//Stop
					System.out.println("Got Stop. Exiting");
					System.exit(0);
					
				} else {
					//Number
					System.out.println(Integer.parseInt(line.trim()));
					/*int numHashTagsToOutput = Integer.parseInt(line.trim());
					HashTag[] tempHashTag = new HashTag[numHashTagsToOutput];
					//Write to output file
					for (int i = 0; i < numHashTagsToOutput; i++){
						tempHashTag[i] = maxFH.removeMin();
						System.out.println("Min = " + tempHashTag[i].toString());
					}
					for (int i = 0; i < numHashTagsToOutput; i++){
						maxFH.insert(tempHashTag[i]);
					}*/
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException FNFE){
			System.out.println ("File Not Found." + filename);
			System.exit(-1);
		} catch (IOException IOE) {
			IOE.printStackTrace();
			System.exit(0);
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
			return "HashTag [hashTag=" + hashTag + ", count=" + count + "]";
		}


		@Override
		//HashTag Object hashCode is same as the hashCode of the hashtag it holds
		public int hashCode()
		{
			return hashTag.hashCode();
		}
		
		@Override
		//HashTag objects are equal if they have the same hashtag
		public boolean equals( Object obj )
		{
			HashTag ht = (HashTag) obj;
			return hashTag.equals(ht.hashTag);
		}

		@Override
		public int compareTo(HashTag h){
			return this.count - h.count;
		}
	}
}
