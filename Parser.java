/*
    This class provides a recursive descent parser
    for Corgi (the new version),
    creating a parse tree which can be interpreted
    to simulate execution of a Corgi program
*/

import java.util.*;
import java.io.*;

public class Parser {

   private Lexer lex;

   public Parser( Lexer lexer ) {
      lex = lexer;
   }

   //This is the P11 Parser
   public Node parseDefs() {
     System.out.println("-----> parsing <Defs>:");

     Node first = parseDef();
     // look ahead to see if there are more funcDef's
     Token token = lex.getNextToken();

     if ( token.isKind("eof") ) {
        return new Node( "defs", first, null, null );
     }
     else {
        lex.putBackToken( token );
        Node second = parseDefs();
        return new Node( "defs", first, second, null );
     }

   }

   public Node parseDef() {
     System.out.println("-----> parsing <funcDef>:");

     Token token = lex.getNextToken();
     errorCheck( token, "lparen" );

     token = lex.getNextToken();
     errorCheck( token, "define" );

     token = lex.getNextToken();
     errorCheck( token, "lparen" );

     token = lex.getNextToken();
     String nameOfDef = token.getDetails();
     errorCheck( token, "name" );

     token = lex.getNextToken();
     if ( token.isKind("rparen") ) {
       Node expr = parseExpr();

       token = lex.getNextToken();
       errorCheck( token, "rparen" );

       return new Node("def", nameOfDef, null, expr, null);
     } else {
        lex.putBackToken(token);
        // System.out.println("I'm here");
        Node params = parseParams();

        token = lex.getNextToken();
        errorCheck( token, "rparen" );

        Node expr = parseExpr();

        token = lex.getNextToken();
        errorCheck( token, "rparen" );

        return new Node("def", nameOfDef, params, expr, null);
     }

   }

   public Node parseParams() {
     System.out.println("-----> parsing <params>:");

     Token token = lex.getNextToken();
     Node name = new Node("name", token.getDetails(), null, null, null);
     errorCheck( token, "name" );

     token = lex.getNextToken();

     if ( token.isKind("name") ) {
        lex.putBackToken(token);
        Node params = parseParams();
        return new Node("params", name, params, null);
     }
     else {
        lex.putBackToken(token);
        return new Node("params", name, null, null);
     }
   }

   public Node parseExpr() {
     System.out.println("-----> parsing <Expr>:");

     Token token = lex.getNextToken();

     if ( token.isKind("number") ) {
        return new Node("number", token.getDetails(), null, null, null);
     }
     else if ( token.isKind("name") ) {
        return new Node("name", token.getDetails(), null, null, null);
     }
     else {
        lex.putBackToken(token);
        Node list = parseList();
        return list;
     }
   }

   public Node parseList() {
     System.out.println("-----> parsing <List>:");

     Token token = lex.getNextToken();
     errorCheck( token, "lparen" );

     token = lex.getNextToken();
     if( token.isKind("rparen") ) {
        return new Node("list", null, null, null);
     }
     else {
        lex.putBackToken(token);
        Node items = parseItems();
        return new Node("list", items, null, null);
     }
   }

   public Node parseItems() {
     System.out.println("-----> parsing <items>:");

     Node expr = parseExpr();
     Token token = lex.getNextToken();

     if( token.isKind("rparen") ) {
        return new Node("items", expr, null, null);
     } else {
        lex.putBackToken(token);
        Node items = parseItems();
        return new Node("items", expr, items, null);
     }
   }

  // check whether token is correct kind
  private void errorCheck( Token token, String kind ) {
    if( ! token.isKind( kind ) ) {
      System.out.println("Error:  expected " + token +
                         " to be of kind " + kind );
      System.exit(1);
    }
  }

  // check whether token is correct kind and details
  private void errorCheck( Token token, String kind, String details ) {
    if( ! token.isKind( kind ) ||
        ! token.getDetails().equals( details ) ) {
      System.out.println("Error:  expected " + token +
                          " to be kind= " + kind +
                          " and details= " + details );
      System.exit(1);
    }
  }

}
