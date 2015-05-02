package graphex;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class Regex {

	String src = "";
	int currSubString = 0;
	ArrayDeque<NFANode> fragmentStack = new ArrayDeque<NFANode>();
	Set<String> alphabet = new HashSet<String>();
	String messages = "";
	
	public Regex(String src){
		this.src = src;
	}
	
	public NFANode parse(){
		parseRegex();
		return fragmentStack.pop();
	}
	
	public void parseRegex(){
		messages += "REGEX\n";
		//TODO
		parseTerm();
		if(next().equals("|")){
			match("|");
			parseRegex();
			//Union a | b
			NFANode tmpB = fragmentStack.pop();
			NFANode tmpA = fragmentStack.pop();
			NFANode union = NFANode.makeUnion(tmpA, tmpB);
			fragmentStack.push(union);
			
		} else {
			//Epsilon production
		}
	}
	
	public void parseTerm(){
		messages += "TERM\n";
		//TODO
		parseFactor();
		
		if(!next().equals(")") && !next().equals("|") && !next().equals("")){
			parseTerm();
			//Concatenate a . b
			NFANode tmpB = fragmentStack.pop();
			NFANode tmpA = fragmentStack.pop();
			tmpA = NFANode.makeConcat(tmpA, tmpB);
			fragmentStack.push(tmpA);
		} else {
			//epsilon production
		}
			
	}
	
	public void parseFactor(){
		messages += "FACTOR\n";
		//TODO
		parseBase();
		while(next().equals("*")){
			match("*");
			NFANode tmpNode = fragmentStack.pop();
			tmpNode = NFANode.makeStar(tmpNode);
			fragmentStack.push(tmpNode);
		} 
	}
	
	public void parseBase(){
		messages += "BASE\n";
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
		
		NFANode nodeFragment = new NFANode(next());
		fragmentStack.push(nodeFragment);
		
		alphabet.add(next());
		
		messages += "CHAR\n";
		messages += next() + "\n";
		currSubString++;
	}
	
	
	
	public void match(String input){
		messages += "Expecting: " + input + "\n";
		if(input.equals(next())){
			//Good match
			messages += "Matched: " + next() + "\n";
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
		}		
		return output;
	}
	
	public static Boolean testString(DFANode head, String test){
		Boolean matched = false;
		DFANode currNode = head;
		
		if(head.acceptState){
			matched = true;
		}
		
		for(int i = 0; i<test.length(); i++){
			String sub = test.substring(i, i+1);
			
			currNode = currNode.transitionStates.get(sub);
			if(currNode==null){
				return false;
			}
			if(currNode.acceptState){
				matched = true;
			}
		}
		
		
		
		return matched;
	}
	
}
