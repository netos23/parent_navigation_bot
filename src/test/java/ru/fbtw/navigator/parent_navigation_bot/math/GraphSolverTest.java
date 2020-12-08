package ru.fbtw.navigator.parent_navigation_bot.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fbtw.navigator.parent_navigation_bot.io.GraphJsonParser;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GraphSolverTest {

	private GraphSolver solver0;
	private GraphSolver solver1;

	@BeforeEach
	void setUp() {
		try {
			String pathname0 = "test_dir/serialize_test.json";
			File testFile0 = new File(pathname0);
			GraphJsonParser parser0 = new GraphJsonParser(testFile0);
			HashSet<Node> unNodes = new HashSet<>();
			HashMap<String,Node> test0 = parser0.parse(unNodes);
			solver0 = new GraphSolver(test0,unNodes);

			String pathname1 = "test_dir/serialize_test1.json";
			File testFile1 = new File(pathname1);
			GraphJsonParser parser1 = new GraphJsonParser(testFile1);
			HashSet<Node> unNodes1 = new HashSet<>();
			HashMap<String,Node> test1 = parser1.parse(unNodes1);
			solver1 = new GraphSolver(test1,unNodes1);
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	void getPath0() throws Exception {
		Assertions.assertEquals(2,solver0.getPath("Node 0","Node 1").size());
	}

	@Test
	void getPath1() throws Exception {
		Assertions.assertEquals(7,solver1.getPath("Node 11","Node 0").size());
	}
}