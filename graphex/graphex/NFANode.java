package graphex;

import java.util.*;

public class NFANode {

	public final static String EPSILON = "\u03B5";
	
	public String name = "";
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
		//Adds epsilon transition from current accept state
		//to the current start state
		
		NFANode newAccept = new NFANode();
		newAccept.acceptState = true;
		
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
				set.add(newAccept);
				next.acceptState=false;
			}
		}
		
		//Adds new 
		NFANode newStart = new NFANode(EPSILON, starStart);
		HashSet<NFANode> set = newStart.transitionStates.get(EPSILON);
		set.add(newAccept);
		
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
		
		NFANode newAccept = new NFANode();
		newAccept.acceptState = true;
		Iterator<NFANode> nodeA = getNFANodes(fragA).iterator();
		while(nodeA.hasNext()){
			NFANode next = nodeA.next();
			if(next.acceptState){
				HashSet<NFANode> setA = next.transitionStates.get(EPSILON);
				if(setA==null){
					setA = new HashSet<NFANode>();
					next.transitionStates.put(EPSILON, setA);
				}
				setA.add(newAccept);
				next.acceptState=false;
			}
		}
		
		Iterator<NFANode> nodeB = getNFANodes(fragB).iterator();
		while(nodeB.hasNext()){
			NFANode next = nodeB.next();
			if(next.acceptState){
				HashSet<NFANode> setB = next.transitionStates.get(EPSILON);
				if(setB==null){
					setB = new HashSet<NFANode>();
					next.transitionStates.put(EPSILON, setB);
				}
				setB.add(newAccept);
				next.acceptState=false;
			}
		}
		
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
	
	public static String printNFA(NFANode head){
		//Label all nodes
		//Get accept nodes and make double circle
		String acceptStates = "";
		
		String transitions = "";
		int i = 0;
		Iterator<NFANode> node = getNFANodes(head).iterator();
		while(node.hasNext()){
			NFANode next = node.next();
			next.name = "q" + i;
			i++;
			if(next.acceptState){
				acceptStates = acceptStates + next.name + ", ";
			}
		}
		
		acceptStates = acceptStates.substring(0, acceptStates.length()-2) + ";";
		//System.out.println(acceptStates);
		
		//Traverse NFA add all transitions with label
		transitions = "\"\" -> " + head.name + ";\n";
		Set<NFANode> nodeSet = new HashSet<NFANode>();
		transitions = getTransitions(head, nodeSet, transitions);
		
		//Build complete dot file
		String output = "";
		output += "digraph NFA { \n";
		output += "rankdir=LR; \n";
		output += "node [ shape = none]; \"\"; \n";
		output += "node [ shape = doublecircle]; " + acceptStates + " \n";
		output += "node [ shape = circle];\n";
		output += transitions;
		output += "}";
		
		return output;
	}
	
	//Helper Method for getNFANodes
	//Should only be called within 
	private static String getTransitions(NFANode node, Set<NFANode> set, String builtTransitions){
		if( set.add(node)){
			Set<NFANode> thisNodeTransitions = new HashSet<NFANode>();
			//Gets Iterator for each Key in HashMap
			Iterator<String> keySet = node.transitionStates.keySet().iterator();
			while(keySet.hasNext()){
				String key = keySet.next();
				Iterator<NFANode> keyTransitions = node.transitionStates.get(key).iterator();
				while(keyTransitions.hasNext()){
					NFANode nodeTransition = keyTransitions.next();
					
					builtTransitions = builtTransitions + node.name + " -> " + nodeTransition.name + " [ label = \"" + key + "\" ];\n";
					thisNodeTransitions.add(nodeTransition);
				}
				
				//thisNodeTransitions.addAll(keySet.next());
			}
			Iterator<NFANode> nextNodes = thisNodeTransitions.iterator();		
			while(nextNodes.hasNext()){
				builtTransitions = getTransitions( nextNodes.next(),set,builtTransitions);
			}
		}
			return builtTransitions;
		}
	
	
	private Set<NFANode> move(NFANode node, String symbol){
		return node.transitionStates.get(symbol);
	}
	
	public Set<NFANode> epsilonClosure(NFANode node){
		return node.transitionStates.get(EPSILON);
	}
	
	public static Set<NFANode> epsilonClosure(Set<NFANode> nodes){
		Deque<NFANode> statesClosure = new ArrayDeque<NFANode>();
		statesClosure.addAll(nodes);
		
		Set<NFANode> result = nodes;
		
		while(!statesClosure.isEmpty()){
			NFANode currNode = statesClosure.pop();
			
			if(currNode.transitionStates.containsKey(EPSILON)){
				Iterator<NFANode> epsilonTransitions = currNode.transitionStates.get(EPSILON).iterator();
				while(epsilonTransitions.hasNext()){
					NFANode transition = epsilonTransitions.next();
					if(result.add(transition)){
						statesClosure.push(transition);
					}
				}
			}
		}
		
		return result;
	}
	
}
