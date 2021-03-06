Mercor is the latin word for "I trade" and that is precisely the purpose
of this language. Mercor can be used to automate trading strategies,
make more accurate predictions based on trading models, or even just
learn more about market trends. Here are some basic examples of the
language in use:

**Ex 1:**

start

x=((3.02\*(5.05-3.92))/4.12412)

give x

end

**Terminal/Output:**

0.8274734973764101

**[Key Takeaways]**: doubles, addition, subtraction, division,
printing, braces/brackets

**Ex 2:**

start

enter LLNW

give Current_stock_price

enter BNTX

smaDist=Current_stock_price +
((Distance_from_twohundred_Day_Simple_Moving_Average \* 0.01)\*
Current_stock_price)

give smaDist

end

**Terminal/Output: (note that this depends on the current values)**

154.67

4.533

**[Key Takeaways:]** entering a stock, changing stocks, stock
commands, command operations, negative doubles

**Ex 3:**

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

**Terminal/Output:**

you_should_buy

buyOneStock

buyOneStock

buyOneStock

buyOneStock

**[Key Takeaways:]** conditionals, ifs/else, text, loops, is/!is
(notis), and/or, iterations of loops

**[Further Development:]{.ul}** There are a lot more commands and a lot
more stocks you can analyze. ***Find a whole list of supported commands
here:***
[**[https://tinyurl.com/mercordocumentation]**](https://tinyurl.com/mercordocumentation)
Additionally, there is a lot more operations and uses for this that are
still in development such as connection to api's to execute these
trades, notifications of when you should take specific trades according
to the script you set, and a web application so that anyone can access
and use this regardless of their ability to code. If you want to help me
develop this even further, contact me through my github:
**[[https://github.com/sidereior]](https://github.com/sidereior)**

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
