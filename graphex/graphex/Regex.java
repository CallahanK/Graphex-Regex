package graphex;

public class Regex {

	String src = "";
	int currSubString = 0;
	
	public Regex(String src){
		this.src = src;
		parseRegex();
	}
	
	public void parseRegex(){
		System.out.println("REGEX");
		//TODO
		parseTerm();
		if(next().equals("|")){
			match("|");
			parseRegex();
		} else {
			//Epsilon production
		}
	}
	
	public void parseTerm(){
		System.out.println("TERM");
		//TODO
		parseFactor();
		/*if(next is term){
			parseTerm();
		} else {
			//epsilon production
		}
			 */
	}
	
	public void parseFactor(){
		System.out.println("FACTOR");
		//TODO
		parseBase();
		while(next().equals("*")){
			match("*");
		} 
	}
	
	public void parseBase(){
		System.out.println("BASE");
		//TODO	
		if(next().equals("(")){
			match("(");
			parseRegex();
			match(")");
		} else {
			parseChar();
		}
	}
	
	public void parseChar(){
		System.out.println("CHAR");
		//TODO
		System.out.println(next());
		currSubString++;
	}
	
	public void match(String input){
		System.out.println("Expecting: " + input);
		if(input.equals(next())){
			//Good match
			System.out.println("Matched: " + next());
		} else {
			//Error
			System.out.println("ERROR UNEXPECTED INPUT");
			//Expected input got next
		}
		currSubString++;
	}
	
	public String next(){
		String output = "";
		if(currSubString < src.length()){
			output = src.substring(currSubString, currSubString+1);
		} else {
			//Error 
			System.out.println("ERROR EOF REACHED EARLY");
		}
		
		return output;
	}
	
}
