package test.fuzzytest;

import java.util.ArrayList;
import java.util.Random;

import static test.fuzzytest.KeywordLibrary.ListType.*;

public class KeywordLibrary {
    private Random rand;
    
    public enum ListType {
        NONE,
        CONNECTOR,
        DATETIMECONNECTOR,
        COMMAND,
        DATETIME,
        ITEM,
        NUMBER,
        VALIDNUMBER,
        TASKID,
        VALIDTASKID,
        RANDOM,
        SYMBOL,
        COMMA,
        EDITKEYWORD,
        EDIT,
        DELETE,
        SEARCH,
        DETAILS,
        ADD,
        RETURN,
        QUIT,
        ALL
    }

    private ListType[] allListTypes;
    private ArrayList<ArrayList<String>> libraryLists;
    
    public KeywordLibrary(int seed) {
        rand = new Random(seed);
        createLists();
        addContent();
    }
    
    public String getRandom(ListType listType) {
        ArrayList<String> list = getList(listType);
        return getRandomElement(list);
    }
    
    public String getRandom(ListType...listTypes) {
        ArrayList<String> list = getRandomList(listTypes);
        return getRandomElement(list);
    }
    
    private void addContent() {
        

        addToList("", NONE, DATETIMECONNECTOR, CONNECTOR, RANDOM);
        addToList("to", DATETIMECONNECTOR, CONNECTOR, TASKID, RANDOM);
        addToList("-", DATETIMECONNECTOR, RANDOM);
        addToList("until", DATETIMECONNECTOR, CONNECTOR);
        
        addToList("at", CONNECTOR, TASKID, RANDOM);
        addToList("from", CONNECTOR, RANDOM);
        addToList("after", CONNECTOR);
        addToList("before", CONNECTOR);
        
        addToList("2014", DATETIME, RANDOM);
        addToList("29 oct 2014", DATETIME, RANDOM);
        addToList("2pm", DATETIME, TASKID, VALIDTASKID, RANDOM);
        addToList("3am", DATETIME, TASKID, VALIDTASKID);
        addToList("0pm", DATETIME, TASKID, VALIDTASKID);
        addToList("12am", DATETIME, TASKID);
        addToList("4 pm", DATETIME, NUMBER, RANDOM);
        addToList("12 am", DATETIME);
        addToList("13 am", DATETIME);
        addToList("13 pm", DATETIME);
        addToList("1300", DATETIME, NUMBER);
        addToList("1300h", DATETIME. NUMBER);
        addToList("12-am", DATETIME);
        addToList("12-14pm", DATETIME);
        addToList("tuesday", DATETIME);
        addToList("oct", DATETIME, TASKID, RANDOM);
        addToList("may", DATETIME, TASKID, CONNECTOR);
        addToList("mon", DATETIME, TASKID, RANDOM);
        addToList("sun", DATETIME, TASKID);
        addToList("sat", DATETIME, TASKID);
        addToList("today", DATETIME);
        addToList("tomorrow", DATETIME);
        addToList("yesterday", DATETIME);
        addToList("next week", DATETIME);
        addToList("last month", DATETIME);
        addToList("tonight", DATETIME);
        addToList("day", DATETIME, TASKID);
        addToList("lunchtime", DATETIME);

        addToList("+high", ITEM);
        addToList("+low", ITEM);
        addToList("+medium", ITEM);
        addToList("+med", ITEM);
        addToList("#taskline", ITEM);
        addToList("#orange", ITEM);
        addToList("#tag", ITEM);
        addToList("##", ITEM);
        addToList("done", ITEM);
        addToList("undone", ITEM);
        addToList("++high", ITEM);

        addToList("show", COMMAND, SEARCH);
        addToList("search", COMMAND, SEARCH);
        addToList("ls", COMMAND, SEARCH, RANDOM);
        addToList("set", COMMAND, EDIT, TASKID, RANDOM);
        addToList("edit", COMMAND, EDIT);
        addToList("change", COMMAND, EDIT);
        addToList("modify", COMMAND, EDIT);
        addToList("remove", COMMAND, DELETE);
        addToList("rm", COMMAND, DELETE);
        addToList("detail", COMMAND, DETAILS);
        addToList("details", COMMAND, DETAILS);
        addToList("return", COMMAND, RETURN);
        addToList("back", COMMAND, RETURN);
        addToList("quit", COMMAND, QUIT);
        addToList("exit", COMMAND, QUIT);

        addToList("tag", COMMAND, EDIT, EDITKEYWORD, TASKID);
        addToList("add", COMMAND, ADD, EDITKEYWORD, TASKID, RANDOM);
        addToList("delete", COMMAND, DELETE, EDITKEYWORD, RANDOM);
        addToList("del", COMMAND, DELETE, EDITKEYWORD, TASKID);

        addToList("time", EDITKEYWORD);
        addToList("name", EDITKEYWORD, RANDOM);
        addToList("date", EDITKEYWORD, RANDOM);
        addToList("datetime", EDITKEYWORD);
        addToList("priority", EDITKEYWORD);
        addToList("status", EDITKEYWORD);
        addToList("edit", EDITKEYWORD);
        addToList("tag add", EDITKEYWORD);
        addToList("tag del", EDITKEYWORD);
        
        addToList("1", NUMBER, VALIDNUMBER);
        addToList("2", NUMBER, VALIDNUMBER);
        addToList("-1", NUMBER);
        addToList("-34", NUMBER);
        addToList("431", NUMBER);
        addToList("4,31", NUMBER);
        addToList("67", NUMBER);
        addToList("5-1", NUMBER);
        addToList("5--6", NUMBER);
        addToList("2-4,3", NUMBER, VALIDNUMBER);
        addToList("5", NUMBER, VALIDNUMBER);
        addToList("1,3", NUMBER, VALIDNUMBER);
        addToList("2-5", NUMBER, VALIDNUMBER, RANDOM);
        addToList("8, 3, 1", NUMBER, VALIDNUMBER);
        addToList("1-5, 6", NUMBER, VALIDNUMBER, RANDOM);
        addToList("3.534", NUMBER);
        addToList("123(", NUMBER);
        addToList("^", NUMBER, SYMBOL);
        addToList("#", NUMBER, SYMBOL);
        addToList("@2", NUMBER, RANDOM);
        addToList("+341", NUMBER, RANDOM);
        addToList("%123", NUMBER, TASKID);
        addToList("-84", NUMBER);
        addToList("-pi", NUMBER, TASKID);
        addToList("sqrt(2)", NUMBER);
        addToList("255", NUMBER);
        addToList("2147483647", NUMBER);
        addToList("2147483648", NUMBER);
        addToList("-2147483648", NUMBER);
        addToList("999999999999999", NUMBER);
        addToList("0", NUMBER);
        addToList("$4", NUMBER);
        addToList("i", NUMBER);
        addToList("", NUMBER);
        addToList("000", NUMBER, RANDOM);
        
        for (int i = 5; i < 30; i++) {
            addToList(i + "", VALIDNUMBER);
            addToList(i + "-" + (i+5), VALIDNUMBER);
        }

        addToList("ab9", TASKID, VALIDTASKID);
        addToList("9ab", TASKID, VALIDTASKID);
        addToList("1aa", TASKID, VALIDTASKID);
        addToList("aa0", TASKID, VALIDTASKID);
        addToList("po9", TASKID, VALIDTASKID);
        addToList("c93", TASKID);
        addToList("999", TASKID, RANDOM);
        addToList("aca", TASKID);
        addToList("12", NUMBER, TASKID);
        addToList("oct", DATETIME, TASKID, RANDOM);
        addToList("op2", TASKID, VALIDTASKID, RANDOM);
        addToList("9sds", TASKID);
        addToList("s9asd", TASKID);
        addToList("4rea", TASKID);
        addToList("%53", TASKID);
        addToList("%%%", TASKID, RANDOM);
        addToList("#ta3", TASKID);

        addToList("a", RANDOM);
        addToList("%", RANDOM, SYMBOL);
        addToList("aj3", RANDOM);
        addToList("T##", RANDOM);
        addToList("\"test\"", RANDOM);
        addToList("\"", RANDOM, SYMBOL);
        addToList("\n", RANDOM);
        addToList("five", RANDOM);
        addToList("    ", RANDOM);
        addToList("___", RANDOM);
        addToList("orange", RANDOM, ITEM);
        addToList("apple", RANDOM, ITEM);
        addToList("pear pear", RANDOM, ITEM);
        addToList("-", RANDOM, SYMBOL, CONNECTOR);
        addToList(",", RANDOM, SYMBOL, CONNECTOR, COMMA);
        addToList("+", RANDOM, SYMBOL);
        addToList(">", RANDOM, SYMBOL);
        addToList("\\", RANDOM, SYMBOL);
    }

    private String getRandomElement(ArrayList<String> list) {
        int next = rand.nextInt(list.size());
        return list.get(next);
    }

    private ArrayList<String> getRandomList(ListType...listTypes) {
        int next = rand.nextInt(listTypes.length);
        return getList(listTypes[next]);
    }

    private ArrayList<String> getList(ListType listType) {
        if (listType == ListType.ALL) {
            return getRandomList(allListTypes);
        } else {
            int index = listType.ordinal();
            return libraryLists.get(index);
        }
    }
    
    private void createLists() {
        int nTypes = ListType.values().length;
        
        libraryLists = new ArrayList<>();
        allListTypes = new ListType[nTypes - 1];
        
        for (int i = 0; i < nTypes; i++) {
            ListType currentListType = ListType.values()[i];
            
            if (currentListType != ListType.ALL) {
                libraryLists.add(new ArrayList<String>());
                allListTypes[i] = currentListType;
            }
        }
    }
    
    private void addToList(String token, ListType...listTypes) {
        for (ListType listType : listTypes) {
            ArrayList<String> list = getList(listType);
            list.add(token);
        }
    }
}
