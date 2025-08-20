public class SecretCodeGuesser {
    SecretCode code = new SecretCode();
    private char[] alphabet = {'B', 'A', 'C', 'X', 'I', 'U'};
    private int length;
    private int score;
    private String guessString;
    public void start() {

        // ===== STEP 1: Find length =====
        length = findLength();
        if (length == -1) {
            System.out.println("Failed to determine secret code length.");
            return;
        }
        System.out.println("Length found: " + length);

        guessString = initialGuess(alphabet, length);

            // ===== STEP 2: Donald Knuth's Algorithm and Position-by-position deduction Algorithm =====

        if (length <= 6) {
            // ===== Donald Knuth's Algorithm =====
            int totalPossible = (int) Math.pow(alphabet.length, length);

            // Generate all possible codes in an array
            String[] possibleCodes = new String[totalPossible];
            generateAllCodes(possibleCodes, alphabet, length);

            boolean[] eliminated = new boolean[totalPossible]; // false = still possible

            while (true) {
                score = code.guess(guessString);
                if (score == length) {
                    System.out.println("I found the secret code. It is " + guessString);
                    break;
                }

                // Eliminate inconsistent codes
                for (int i = 0; i < totalPossible; i++) {
                    if (!eliminated[i]) {
                        if (feedback(possibleCodes[i], guessString) != score) {
                            eliminated[i] = true;
                        }
                    }
                }
                // Eliminate inconsistent codes
                for (int i = 0; i < totalPossible; i++) {
                    if (!eliminated[i]) {
                        if (feedback(possibleCodes[i], guessString) != score) {
                            eliminated[i] = true;
                        }
                    }
                }

                // Pick next guess: choose one minimizing max remaining possibilities
                int bestIndex = -1;
                int bestWorst = Integer.MAX_VALUE;
                for (int i = 0; i < totalPossible; i++) {
                    if (eliminated[i]) continue;

                    int worstCase = 0;
                    for (int f = 0; f <= length; f++) {
                        int count = 0;
                        for (int j = 0; j < totalPossible; j++) {
                            if (!eliminated[j] && feedback(possibleCodes[j], possibleCodes[i]) == f) {
                                count++;
                            }
                        }
                        if (count > worstCase) worstCase = count;
                    }
                    if (worstCase < bestWorst) {
                        bestWorst = worstCase;
                        bestIndex = i;
                    }
                }
                guessString = possibleCodes[bestIndex];
            }
        } else {
            // ===== Position-by-position deduction algorithm =====
            score = code.guess(guessString);
            char[] correctCode = new char[length]; 
            // Deduce each position 
            for (int i = 0; i < length; i++) {  
                for (char c : new char[]{'A','C', 'X', 'I', 'U'}) { 
                    String testChar = setAt(guessString, i, c); 
                    int r = code.guess(testChar); 
                    if (r == score + 1) { 
                        correctCode[i] = c; 
                        break; 
                    } 
                    if (r == score - 1) { 
                        correctCode[i] = 'B'; 
                        break; 
                    }   
                } 
            } 
            // Verify final guess 
            String finalGuess = new String(correctCode); 
            code.guess(finalGuess); 
            System.out.println("I found the secret code. It is " + finalGuess);
        }
    }

    // ----Helper method----
    // Find length
    public int findLength() { 
        int len = 1; 
        while (true) { 
            guessString = "B".repeat(len); 
            int result = code.guess(guessString); 
            if (result != -2) { return len; 
            } 
            len++; 
        } 
    }
    // Initial guess
    private String initialGuess(char[] alphabet, int length) {
        if(length <= 6) {
            char[] arr = new char[length];
            for (int i = 0; i < length; i++) {
                arr[i] = alphabet[i % alphabet.length];
            }
            return new String(arr);
        } else {
            String str = new String();
            for(int i = 0; i < length; i++) {
                str += "B";
            }
            return str;
        }
    }

    // Knuth's Algorithm: Generate all possible codes into given array
    private void generateAllCodes(String[] store, char[] alphabet, int length) {
        int total = store.length;
        for (int i = 0; i < total; i++) {
            char[] arr = new char[length];
            int temp = i;
            for (int pos = length - 1; pos >= 0; pos--) {
                arr[pos] = alphabet[temp % alphabet.length];
                temp /= alphabet.length;
            }
            store[i] = new String(arr);
        }
    }

    // Knuth's Algorithm: Feedback function — counts correct chars in correct position
    private int feedback(String code, String guess) {
      int correct = 0;
      for (int i = 0; i < code.length(); i++) {
        if (code.charAt(i) == guess.charAt(i)) {
            correct++;
        }
      }   
      return correct;
    }

    // Creates a new string
    public String setAt(String s, int index, char c) { 
        char[] arr = s.toCharArray(); 
        arr[index] = c; 
        return new String(arr); 
    }
}

