package org.parser.json;

import java.util.ArrayList;
import java.util.List;

import static org.parser.util.Constants.*;

public class JsonTokenizer {
    private final String json;
    private int index = 0;

    public JsonTokenizer(String json) {
        this.json = json.trim();
    }

    public List<String> tokenize() {
        List<String> tokens = new ArrayList<>();
        while (index < json.length()) {
            char currentChar = json.charAt(index);
            if (Character.isWhitespace(currentChar)) {
                index++;
                continue;
            }
            switch (currentChar) {
                case '[':
                case ']':
                case '{':
                case '}':
                case ':':
                case ',':
                    tokens.add(String.valueOf(currentChar));
                    index++;
                    break;
                case '"':
                    tokens.add(parseString());
                default:
                    if( Character.isDigit(currentChar) || currentChar == '-' ) {
                        tokens.add( parseNumber() );
                    } else {
                        tokens.add( parseLiteral() );
                    }
                    break;
            }
        }

        return tokens;
    }

    private String parseLiteral() {
        int start = index;
        while ( index < json.length() && Character.isLetter( json.charAt( index ) ) ) {
            index++;
        }
        String literal = json.substring(start, index);
        if (!TRUE.equals( literal ) && !FALSE.equals( literal ) && !NULL.equals( literal ) ) {
            throw new IllegalArgumentException( "Unexpected token: " + literal );
        }
        return literal;
    }

    private String parseNumber() {
        int start = index;
        if( json.charAt( index ) == '-' ) {
            index++;
        }
        while( index < json.length() && Character.isDigit( json.charAt( index ) ) ) {
            index++;
        }
        if( index < json.length() && json.charAt( index ) == '.' ) {
            index++;
            while( index < json.length() && Character.isDigit( json.charAt( index ) ) ) {
                index++;
            }
        }
        return json.substring(start, index);
    }

    private String parseString() {
        index++; // skip opening quote
        StringBuilder result = new StringBuilder();
        while (index < json.length() && json.charAt( index ) != '"') {
            if (json.charAt(index) == '\\') {
                index++;
                char escapedChar = json.charAt( index );
                switch (escapedChar) {
                    case '"':
                    case '\\':
                    case '/':
                        result.append( escapedChar );
                        break;
                    case 'b':
                        result.append( '\b' );
                        break;
                    case 'f':
                        result.append( '\f' );
                        break;
                    case 'n':
                        result.append( '\n' );
                        break;
                    case 'r':
                        result.append( '\r' );
                        break;
                    case 't':
                        result.append( '\t' );
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Invalid escape sequence: \\" + escapedChar );
                }
            } else {
                result.append( json.charAt( index ) );
            }
            index++;
        }
        index++; // skip closing quote
        return result.toString();
    }
}
