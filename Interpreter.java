import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;

//alt shift f to form
public class Interpreter {
    private static Scanner in = new Scanner(System.in);
    
    public static void main(String[] args) {
      // test(args);
        Parser p = new Parser();
        State state = new State();
        p.addPrefix("give", 1);

        p.addPrefix("enter", 1);

        p.addInfix(">",2);

        p.addInfix("<",2);

        p.addInfix("=", 2);

        p.addInfix("+", 2);

        p.addInfix("-", 2);

        p.addInfix("*", 2);

        p.addInfix("/", 2);

        p.addDelimiters("(", ")");

        p.addDelimiters("start", "end");

        Object program = p.parse(load("program.txt"));

        eval(program, state);
    }

    
    
    public static Object eval(Object exp, State state) {
        if (exp instanceof Integer) {
            // the value of an integer is itself
            return exp;
        } else if (exp instanceof String) {
            String varName = (String) exp;//this is where you would put reserved strings
            System.out.println(varName);
            if(varName.equals("closing")) 
            {
                try{
                String value=Stock.getData("Prev Close");
                Double last=Double.parseDouble(value);
                System.out.println(last);
                return last;
                }
                catch( Exception e){
                    throw new RuntimeException("cannot");
                }
            }
            else if(!state.getVariableValue(varName).equals(null))
            {
            return state.getVariableValue(varName);
            }
            throw new RuntimeException("cannot find variable:  " + varName);
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

            if(list.get(0).equals("enter")){ // open-brace subEXP close-brace
                Object argument = list.get(1);
                state.setVariableValue("stock", argument);//how reserved words work
                
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

            /*
            if (list.get(0).equals("if")) //if (exp)
            {
                Object argument1 = list.get(1);
                return (Integer) eval(argument1, state) + (Integer) eval(argument2, state);
            } 
            */

            if (list.get(1).equals("="))//assignment operator
            {
                state.setVariableValue(list.get(0), eval(list.get(2), state));
                return "OK";

            }

            if (list.get(1).equals("+")) // EXP + EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer) eval(argument1, state) + (Integer) eval(argument2, state);
            }

            if (list.get(1).equals("-")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer) eval(argument1, state) - (Integer) eval(argument2, state);
            }

            if (list.get(1).equals("*")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer)(((Integer) eval(argument1, state))*((Integer) eval(argument2, state)));
            }

            if (list.get(1).equals("/")) // EXP - EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer)(((Integer) eval(argument1, state))/((Integer) eval(argument2, state)));
            }

            if (list.get(1).equals("<")) // EXP < EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer) eval(argument1, state) < (Integer) eval(argument2, state);
            }

            if (list.get(1).equals(">")) // EXP > EXP
            {
                Object argument1 = list.get(0);
                Object argument2 = list.get(2);
                return (Integer) eval(argument1, state) > (Integer) eval(argument2, state);
            }
            
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