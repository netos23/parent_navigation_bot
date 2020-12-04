package ru.fbtw.navigator.parent_navigation_bot.math;

import ru.fbtw.navigator.parent_navigation_bot.navigation.Level;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.util.ArrayList;
import java.util.Collections;

public class GraphNode {
	private Node baseNode;
	private ArrayList<Edge> connections;
	private double destination;
	private GraphNode prev;
	private boolean isFinal;
	private Level level;

	public GraphNode(Node baseNode) {
		this.baseNode = baseNode;
		connections = new ArrayList<>();
	}

	public void applyConnections(){
		Collections.sort(connections);
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean aFinal) {
		isFinal = aFinal;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setDestination(double destination) {
		this.destination = destination;
	}

	public Node getBaseNode() {
		return baseNode;
	}

	public ArrayList<Edge> getConnections() {
		return connections;
	}

	public double getDestination() {
		return destination;
	}

	public GraphNode getPrev() {
		return prev;
	}

	public void setPrev(GraphNode prev) {
		this.prev = prev;
	}
}
