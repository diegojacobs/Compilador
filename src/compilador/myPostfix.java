//Postfix tomado de https://gist.github.com/gmenard/6161825

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Diego Jacobs 13160
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class myPostfix {
    
    public myPostfix()
    {
        
    }
    
    /** Operators precedence map. */
	private static final Map<Character, Integer> precedenceMap;
	static {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		/*
                map.put('(', 1);
		map.put('|', 2);
		map.put('.', 3); // explicit concatenation operator
		map.put('?', 4);
		map.put('*', 4);
		map.put('+', 4);
                */
                map.put('四', 1);
		map.put('少', 2);
		map.put('要', 3); // explicit concatenation operator
		map.put('背', 4);
		map.put('砂', 4);
		map.put('北', 4);
		precedenceMap = Collections.unmodifiableMap(map);
	};

	/**
	 * Get character precedence.
	 * 
	 * @param c character
	 * @return corresponding precedence
	 */
	private static Integer getPrecedence(Character c) {
		Integer precedence = precedenceMap.get(c);
		return precedence == null ? 6 : precedence;
	}

	/**
	 * Transform regular expression by inserting a '.' as explicit concatenation
	 * operator.
	 */
	private static String formatRegEx(String regex) {
            String res = new String();
            //List<Character> allOperators = Arrays.asList('|', '?', '+', '*','.');
	    //List<Character> binaryOperators = Arrays.asList('|','.');
	    List<Character> allOperators = Arrays.asList('少', '背', '北', '砂','要');
	    List<Character> binaryOperators = Arrays.asList('少','要');

	    for (int i = 0; i < regex.length(); i++) {
		Character c1 = regex.charAt(i);

                if (i + 1 < regex.length()) {
                    Character c2 = regex.charAt(i + 1);

                    res += c1;

                    if (!c1.equals('四') && !c2.equals('枼') && !allOperators.contains(c2) && !binaryOperators.contains(c1)) {
                        res += '要';
		    }
                }
	    }
	    res += regex.charAt(regex.length() - 1);

	    return res;
	}

	/**
	 * Convert regular expression from infix to postfix notation using
	 * Shunting-yard algorithm.
	 * 
	 * @param regex infix notation
	 * @return postfix notation
	 */
	public static String infixToPostfix(String regex) {
            
            //Quitamos la abreviación +
            String abr = abr_cer_pos(regex);
            
            //Quitamos la abreviación ?
            abr = abr_signo(abr);
	    
            String postfix = new String();
                   
            regex = regex.trim(); //Quitar espacios en blanco
		
            Stack<Character> stack = new Stack<Character>();
                    
	    String formattedRegEx = formatRegEx(abr);

	    for (Character c : formattedRegEx.toCharArray()) {
	        switch (c) {
                    case '四':
			stack.push(c);
			break;

		case '枼':
		    while (!stack.peek().equals('四')) {
                        postfix += stack.pop();
                    }
                    stack.pop();
                    break;
                                        

		default:
                    while (stack.size() > 0) {
                        Character peekedChar = stack.peek();

			Integer peekedCharPrecedence = getPrecedence(peekedChar);
			Integer currentCharPrecedence = getPrecedence(c);

			if (peekedCharPrecedence >= currentCharPrecedence) {
                            postfix += stack.pop();
			} else {
                            break;
			}
                    }
                    stack.push(c);
                    break;
		}

            }

            while (stack.size() > 0)
		postfix += stack.pop();

            return postfix;
	}
        
        public static String abr_cer_pos(String entrada)
        {
            String exp = new String();
            String formattedRegEx = formatRegEx(entrada);
            Stack<Character> stack = new Stack<Character>();            
            
            for (int i=0;i<formattedRegEx.length();i++)
            {
                Character c1 = formattedRegEx.charAt(i);
                
                if (c1.equals('北'))
                { 
                    //Revisamos si hay una expresión entre ()
                    Character c2 = formattedRegEx.charAt(i-1);
                    int contp1 = 0;
                    int contp2 = 0;
                    if (c2.equals('枼') || stack.peek()=='枼')
                    {   
                       contp2++;
                       String abr = new String();
                       boolean flag = true;
                       //Sacamos todo hasta el ( correspondiente y despues debemos darle vuelta al string
                       while (flag && contp2!=contp1)
                       {
                           if (stack.peek()=='枼')
                            contp2++;
                           if (stack.peek()=='四')
                            contp1++;
                            
                           abr += stack.pop();
                           if (contp2-1 == contp1)
                               flag=false;
                       }
                       //Arreglamos el orden de la expresión
                       String temp = new String();
                       for (int j=abr.length();j>0;j--)
                       {
                            temp += abr.charAt(j-1);
                       }
                       abr=temp;
                       
                       for (int k=0;k<abr.length();k++)
                       {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                       }
                       stack.push('要');
                       for (int k=0;k<abr.length();k++)
                       {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                       }
                       stack.push('砂');
                    }
                    //si no solo tomamos el ultimo caracter
                    else
                    {
                        int j=i-1;
                        String abr = new String();
                        while (j>=0 && (getPrecedence(formattedRegEx.charAt(j))==6))
                        {
                            abr += stack.pop();
                            j--;
                        }
                        stack.push('四');
                        for (int k=0;k<abr.length();k++)
                        {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                        }
                        stack.push('要');
                        for (int k=0;k<abr.length();k++)
                        {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                        }
                        stack.push('砂');  
                        stack.push('枼');
                    }
                }
                else
                    stack.push(c1);
            }
            //Sacamos todo del stack
            while (stack.size() > 0)
            {
                exp += stack.pop();
            }
            //Al sacarlo del stack quedo de fin a inicio, debemos darle vuelta a la expresión
            String res = new String();
            for (int i=exp.length();i>0;i--)
            {
                res += exp.charAt(i-1);
            }
            return res;
        }
        
        public static String abr_signo(String entrada)
        {
            String exp = new String();
            String formattedRegEx = formatRegEx(entrada);
            Stack<Character> stack = new Stack<Character>();            
            
            for (int i=0;i<formattedRegEx.length();i++)
            {
                Character c1 = formattedRegEx.charAt(i);
                
                if (c1.equals('背'))
                { 
                    //Revisamos si hay una expresión entre ()
                    Character c2 = formattedRegEx.charAt(i-1);
                    int contp1 = 0;
                    int contp2 = 0;
                    if (c2.equals('枼') || stack.peek()=='枼')
                    {   
                       contp2++;
                       String abr = new String();
                       boolean flag = true;
                       //Sacamos todo hasta el ( correspondiente y despues debemos darle vuelta al string
                       while (flag && contp2!=contp1)
                       {
                           if (stack.peek()=='枼')
                            contp2++;
                           if (stack.peek()=='四')
                            contp1++;
                            
                           abr += stack.pop();
                           if (contp2-1 == contp1)
                               flag=false;
                       }
                       //Agregamos un (
                       stack.push('四');
                        //Arreglamos el orden de la expresión
                       String temp = new String();
                       for (int j=abr.length();j>0;j--)
                        {
                            temp += abr.charAt(j-1);
                        }
                       abr=temp;
                       
                       for (int k=0;k<abr.length();k++)
                        {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                        }
                        stack.push('少');
                        stack.push('永');
                        stack.push('枼');
                    }
                    //si no solo tomamos el ultimo caracter
                    else
                    {
                        int j=i-1;
                        String abr = new String();
                        while (j>=0 && (getPrecedence(formattedRegEx.charAt(j))==6))
                        {
                            abr += stack.pop();
                            j--;
                        }
                        stack.push('四');
                        for (int k=0;k<abr.length();k++)
                        {
                            Character c3 = abr.charAt(k);
                            stack.push(c3);
                        }
                        stack.push('少');
                        stack.push('永');  
                        stack.push('枼');
                    }
                }
                else
                    stack.push(c1);
            }
            //Sacamos todo del stack
            while (stack.size() > 0)
            {
                exp += stack.pop();
            }
            //Al sacarlo del stack quedo de fin a inicio, debemos darle vuelta a la expresión
            String res = new String();
            for (int i=exp.length();i>0;i--)
            {
                res += exp.charAt(i-1);
            }
            return res;
        }
}