package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.exception.EmptyFileException;
import ru.yandex.practicum.exception.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordleDictionaryLoaderTest {

    private WordleDictionaryLoader loader;
    private static Path tempDir;

    @BeforeAll
    static void setTempDir() throws IOException {
        tempDir = Files.createTempDirectory("wordle_test");
    }

    @BeforeEach
    void setUp() {
        loader = new WordleDictionaryLoader();
    }

    @Test
    void testLoadValidDictionaryFile() throws Exception {
        Path file = tempDir.resolve("dict.txt");
        List<String> expectedWords = List.of("шалаш", "весна", "осень");

        Files.write(file, expectedWords);

        WordleDictionary dictionary = loader.loadWordleDictionary(file.toString());

        assertEquals(expectedWords, dictionary.getWords());
    }

    @Test
    void testLoadFileWithEmptyLinesIgnored() throws Exception {
        Path file = tempDir.resolve("with_empty.txt");
        List<String> lines = List.of("шалаш", "", "весна", "  ", "осень");

        Files.write(file, lines);

        WordleDictionary dictionary = loader.loadWordleDictionary(file.toString());

        assertEquals(List.of("шалаш", "весна", "осень"), dictionary.getWords());
    }

    @Test
    void testThrowInvalidFileFormatWhenNotTxt() throws IOException {
        Path invalidPath = tempDir.resolve("dict.csv");
        Files.createFile(invalidPath);

        assertThrows(InvalidFileFormatException.class, () -> {
            loader.loadWordleDictionary(invalidPath.toString());
        });
    }

    @Test
    void testThrowEmptyFileExceptionWhenFileEmpty() throws IOException {
        Path emptyFile = tempDir.resolve("empty.txt");
        Files.createFile(emptyFile);

        assertThrows(EmptyFileException.class, () -> {
            loader.loadWordleDictionary(emptyFile.toString());
        });
    }
}
