package ru.fbtw.navigator.parent_navigation_bot.math;

import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;
import ru.fbtw.navigator.parent_navigation_bot.navigation.NodeType;

import java.util.*;

public class GraphSolver {
	private boolean isSecure;
	private HashMap<String, Node> nodesStorage;

	public GraphSolver(boolean isSecure, HashMap<String, Node> nodesStorage) {
		this.nodesStorage = nodesStorage;
		testSecurity();
	}

	private static <T> T getFirstFormSet(Set<T> collection) {
		return collection.iterator().next();
	}

	public boolean testSecurity() {
		HashSet<Node> destinations = new HashSet<>(nodesStorage.values());

		LinkedList<Node> queue = new LinkedList<>();
		queue.add(getFirstFormSet(destinations));

		while (!queue.isEmpty()) {
			Node el = queue.pollFirst();
			if (el != null &&( el.getType() == NodeType.TEMP || destinations.remove(el))) {
				queue.addAll(el.getNeighbours());
			}
		}

		return (isSecure = destinations.isEmpty());
	}

	public LinkedHashSet<Node> getPath(String targetName, String startName){
		Node target = nodesStorage.get(targetName);
		Node start = nodesStorage.get(startName);

		if(target != null && start != null){
			return searchPath(target,start);
		}else{
			return null;
		}
	}

	private LinkedHashSet<Node> searchPath(Node target, Node start) {
		return null;
	}

	public boolean isSecure() {
		return isSecure;
	}
}
