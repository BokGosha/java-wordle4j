package ru.yandex.practicum;

import ru.yandex.practicum.exception.EmptyHintsException;
import ru.yandex.practicum.exception.RepeatWordException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;
    private final int steps;
    private final WordleDictionary dictionary;
    private int userSteps = 1;
    private GameState gameState;

    private int hintIndex;
    private List<String> hints = new ArrayList<>();

    private final Map<String, String> userAnswers = new HashMap<>();
    private int userAnswersSize = 0;

    public WordleGame(String answer, int steps, WordleDictionary dictionary) {
        this.answer = answer;
        this.steps = steps;
        this.dictionary = dictionary;
    }

    public String getAnswer(String userAnswer) throws WordNotFoundInDictionaryException, RepeatWordException {
        if (userAnswer.equals(answer)) {
            return setState(GameState.WON);
        }

        if (userSteps == steps) {
            return setState(GameState.LOST);
        }

        userSteps++;

        if (!dictionary.containsWord(userAnswer)) {
            throw new WordNotFoundInDictionaryException("Слово " + userAnswer + " не найдено в словаре");
        }

        return setHint(userAnswer);
    }

    private String setState(GameState gameState) {
        this.gameState = gameState;

        return handleGameState();
    }

    private String setHint(String userAnswer) throws RepeatWordException {
        StringBuilder helpLine = new StringBuilder();

        for (int i = 0; i < userAnswer.length(); i++) {
            if (userAnswer.charAt(i) == answer.charAt(i)) {
                helpLine.append("+");
            } else if (answer.indexOf(userAnswer.charAt(i)) != -1) {
                helpLine.append("^");
            } else {
                helpLine.append("-");
            }
        }

        if (userAnswers.get(userAnswer) != null) {
            throw new RepeatWordException("Вы уже вводили такое слово");
        }

        userAnswers.put(userAnswer, helpLine.toString());

        return helpLine.toString();
    }

    public List<String> getHintWords() {
        Set<Character> excludedChars = new HashSet<>();
        Set<Character> requiredChars = new HashSet<>();
        Map<Integer, Character> fixedChars = new HashMap<>();
        for (Map.Entry<String, String> entry : userAnswers.entrySet()) {
            excludedChars.addAll(listSymbols(entry.getKey(), entry.getValue(), '-'));
            requiredChars.addAll(listSymbols(entry.getKey(), entry.getValue(), '^'));
            fixedChars.putAll(fixedPositions(entry.getKey()));
        }

        return dictionary.getWords(excludedChars, requiredChars, fixedChars);
    }

    public String getHint() throws EmptyHintsException {
        if (hints.isEmpty() || userAnswers.size() > userAnswersSize) {
            hintIndex = -1;
            hints = getHintWords();
            userAnswersSize = userAnswers.size();
        }

        hintIndex++;

        if (hintIndex >= hints.size()) {
            throw new EmptyHintsException("Подсказки закончились");
        }

        return hints.get(hintIndex);
    }

    public GameState getGameState() {
        return gameState;
    }

    private List<Character> listSymbols(String answer, String hint, char symbol) {
        List<Character> list = new ArrayList<>();

        for (int i = 0; i < answer.length(); i++) {
            if (hint.charAt(i) == symbol) {
                list.add(answer.charAt(i));
            }
        }

        return list;
    }

    private Map<Integer, Character> fixedPositions(String userAnswer) {
        Map<Integer, Character> fixedPositions = new HashMap<>();
        for (int i = 0; i < userAnswer.length(); i++) {
            if (userAnswer.charAt(i) == answer.charAt(i)) {
                fixedPositions.put(i, answer.charAt(i));
            }
        }

        return fixedPositions;
    }

    private String handleGameState() {
        return switch (gameState) {
            case LOST -> "Вы проиграли. Правильный ответ: " + answer;
            case WON -> "Вы выиграли. Количество попыток: " + userSteps;
        };
    }
}
