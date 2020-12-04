package ru.fbtw.navigator.parent_navigation_bot.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fbtw.navigator.parent_navigation_bot.io.GraphJsonParser;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

class GraphSolverSetupTest {
	private HashMap<String, Node> test0;
	private HashMap<String, Node> test1;


	@BeforeEach
	void setUp() {
		try {
			String pathname0 = "test_dir/serialize_test.json";
			File testFile0 = new File(pathname0);
			GraphJsonParser parser0 = new GraphJsonParser(testFile0);
			test0 = parser0.parse();

			String pathname1 = "test_dir/serialize_test1.json";
			File testFile1 = new File(pathname1);
			GraphJsonParser parser1 = new GraphJsonParser(testFile1);
			test1 = parser1.parse();
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	void testSecurity0() {
		Assertions.assertEquals(3, test0.size());
		GraphSolver solver = new GraphSolver(test0);
		Assertions.assertTrue(solver.testSecurity());
	}

	@Test
	void testSecurity1() {
		Assertions.assertEquals(8, test1.size());
		GraphSolver solver = new GraphSolver(test1);
		Assertions.assertTrue(solver.testSecurity());
	}


	@Test
	void testPath(){

	}

}