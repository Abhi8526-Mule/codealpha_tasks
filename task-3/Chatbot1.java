import java.util.*;
import java.util.regex.*;

public class Chatbot1 {
    private static final Map<String, String> responses = new LinkedHashMap<>();

    static {
       
        responses.put("hi", "Hello! How can I help you?");
        responses.put("hello", "Hi there! How can I assist you today?");
        responses.put("how are you", "I'm just a bot, but I'm doing fine. How can I assist?");
        responses.put("what is your name", "I am your AI chatbot assistant.");
        responses.put("help", "Sure, I'm here to help. Ask me anything.");
        responses.put("weather", "I can't predict the weather yet, but I can help with other queries.");
        responses.put("thanks", "You're welcome!");
        responses.put("bye", "Goodbye! Have a great day!");
    }

    public static String getResponse(String userInput) {
        userInput = userInput.toLowerCase().trim();

        for (String key : responses.keySet()) {
            
            if (userInput.matches(".*\\b" + Pattern.quote(key) + "\\b.*")) {
                return responses.get(key);
            }
        }

        return "I'm sorry, I didn't understand that. Can you rephrase?";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("?? Chatbot: Hello! I'm your assistant. Type 'bye', 'exit' or 'quit' to end.");

        while (true) {
            System.out.print("?? You: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye") || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("?? Chatbot: " + responses.get("bye"));
                break;
            }

            String response = getResponse(input);
            System.out.println("?? Chatbot: " + response);
        }

        scanner.close();
    }
}
