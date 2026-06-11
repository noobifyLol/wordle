/*
 *wordle.java
 *
 * prince wang
 * 
 */

public class wordle {

    private WordleGWindow gw;
    private String hiddenWord;

    public void run() {
        gw = new WordleGWindow();
        gw.addEnterListener((s) -> enterAction(s));
        
        int randomIndex = (int) (Math.random() * WordleDictionary.FIVE_LETTER_WORDS.length);
        hiddenWord = WordleDictionary.FIVE_LETTER_WORDS[randomIndex].toUpperCase();
    }

    public void enterAction(String s) {
        String guess = s.toUpperCase();
        // checks words list
        if (!isValidWord(guess)) {
            gw.showMessage("Not in word list");
            return;
        }
        
        gw.showMessage("");
        colorBoxesAndKeys(guess);
        
        if (guess.equals(hiddenWord)) {
            gw.showMessage("Impressive!");
        } else {
            int currentRow = gw.getCurrentRow();
            if (currentRow >= WordleGWindow.N_ROWS - 1) { // to next line
                gw.showMessage("The word was " + hiddenWord);
            } else {
                gw.setCurrentRow(currentRow + 1);
            }
        }
    }
    
    private boolean isValidWord(String guess) {
        String lowerGuess = guess.toLowerCase(); 
        for (String word : WordleDictionary.FIVE_LETTER_WORDS) {
            if (word.equals(lowerGuess)) {
                return true;
            }
        }
        return false;
    }

    private void colorBoxesAndKeys(String guess) {
        int row = gw.getCurrentRow();
        java.awt.Color[] squareColors = new java.awt.Color[WordleGWindow.N_COLS];
        boolean[] hiddenLetterMatched = new boolean[WordleGWindow.N_COLS];
        boolean[] guessLetterMatched = new boolean[WordleGWindow.N_COLS];
        
        for (int i = 0; i < WordleGWindow.N_COLS; i++) {
            if (guess.charAt(i) == hiddenWord.charAt(i)) {
                squareColors[i] = WordleGWindow.CORRECT_COLOR;
                hiddenLetterMatched[i] = true;
                guessLetterMatched[i] = true;
            }
        }
        
        for (int i = 0; i < WordleGWindow.N_COLS; i++) {
            if (!guessLetterMatched[i]) {
                boolean found = false;
                for (int j = 0; j < WordleGWindow.N_COLS; j++) {
                    if (!hiddenLetterMatched[j] && guess.charAt(i) == hiddenWord.charAt(j)) {
                        squareColors[i] = WordleGWindow.PRESENT_COLOR;
                        hiddenLetterMatched[j] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    squareColors[i] = WordleGWindow.MISSING_COLOR;
                }
            }
        }
        
        for (int i = 0; i < WordleGWindow.N_COLS; i++) {
            gw.setSquareColor(row, i, squareColors[i]);
            updateKeyColor(guess.substring(i, i + 1), squareColors[i]);
        }
    }
    
    private void updateKeyColor(String letter, java.awt.Color newColor) {
        java.awt.Color currentColor = gw.getKeyColor(letter);
        
        if (WordleGWindow.CORRECT_COLOR.equals(currentColor)) {
            return;
        }
        if (WordleGWindow.PRESENT_COLOR.equals(currentColor) && WordleGWindow.MISSING_COLOR.equals(newColor)) {
            return;
        }
        gw.setKeyColor(letter, newColor);
    }

    public static void main(String[] args) {
        new wordle().run();
    }
}