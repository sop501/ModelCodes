// $ANTLR 3.1.1 Random.g 2016-01-20 14:54:41

package org.eclipse.epsilon.emg.random;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RandomParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RANDOM", "NUMBER", "WORD", "DIGIT", "WHITESPACE", "'{'", "','", "'}'"
    };
    public static final int WORD=6;
    public static final int T__11=11;
    public static final int T__10=10;
    public static final int NUMBER=5;
    public static final int WHITESPACE=8;
    public static final int DIGIT=7;
    public static final int RANDOM=4;
    public static final int EOF=-1;
    public static final int T__9=9;

    // delegates
    // delegators


        public RandomParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RandomParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return RandomParser.tokenNames; }
    public String getGrammarFileName() { return "Random.g"; }


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



    // $ANTLR start "number"
    // Random.g:35:1: number : '{' RANDOM ',' NUMBER ',' NUMBER ( ',' WORD )? '}' ;
    public final void number() throws RecognitionException {
        try {
            // Random.g:35:11: ( '{' RANDOM ',' NUMBER ',' NUMBER ( ',' WORD )? '}' )
            // Random.g:35:13: '{' RANDOM ',' NUMBER ',' NUMBER ( ',' WORD )? '}'
            {
            match(input,9,FOLLOW_9_in_number60); 
            match(input,RANDOM,FOLLOW_RANDOM_in_number61); 
            match(input,10,FOLLOW_10_in_number62); 
            match(input,NUMBER,FOLLOW_NUMBER_in_number64); 
            match(input,10,FOLLOW_10_in_number65); 
            match(input,NUMBER,FOLLOW_NUMBER_in_number67); 
            // Random.g:35:43: ( ',' WORD )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==10) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // Random.g:35:44: ',' WORD
                    {
                    match(input,10,FOLLOW_10_in_number70); 
                    match(input,WORD,FOLLOW_WORD_in_number72); 

                    }
                    break;

            }

            match(input,11,FOLLOW_11_in_number75); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "number"

    // Delegated rules


 

    public static final BitSet FOLLOW_9_in_number60 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RANDOM_in_number61 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_number62 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NUMBER_in_number64 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_number65 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_NUMBER_in_number67 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_10_in_number70 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_WORD_in_number72 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_number75 = new BitSet(new long[]{0x0000000000000002L});

}