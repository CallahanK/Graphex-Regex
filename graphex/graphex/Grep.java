package graphex;

public class Grep {

	public static void main(String[] args) {
		System.out.println("Running");
		int argParseCurr = 0;
		
		String nfaFileLoc = "";
		String dfaFileLoc = "";
		String regexSrc = "";
		String srcFileLoc = "";
		
		if (args.length==0){
			//Error missing arguments
		} else {
			
			if (args[argParseCurr].equals("-n")){
				System.out.println("Making NFA");
				nfaFileLoc = args[argParseCurr+1];
				argParseCurr+=2;
			}
			if (args[argParseCurr].equals("-d")){
				System.out.println("Making DFA");
				dfaFileLoc = args[argParseCurr+1];
				argParseCurr+=2;
			}
			regexSrc = args[argParseCurr];
			srcFileLoc = args[argParseCurr+1];
		}
		
		System.out.println(nfaFileLoc);
		System.out.println(dfaFileLoc);
		System.out.println(regexSrc);
		System.out.println(srcFileLoc);
		System.out.println("\n");
		
		Regex reg = new Regex(regexSrc);
		reg.parseRegex();
		
	}

}
