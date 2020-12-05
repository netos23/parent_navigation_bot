package ru.fbtw.navigator.parent_navigation_bot.search;

import lombok.extern.slf4j.Slf4j;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphSolver;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import java.util.HashMap;
import java.util.Set;

@Slf4j
public class SearchingService {
    private GraphSolver solver;
    private HashMap<String, Node> nodesStorage;

    public SearchingService(HashMap<String, Node> nodesStorage) {
        this.nodesStorage = nodesStorage;
        solver = new GraphSolver(nodesStorage);

        if(solver.isSecure()){
            log.warn("The node system is not closed. This can lead to errors");
        }
    }

    public Set<String> getNamesSet(){
        return nodesStorage.keySet();
    }

    public void update(HashMap<String, Node> nodesStorage){
        log.info("Updating the node system");

        this.nodesStorage = nodesStorage;
        solver = new GraphSolver(nodesStorage);

        if(solver.isSecure()){
            log.warn("The node system is not closed. This can lead to errors");
        }
    }

}
