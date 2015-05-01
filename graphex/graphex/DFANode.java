package graphex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFANode {

	public Set<NFANode> name = new HashSet<NFANode>(); 
	public Map<String, DFANode> transitionStates = new HashMap<String, DFANode>();
	public Boolean acceptState = false;
	
	//Construct a single DFA node from a single NFA node
	public DFANode(NFANode start){
		this.name.add(start);
	}
	
	//Constructs a single DFA node from a set of NFA nodes 
	//with a single transition from an existing node
	public DFANode(DFANode currNode, HashSet<NFANode> newNode, String transitionSymbol){
		this.name.addAll(newNode);
		currNode.transitionStates.put(transitionSymbol, this);
		// ? return this ?//
	}
	
	
	
}
