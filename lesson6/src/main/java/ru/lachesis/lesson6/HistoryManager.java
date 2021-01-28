package ru.lachesis.lesson6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryManager {

    private final int MESSAGES_TO_OUTPUT = 10;

    String login;
    String historyFileName;
    List<String> messageTexts = new ArrayList<>();

    HistoryManager(String login) {
        this.login = login;
        historyFileName = "history\\history_" + login + ".txt";
    }

    public List<String> readHistory() {
        Path filePath = Paths.get(historyFileName);
        try {
            return messageTexts = getMessageTexts(filePath);
            } catch (IOException e) {
            e.printStackTrace();
            return messageTexts ;
        }
    }

    private List<String> getMessageTexts(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            List<String> messageTexts = Files.readAllLines(filePath);
            if (messageTexts.size() > MESSAGES_TO_OUTPUT)
                return messageTexts.stream().skip(messageTexts.size() - MESSAGES_TO_OUTPUT).limit(MESSAGES_TO_OUTPUT).collect(Collectors.toList());
            else if (messageTexts.size() > 0)
                return messageTexts;
        } else Files.createFile(filePath);
        return null;
    }

    public void saveHistory(Message message) {
        Path historyPath = Paths.get(historyFileName);
        if (!Files.exists(historyPath.getParent()))
            createHistoryDirectory(historyPath.getParent());
        try {
            Files.write(Path.of(historyFileName), (message.text + "\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHistoryDirectory(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}