grammar Random;
// D:\antlr>"D:\Program Files\Java\jdk1.7.0_79\bin\java" -cp antlr-3.1.1.jar org.antlr.Tool D:\Git\ModelCodes\org.eclipse.epsilon.emg.engine\src\org\eclipse\epsilon\emg\random\random.g

tokens {
    RANDOM    = 'random' ;
}

@header {
package org.eclipse.epsilon.emg.random;
}

@lexer::header {package org.eclipse.epsilon.emg.random;}

@members {
    public static void main(String[] args) throws Exception {
        RandomLexer lex = new RandomLexer(new ANTLRStringStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        //ParseTreeBuilder builder = new ParseTreeBuilder("number");
        //RandomParser parser = new RandomParser(tokens, builder);
        RandomParser parser = new RandomParser(tokens);
        //System.out.println(builder.getTree().toStringTree());
 
        try {
            parser.number();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
    }
}
 
/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
 
number    : '{'RANDOM',' NUMBER',' NUMBER (',' WORD)?'}' ;
 
 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
 
NUMBER  : (DIGIT)+ ;

WORD    : ('a'..'z' | 'A'..'Z')+;
 
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; } ;
 
fragment DIGIT  : '0'..'9' ;