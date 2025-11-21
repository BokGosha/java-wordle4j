package ru.yandex.practicum;

import ru.yandex.practicum.exception.EmptyFileException;
import ru.yandex.practicum.exception.InvalidFileFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public WordleDictionary loadWordleDictionary(String filePath) throws EmptyFileException
            , InvalidFileFormatException
            , IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            validateFormatFile(filePath);
            validateFileContent(filePath);

            List<String> words = new ArrayList<>();

            while (br.ready()) {
                String word = br.readLine();
                words.add(word);
            }

            return new WordleDictionary(words);
        }
    }

    private void validateFormatFile(String filePath) throws InvalidFileFormatException {
        if (!filePath.endsWith(".txt")) {
            throw new InvalidFileFormatException(filePath + " (Неверный формат файла)");
        }
    }

    private void validateFileContent(String filePath) throws EmptyFileException {
        Path path = Paths.get(filePath);

        if (path.toFile().length() == 0) {
            throw new EmptyFileException(filePath + " (Файл пустой)");
        }
    }
}
