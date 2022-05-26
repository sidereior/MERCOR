import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.lang.StringBuilder;

//alt shift f to form
public class Interpreter {
    private static Scanner in = new Scanner(System.in);
    private static String currStock = "have not set a stock";
    private static String[] stockList = { "BNTX", "TSLA", "LLNW", "BSFC", "APPL", "MSFT", "GOOGL", "AMZN", "FB", "TSM",
            "NVDA", "JPM", "JNJ", "UNH", "SPY", "WMT", "LVMUY", "PG", "BAC", "HD", "MA", "BABA", "XOM", "PFE", "ASML",
            "TM", "DIS", "KO", "CVX", "GDX", "SPY" };
    private static String[] commandList = { "Previous close", "Current stock price", "Volume",
            "Volatility of Week Month", "Average volume three month",
            "Distance from twohundred Day Simple Moving Average", "Distance from fifty Day Simple Moving Average",
            "Distance from twenty Day Simple Moving Average", "Major index membership", "Insider ownership",
            "Shares outstanding", "Market capitalization", "EPS estimate for next year", "Shares float",
            "EPS estimate for next quarter", "Institutional ownership", "Short interest share", "EPS growth this year",
            "Short interest ratio", "EPS growth next year", "Analysts' mean target price",
            "Annual EPS growth past 5 years", "Annual sales growth past 5 years", "Full time employees",
            "Relative Strength Index", "Relative volume" };

    private static int startNumDaysBehind = -10;
    private static int endNumDaysBehind = 0;
    private static String overall = "valid";
    private static ArrayList<Integer> rets = new ArrayList<Integer>();

    public static void main(String[] args) {
        // test(args);
        System.out.println("overall: " + overall);

        System.out.println("");
        Parser p = new Parser();
        State state = new State();
        p.addPrefix("give", 1);

        p.addPrefix("enter", 1);

        p.addInfix("and", 2);

        p.addInfix("or", 2);

        // p.addPrefix("comment", 1);

        p.addInfix(">", 2);

        // p.addInfix(".",2);

        p.addInfix("<", 2);

        p.addPrefix("text", 1);

        p.addInfix("=", 2);

        p.addInfix("+", 2);

        p.addInfix("-", 2);

        p.addInfix("is", 2);

        p.addInfix("notis", 2);

        p.addInfix("*", 2);

        p.addInfix("/", 2);

        p.addDelimiters("(", ")");

        p.addDelimiters("start", "end");

        p.addPrefix("if", 3);

        p.addPrefix("loop", 2);

        Object program = p.parse(load("program.txt"));

        eval(program, state);
        // TODO: loops, if, else, graphing?, time, volatility, basic commands, simple
        // moving average crossing program
    }

    public static Object eval(Object exp, State state) {
        if (exp instanceof Double) {// ERROR HERE
            // the value of an integer is itself
            return (Double) exp;
        } else if (exp instanceof String) {
            String varName = exp.toString();// this is where you would put reserved strings
            varName = varName.replaceAll("_", " ");
            Boolean worked = false;
            // System.out.println(varName);
            // if(!varName.equals("and") || varName.equals("or"))
            // {
            if (varName.equals("true")) {
                return true;
            }
            if (varName.equals("false")) {
                return false;
            }
            if (varName.substring(0, 1).equals("\"") && varName.substring(varName.length() - 1).equals("\"")) {
                return varName.substring(1, varName.length() - 1);
            }
            // a few specialized cases for the language where the syntax is odd from the
            // website
            if (varName.equals("Volatility of Week Month"))// TODO: SPLIT THIS BETWEEN WEEK AND MONTH
            {
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Volatility (Week, Month)");
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    Double last = Stock.solveString(val);
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }
            if (varName.equals("Average volume three month"))// TODO: SPLIT THIS BETWEEN WEEK AND MONTH
            {
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Average volume (3 month)");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    // System.out.println(val);
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }
            if (varName.equals("Distance from twohundred Day Simple Moving Average")) {
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Distance from 200-Day Simple Moving Average");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    // System.out.println(val);
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }

            if (varName.equals("Distance from fifty Day Simple Moving Average")) {
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Distance from 50-Day Simple Moving Average");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    // System.out.println(val);
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }

            if (varName.equals("Distance from twenty Day Simple Moving Average")) {
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Distance from 20-Day Simple Moving Average");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    // System.out.println(val);
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }
            for (String i : commandList) {
                // System.out.println(i);
                // System.out.println(varName);
                // System.out.println(i.equals(varName));
                if (i.equals(varName)) {
                    worked = true;
                    // System.out.println("worked:" + worked);
                }
            }
            // System.out.println(worked);
            // worked is true so it is in the try/catch
            if (worked) {
                try {
                    // System.out.println(currStock);
                    String current = currStock;// TODO: GET CHANGING STOCK NAMES WORKING
                    String val = Stock.getData(current, varName);
                    // System.out.println(val);
                    // System.out.println("it is right here");
                    try {
                        Double last = Stock.solveString(val);// throws an exception
                        overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                        // System.out.println(last);
                        return last;
                    } catch (Exception e) {
                        // System.out.println("this did not run");
                    }
                } catch (Exception e) {
                    // throw new RuntimeException("invalid stock command/ticker");
                }
            }
            // System.out.println("exited");
            if (state.getVariableValue(varName) != null) {
                // System.out.println("gothere");
                return state.getVariableValue(varName);
            }
            throw new RuntimeException("cannot find variable/stockTicker:  " + varName);
            // }
        } else {
            // must be a List
            List list = (List) exp;
            /*
             * if(list.get(0).equals("comment"))
             * {
             * 
             * }
             */

            if (list.get(0).equals("start")) { // open-brace subEXP close-brace
                for (int i = 1; i < list.size() - 1; i++) {
                    eval(list.get(i), state);
                }
                return "OK";
            }

            if (list.get(0).equals("(")) { /// open-parens subEXP close-parens
                return eval(list.get(1), state);
            }

            if (list.get(0).equals("give")) // print EXP with [3+12]
            {
                Object argument = list.get(1);
                try {
                    PrintStream o = new PrintStream(new File("output.txt"));
                    System.setOut(o);
                    System.out.println(eval(argument, state));
                } catch (Exception e) {

                }
                return "OK";
            }

            if (list.get(0).equals("text")) {
                String argument = (String) list.get(1);
                return argument;
            }

            if (list.get(0).equals("enter")) // enter stock value, store as variable, cannot use any all caps stock
                                             // tickers
            {
                Object argument = list.get(1);
                // any way to not have this be
                Boolean worked = false;
                for (int i = 0; i < stockList.length; i++) {
                    if (stockList[i].equals(argument)) {
                        worked = true;
                        currStock = argument.toString();
                    }
                }
                if (!worked) {
                    throw new RuntimeException("cannot find stock ticker:  " + argument);
                }
                return "OK";
            }

            if (list.get(0).equals("loop"))// loop (condition (action))
            {
                // System.out.println(eval(list.get(1),state));
                while ((Boolean) eval(list.get(1), state)) {
                    eval(list.get(2), state);
                }
                return null;
            }

            // and and or

            if (list.get(1).equals("and")) {
                return (Boolean) eval(list.get(0), state) == (Boolean) eval(list.get(2), state);
            }
            if (list.get(1).equals("or")) {
                return (Boolean) eval(list.get(0), state) || (Boolean) eval(list.get(2), state);
            }

            if (list.get(0).equals("if")) // if (exp)
            {
                if (true == (Boolean) eval(list.get(1), state)) {
                    return eval(list.get(2), state);
                } else {// this would be the if
                    return eval(list.get(3), state);
                }
                // eval parenthessees
                // eval 2
                // if(x>3) [][]
                // otherwise do eval 3
                /*
                 * start
                 * enter TSLA
                 * x = Volatility_of_Week_Month
                 * 
                 * give x
                 * end
                 */
            }

            if (list.get(1).equals("="))// assignment operator
            {
                state.setVariableValue(list.get(0), eval(list.get(2), state));
                return "OK";
            }

            if (list.get(1).equals("is")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) == (Double) eval(argument2, state);
            }
            if (list.get(1).equals("notis")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) != (Double) eval(argument2, state);
            }

            if (list.get(1).equals("+")) // EXP + EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) + (Double) eval(argument2, state);
            }

            if (list.get(1).equals("-")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) - (Double) eval(argument2, state);
            }

            if (list.get(1).equals("*")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (((Double) eval(argument1, state)) * ((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("/")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (((Double) eval(argument1, state)) / ((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("<")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) < (Double) eval(argument2, state);
            }

            if (list.get(1).equals(">")) // EXP > EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) > (Double) eval(argument2, state);
            }
            /*
             * if (list.get(1).equals(".")) // EXP > EXP
             * {
             * Object argument1 = list.get(0);
             * Object argument2 = list.get(2);
             * String doubvalue=argument1.toString() + "." + argument2.toString();
             * return Double.valueOf(doubvalue);
             * }
             */
            throw new RuntimeException("unable to evaluate:  " + exp);

        }
        // return null;
    }

    public static String input() {
        return in.nextLine();
    }

    public static String load(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}