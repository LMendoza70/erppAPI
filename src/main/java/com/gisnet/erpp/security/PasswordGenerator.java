package com.gisnet.erpp.security;


import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
    String charactersToBeUsed;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int MIN_LENGHT = 10;
    private static final int MAX_LENGHT = 20;

    // default constructor
    public PasswordGenerator() { 
        this(CHARACTERS);
    }

    // constructor
    public PasswordGenerator(String characters) {
        this.charactersToBeUsed = characters;
    }

    public String generate() {
    	Random rn = new Random();
        return generate(rn.nextInt(MAX_LENGHT - MIN_LENGHT + 1) + MIN_LENGHT);
    }

    public String generate(int length) {
        char[] password = new char[length];

        char[] possibleCharacters = charactersToBeUsed.toCharArray();
        SecureRandom r = new SecureRandom();

        for (int i = 0; i < length; i++) {
            password[i] = possibleCharacters[r.nextInt(possibleCharacters.length)];
        }
        return new String(password);
    }
}