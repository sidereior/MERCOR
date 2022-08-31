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
    //Interpreter for the language
    private static Scanner in = new Scanner(System.in);
    private static String currStock = "have not set a stock";
    //List of available stocks, can be added to through finviz
    private static String[] stockList = { "BNTX", "TSLA", "LLNW", "BSFC", "APPL", "MSFT", "GOOGL", "AMZN", "FB", "TSM",
            "NVDA", "JPM", "JNJ", "UNH", "SPY", "WMT", "LVMUY", "PG", "BAC", "HD", "MA", "BABA", "XOM", "PFE", "ASML",
            "TM", "DIS", "KO", "CVX", "GDX", "SPY" };
    //Command list of available stocks from finviz, can be added to through finviz
    //All command which have general formula, rest of commands require special parsing
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

    public static void main(String[] args) {
        //Add prefixes, delimiters, and infixes to the language
        //Makes it so that you can dynamically use this to make another language

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

        //evaluates the program (runs it) given prefixes, infixes, delimeters
        eval(program, state);
        // TODO: loops, if, else, graphing?, time, volatility, basic commands, simple
    }

    public static Object eval(Object exp, State state) {
        if (exp instanceof Double) {
            //Parses and evaluates doubles
            return (Double) exp;
        } else if (exp instanceof String) {
            //Parses and evaluates Strings
            String varName = exp.toString();
            varName = varName.replaceAll("_", " ");
            //Replace command spaces with underscores
            Boolean worked = false;
            if (varName.equals("true")) {
                return true;
            }
            if (varName.equals("false")) {
                return false;
            }
            if (varName.substring(0, 1).equals("\"") && varName.substring(varName.length() - 1).equals("\"")) {
                return varName.substring(1, varName.length() - 1);
            }
            //Specialized cases where the general formula of parsing it is not contained in the commands list
            if (varName.equals("Volatility of Week Month"))// TODO: SPLIT THIS BETWEEN WEEK AND MONTH
            {
                //Parses Volatility of Week, Month
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
                //Parses average volume for three months
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Average volume (3 month)");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }
            if (varName.equals("Distance from twohundred Day Simple Moving Average")) {
                //Parses two hundred day simple moving average
                try {
                    String current = currStock.toString();
                    String val = Stock.getData(current, "Distance from 200-Day Simple Moving Average");
                    Double last = Stock.solveString(val);
                    overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                    return last;
                } catch (Exception e) {
                    throw new RuntimeException("invalid stock command/ticker");
                }
            }

            if (varName.equals("Distance from fifty Day Simple Moving Average")) {
                //Parses distance from fifty day simple moving average
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
                //Parses distance from twenty day simple moving average
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
                //If none of these are true, then it is contained in the command list
                if (i.equals(varName)) {
                    worked = true;
                }
            }
            if (worked) {
                try {
                    //Applies command selected to current stock being used to get specific value
                    String current = currStock;
                    String val = Stock.getData(current, varName);
                    try {
                        Double last = Stock.solveString(val);
                        overall = current + "timeBack: " + startNumDaysBehind + "timeCurr: " + endNumDaysBehind;
                        return last;
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
            }
            if (state.getVariableValue(varName) != null) {
                return state.getVariableValue(varName);
            }
            //If the stock is not contained within the list, then throw exception
            throw new RuntimeException("cannot find variable/stockTicker:  " + varName);

        } else {
            //Otherwise, it must be a list
            List list = (List) exp;

            if (list.get(0).equals("start")) { // open-brace subEXP close-brace
                for (int i = 1; i < list.size() - 1; i++) {
                    eval(list.get(i), state);
                }
                return "OK";
            }
            if (list.get(0).equals("(")) { /// open-parens subEXP close-parens
                return eval(list.get(1), state);
            }
            if (list.get(0).equals("give")) // print EXP
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
            if (list.get(0).equals("enter")) //start a new method/function
            {
                //When starting a new method, you must also enter a stock to accompany it
                Object argument = list.get(1);
                Boolean worked = false;
                for (int i = 0; i < stockList.length; i++) {
                    if (stockList[i].equals(argument)) {
                        worked = true;
                        currStock = argument.toString();
                    }
                }
                if (!worked) {
                    //If the stock does not exist, throw an exception
                    throw new RuntimeException("cannot find stock ticker:  " + argument);
                }
                return "OK";
            }
            if (list.get(0).equals("loop"))
            {
                //Loop within the program
                while ((Boolean) eval(list.get(1), state)) {
                    eval(list.get(2), state);
                }
                return null;
            }

            if (list.get(1).equals("and")) {
                //and within a conditional
                return (Boolean) eval(list.get(0), state) == (Boolean) eval(list.get(2), state);
            }
            if (list.get(1).equals("or")) {
                //or within a conditional
                return (Boolean) eval(list.get(0), state) || (Boolean) eval(list.get(2), state);
            }
            if (list.get(0).equals("if"))
            {
                //If statement (conditional)
                if (true == (Boolean) eval(list.get(1), state)) {
                    return eval(list.get(2), state);
                } else {
                    return eval(list.get(3), state);
                }
            }

            if (list.get(1).equals("="))
            {
                // assignment operator
                state.setVariableValue(list.get(0), eval(list.get(2), state));
                return "OK";
            }

            if (list.get(1).equals("is"))
            {
                // EXP == EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) == (Double) eval(argument2, state);
            }
            if (list.get(1).equals("notis"))
            {
                // EXP != EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) != (Double) eval(argument2, state);
            }

            if (list.get(1).equals("+"))
            {
                // EXP + EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) + (Double) eval(argument2, state);
            }

            if (list.get(1).equals("-"))
            {
                // EXP - EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) - (Double) eval(argument2, state);
            }

            if (list.get(1).equals("*"))
            {
                // EXP * EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (((Double) eval(argument1, state)) * ((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("/"))
            {
                // EXP / EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (((Double) eval(argument1, state)) / ((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("<"))
            {
                // EXP < EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) < (Double) eval(argument2, state);
            }

            if (list.get(1).equals(">"))
            {
                // EXP > EXP
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) > (Double) eval(argument2, state);
            }
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