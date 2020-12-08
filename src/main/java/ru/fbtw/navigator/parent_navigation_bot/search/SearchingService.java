package ru.fbtw.navigator.parent_navigation_bot.search;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.fbtw.navigator.parent_navigation_bot.io.PathDrawer;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphNode;
import ru.fbtw.navigator.parent_navigation_bot.math.GraphSolver;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Slf4j
public class SearchingService {
    private GraphSolver solver;
    private HashMap<String, Node> nodesStorage;

    public SearchingService(HashMap<String, Node> nodesStorage, HashSet<Node> privateNodes) {
        this.nodesStorage = nodesStorage;
        solver = new GraphSolver(nodesStorage,privateNodes);

    }

    public Set<String> getNamesSet(){
        return nodesStorage.keySet();
    }

    public void update(HashMap<String, Node> nodesStorage, HashSet<Node> privateNodes){
        log.info("Updating the node system");

        this.nodesStorage = nodesStorage;
        solver = new GraphSolver(nodesStorage,privateNodes);
    }

    public boolean hasName(String text){
        return nodesStorage.containsKey(text);
    }

    public SendPhoto[] getPath(String from, String to){
        try {
            List<GraphNode> path = solver.getPath(to,from);
            PathDrawer drawer = new PathDrawer(path);
            List<BufferedImage> drawPath = drawer.drawPath();

            SendPhoto[] result = new SendPhoto[drawPath.size()];
            for (int i = 0, drawPathSize = drawPath.size(); i < drawPathSize; i++) {
                BufferedImage image = drawPath.get(i);
                InputStream imageIs = getBufferedImageIs(image);
                InputFile file = new InputFile(imageIs,"room.png");
               // File file = getFileFromBI(image);
                result[i] = new SendPhoto()
                        .setPhoto(file);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private InputStream getBufferedImageIs(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"png",outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        return inputStream;
    }

    private File getFileFromBI(BufferedImage image) throws IOException{
        String filename = "tmp\\"+ UUID.randomUUID().toString()
                .replaceAll("-","") +".png";
        File file = new File(filename);
        ImageIO.write(image,"png",file);
        return file;
    }

}
