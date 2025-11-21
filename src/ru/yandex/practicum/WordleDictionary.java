package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        prepare(words);
    }

    public List<String> getWords() {
        return words;
    }

    private void prepare(List<String> words) {
        List<String> dictionary = new ArrayList<>();

        for (String word : words) {
            if (word.length() == 5) {
                word = word.toLowerCase().replace("ё", "е");
                dictionary.add(word);
            }
        }

        this.words = dictionary;
    }

    public boolean containsWord(String word) {
        return words.contains(word);
    }

    public List<String> getWords(
            Set<Character> excludedChars,
            Set<Character> requiredChars,
            Map<Integer, Character> fixedChars
    ) {
        List<String> dictionary = new ArrayList<>();
        for (String word : words) {
            if (excludedCharsFound(excludedChars, word) || !allRequiredCharsFound(requiredChars, word)) {
                continue;
            }

            if (fixedCharsFound(fixedChars, word)) {
                dictionary.add(word);
            }
        }

        return dictionary;
    }

    private boolean excludedCharsFound(Set<Character> excludedChars, String word) {
        boolean excludedFound = false;
        for (Character character : excludedChars) {
            if (word.indexOf(character) != -1) {
                excludedFound = true;
                break;
            }
        }

        return excludedFound;
    }

    private boolean allRequiredCharsFound(Set<Character> requiredChars, String word) {
        boolean allRequiredFound = true;
        for (Character character : requiredChars) {
            if (word.indexOf(character) == -1) {
                allRequiredFound = false;
                break;
            }
        }

        return allRequiredFound;
    }

    private boolean fixedCharsFound(Map<Integer, Character> fixedChars, String word) {
        boolean positionsMatch = true;
        for (Map.Entry<Integer, Character> entry : fixedChars.entrySet()) {
            int position = entry.getKey();
            char expectedChar = entry.getValue();

            if (word.charAt(position) != expectedChar) {
                positionsMatch = false;
                break;
            }
        }

        return positionsMatch;
    }
}
