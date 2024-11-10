package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MyScanner {
    // Based code on the labs
    enum TOKEN { SCANEOF, ID, INTLITERAL, INTDATATYPE, DECLARE, PRINT, SET, EQUALS, IF, THEN, ENDIF, CALC, PLUS  }
    private ArrayList<String> reserved = new ArrayList<>(Arrays.asList("declare", "int", "print", "set", "if", "then", "endif", "calc"));
    private PushbackReader pbr;
    private StringBuilder buffer = new StringBuilder();

    public MyScanner(PushbackReader pbr) {
        this.pbr = pbr;
    }

    // This code is from lab but simplified (throws instead of try catch)
    private int readNextChar() throws IOException {
        return pbr.read();
    }
    // This code is also from lab
    private void unread(int c) throws IOException {
        pbr.unread(c);
    }

    public TOKEN scan() throws IOException {
        int c = readNextChar();
        buffer.setLength(0);

        // Check if entry input string
        if (c == -1) {
            System.out.println(TOKEN.SCANEOF);
            return TOKEN.SCANEOF;
        }

        while (c != -1) {
            // Check whitespace
            if (isWhitespace(c)) {
                c = readNextChar();
                continue;
            }
            // Check if digit
            if (Character.isDigit(c)) {
                buffer.append((char) c);
                c = readNextChar();
                while (Character.isDigit(c)) {
                    buffer.append((char) c);
                    c = readNextChar();
                }
                unread(c);
                System.out.println(TOKEN.INTLITERAL + " " + getTokenBufferString());
                return TOKEN.INTLITERAL;
            }
            // Check for reserved words and id, code from lab
            if (Character.isLetter(c)) {
                buffer.append((char) c);
                while (true) {
                    c = readNextChar();
                    if (!Character.isLetterOrDigit(c)) {
                        unread(c);
                        break;
                    }
                    buffer.append((char) c);
                }
                String word = buffer.toString();
                // The reserved words switch
                if (reserved.contains(word)) {
                    switch (word) {
                        case "declare":
                            System.out.println(TOKEN.DECLARE + " " + getTokenBufferString());
                            return TOKEN.DECLARE;
                        case "int":
                            System.out.println(TOKEN.INTDATATYPE + " " + getTokenBufferString());
                            return TOKEN.INTDATATYPE;
                        case "print":
                            System.out.println(TOKEN.PRINT + " " + getTokenBufferString());
                            return TOKEN.PRINT;
                        case "set":
                            System.out.println(TOKEN.SET + " " + getTokenBufferString());
                            return TOKEN.SET;
                        case "if":
                            System.out.println(TOKEN.IF + " " + getTokenBufferString());
                            return TOKEN.IF;
                        case "then":
                            System.out.println(TOKEN.THEN + " " + getTokenBufferString());
                            return TOKEN.THEN;
                        case "endif":
                            System.out.println(TOKEN.ENDIF + " " + getTokenBufferString());
                            return TOKEN.ENDIF;
                        case "calc":
                            System.out.println(TOKEN.CALC + " " + getTokenBufferString());
                            return TOKEN.CALC;
                    }
                } else {
                    System.out.println(TOKEN.ID + " " + getTokenBufferString());
                    return TOKEN.ID;
                }
            }
            // Check + or =, also based on lab code
            if (c == '+') {
                buffer.append((char) c);
                System.out.println(TOKEN.PLUS + " " + getTokenBufferString());
                return TOKEN.PLUS;
            } else if (c == '=') {
                buffer.append((char) c);
                System.out.println(TOKEN.EQUALS + " " + getTokenBufferString());
                return TOKEN.EQUALS;
            }
            c = readNextChar();
        }
        // End of File
        System.out.println(TOKEN.SCANEOF);
        return TOKEN.SCANEOF;
    }

    // Also code from lab
    private boolean isWhitespace(int c) {
        if (c == 32 || c == 9 || c == 10 || c == 13) {
            return true;
        }
        return false;
    }

    public String getTokenBufferString() {
        return buffer.toString();
    }

}
