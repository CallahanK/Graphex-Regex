package graphex;

import java.util.HashMap;

public class NFANode {

	public HashMap<String,NFANode> transitionStates = new HashMap<>();
	public Boolean acceptState = false;
	
	
	public NFANode(){
		
	}
	
	
}
