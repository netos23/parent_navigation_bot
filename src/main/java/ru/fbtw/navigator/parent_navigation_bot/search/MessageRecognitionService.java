package ru.fbtw.navigator.parent_navigation_bot.search;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class MessageRecognitionService {
    private final RecognitionConfig config;
    private final SpeechClient speechClient;

    public MessageRecognitionService() throws IOException {
        config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.OGG_OPUS)
                .setSampleRateHertz(48000)
                .setLanguageCode("ru-RU")
                .build();

        speechClient = SpeechClient.create();
    }

    public String recognize(byte[] audioBytes) {
        log.info("Recognition of voice message begun");
        ByteString byteString = ByteString.copyFrom(audioBytes);

        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(byteString)
                .build();

        RecognizeResponse response = speechClient.recognize(config, audio);

        return parseResultList(response.getResultsList());
    }

    private String parseResultList(List<SpeechRecognitionResult> resultsList) {
        StringBuilder builder = new StringBuilder();
        for (SpeechRecognitionResult result : resultsList) {
            //builder.append(result.getAlternatives(0).getTranscript());
            result.getAlternativesList().forEach(alternative ->
                    builder.append(alternative.getTranscript())
            );

        }

        log.info("Text of recognition: {}",builder.toString());
        return builder.toString();
    }
}
