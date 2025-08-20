public class SecretCodeGuesser {
    private SecretCode code = new SecretCode(); // We'll hold a reference to the judge 
    private int length; // length of the secret code 
    private int matched; // matches from guessString "BBBB...B" 
    private String guessString;
    public void start() { 
        // Find length and set guessString = all "B"  
        length = findLength(); 
        matched = code.guess(guessString); 
        char[] correctCode = new char[length]; 
        // Deduce each position 
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
}