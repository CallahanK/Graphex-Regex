package graphex;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Grep {

	public static void main(String[] args) {
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
					PrintWriter out = new PrintWriter(nfaFileLoc);
					out.println(NFAString);
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//End print NFA
			
			//TEST EPSILONCLOSURE
			Set<NFANode> test = new HashSet<NFANode>();
			test.add(head);
			test = NFANode.epsilonClosure(test);
			Iterator<NFANode> print = test.iterator();
			while(print.hasNext()){
				System.out.print(print.next().name + "\n");
			}
			//END TEST EPSILON CLOSURE
			
		}
		
		System.out.println("Arguments");
		System.out.println(nfaFileLoc);
		System.out.println(dfaFileLoc);
		System.out.println(regexSrc);
		System.out.println(srcFileLoc);
		System.out.println("\n");
		
		
	}

}
