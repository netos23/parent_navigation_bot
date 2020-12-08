package ru.fbtw.navigator.parent_navigation_bot.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ResourceDownloader {
    private static final String FILE_PATH_URL
            = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    private static final String FILE_URL
            = "https://api.telegram.org/file/bot%s/%s";
    private final String botToken;
    private final OkHttpClient httpClient;


    public ResourceDownloader(String botToken) {
        this.botToken = botToken;

        httpClient = new OkHttpClient().newBuilder()
                .build();
    }

    public byte[] downloadFileById(String fileId) {
        String path = getFilePath(fileId);
        if (path != null) {
            log.info("File id successfully delivered");
            return getFile(path);
        }

        return null;
    }

    private byte[] getFile(String path) {
        log.info("Downloading file for path: {}", path);
        try {
            String url = getGetFileUrl(path);

            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();

            Response response = httpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            Objects.requireNonNull(responseBody);

            return extractFile(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Unable to get file for path: {} with exception: {}", path, e);
        } catch (NullPointerException e) {
            log.error("Exception while getting file: response hasn't body");
        }
        return null;
    }

    private byte[] extractFile(ResponseBody responseBody) throws IOException {
        log.info("Extracting file");
        return responseBody.bytes();
    }

    public String getFilePath(String fileId) {
        log.info("Started getting path for file id: {}", fileId);
        try {
            String url = getGetFilePathUrl(fileId);

            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();

            Response response = httpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            Objects.requireNonNull(responseBody);

            return extractFilePath(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Unable to get file path for id: {} with exception: {}", fileId, e);
        } catch (NullPointerException e) {
            log.error("Exception while getting file path: response hasn't body");
        }

        return null;
    }

    private String extractFilePath(ResponseBody body) throws IOException {
        String bodyJson = body.string();
        JsonObject rootBody = JsonParser.parseString(bodyJson).getAsJsonObject();
        JsonObject resultJson = rootBody.getAsJsonObject("result");
        return resultJson.get("file_path").getAsString();
    }

    private String getGetFilePathUrl(String fileId) {
        return String.format(FILE_PATH_URL, botToken, fileId);
    }

    private String getGetFileUrl(String filePath) {
        return String.format(FILE_URL, botToken, filePath);
    }
}
