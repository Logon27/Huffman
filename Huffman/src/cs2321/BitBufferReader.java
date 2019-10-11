package cs2321;

import java.io.FileOutputStream;
import java.io.IOException;

public class BitBufferReader {
	
    int[] byteArray;
    ArrayList<Byte> outputByte;
    ArrayList<Integer> binaryArray;
    String outputfile = "";
	
    int writeCount = 0;
    int readCount = 0;
    
    public BitBufferReader(String file) {
    	//initialize arrays, reset counts, and set output file.
    	outputfile = file;
    	writeCount = 0;
    	readCount = 0;
    	binaryArray = new ArrayList<Integer>();
    	outputByte = new ArrayList<Byte>();
    	byteArray = new int[8];
    }
    
    public int getNextBit() {
    	//grab the next bit in the binary array
        int temp = binaryArray.get(readCount);
        readCount++;

        return temp;
    }

    public int readNextByte() {
    	//to get the next byte concat the next 8 bits to the string and parse.
        String temp = "";
        for (int i = 0; i < 8; i++) {
            temp += getNextBit();
        }
		//overloaded method to return int of binary string
        return Integer.parseInt(temp, 2);
    }
    
    public void writeBit(int input) {

    	//increment the count and add the input the the byte array.
        byteArray[writeCount] = input;
        writeCount++;
        
        if (writeCount == 8) {
        	//if the byte is full then convert all the integers in the byte array to a byte
            int b = 0;
            int pos = 7;
            for (int i : byteArray) {
                if (i == 0) {
                	//if bit is 0 just decrement the position
                	pos--;
                }
                if (i == 1) {
                	//if bit is 1 convert the value at the current position to 1 and decrement the position
                    b = b | (1 << pos);
                    pos--;
                }
            }
            //add the byte to our output arraylist of bytes and reset the byte array and count
            outputByte.addLast((byte) b);
            byteArray = new int[8];
            writeCount = 0;
        }
    }

    //writes the byte array to file
    public void writeToFile() {
    	//create a byte array the size of our arraylist of bytes
        byte[] outputByteArray = new byte[outputByte.size()];

        // add all the bytes from our byte arraylist to our new byte array
        for (int i = 0; i < outputByte.size(); i++) {
            outputByteArray[i] = outputByte.get(i);
        }

        try(FileOutputStream writer = new FileOutputStream(outputfile)) {
        	//write the byte array to the output file
        	writer.write(outputByteArray);
        } catch (IOException e) {
            System.out.println("Error Writing to file.");
        }
    }
    
    //getter method for the write count for padding purposes.
    public int getWriteCount() {
    	return writeCount;
    }
    
    //getter method for the input binary array.
    public ArrayList<Integer> getInputBinaryArray() {
    	return binaryArray;
    }

}
