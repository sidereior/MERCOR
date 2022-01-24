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
        try {
            String val = getData("BNTX", "Volume");
            System.out.println(val);
            Double last=Stock.solveString(val);
            System.out.println(last);
            //Double ret=Double.parseDouble(last);
            //System.out.println(last);
            //System.out.println("errored?");
        } catch (Exception e) {
        }
    }

    public static String getData(String quote, String target) throws IOException {
        // formatting target is awlays cpatial first leet of words
        String SYM = "[" + target + "]";
        URL url = new URL("https://finviz.com/quote.ashx?t=" + quote);
        URLConnection urlConn = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
        BufferedReader buff = new BufferedReader(inStream);
        String line = buff.readLine();
        String result = "didn't work";
        while (line != null) {
            if (line.contains(SYM)) {
                result = line;
                break;
            }
            line = buff.readLine();
        }
        inStream.close();
        buff.close();
        // System.out.println(result);
        return result;
        // System.out.println("work");

    }

    public static Double solveString(String result) {
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
            // System.out.println(last);

        }
        else if (result.contains("</span></b></td>")) {
            value = result.substring(0, result.indexOf("</span></b></td>"));
            last = value;
            for (int i = value.length() - 1; i >= 0; i--) {
                if ((Character.toString(value.charAt(i))).equals(">")) {
                    last = value.substring(i + 1, value.length());
                    break;
                }
            }
            // System.out.println(last);
        }
        else if (result.contains("</b></td>")) {
            //System.out.println("did this work");
            value = result.substring(0, result.indexOf("</b></td>"));
            last = value;
            for (int i = value.length() - 1; i >= 0; i--) {
                if ((Character.toString(value.charAt(i))).equals(">")) {
                    last = value.substring(i + 1, value.length());
                    break;
                }
            }
            
        }
        // do it here:
        //System.out.println(last);
        if (last.contains("%") || last.contains("M") || last.contains("B")) {
            try {
                //System.out.println("here");
                String dubs =(last.substring(0, last.length() - 1));
                try {
                     dubs=dubs.replaceAll(",","");
                     Double fin=Double.parseDouble(dubs);
                     //System.out.println(fin);
                     return fin;
                     
                 } catch (NumberFormatException b) {
                     throw new RuntimeException("could not be resolved to type double");
                 }
            } catch (NumberFormatException e) {
                throw new RuntimeException("could not be resovled to type double");
            }
        }
         else {
             //System.out.println("did this run");
            try {
               // System.out.println("got here tho");
               // System.out.println(last);
                //so its with the string
                String ret2=last;
                ret2=ret2.replaceAll(",","");
                //System.out.println("res");
                //System.out.println(ret2);
                
                Double fin=Double.parseDouble(ret2);
                //System.out.println(fin);
                return fin;
                
            } catch (NumberFormatException b) {
                throw new RuntimeException("could not be resolved to type double");
            }
        }
        //return null;
    }
}
