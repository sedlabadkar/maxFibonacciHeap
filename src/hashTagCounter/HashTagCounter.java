/**
 * 
 */
package hashTagCounter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author   Sachin Edlabadkar
 *  
 */
public class HashTagCounter {
	
	private static final String INPUTFILE  = "input.txt";
	private static final String OUTPUTFILE = "hashTagCounterOutput.txt";
	public static void main(String[] args) {
		String inputFile, line;
		BufferedReader br;
		BufferedWriter bw;
		MaxFibonacciHeap<HashTag> maxFH = new MaxFibonacciHeap<HashTag>();
		HashMap<String, Node<HashTag>> elemMap = new HashMap<String, Node<HashTag>>();
		long z = 0;
		
		//The first and only argument is assumed to be the file name
		if (args.length == 0){
			System.out.println("No input provided, using default");
			inputFile = INPUTFILE; //if no input is provided, use default input file
		} else {
			inputFile = args[0];
			System.out.println("Input = " + inputFile);
		}
		
		//Initiate FibonnaciHeap
		try {
			//Initiate input and output and start working
			br = new BufferedReader(new FileReader(inputFile));
			bw = new BufferedWriter(new FileWriter(OUTPUTFILE));
			line = br.readLine();
			while (line != null){
				z++;
				if (line.startsWith("#")){
					//HashTag
					Node<HashTag> node;
					String ht = line.substring(1, line.indexOf(' '));
					int count = Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim());
					HashTag newHashTag = new HashTag(ht, count), currHT;
					node = elemMap.get(newHashTag.getHashTag());
					if (node != null){
						currHT = node.data;
						newHashTag.count += currHT.count;
						maxFH.increaseKey(node, newHashTag);
					} else {
						node = maxFH.insert(newHashTag);
						elemMap.put(newHashTag.getHashTag(), node);
					}
					//maxFH.printHeap();
				} else if (line.toLowerCase().trim().equals("stop")){
					//Stop
					br.close();
					bw.close();
					System.out.println("\nGot Stop. Exiting");
					System.out.println("Output saved in " + OUTPUTFILE);
					System.exit(0);
				} else {
					//Number
					int numHashTagsToOutput = Integer.parseInt(line.trim());
					Node<HashTag> tempHT;
					ArrayList<Node<HashTag>> tempHashTag = new ArrayList<Node<HashTag>>();
					StringBuilder outString = new StringBuilder();
					for (int i = 0; i < numHashTagsToOutput; i++){
						tempHT = maxFH.removeMax();
						tempHashTag.add(tempHT);
						//outString.append(tempHT.getData().getHashTag() + "" + tempHT.getData().getCount()); //output with count
						outString.append(tempHT.getData().getHashTag()); //Only the hashTag needs to go in the output file
						if (i != numHashTagsToOutput - 1)
							outString.append(",");
						//System.out.println("Removed Max HashTag = " + tempHT.getData().toString() + "\n");
					}
					//System.out.println("RemoveMax operation result : " + outString.toString());
					//System.out.println("Result Written to output file.\n");
	                try {
	                    bw.append(outString.toString());
	                    bw.newLine();
	                    bw.flush();
	                } catch (IOException e) {
	                   System.out.println("Unable to append to output_file");
	                }
					for (int i = 0; i < numHashTagsToOutput; i++){
						maxFH.insert(tempHashTag.get(i));
					}
					//System.out.println("------------------------------------------------------------------------");
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException FNFE){
			System.out.println (FNFE.getMessage());
			System.exit(-1);
		} catch (IOException IOE) {
			IOE.printStackTrace();
			System.exit(0);
		} catch (NumberFormatException NFE){
			System.out.println("Fault at line " + z);
		}
    }
}

class HashTag implements Comparable<HashTag>{
	String hashTag;
	Integer count;
	
	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public HashTag(String hashTag, Integer count){
		this.hashTag = hashTag;
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "[HashTag=" + hashTag + ", Count=" + count + "]";
	}
	
	public HashTag increaseCount(int amount) {
        this.count += amount;
        return this;
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
		return (this.count - h.count);
	}
}