public class SecretCodeGuesser {
    private SecretCode code = new SecretCode(); // We'll hold a reference to the judge 
    private int length; // length of the secret code 
    private int matched; // matches from guessString "BBBB...B" 
    private String guessString;
    public void start() { 
        // Find length and set guessString = all "B"  
        length = findLength(); 
        char[] correctCode = new char[length];
        // Deduce each position 
        if (length > 9) {
            matched = code.guess(guessString); 
            for (int i = 0; i < length; i++) {  
                for (char c : new char[]{'A','C', 'X', 'I', 'U'}) { 
                    String testChar = setAt(guessString, i, c); 
                    int r = code.guess(testChar); 
                    if (r == matched + 1) { 
                        correctCode[i] = c; 
                        break; 
                    } 
                    if (r == matched - 1) { 
                        correctCode[i] = 'B'; 
                        break; 
                    }   
                } 
            }
        } else { 
            char[] alphabet = {'B', 'A', 'C', 'X', 'I', 'U'};
        int totalPossible = (int) Math.pow(alphabet.length, length);

        // Generate all possible codes in an array
        String[] possibleCodes = new String[totalPossible];
        generateAllCodes(possibleCodes, alphabet, length);

        boolean[] eliminated = new boolean[totalPossible]; // false = still possible

        // Initial guess (classic Knuth starts with "AABB" for Mastermind; adapt here)
        String guess = initialGuess(alphabet, length);
        int guessIndex = -1;

        while (true) {
            int score = code.guess(guess);
            if (score == length) {
                System.out.println("I found the secret code. It is " + guess);
                break;
            }

            // Eliminate inconsistent codes
            for (int i = 0; i < totalPossible; i++) {
                if (!eliminated[i]) {
                    if (feedback(possibleCodes[i], guess) != score) {
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
            guess = possibleCodes[bestIndex];
        } 
        }
        // Verify final guess 
        String finalGuess = new String(correctCode); 
        int finalScore = code.guess(finalGuess); 
        if (finalScore == length) { 
            System.out.println("Secret code cracked: " + finalGuess); 
        } else { 
            System.out.println("Something went wrong. Best guess: " + finalGuess); 
        } 
    } // --- Helper methods --- 
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

    public String setAt(String s, int index, char c) { 
        char[] arr = s.toCharArray(); arr[index] = c; 
        return new String(arr); 
    }

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

    private int feedback(String code, String guess) {
        int correct = 0;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == guess.charAt(i)) {
                correct++;
            }
        }
        return correct;
    }

    private String initialGuess(char[] alphabet, int length) {
        char[] arr = new char[length];
        for (int i = 0; i < length; i++) {
            arr[i] = alphabet[i % alphabet.length];
        }
        return new String(arr);
    }
}