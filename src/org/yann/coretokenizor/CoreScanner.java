package org.yann.coretokenizor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CoreScanner {

    private BufferedReader reader; // Used to read a file
    private String currLine; // Current line being tokenized
    private int cursorIndex; // Current index of the token in the line
    private int lineNum;
    private String[] currentTokens; // Tokens in the current line
    private ArrayList<String> tokens; // All tokens in the file

    public CoreScanner(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName)); // Open the file and create a reader object
        tokens = new ArrayList<String>(); // Initialize ArrayList
        tokenizeLine(); // Tokenize the first line of the file
        lineNum=1;
    }

    private void tokenizeLine() throws IOException {
        do {
            currLine = reader.readLine(); // Read the next line
            if (currLine == null) {
                currentTokens = new String[0]; // If the line is null, set the tokens to empty and return
                return;
            }
            currentTokens = currLine.trim().split("\\s+"); // Split the line into individual tokens
            for (int i =0; i < currentTokens.length; i++){
                processToken(currentTokens[i]); // Process each token
            }
            currentTokens = new String[tokens.size()]; // Set the current tokens to the size of the ArrayList
            for (int i =0; i < tokens.size(); i++){
                currentTokens[i] = tokens.get(i); // Fill current tokens with tokens from ArrayList
            }

        } while (currentTokens.length == 0); // Loop until a non-empty line is found
        cursorIndex = 0; // Reset the cursor index
        tokens = new ArrayList<String>(); // Clear the ArrayList for the next line
        lineNum++;
    }
    public void processToken(String token) {
        int tokenType = getTokenType(token);
        //add token to the arraylist if it's not an invalid token.
        if (tokenType != 34 && tokenType != -1) {
            tokens.add(token);
        } else if(tokenType == -1){
            System.out.println("Gotcha!");
            tokens.add("Invalid(" + token +")");
        }else {
            for (int i = 0; i < token.length(); i++) {
                StringBuilder sb = new StringBuilder();
                char ch = token.charAt(i);
                if (Character.isAlphabetic(ch)) { //check for identifier
                    for (int j = i; j < token.length(); j++) {
                        ch = token.charAt(j);
                        if (Character.isAlphabetic(ch) || Character.isDigit(ch)) {
                            sb.append(ch); //possible id
                        } else {
                            break;
                        }
                    }
                    tokens.add(sb.toString());
                    i += sb.length() - 1;
                }
                else if (getTokenType(String.valueOf(ch)) != 34 && i < token.length() - 1) { //check for double char ss
                    sb.append(ch);
                    sb.append(token.charAt(i + 1));
                    if (getTokenType(sb.toString()) != 34) {
                        tokens.add(sb.toString());
                        i++;
                    } else {
                        tokens.add(String.valueOf(ch));
                    }
                } else if(i < token.length() - 1){ //check for single char ss
                    sb.append(ch);
                    sb.append(token.charAt(i + 1));
                    if (getTokenType(sb.toString()) != 34) {
                        tokens.add(sb.toString());
                        i++;
                    }
                } else {
                    tokens.add(String.valueOf(ch));
                }
            }
        }
    }

    public int getToken() {
        if (currentTokens.length == 0) { //if there are no tokens left, return an invalid token
            return 33;
        }
        String tokenString = currentTokens[cursorIndex]; //get the token at the cursor index
        int token = getTokenType(tokenString); //get the type of the token using getTokenType method
        return token;
    }
    public int getLineNum() {
        return lineNum;
    }

    private int getTokenType(String tokenString) {
        if (tokenString.matches("\\d+[A-Z].*")) { // illegal token due to adjacent int/id
            return -1;
        }
        else if (tokenString.matches("\\d+")) { // integer literal
            return 31;
        }
        else if (tokenString.matches("[A-Z][A-Z0-9]*")) { // identifier
            return 32;
        }else{
            switch (tokenString) {
                case "program":
                    return 1;
                case "begin":
                    return 2;
                case "end":
                    return 3;
                case "int":
                    return 4;
                case "if":
                    return 5;
                case "then":
                    return 6;
                case "else":
                    return 7;
                case "while":
                    return 8;
                case "loop":
                    return 9;
                case "read":
                    return 10;
                case "write":
                    return 11;
                case ";":
                    return 12;
                case ",":
                    return 13;
                case "=":
                    return 14;
                case "!":
                    return 15;
                case "[":
                    return 16;
                case "]":
                    return 17;
                case "&&":
                    return 18;
                case "||":
                    return 19;
                case "(":
                    return 20;
                case ")":
                    return 21;
                case "+":
                    return 22;
                case "-":
                    return 23;
                case "*":
                    return 24;
                case "!=":
                    return 25;
                case "==":
                    return 26;
                case "<":
                    return 27;
                case ">":
                    return 28;
                case "<=":
                    return 29;
                case ">=":
                    return 30;
                case "null":
                    return 33;
                default:
                    return 34; // illegal token
            }
        }
    }


    public void skipToken() {
        if (currentTokens.length == 0 || getToken() == 33 || getToken() == 34) { // if current token is null, end of input, or invalid token
            return; // return without doing anything
        }
        cursorIndex++; // move cursor index to next token in array
        if (cursorIndex >= currentTokens.length) { // if end of currentTokens array is reached
            try {
                tokenizeLine(); // tokenize next line of input
            } catch (IOException e) { // if IOException is thrown during tokenization
                currentTokens = new String[0]; // set currentTokens array to empty array
            }
        }
    }

    public int intVal() throws IOException {
        if (getToken() != 31) {
            System.err.println("Error: Current token is not an integer." + " at line " + getLineNum());
            System.exit(1);
        }
        return Integer.parseInt(currentTokens[cursorIndex]);
    }

    public String idName() throws IOException {
        if (getToken() != 32) {
            System.err.println("Error: Current token is not an identifier." + " at line " + getLineNum());
            System.exit(1);
        }
        return currentTokens[cursorIndex];
    }

    public String getTokenString(int tokenType) {
        switch (tokenType) {
            case 1:
                return "program";
            case 2:
                return "begin";
            case 3:
                return "end";
            case 4:
                return "int";
            case 5:
                return "if";
            case 6:
                return "then";
            case 7:
                return "else";
            case 8:
                return "while";
            case 9:
                return "loop";
            case 10:
                return "read";
            case 11:
                return "write";
            case 12:
                return ";";
            case 13:
                return ",";
            case 14:
                return "=";
            case 15:
                return "!";
            case 16:
                return "[";
            case 17:
                return "]";
            case 18:
                return "&&";
            case 19:
                return "||";
            case 20:
                return "(";
            case 21:
                return ")";
            case 22:
                return "+";
            case 23:
                return "-";
            case 24:
                return "*";
            case 25:
                return "!=";
            case 26:
                return "==";
            case 27:
                return "<";
            case 28:
                return ">";
            case 29:
                return "<=";
            case 30:
                return ">=";
            case 31:
                return "integer_literal";
            case 32:
                return "identifier";
            case 33:
                return "null";
            case 34:
            default:
                return "illegal_token";
        }
    }

}