package ru.fbtw.navigator.parent_navigation_bot.io;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphNode;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphSolver;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PathDrawerTest {

	private List<GraphNode> test0;
	private List<GraphNode> test1;

	@BeforeEach
	void setUp() throws Exception {
		String pathname0 = "test_dir/serialize_test2.json";
		File testFile0 = new File(pathname0);
		GraphJsonParser parser0 = new GraphJsonParser(testFile0);
		HashMap<String, Node> testData0 = parser0.parse();
		GraphSolver solver0 = new GraphSolver(testData0);
		test0 = solver0.getPath("Node 0","Node 6");

		String pathname1 = "test_dir/serialize_test1.json";
		File testFile1 = new File(pathname1);
		GraphJsonParser parser1 = new GraphJsonParser(testFile1);
		HashMap<String,Node> testData1 = parser1.parse();
		GraphSolver solver1 = new GraphSolver(testData1);
		test1 = solver0.getPath("Node 0", "Node 4");
	}

	@Test
	void drawPath0() throws IOException {
		PathDrawer drawer = new PathDrawer(test0);

		ArrayList<BufferedImage> images = drawer.drawPath();
		Assertions.assertTrue(images.size() > 0);

		for(BufferedImage imageView : images){
			ImageUtils.write(imageView);
		}
	}

	@Test
	void drawPath1() throws IOException {
		PathDrawer drawer = new PathDrawer(test1);

		ArrayList<BufferedImage> images = drawer.drawPath();
		Assertions.assertTrue(images.size() > 0);

		for(BufferedImage imageView : images){
			ImageUtils.write(imageView);
		}
	}
}