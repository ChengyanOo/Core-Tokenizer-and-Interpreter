# Core Tokenizer and Interpreter

This repository contains a custom tokenizer and interpreter based on BNF grammar. The interpreter is written in Java, and it supports a simple programming language called Core. The language allows users to declare variables, perform basic arithmetic operations, and implement conditional statements and loops.

## Getting Started

### Prerequisites
JDK 17.0 or higher
Compilation
To compile the code, follow these steps:

Navigate to the "src" directory using the command line.
Run the following commands:
```sh
javac org/yann/coretokenizor/*.java    
java org/yann/coretokenizor/CoreInterpreter input.txt data.txt
```
To test the output, you can change the content of the "input.txt" file under the "src" directory.
### Sample Program
```sh
program
    int N, I, SUM;
begin
    read N;
    SUM = 0;
    I = 1;
    while (I <= N) loop
        SUM = SUM + I;
        I = I + 1;
    end;
    write SUM;
    write N;
    write I;
end
```
### BNF Grammar for Core
```sh
<prog> ::= program <decl seq> begin <stmt seq> end
<decl seq> ::= <decl> | <decl> <decl seq>
<stmt seq> ::= <stmt> | <stmt> <stmt seq>
<decl> ::= int <id list>;
<id list> ::= <id> | <id>, <id list>
<stmt> ::= <assign>|<if>|<loop>|<in>|<out>
<assign> ::= <id> = <exp>;
<if> ::= if <cond> then <stmt seq> end;
      | if <cond> then <stmt seq> else <stmt seq> end;
<loop> ::= while <cond> loop <stmt seq> end;
<in> ::= read <id list>;
<out> ::= write <id list>;
<cond> ::= <comp>|!<cond>
         | [<cond> && <cond>] | [<cond> || <cond>]
<comp> ::= (<op> <comp op> <op>)
<exp> ::= <fac>|<fac>+<exp>|<fac>-<exp>
<fac> ::= <op> | <op> * <fac>
<op> ::= <int> | <id> | (<exp>)
<comp op> ::= != | == | < | > | <= | >=
<id> ::= ... as defined by RE ...
<int> ::= ... as defined by RE (only unsigned numbers)
```
### Language Features
- 11 reserved words: program, begin, end, int, if, then, else, while, loop, read, write
- 19 special symbols: ; , = ! [ ] && || ( ) + - * != == < > <= >=
- Integers (unsigned)
- Identifiers: start with an uppercase letter, followed by zero or more uppercase letters and zero or more digits. Note that "ABCend" is illegal as an id because of the lowercase letters; and it is not two tokens because of the lack of whitespace. However, ABC123 and A1B2C3 are both legal.
- White space requirement: White space, i.e., one or more blanks or tab characters or carriage returns, is required between any pair of tokens unless one or both of them is/are special symbols. If one or both of them is/are special symbols, white space is optional. Note that white space is not a token.
### Usage

To run the Core Interpreter with your own input file, follow these steps:
- Create an input file (e.g., input.txt) containing your Core program.
- Place the input file in the "src" directory.
- Run the following commands in the "src" directory:
- The interpreter will process the input file and generate the output file (e.g., output.txt) in the same directory.
```sh
javac org/yann/coretokenizor/*.java    
java org/yann/coretokenizor/CoreInterpreter input.txt output.txt
```
### Examples
Here are some more examples of Core programs:
#### Calculate the factorial of a number:
```sh
program
    int N, I, FACT;
begin
    read N;
    FACT = 1;
    I = 1;
    while (I <= N) loop
        FACT = FACT * I;
        I = I + 1;
    end;
    write FACT;
end
```
#### Calculate the greatest common divisor (GCD) of two numbers:
```sh
program
    int A, B, TEMP;
begin
    read A;
    read B;
    while (B != 0) loop
        TEMP = B;
        B = A % B;
        A = TEMP;
    end;
    write A;
end
```
### Acknowledgements
This project was inspired by the BNF grammar provided and the desire to create a custom tokenizer and interpreter for educational purposes. We hope that this project helps others understand the fundamentals of compilers and interpreters.
