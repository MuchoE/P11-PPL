import java.util.Scanner;
import java.io.*;

public class P11 {

   public static void main(String[] args) throws Exception {

      String name;
      Scanner keys = new Scanner( System.in );

      if ( args.length == 1 ) {
         name = args[0];
      }
      else {
        System.out.print("Enter name of P11 definitions file: ");
        name = keys.nextLine();
      }

      Lexer lex = new Lexer( name );
      Parser parser = new Parser( lex );

      // start with <statements>
      Node root = parser.parseDefs();

      // display parse tree for debugging/testing:
      // TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 800, 500, root );
      while(true){
        String expression;
        System.out.print("Enter a P11 Expression: ");
        expression = keys.nextLine();
        PrintWriter e = new PrintWriter(new File("expre"));
        e.println(expression);
        e.close();
        Lexer lex2 = new Lexer( "expre" );
        Parser parser2 = new Parser( lex2 );
        Node root2 = parser2.parseExpr();
        // System.out.println(root2.evaluate());
        TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 800, 500, root2);
        // execute the parse tree
        //root.execute();
      }

   }// main

}
