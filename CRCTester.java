// Sebastian Patino
// CIS3360 HW2

import java.io.*;
import java.util.*;

public class CRCTester
{
	public static void main(String [] args) throws Exception
	{

		String op;
		System.out.println();
		if(args[0].equals("c")){
			op="calculation";
		}else if(args[0].equals("v")){
			op="verification";
		}else{
			op="Invalid operation";
		}
		
		String[] binary;
		binary=HexToBinary(args[1],args[0]);
		int count=0;
		String[] polynomial=new String [25];
		
		
		//Sets up the parameters needed for the output file
		System.out.println("CIS3360 CRC Tester by Sebastian Patino");
		System.out.println();
		System.out.println("Mode of operation: "+op);
		System.out.println("The input string(hex): "+args[1]);
		System.out.print("The input string(binary): ");
		while(count<=args[1].length()*4-1){
			System.out.print(binary[count]);
			if((count+1)%4==0){
				System.out.print(" ");
			}
			count++;
		}
		int m=count;
		if(args[0].equals("c")){
			for(count=0;count<12;count++){
				binary[m+count]="0";
			}
			m=m+12;
		}
		
		System.out.println();
		System.out.println();
		System.out.print("The polynomial that was used (binary bit string): ");
		
		for(count=0;count<=24;count++){
			polynomial[count]="Uh oh";
			if(count==0||count==1||count==5||count==7||count==8||count==11||count==12){
				polynomial[count]="1";
				System.out.print(polynomial[count]);
			}else if(count<=12){
				polynomial[count]="0";
				System.out.print(polynomial[count]);	
			}
			if((count+1)%4==0&&count<=12)
				System.out.print(" ");
		}
		System.out.println();
		String[] lastThree=new String [3];
		String[] test;
		int n=0;
		int counter;
		
		
		
		char currentLetter;
		String letHolder="";
		int key;
		int notsure;
		if(args[0].equals("c")){
			System.out.print("Number of zeroes that will be appended to the binary input: ");
			System.out.print(count-13);
		}else if(args[0].equals("v")){
			System.out.print("The CRC observed at the end of the input: ");
			lastThree=HexToBinary(letHolder,"v");
			count=args[1].length()-1;
			int really=args[1].length()-3;
			for(;count>=really;count--){
				currentLetter=args[1].charAt(count);
				letHolder=letHolder+currentLetter;
			}
			lastThree=HexToBinary(letHolder,"v");
			for(count=2;count>=0;count--){
				for(key=0;key<=3;key++){
					notsure=count*4;
					System.out.print(lastThree[key+notsure]);
				}
				System.out.print(" ");
			}
			count=args[1].length()-1;
			System.out.print(" (bin) = ");
			for(;really<=count;really++){
				currentLetter=args[1].charAt(really);
				System.out.print(currentLetter);
			}
			System.out.print(" (hex)");
			
		}
		System.out.println();
		System.out.println();
		System.out.println("The binary string difference after each XOR step of the CRC calculation:");
		
		System.out.println();
		System.out.println();
		printArray(binary,m);
		binary=OneStep(binary,polynomial,m,"c");
		System.out.println();
		System.out.print("The CRC computed from the input:");
		if(args[0].equals("c")){
			lastStep(binary,m,args[0],letHolder);
		}else if(args[0].equals("v")){
			StringBuilder front= new StringBuilder();
			int what;
			char thisone;
			String almost;
			for(what=0;what<args[1].length()-3;what++){
				thisone=args[1].charAt(what);
				front.append(thisone);
			}
			almost=front.toString();
			test=HexToBinary(almost,"c");
			n=almost.length()*4;
			for(count=0;count<12;count++){
				test[n+count]="0";
			}
			n=n+12;
			test=OneStep(test,polynomial,n,args[0]);
			
			lastStep(test,n,args[0],letHolder);
		}
		
	}
	
	
	public static void printArray(String[] line, int size){
		int counter;
		for(counter=0;counter<size;counter++){
			System.out.print(line[counter]);
			if((counter+1)%4==0){
				System.out.print(" ");
			}
		}
		System.out.println();
	}
	
	
	public static void lastStep(String[]line, int size,String type,String helpful){
		int x=size-12;
		int n=0;
		StringBuilder value= new StringBuilder();
		String current;
		String three[]= new String[3];
		String letter[]= new String[3];
		for(;x<=size-1;x++){
			value.append(line[x]);
			if((x+1)%4==0){
				current=value.toString();
				three[n]=current;
				System.out.print(three[n]+" ");
				letter[n]=BinaryToHex(three[n]);
				n++;
				value= new StringBuilder();
			}
		}
		System.out.print("(bin) = ");
		for(x=0;x<3;x++){
			System.out.print(letter[x]);
		}
		System.out.print("(hex)");
		System.out.println();
		System.out.println();
		StringBuilder stretch= new StringBuilder();
		StringBuilder second= new StringBuilder();
		char end;
		String compare;
		String that;
		if(type.equals("v")){
			System.out.print("Did the CRC check pass? (Yes or No): ");
			for(x=0;x<3;x++){
				stretch.append(letter[x]);
			}
			for(x=2;x>=0;x--){
				second.append(helpful.charAt(x));
			}
			compare=stretch.toString();
			that=second.toString();
			if(compare.equals(that)){
				System.out.print("Yes");
			}else{
				System.out.print("No");
			}
		}
		System.out.println();
		System.out.println();
		
		
	}
	
	
	public static String[] OneStep(String[]line, String[]polynomial, int size, String type){
		int counter=0;
		int move=0;
		String newline[]=new String[size];
		int exit=0;
		int n=1;
		int test=0;
		int limit=size-12;
		while(exit!=1){
			int polycount=0;
			for(;polycount<=12;polycount++){
				if(line[counter].equals(polynomial[polycount])){
					line[counter]="0";
					test++;
				}else{
					line[counter]="1";
					if(n==1){
						move=counter;
					}
					n++;
					
				}
				counter++;
			}
			if(test==13){
				exit=1;
			}
			test=0;
			if(type.equals("c")){
				printArray(line,size);
			}
			
			n=1;
			counter=move;
			if(move>=limit){
				exit=1;
			}
		}
		return line;
	}
	public static String BinaryToHex(String input){
		int w,x,y,z;
		Character val;
		y=3;
		z=0;
		String fin;
		boolean check;
		for(x=0;x<4;x++){
			val=input.charAt(x);
			check=val.equals('1');
			if(check){
				w=(int)Math.pow(2,y);
				z=z+w;
				y=y-1;
			}else{
				y=y-1;
			}
		}
		fin=" ";
		
		if(z>=10){
			switch(z){
				case 10:	fin="A";
							break;
				case 11:	fin="B";
							break;
				case 12:	fin="C";
							break;
				case 13:	fin="D";
							break;
				case 14:	fin="E";
							break;
				case 15:	fin="F";
							break;
			}
		}else{
			fin=Integer.toString(z);
		}
		return fin;
		
	}
	
	
	
	
	
	
	
	
	public static String[] HexToBinary(String input, String type){
		int j,l,y,hold,mul,fin,count;
		char k;
		String[] value;
		if(type.equals("c")){
			value=new String[input.length()*4+12];
		}else{
			value=new String[input.length()*4];
		}
		
		String correct;
		//int size=input.length()*4;
		for(j=0;j<input.length();j++){
			StringBuilder insert= new StringBuilder();
			int x=3;
			k=input.charAt(j);
			fin=0;
			if(k>='0'&&k<='9'){
				l=k-'0';
			}else{
				l=k-'A'+10;
			}
			while(x>=0){
				y=(int)Math.pow(2,x);
				hold=l/y;
				l=l%y;
				mul=(int)Math.pow(10,x);
				hold=hold*mul;
				fin=fin+hold;
				x--;
			}
			if(fin<1000&&fin>=100){
				insert.append('0');
				insert.append(fin);
			}else if(fin<100&&fin>=10){
				insert.append("00");
				insert.append(fin);
			}else if(fin<10){
				insert.append("000");
				insert.append(fin);
			}else{
				insert.append(fin);
			}
			String sbFin= insert.toString();
			for(count=0;count<4;count++){
				StringBuilder finish= new StringBuilder();
				k=sbFin.charAt(count);
				finish.append(k);
				correct=finish.toString();
				value[count+j*4]=correct;
			}
			
		}
		return value;
	}
	
}
