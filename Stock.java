import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.lang.StringBuilder;

public class Stock {

    public static void main(String[] args) {
        //main method just to test stock operations
        try {
            String val = getData("BSFC", "Average volume (3 month)");
            System.out.println(val);
            Double last = Stock.solveString(val);
            System.out.println(last);
        } catch (Exception e) {
        }
    }

    public static String getData(String quote, String target) throws IOException {
        //Takes in a quote and a target and getsData for given stock
        //quote is the stock you are looking at
        //target is the data you are looking for
        String timed = "null";
        String SYM = "[" + target + "]";
        URL url = new URL("https://finviz.com/quote.ashx?t=" + quote);
        URLConnection urlConn = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
        BufferedReader buff = new BufferedReader(inStream);
        String line = buff.readLine();
        String result = "didn't work";
        while (line != null) {
            if (line.contains(SYM)) {
                if (!(line.contains("overallTIME") && line.contains("timeBack"))) {
                    // System.out.println("timeBack");
                    timed = "atBack";
                    result = line;
                    break;
                }
                if (!(line.contains("overallTIME") && line.contains("timeCurr"))) {
                    // System.out.println("endTime");
                    timed = "atFront";
                    result = line;
                    break;
                }
            }
            line = buff.readLine();
            timed += line;
        }
        inStream.close();
        buff.close();
        // System.out.println(result);
        return result;
        // System.out.println("work");

    }

    public static Double solveString(String result) {
        //Takes in a string and solves it
        //Parses it into a valuable number
        String value = "didn't work";
        String last = "no work";
        if (result.contains("</small></b></td>")) {
            value = result.substring(0, result.indexOf("</small></b></td>"));
            last = value;
            for (int i = value.length() - 1; i >= 0; i--) {
                if ((Character.toString(value.charAt(i))).equals(">")) {
                    last = value.substring(i + 1, value.length());
                    break;
                }
            }
                   } else if (result.contains("</span></b></td>")) {
            value = result.substring(0, result.indexOf("</span></b></td>"));
            last = value;
            for (int i = value.length() - 1; i >= 0; i--) {
                if ((Character.toString(value.charAt(i))).equals(">")) {
                    last = value.substring(i + 1, value.length());
                    break;
                }
            }
        } else if (result.contains("</b></td>")) {
            //parses end of the stock data
            value = result.substring(0, result.indexOf("</b></td>"));
            last = value;
            for (int i = value.length() - 1; i >= 0; i--) {
                if ((Character.toString(value.charAt(i))).equals(">")) {
                    last = value.substring(i + 1, value.length());
                    break;
                }
            }

        }
        //Does abbreviations for for numbers magnitude
        if (last.contains("%") || last.contains("M") || last.contains("B") || last.contains("K")) {
            try {
                String dubs = (last.substring(0, last.length() - 1));
                try {
                    dubs = dubs.replaceAll(",", "");
                    Double fin = Double.parseDouble(dubs);
                    return fin;

                } catch (NumberFormatException b) {
                    throw new RuntimeException("could not be resolved to type double");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("could not be resovled to type double");
            }
        } else {
            try {
                //if its a string then you need to parse each double
                String ret2 = last;
                ret2 = ret2.replaceAll(",", "");
                Double fin = Double.parseDouble(ret2);
                return fin;

            } catch (NumberFormatException b) {
                throw new RuntimeException("could not be resolved to type double");
            }
        }
        // return null;
    }
}
