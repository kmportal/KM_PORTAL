package com.ibm.km.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class KeyChar {

	private static final int IS_BRANCH = 0;
	private static final int IS_LEAF = 1;
	private static final int MAXSIZE = 10;
	private String key;
	private String value;
	private int state = IS_LEAF;
	private HashMap<String, KeyChar> nodes = new HashMap<String, KeyChar>();
	private boolean validWord = false;
	private static boolean rootNode = true;
	private static KeyChar root = null;
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public static int index = 0;
	

	public KeyChar(String value) {
		if( root == null ) root = this;
		index++;
		numberOfElements++;
		String key = value.substring(0, 1).toUpperCase();
		this.setKey(key);
		this.setValue(value.substring(0, 1));

		if(rootNode) {
			nodes.put(key, this);
			rootNode = false;
			}

		if(value == null || value.length() == 0) { 
			this.setKey(" ");
			return;
		}

		if(value.length() > 1) {
			addNode(value.substring(1));
			this.setState(IS_BRANCH);
		} else {
			this.setValidWord(true);
		}
	}
	
	public void addNode(String value) {
		
		if(value == null || value.length() == 0) { 
			return;
		}
		String key = value.substring(0, 1).toUpperCase();
		
		if( this.nodes.get(key) != null) { 
			KeyChar kc = this.nodes.get(key);
			if(value.length() > 1) {
				index++;
				kc.addNode(value.substring(1));
			}
			
		} else
		{ 
			KeyChar kc = new KeyChar(value);
			this.nodes.put(key, kc);
		}
	}
	
	public ArrayList<String> match(String value) {
		ArrayList<String> retValue = new ArrayList<String>();
		retValue = findInTheTree(value);
/*		ArrayList<String> match = new ArrayList<String>();
		if(retValue != null)
		for( int i = 0; retValue.size() > i && i < MAXSIZE; i++) {
			match.add(retValue.get(i).substring(1));
			}
		return match;
*/
		return retValue;
		}
	
	public ArrayList<String> findInTheTree(String value) {
		String key = value.substring(0, 1).toUpperCase();
		ArrayList<String> matchTree = null;
		if( this.nodes.get(key) != null && value.length() > 0) {
			KeyChar kc = this.nodes.get(key);
			if(value.length() == 1) {
				matchTree = kc.getNodeContent();
			}
			else {
				matchTree = kc.findInTheTree(value.substring(1));
				ArrayList<String> temp = new ArrayList<String>();
				for(int i = 0 ; matchTree != null && i < matchTree.size(); i++) {
					//matchTree.add(i, key + matchTree.get(i) );
					// matchTree.add(i, matchTree.get(i) );
					temp.add(kc.getValue() + matchTree.get(i));
				}
				matchTree = temp;
			}
		} 

		return matchTree;
	}
	
	public HashMap getNodes() {
		return nodes;
	}
	
	public ArrayList<String> getNodeContent() {
		
		Set<String> nodeList = this.nodes.keySet();
		 ArrayList<String> nodeValues = null;
		 Iterator it = (Iterator) nodeList.iterator();
			 ArrayList<String> temp = null;
			while(it.hasNext()) {
				String subKey = (String) it.next(); 
				if( subKey != null) {
					KeyChar subKc = nodes.get(subKey);
					if(subKc == this)
						continue;
					
					temp = subKc.getNodeContent();
					if(temp != null)
					for(int i = 0; i < temp.size() && i < MAXSIZE; i++) {
						// temp.set(i, key + temp.get(i).toString() );
						temp.set(i, value + temp.get(i).toString() );
					}
					if(nodeValues == null) {
						nodeValues = temp;
					} else {
						nodeValues.addAll(temp);
					}
				}
				if(nodeValues != null && nodeValues.size() > MAXSIZE)
					break;
			 }

		if(nodeValues == null) {
			nodeValues = new ArrayList<String>();
			nodeValues.add(getValue());
		}
		return nodeValues;
	}

	public boolean isValidWord() {
		return validWord;
	}

	public void setValidWord(boolean validWord) {
		this.validWord = validWord;
	}

	
	public static int numberOfElements = 1;
	public static int getNumberOfElements() {
		return numberOfElements;
	}

	public static KeyChar getRoot() {
		return root;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static void setRootNull()
	{
		root = null;
	}
}

