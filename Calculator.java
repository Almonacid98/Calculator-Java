import java.util.Scanner;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.print("Ingresa una expresión matemática (por ejemplo, 2+10/3*2): ");
            String expresion = scanner.nextLine();

            double resultado = evaluarExpresion(expresion);

            System.out.println("El resultado es: " + resultado);

            System.out.println("¿Deseas realizar otra operación? (s/n)");
            char respuesta = scanner.next().charAt(0);
            if (respuesta == 'n' || respuesta == 'N') {
                continuar = false;
            }
            scanner.nextLine(); // Consumir el salto de línea después de la entrada de la respuesta
        }

        scanner.close();
    }

    public static double evaluarExpresion(String expresion) {
        // algoritmo Shunting Yard
        String postfix = infixToPostfix(expresion);
        System.out.println("Expresión postfix: " + postfix); //  depurar

        // Evaluar la expresión postfix
        return evaluarPostfix(postfix);
    }

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        // Definir la precedencia de los operadores
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);

        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c)) {
                postfix.append(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postfix.append(' ').append(operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                while (!operatorStack.isEmpty() && precedence.getOrDefault(operatorStack.peek(), 0) >= precedence.get(c)) {
                    postfix.append(' ').append(operatorStack.pop());
                }
                operatorStack.push(c);
                postfix.append(' ');
            }
        }

        while (!operatorStack.isEmpty()) {
            postfix.append(' ').append(operatorStack.pop());
        }

        return postfix.toString().trim();
    }

    public static double evaluarPostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                stack.push(Double.parseDouble(token));
            } else {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Expresión postfija inválida: no hay suficientes operandos para el operador " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            System.out.println("La división por cero no tiene solución.");
                            stack.push(Double.NaN); // Agregamos NaN (Not a Number) a la pila para indicar que no hay solución
                        } else {
                            stack.push(a / b);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Operador desconocido: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Expresión postfija inválida: la pila debe contener exactamente un elemento al final");
        }

        return stack.pop();
    }
}

