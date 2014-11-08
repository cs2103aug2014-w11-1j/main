package io.json;

import io.InvalidFileFormatException;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;

import main.command.alias.AliasValuePair;

/**
 * the main Json Parser for Aliases.
 */
//@author A0065475X
public class JsonAliasParser {
    private static final String JSON_ALIASES = "aliases";
    private static final String JSON_ALIAS = "alias";
    private static final String JSON_VALUE = "value";

    
    public static void aliasesToJson(Writer writer, AliasValuePair[] aliases) { 
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        JsonArrayBuilder aliasArrayJson = Json.createArrayBuilder();
        for (AliasValuePair alias : aliases) {
            aliasArrayJson.add(createJsonObjectBuilder(alias));
        }
        
        builder.add(JSON_ALIASES, aliasArrayJson);
        
        JsonObject jsonObject = builder.build();
        JsonItemParser.writePrettyPrint(writer, jsonObject);
    }
    
    public static AliasValuePair[] jsonToAliases(Reader reader)
            throws InvalidFileFormatException {
        
        Queue<AliasValuePair> aliasQueue = new LinkedList<>();
        
        JsonParser parser = Json.createParser(reader);
        try {
            Event event = parser.next();
            
            while (!(event == Event.KEY_NAME && parser.getString().equals(JSON_ALIASES))) {
                event = parser.next();
            }
            
            event = parser.next();
            if (event != Event.START_ARRAY) {
                throw new InvalidFileFormatException("Aliases is not an array.");
            }
            
            event = parser.next();
            while (event == Event.START_OBJECT) {
                aliasQueue.offer(parseAlias(parser));
                event = parser.next();
            } 
            
            if (event == Event.END_ARRAY) {
                parser.close();
                
            } else {
                throw new InvalidFileFormatException("End of tasks array not found");
            }
            
        } catch (NoSuchElementException e) {
            throw new InvalidFileFormatException("Reached end of file unexpectedly.");
        } catch (JsonParsingException e) {
            throw new InvalidFileFormatException("Invalid JSON encountered.");
        } catch (JsonException e) {
            throw new InvalidFileFormatException("Unable to read file.");
        }
        
        AliasValuePair[] aliases = new AliasValuePair[aliasQueue.size()];
        for (int i = 0; i < aliases.length; i++) {
            aliases[i] = aliasQueue.poll();
        }
        
        return aliases;
    }

    private static AliasValuePair parseAlias(JsonParser parser)
            throws InvalidFileFormatException {

        String[] aliasAndValue = new String[2];
        
        Event event = parser.next();
        while (event != Event.END_OBJECT) {
            
            if (event != Event.KEY_NAME) {
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_WRONG_EVENT + event.name());
            }
            String key = parser.getString();
            
            event = parser.next();
            if (event == Event.VALUE_STRING) {
                String value = parser.getString();
                readKeyValuePair(aliasAndValue, key, value);
                
            } else {
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_WRONG_EVENT + event.name());
            }
            
            event = parser.next();
        }
        
        String alias = aliasAndValue[0];
        String value = aliasAndValue[1];
        return new AliasValuePair(alias, value);
    }

    private static void readKeyValuePair(String[] aliasAndValue,
            String key, String value) throws InvalidFileFormatException {
        
        switch(key) {
            case JSON_ALIAS :
                aliasAndValue[0] = JsonItemParser.jsonStringToString(value);
                break;
            case JSON_VALUE :
                aliasAndValue[1] = JsonItemParser.jsonStringToString(value);
                break;
            default :
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_UNKNOWN_ELEMENT + key);
        }
    }

    private static JsonObjectBuilder createJsonObjectBuilder(
            AliasValuePair aliasValuePair) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JSON_ALIAS,
                JsonItemParser.stringToJsonString(aliasValuePair.alias));
        builder.add(JSON_VALUE,
                JsonItemParser.stringToJsonString(aliasValuePair.value));
        
        return builder;
    }
}
