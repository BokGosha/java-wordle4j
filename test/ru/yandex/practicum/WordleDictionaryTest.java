package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList(
                "кот",
                "дом",
                "лес",
                "привет",
                "яблоко",
                "слово",
                "книга",
                "тёрка",
                "ТЕСТЪ",
                "шалаш"
        );
        dictionary = new WordleDictionary(words);
    }

    @Test
    void testPrepareFiltersWordsByLength() {
        List<String> actualWords = dictionary.getWords();
        assertEquals(5, actualWords.size());
        assertTrue(actualWords.contains("слово"));
        assertTrue(actualWords.contains("книга"));
        assertTrue(actualWords.contains("терка"));
        assertTrue(actualWords.contains("тестъ"));
        assertTrue(actualWords.contains("шалаш"));
    }

    @Test
    void testContainsWordFound() {
        assertTrue(dictionary.containsWord("слово"));
        assertTrue(dictionary.containsWord("терка"));
    }

    @Test
    void testContainsWordNotFound() {
        assertFalse(dictionary.containsWord("кот"));
        assertFalse(dictionary.containsWord("привет"));
        assertFalse(dictionary.containsWord("неизвестное"));
    }

    @Test
    void testContainsWordCaseSensitive() {
        assertTrue(dictionary.containsWord("тестъ"));
        assertFalse(dictionary.containsWord("ТЕСТЪ"));
    }

    @Test
    void testGetWordsWithExcludedChars() {
        Set<Character> excluded = new HashSet<>(Arrays.asList('с', 'к'));

        List<String> result = dictionary.getWords(excluded, Collections.emptySet(), Collections.emptyMap());

        assertEquals(1, result.size());
        assertEquals("шалаш", result.get(0));
    }

    @Test
    void testGetWordsWithRequiredChars() {
        Set<Character> required = new HashSet<>(Arrays.asList('к', 'а'));

        List<String> result = dictionary.getWords(Collections.emptySet(), required, Collections.emptyMap());

        assertEquals(2, result.size());
        assertTrue(result.contains("терка"));
        assertTrue(result.contains("книга"));
    }

    @Test
    void testGetWordsWithFixedChars() {
        Map<Integer, Character> fixed = new HashMap<>();
        fixed.put(0, 'т');

        List<String> result = dictionary.getWords(Collections.emptySet(), Collections.emptySet(), fixed);

        assertEquals(2, result.size());
        assertEquals("терка", result.get(0));
        assertEquals("тестъ", result.get(1));
    }

    @Test
    void testGetWordsCombinedFilters() {
        Set<Character> excluded = new HashSet<>(List.of('с'));
        Set<Character> required = new HashSet<>(List.of('а'));
        Map<Integer, Character> fixed = new HashMap<>();
        fixed.put(4, 'ш');

        List<String> result = dictionary.getWords(excluded, required, fixed);

        assertEquals(1, result.size());
        assertEquals("шалаш", result.get(0));
    }
}
