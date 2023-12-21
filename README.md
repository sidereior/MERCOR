<h1> MERCOR </h1>
<p1>
Mercor is the latin word for "I trade" and that is precisely the purpose of this language. Mercor can be used to automate trading strategies,make more accurate predictions based on trading models, or even just learn more about market trends. It is designed to make algorhtmic trading models significantly more easy to develop and straightforward for those to follow who have a limited background in stock analysis or programming. Here are some basic examples of the language in use:
</p1> 


<h2> Ex 1: </h2>
<p2>
~~~
start

x=((3.02\*(5.05-3.92))/4.12412)

give x

end
~~~
**Terminal/Output:**

0.8274734973764101

**[Key Takeaways]**: doubles, addition, subtraction, division,
printing, braces/brackets
</p2>


<h3> Ex 2: </h3>
<p3>
~~~
start

enter LLNW

give Current_stock_price

enter BNTX

smaDist=Current_stock_price +
((Distance_from_twohundred_Day_Simple_Moving_Average \* 0.01)\*
Current_stock_price)

give smaDist

end
~~~
**Terminal/Output: (note that this depends on the current values)**

154.67

4.533

**[Key Takeaways:]** entering a stock, changing stocks, stock
commands, command operations, negative doubles
</p3>


<h4> Ex 3: </h4>
<p4>
~~~
start

enter BNTX

if(Current_stock_price\>145.212)start

give text you_should_buy

end

start

give text sell_asap

end

enter GDX

savings=100.01

loop((Distance_from_twohundred_Day_Simple_Moving_Average notis
Distance_from_fifty_Day_Simple_Moving_Average) and (savings\>0.00))start

give text buyOneStock

savings=savings-Current_stock_price

end

end
~~~
**Terminal/Output:**

you_should_buy

buyOneStock

buyOneStock

buyOneStock

buyOneStock

**[Key Takeaways:]** conditionals, ifs/else, text, loops, is/!is
(notis), and/or, iterations of loops
</p4>


**[Further Development:]{.ul}** There are a lot more commands and a lot
more stocks you can analyze. ***Find a whole list of supported commands
here:***
[**[https://tinyurl.com/mercordocumentation]**](https://tinyurl.com/mercordocumentation)
Additionally, there is a lot more operations and uses for this that are
still in development such as connection to api's to execute these
trades, notifications of when you should take specific trades according
to the script you set, and a web application so that anyone can access
and use this regardless of their ability to code. If you want to help me
develop this even further, contact me through my email: alexanderlnanda@gmail.com

**[Upcoming Changes]**:

-   Web interaction to perform trades automatically [(in
    development)]

-   PaperMoney UI [(in development)]

-   Unit Testing [(in development)]

-   Feature request/bug address system/community

-   Easier tracking of funds in account

-   "Floor rules" (transaction costs, trade limiter, etc)

-   Option trading functionality

-   Competition system
