package org.parser.json;

import org.parser.util.Constants;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.parser.util.Constants.*;

public class JsonParser {
    List<String> tokens;
    int index = 0;

    public Object parse( String json )  {
        JsonTokenizer jsonTokenizer = new JsonTokenizer( json );
        tokens = jsonTokenizer.tokenize();
        return parseValue();
    }

    private Object parseValue() {
        String token = tokens.get( index );
        if (token.equals(OPEN_CURLY_BRACE)) {
            return parseObject();
        }
        return null;
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> objects = new LinkedHashMap<>();
        index++; // skip opening braces
        while( !CLOSE_CURLY_BRACE.equals(tokens.get( index ) ) ) {
            String key = parseString();
            index++; // skip colon
            Object value = parseValue();
            objects.put( key, value );
            if( COMMA.equals( tokens.get( index ) ) ) {
                index++; // skip to next object
            }
        }
        index++; // skip closing braces
        return objects;
    }

    /**
     * eg usage : tokens = ["\"username\"", index++ ->":", "\"peter123\"", ","]
     * @return token
     */
    private String parseString() {
        String token = tokens.get( index );
        index++;
        return token;
    }
}
