package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.exception.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList("кошка", "мышка", "крыша", "книга", "астра", "пушка", "порча", "ложка");
        dictionary = new WordleDictionary(words);

        game = new WordleGame("кошка", 6, dictionary);
    }

    @Test
    void testWinOnFirstTry() throws Exception {
        String result = game.getAnswer("кошка");

        assertEquals(GameState.WON, game.getGameState());
        assertEquals("Вы выиграли. Количество попыток: 1", result);
    }

    @Test
    void testLoseOnLastTry() throws Exception {
        game.getAnswer("мышка");
        game.getAnswer("астра");
        game.getAnswer("порча");
        game.getAnswer("книга");
        game.getAnswer("пушка");

        String result = game.getAnswer("крыша");

        assertEquals(GameState.LOST, game.getGameState());
        assertEquals("Вы проиграли. Правильный ответ: кошка", result);
    }

    @Test
    void testCannotExceedMaxSteps() throws Exception {
        game.getAnswer("мышка");
        game.getAnswer("астра");
        game.getAnswer("порча");
        game.getAnswer("книга");
        game.getAnswer("пушка");
        game.getAnswer("крыша");
        game.getAnswer("ложка");

        assertEquals(GameState.LOST, game.getGameState());
    }

    @Test
    void testHintGenerationWithMatch() throws Exception {
        String hint = game.getAnswer("крыша");
        assertEquals("+--^+", hint);
    }

    @Test
    void testWordNotFoundInDictionary() {
        assertThrows(WordNotFoundInDictionaryException.class, () -> {
            game.getAnswer("слон");
        });
    }

    @Test
    void testRepeatWordException() throws Exception {
        game.getAnswer("мышка");

        assertThrows(RepeatWordException.class, () -> {
            game.getAnswer("мышка");
        });
    }

    @Test
    void testEmptyHintsException() throws Exception {
        game.getHint();
        game.getHint();
        game.getHint();
        game.getHint();
        game.getHint();
        game.getHint();
        game.getHint();
        game.getHint();

        assertThrows(EmptyHintsException.class, () -> {
            game.getHint();
        });
    }

    @Test
    void testGetHintWordsAfterOneGuess() throws Exception {
        game.getAnswer("мышка");

        List<String> hints = game.getHintWords();
        assertTrue(hints.contains("кошка"));
    }

    @Test
    void testGetHintWordsAfterMultipleGuesses() throws Exception {
        game.getAnswer("мышка");
        game.getAnswer("крыша");

        List<String> hints = game.getHintWords();

        assertTrue(hints.contains("кошка"));
        assertFalse(hints.contains("мышка"));
        assertFalse(hints.contains("крыша"));
    }
}
