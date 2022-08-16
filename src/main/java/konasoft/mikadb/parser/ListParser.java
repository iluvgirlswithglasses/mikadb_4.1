package konasoft.mikadb.parser;

import java.util.ArrayList;
import java.util.List;

public class ListParser {

    /**
     * @ basic
     * */
    public List<String> stringToList(String str) {
        int n = str.length();
        if (str.charAt(0) != '[' || str.charAt(n-1) != ']') return new ArrayList<>();
        str = str.substring(1, n-1); n = str.length();
        List<StringBuilder> res = new ArrayList<>(); int cr = -1;
        // every character after down-slash is escaped
        int[] escaped = new int[n+1];
        for (int i = 0; i < n; i++) if (str.charAt(i) == '\\' && escaped[i] != 2) {
            escaped[i] = 1;     // is escape character, don't display
            escaped[i+1] = 2;   // is escaped, treat as string
        }
        //
        boolean inString = false;
        for (int i = 0; i < n; i++) {
            if (inString) {
                if (escaped[i] == 0) {
                    // normal
                    if (str.charAt(i) == '\'') {
                        inString = false;
                    } else {
                        res.get(cr).append(str.charAt(i));
                    }
                } else if (escaped[i] == 2) {
                    // is escaped
                    res.get(cr).append(str.charAt(i));
                }
                // escaped[i] == 1 --> an escape character, don't do anything
            } else {
                if (str.charAt(i) == '\'') {
                    inString = true;
                    res.add(new StringBuilder()); cr++;
                }
            }
        }
        //
        List<String> strRes = new ArrayList<>();
        for (StringBuilder i: res) strRes.add(i.toString());
        return strRes;
    }

    public String listToString(List<String> lst) {
        StringBuilder s = new StringBuilder("[");
        for (String i: lst) {
            s.append("'")
             .append(i.replace("'", "\\'"))
             .append("', ");
        }
        return s.toString() + "]";
    }

    /**
     * additional utils
     * */
    public String listToHtmlString(List<String> lst) {
        StringBuilder pstr = new StringBuilder();
        for (String i: lst) {
            pstr.append(i).append(',').append("<br/>");
        }
        return pstr.toString().substring(0, pstr.length() - "<br/>".length());
    }
}
