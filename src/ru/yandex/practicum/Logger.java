package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logger {

    private final String fileName = "logs.txt";
    private final PrintWriter writer;

    public Logger() throws IOException {
        Path logFilePath = Paths.get(fileName);
        if (!Files.exists(logFilePath)) {
            Files.createFile(logFilePath);
        }

        writer = new PrintWriter(new FileWriter(logFilePath.toFile(), true), true);
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
