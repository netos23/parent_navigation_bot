package ru.fbtw.navigator.parent_navigation_bot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import ru.fbtw.navigator.parent_navigation_bot.io.GraphJsonParser;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;
import ru.fbtw.navigator.parent_navigation_bot.response.BaseResponse;
import ru.fbtw.navigator.parent_navigation_bot.response.Response;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@RestController
public class SettingsController {

	private SearchingService searchingService;

	public SettingsController(SearchingService searchingService) {
		this.searchingService = searchingService;
	}

	@RequestMapping(value = "/api/update", method = RequestMethod.POST)
	public BaseResponse update(
			@RequestBody String updateData
	) {

		HashMap<String, Node> nodesStorage;
		HashSet<Node> privateNodes = new HashSet<>();
		try {
			GraphJsonParser parser = new GraphJsonParser(updateData);
			nodesStorage = parser.parse(privateNodes);

			searchingService.update(nodesStorage, privateNodes);
			//File newEnv = new File("default_env_x.json");
			File newEnv = new File("default_env.json");
			FileUtils.write(newEnv,updateData,"UTF-8");

			return new Response("ok", 200);
		} catch (FileNotFoundException e) {
			log.error("Failed to load the default configuration");
		} catch (IOException e) {
			log.error("The default configuration file is corrupted");
		}

		return new Response("Error", 500);
	}
}
