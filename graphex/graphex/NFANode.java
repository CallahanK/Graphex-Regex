package graphex;

import java.util.*;

public class NFANode {

	public final static String EPSILON = "\u03B5";
	
	public Map<String,HashSet<NFANode>> transitionStates = new HashMap<String,HashSet<NFANode>>();
	public Boolean acceptState = false;
	
	
	public NFANode(){
		//TODO
	}
	
	//For single char transition
	public NFANode(String charTransition){
		//NFANode start = new NFANode();
		NFANode accept = new NFANode();
		
		HashSet<NFANode> set = new HashSet<NFANode>();
		set.add(accept);
		
		this.transitionStates.put(charTransition, set);
		accept.acceptState = true;
		
	}
	
	public NFANode(String charTransition, NFANode current){
		HashSet<NFANode> set = new HashSet<NFANode>();
		set.add(current);
		
		this.transitionStates.put(charTransition, set);
	}
	
	public static NFANode makeStar(NFANode starStart){
		Iterator<NFANode> node = getNFANodes(starStart).iterator();
		while(node.hasNext()){
			NFANode next = node.next();
			if(next.acceptState){
				HashSet<NFANode> set = next.transitionStates.get(EPSILON);
				if(set==null){
					set = new HashSet<NFANode>();
					next.transitionStates.put(EPSILON, set);
				}
				set.add(starStart);
			}
		}
		
		NFANode newStart = new NFANode(EPSILON, starStart);
		newStart.acceptState = true;
		
		return newStart;
	}
	
	public static NFANode makeConcat(NFANode fragA, NFANode fragB){
		Iterator<NFANode> node = getNFANodes(fragA).iterator();
		while(node.hasNext()){
			NFANode next = node.next();
			if(next.acceptState){
				
				HashSet<NFANode> set = next.transitionStates.get(EPSILON);
				if(set==null){
					set = new HashSet<NFANode>();
					next.transitionStates.put(EPSILON, set);
				}
				set.add(fragB);
				next.acceptState=false;
			}
		}
		return fragA;
	}
	
	public static NFANode makeUnion(NFANode fragA, NFANode fragB){
		NFANode newStart = new NFANode(EPSILON, fragA);
		HashSet<NFANode> set = newStart.transitionStates.get(EPSILON);
		set.add(fragB);
		
		return newStart;
	}
	
	//Starts the recursion for getNodes
	public static Set<NFANode> getNFANodes(NFANode head){
		Set<NFANode> nodeSet = new HashSet<NFANode>();
		
		getNodes(head,nodeSet);
		
		return nodeSet;
	}
	
	//Helper Method for getNFANodes
	//Should only be called within 
	private static void getNodes(NFANode node, Set<NFANode> set){
		if( set.add(node)){
			Set<NFANode> thisNodeTransitions = new HashSet<NFANode>();
			//Gets Iterator for each HashSet mapped to in HashMap
			Iterator<HashSet<NFANode>> nextNodesSet = node.transitionStates.values().iterator();
			while(nextNodesSet.hasNext()){
				//Get Iterator for each NFANode in the HashSet
				
				thisNodeTransitions.addAll(nextNodesSet.next());
			}
			Iterator<NFANode> nextNodes = thisNodeTransitions.iterator();		
			while(nextNodes.hasNext()){
				getNodes( nextNodes.next(),set);
			}
		}
		
	}
	
	
}
