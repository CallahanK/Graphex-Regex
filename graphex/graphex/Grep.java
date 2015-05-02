package graphex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

public class Grep {

	public static void main(String[] args) throws IOException {
		int argParseCurr = 0;
		
		String nfaFileLoc = "";
		String dfaFileLoc = "";
		String regexSrc = "";
		String srcFileLoc = "";
		Boolean printNFA = false;
		Boolean printDFA = false;
		
		if (args.length==0){
			//Error missing arguments
		} else {
			//Begin Reading In Arguments
			if (args[argParseCurr].equals("-n")){
				//System.out.println("Printing NFA");
				nfaFileLoc = args[argParseCurr+1];
				argParseCurr+=2;
				printNFA = true;				
			}
			if (args[argParseCurr].equals("-d")){
				//System.out.println("Printing DFA");
				dfaFileLoc = args[argParseCurr+1];
				argParseCurr+=2;
				printDFA = true;
			}
			regexSrc = args[argParseCurr];
			srcFileLoc = args[argParseCurr+1];
			//End of Arguments
			
			//Begin Processing Regular Expression
			Regex reg = new Regex(regexSrc);
			NFANode head = reg.parse();
			//System.out.print(reg.messages);
			if(printNFA){
			//Print NFA to file
				try {
					String NFAString = NFANode.printNFA(head);
					//PrintWriter out = new PrintWriter("test.gv");
					PrintWriter outNFA = new PrintWriter(nfaFileLoc);
					outNFA.println(NFAString);
					outNFA.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//End print NFA
			
			//NFA to DFA
			DFANode dfaHead = NFANode.makeDFA(head, reg.alphabet);
			
			
			if(printDFA){
				//Print DFA to file
					try {
						String DFAString = DFANode.printDFA(dfaHead);
						//PrintWriter out = new PrintWriter("test.gv");
						PrintWriter outDFA = new PrintWriter(dfaFileLoc);
						outDFA.println(DFAString);
						outDFA.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//End print DFA
			
			try {
				LineNumberReader in = new LineNumberReader(new FileReader(srcFileLoc));
				String line = null;
				
				while ((line = in.readLine()) != null) {

					Boolean test = Regex.testString(dfaHead, line);
					if(test){
						System.out.println(line);
					}
					
				}
				in.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("I/O Error");
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}

}
