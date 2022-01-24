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
    private static String[] stockList = {"BNTX", "TSLA"};
    private static String[] commandList = {"Previous close", "Current stock price", "Volume", "Volatility of Week Month"};

    public static void main(String[] args) {
      // test(args);
        Parser p = new Parser();
        State state = new State();
        p.addPrefix("give", 1);

        p.addPrefix("enter", 1);

        p.addInfix(">",2);

        //p.addInfix(".",2);

        p.addInfix("<",2);

        p.addInfix("=", 2);

        p.addInfix("+", 2);

        p.addInfix("-", 2);

        p.addInfix("is",2);

        p.addInfix("*", 2);

        p.addInfix("/", 2);

        p.addDelimiters("(", ")");

        p.addDelimiters("start", "end");

        p.addPrefix("if", 3);

        Object program = p.parse(load("program.txt"));

        eval(program, state);
        //TODO: loops, if, else, graphing?, time, volatility, basic commands, simple moving average crossing program
    }

    
    
    public static Object eval(Object exp, State state) {
        if (exp instanceof Integer) {//ERROR HERE
            //TODO: DOES THIS WORK
            return (Integer)exp;
        } 
        else if (exp instanceof Double) {//ERROR HERE
            // the value of an integer is itself
            return (Double)exp;
        } 
        else if (exp instanceof String) {
            String varName = exp.toString();//this is where you would put reserved strings
            varName=varName.replaceAll("_"," ");
            Boolean worked=false;
            //System.out.println(varName);
            
            if(varName.equals("Volatility of Week Month"))//TODO: SPLIT THIS BETWEEN WEEK AND MONTH
            {
                try{
                    String current=currStock.toString();
                    String val=Stock.getData(current, "Volatility (Week, Month)");
                    Double last=Stock.solveString(val);
                    return last;
                    }
                    catch(Exception e){
                        throw new RuntimeException("invalid stock command/ticker");
                    }
            }
            
            for(String i:commandList)
            {
                //System.out.println(i);
                if(i.equals(varName))
                {
                    worked=true;
                }
            }
            //System.out.println(worked);
            //worked is true so it is in the try/catch
            if(worked) 
            {   
                try{
                //System.out.println(currStock);
                String current=currStock;//TODO: GET CHANGING STOCK NAMES WORKING
                String val=Stock.getData(current, varName);
                //System.out.println(val);
                //System.out.println("it is right here");
                try{
                Double last=Stock.solveString(val);//throws an exception
                //System.out.println(last);
                return last;
                }
                catch(Exception e){
                    //System.out.println("this did not run");
                }                
                }
                catch(Exception e){
                    //throw new RuntimeException("invalid stock command/ticker");
                }
            }
            //System.out.println("exited");
            if(state.getVariableValue(varName)!=null)
            {
                //System.out.println("gothere");
            return state.getVariableValue(varName);
            }
            throw new RuntimeException("cannot find variable/stockTicker:  " + varName);
        } else {
            // must be a List
            List list = (List) exp;
            
            if(list.get(0).equals("start")){ // open-brace subEXP close-brace
                for(int i=1; i < list.size()-1; i++)
                {
                eval(list.get(i), state);
                }
                return "OK";
            }

            if(list.get(0).equals("(")){ /// open-parens subEXP close-parens
                return eval(list.get(1), state);
            }

            if (list.get(0).equals("give")) // print EXP with [3+12]
            {
                Object argument = list.get(1);
                System.out.println(eval(argument, state));
                return "OK";
            }

            if (list.get(0).equals("enter")) // enter stock value, store as variable, cannot use any all caps stock tickers
            {
                Object argument = list.get(1);
                //any way to not have this be 
                Boolean worked=false;
                for(int i=0; i<stockList.length; i++)
                {
                    if(stockList[i].equals(argument))
                    {
                    worked=true;
                    currStock=argument.toString();
                    }
                }
                if(!worked)
                {
                    throw new RuntimeException("cannot find stock ticker:  " + argument);
                }
                return "OK";
            }

            
            if (list.get(0).equals("if")) //if (exp)
            {
                if((Boolean) true == list.get(1))
                {
                    return eval(list.get(2),state);
                }
                else{//this would be the if
                    return eval(list.get(2),state);
                }
                //eval parenthessees 
                //eval 2
                //if(x>3) [][]
                //otherwise do eval 3
                /*
                start
                enter TSLA
                x = Volatility_of_Week_Month
                
                give x
                end
                */
            } 
        
            if (list.get(1).equals("="))//assignment operator
            {   
                state.setVariableValue(list.get(0), eval(list.get(2), state));
                return "OK";
            }

            if (list.get(1).equals("is")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double)eval(argument1, state)==(Double)eval(argument2, state);
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
                return (((Double) eval(argument1, state))*((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("/")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (((Double) eval(argument1, state))/((Double) eval(argument2, state)));
            }

            if (list.get(1).equals("<")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double)eval(argument1, state) < (Double)eval(argument2, state);
            }

            if (list.get(1).equals(">")) // EXP > EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Double) eval(argument1, state) > (Double) eval(argument2, state);
            }
            /*
            if (list.get(1).equals(".")) // EXP > EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                String doubvalue=argument1.toString() + "." + argument2.toString();
                return Double.valueOf(doubvalue);
            }
            */
            throw new RuntimeException("unable to evaluate:  " + exp);

        }
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