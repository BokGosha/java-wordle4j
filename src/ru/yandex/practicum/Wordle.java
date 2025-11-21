package ru.yandex.practicum;

import ru.yandex.practicum.exception.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private static PrintWriter log;
    private static WordleGame wordleGame;

    private static void run() throws InvalidFileFormatException, EmptyFileException, IOException {
        log = new Logger().getWriter();
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        WordleDictionary wordleDictionary = wordleDictionaryLoader.loadWordleDictionary("words_ru.txt");
        wordleGame = new WordleGame("шалаш", 6, wordleDictionary);
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            run();

            while (wordleGame.getGameState() != GameState.WON && wordleGame.getGameState() != GameState.LOST) {
                try {
                    System.out.println("Введите слово:");
                    String word = sc.nextLine();

                    if (word.isEmpty()) {
                        String hint = wordleGame.getHint();
                        System.out.println("Подсказка: " + hint);
                    } else {
                        validateWord(word);

                        String answer = wordleGame.getAnswer(word.toLowerCase().replace("ё", "е"));

                        System.out.println(answer);
                    }
                } catch (InvalidWordException
                         | WordNotFoundInDictionaryException
                         | RepeatWordException
                         | EmptyHintsException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        } catch (IOException | InvalidFileFormatException | EmptyFileException exception) {
            log.println(exception.getMessage());
            System.out.println("Ошибка запуска игры");
        }
    }

    private static void validateWord(String word) throws InvalidWordException {
        if (word.length() != 5 || word.matches("[a-zA-Z]+")) {
            throw new InvalidWordException("Неверно введено слово");
        }
    }
}
