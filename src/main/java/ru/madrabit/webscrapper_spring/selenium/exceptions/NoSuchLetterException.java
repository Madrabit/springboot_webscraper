package ru.madrabit.webscrapper_spring.selenium.exceptions;

/**
 * Custom exception, when tests Letter not exists.
 */
public class NoSuchLetterException extends Exception {
    public NoSuchLetterException(String errorMessage) {
        super(errorMessage);
    }
}
