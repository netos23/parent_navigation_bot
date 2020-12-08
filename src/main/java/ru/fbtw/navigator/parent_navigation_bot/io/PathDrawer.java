package ru.fbtw.navigator.parent_navigation_bot.io;


import lombok.extern.slf4j.Slf4j;
import ru.fbtw.navigator.parent_navigation_bot.math.Edge;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphNode;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Level;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class PathDrawer {

	private List<GraphNode> path;
	private LinkedHashMap<Level, Graphics2D> scenes;
	private ArrayList<BufferedImage> images;
	private Level editLevel;

	public PathDrawer(List<GraphNode> path) {
		this.path = path;
		scenes = new LinkedHashMap<>();
		images = new ArrayList<>();
	}

	public ArrayList<BufferedImage> drawPath() throws IOException {
		buildScenes();
		for (Graphics2D g2d : scenes.values()) {
			g2d.dispose();
		}
		return images;
	}

	private void buildScenes() throws IOException {
		editLevel = null;
		if(path == null){
			log.info("Nothing is drawn because the path is empty");
			return;
		}
		for (int i = path.size() - 2; i >= 0; i--) {
			GraphNode prevGraphNode = path.get(i + 1);
			GraphNode currentGraphNode = path.get(i);

			Edge edge = currentGraphNode.getConnection(prevGraphNode);
			if (!edge.getLevel().equals(editLevel)) {
				addScene(edge.getLevel());
				editLevel = edge.getLevel();
			}

			Node prev = prevGraphNode.getBaseNode();
			Node current = currentGraphNode.getBaseNode();

			Graphics2D g2d = getCanvasStorage();

			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(10.0f));
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2d.drawLine(
					prev.getX(editLevel),
					prev.getY(editLevel),
					current.getX(editLevel),
					current.getY(editLevel)
			);
		}
	}

	private Graphics2D getCanvasStorage() {
		return scenes.get(editLevel);
	}


	private void addScene(Level level) throws IOException {
		BufferedImage image = ImageUtils.imageFromBase64(level.getImage());
		images.add(image);
		scenes.put(level, image.createGraphics());
	}
}
