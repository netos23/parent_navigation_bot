package ru.fbtw.navigator.parent_navigation_bot.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

class GraphJsonParserTest {

	private File test0;
	private File test1;

	@BeforeEach
	void setUp() {
		String pathname0 = "test_dir/serialize_test.json";
		test0 = new File(pathname0);

		String pathname1 = "test_dir/serialize_test1.json";
		test1 = new File(pathname1);

	}

	@Test
	void parseFile0() {
		try {
			GraphJsonParser parser = new GraphJsonParser(test0);
			HashMap<String, Node> res = parser.parse(new HashSet<>());
			Assertions.assertEquals(3, res.size());
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail(e.getMessage());
		}

	}

	@Test
	void parseFile1() {
		try {
			GraphJsonParser parser = new GraphJsonParser(test1);
			HashMap<String, Node> res = parser.parse(new HashSet<>());
			Assertions.assertEquals(8, res.size());
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail(e.getMessage());
		}

	}
}