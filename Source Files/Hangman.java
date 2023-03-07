/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Hangman Game designed and authored by:
 * @author Aemen Ali, Alex Boshnakov, Nicholas Losardo
 * 
 * Date: Dec 14, 2021
 * 
 * This class HangmanGame is a driver class which initiates Hangman
 * 
 */
package hangmangame;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Hangman extends JFrame
{
    // Declare GUI attributes 
    // Declare JFrame components i.e. Panel, Label, TextFields, Button and Window Size
    public HangmanPanel hangmanPanel;
    private JLabel welcomeLabel, resultLabel, messageLabel, guessedWordLabel, incorrectLabel;
    private JTextField guessedCharText;
    private JTextArea incorrectLettersText; 
    private JButton guessButton, playButton, exitButton;
    private final int WINDOW_WIDTH = 460;
    private final int WINDOW_HEIGHT = 300;
    private GridBagConstraints gbc;
    private String guessedChar, guessedWord, guessedWordTemp, guessedWordDisplay, wordToFind;
    private boolean repeatGameFlag, gameLost = false;
    
    // Declare program attributes
    // String Array declaration to store hangman words from database (or a file)
    private String[] hangmanWords;
    private static final int MAXERRORS = 6;
    private int errorCount, guessLeft;
    

    
    public Hangman(String[] hangmanWords)
    {
        super("Hangman"); // calls JFrame constructor first
        
        this.hangmanWords = hangmanWords;
        
        // Create GridBagLayout and GridBagConstraints;
        gbc = new GridBagConstraints();
        setLayout ( new GridBagLayout() ); // set frame layout

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); //set window size here
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //default exit behavior
        
        // Instantiate JFrame components e.e. Panel, Label, Textfeilds and Buttons 
        hangmanPanel = new HangmanPanel();
        
        welcomeLabel = new JLabel( "         Welcome to Hangman!" );
        resultLabel = new JLabel( "Number of guesses left: " + guessLeft ); // or "You Win!" or "Number of guesses left"
        incorrectLabel = new JLabel( "Incorrect Guesses:" );
        messageLabel = new JLabel( "         " );
       
        guessedWordLabel = new JLabel ( "                          " ); 
        
        guessedCharText = new JTextField( "Type the character you guess" );
        
        incorrectLettersText = new JTextArea(1, 12); // 4 by 8
        incorrectLettersText.setEditable( false );
        incorrectLettersText.setBackground(Color.LIGHT_GRAY);
        
        playButton = new JButton("Play Hangman"); // or "Play Again"
        guessButton = new JButton("Guess");
        exitButton = new JButton("Exit");

        
        //Set key attributes
        errorCount = 0;
        repeatGameFlag = false;
        setGameVisible(false); // make the Game components invisible in the begining
        
        // add components to respective panels
        setHangmanLaout();
        
        // Instantiate and bind KeyListener to recognize Enter key for guessedCharText
        guessedCharText.addKeyListener( new GuessCharTextListener() );

        // Instantiate and bind guessButton listener
        guessButton.addActionListener( new GuessButtonListener() );
        
        // Instantiate and bind guessButton listener
        playButton.addActionListener( new PlayButtonListener() );

        // Instantiate and bind ExitListener
        exitButton.addActionListener( new ExitListener() );
       
        // resetGame
        resetGame();
        
        setVisible(true);
    }
    
    // Extract and set the guessedChar
    private void getGuessedChar()
    {
        String readChar;
        readChar = guessedCharText.getText(); // return the tet of guessedCharText
        readChar = readChar.toUpperCase(); // convert to uppper case
        readChar = readChar.trim();
        if ( ! readChar.isEmpty() )
        {   
            readChar = readChar.substring(0, 1);
        }
        guessedChar = readChar;
    }
   
    // private listener classes
    //Listener for guessButton which implements ActionListener
    private class GuessButtonListener implements ActionListener 
    {    
        //Reads guessedCharText and setValues for incorrectLettersText, guessedWordLabel
        //Override the interface function of ActionListener
        @Override
        public void actionPerformed (ActionEvent e) 
        {
            getGuessedChar();
            incorrectGuessProceedToHang();
        }
     }// end of GuessButtonListener

    //Listener for textField1 which implements KeyListener
    private class GuessCharTextListener implements KeyListener 
    {    
        //Reads guessedCharText and setValues for incorrectLettersText, guessedWordLabel
        // Overrides the interface functions of KeyListener
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) 
        {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) 
            {
                getGuessedChar();
                incorrectGuessProceedToHang();
            }
        }
    }// end GuessCharTextListener
        
    // private listener classes for Play button
    //Listener for guessButton which implements ActionListener
    private class PlayButtonListener implements ActionListener 
    {  
        //Reads guessedCharText and setValues for incorrectLettersText, guessedWordLabel
        //Override the interface function of ActionListener
        @Override
        public void actionPerformed (ActionEvent e) 
        {   
            repeatGameFlag = true;
            playButton.setText("Play New Hangman");
            gameLost = false;
            setGameVisible(true); // make the Game components visible
            
            resetGame();
            System.out.printf ( "\n******Hint: %s\n", wordToFind );
        } 
     }// end of GuessButtonListener

    //Listener for exitButton implements ActionListener
    private class ExitListener implements ActionListener 
    {
        //Overrides the interface of ActionListener
        @Override
        public void actionPerformed (ActionEvent e) 
        {
             // must have a concrete actionPerformed method
             System.exit(0);
        } 
    }// end ExitListener
    
    private void setGuessedWordDisplay()
    {
        StringBuilder strB = new StringBuilder();
        
        guessedWord = guessedWord.trim(); //remove leading/trailing spaces if any
        guessedWord = guessedWord.replaceAll("\\s+", " "); // replace 1+ spaces with 1 space
        
        for (int i=0; i < guessedWord.length(); i++)
        {
            // switch statement 
            switch( guessedWord.charAt(i) )
            {
               case '*' :
                  strB.append("_ ");
                  break;  
               case ' ' :
                  if (!gameLost)
                    strB.append("      ");
                  else
                    strB.append("&nbsp;&nbsp;");
                  break; 
               default :
                  if (gameLost)
                      if (guessedWordTemp.charAt(i) == '*')
                          strB.append("<font color=red>" + guessedWord.charAt(i) + "</font>");
                      else
                          strB.append(guessedWord.charAt(i));
                  else
                      strB.append( guessedWord.charAt(i) );
                  strB.append(" ");
            }
        }
        guessedWordDisplay = strB.toString();
        if (gameLost)
            guessedWordLabel.setText("<html>" + guessedWordDisplay + "</html>");
        else
            guessedWordLabel.setText( guessedWordDisplay );
    }
    
    private void setHangmanLaout()
    {
        // Put constraints on different components to add to JFrame
        // add hangmanPanel
        gbc.anchor = GridBagConstraints.WEST; // left justified
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.weightx = 400;
        gbc.weighty = 40;
        add(hangmanPanel, gbc);
        
        // add welcomeLabel
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(welcomeLabel, gbc);

        // add playButton
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(playButton, gbc);
        
        // add exitButton
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(exitButton, gbc);
        
        // add resultLabel
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(resultLabel, gbc);
        
        // add guessedWordLabel
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(guessedWordLabel, gbc);
        
        // add incorrectLabel
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 10;
        add(incorrectLabel, gbc);
        
        // add incorrectLettersText
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 10;
        add(incorrectLettersText, gbc);
        
        // add guessedCharText
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 10;
        add(guessedCharText, gbc);
        
        // add guessButton
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 10;
        add(guessButton, gbc);  
        
        // add messageLabel
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 10;
        add(messageLabel, gbc);
    }
    
    private void applyErrorCount()
    {
        guessLeft = MAXERRORS - errorCount;
        resultLabel.setText( "Number of guesses left: " + guessLeft );
        
        hangmanPanel.setErrorCount (errorCount); // add body part to hangman in hangmanPanel
        hangmanPanel.repaint();
    }
    
    private void setGameVisible( boolean visibleFlag )
    {
        hangmanPanel.setVisible(visibleFlag);
        resultLabel.setVisible(visibleFlag);
        guessedWordLabel.setVisible(visibleFlag);
        incorrectLabel.setVisible(visibleFlag);
        incorrectLettersText.setVisible(visibleFlag);
        guessedCharText.setVisible(visibleFlag);
        guessButton.setVisible(visibleFlag);
        messageLabel.setVisible(visibleFlag);
    }
    
    private void incorrectGuessProceedToHang()
    {
        // Attributes
        String incorrectStr, newGuessedWord; ;
        StringBuilder stringBuilder;
        boolean validInput = true; //initalized to be true
        boolean alreadyProcessed = true; // initailized to be true
                
        // Initialize
        incorrectStr = incorrectLettersText.getText().trim();
        messageLabel.setText(  "                       " );
        
        if ( incorrectStr.indexOf( guessedChar)  < 0 ) // guessedChar NOT in incorrectLettersText
        {
            if ( guessedWord.indexOf( guessedChar) < 0 ) // guessedChar NOT in guessWord
            {
                alreadyProcessed = false; // Input is not already processed
            }
        }
        
        if ( guessedChar.isEmpty() ) // blank input
        {
            validInput = false;
            messageLabel.setText( "Invalid: Input is blank, try again ..." ) ;
        }
        else if ( guessedChar.matches("[^A-Z]") ) // check if input character is not A-Z
        {
            validInput = false;
            messageLabel.setText( "Invalid: Input not between A-Z, try again ..." ) ;
        }
        else if ( alreadyProcessed )
        {
            validInput = false;
            messageLabel.setText( "Invalid: Input already processed, try again ..." ) ;
        }

        //System.out.println ("");
        if ( validInput )
        {
            if ( wordToFind.indexOf( guessedChar) < 0 )  // process guessed char is not found
            {
                incorrectLettersText.setText( incorrectStr + " " + guessedChar); // add guessedChar to incorrectLettersText

                errorCount = errorCount + 1; // increment errorCount
                applyErrorCount(); // add body part to the hangman

                // Game Over and its lost, maximum attempts reached
                if ( errorCount > 5 )
                {
                    gameLost = true;
                                        
                    resultLabel.setText("Game Over! ...Hanged!");
                    guessedWordTemp = guessedWord;
                    guessedWord = wordToFind;
                    setGuessedWordDisplay();
                    
                    //make the guess component invisible
                    guessedCharText.setVisible(false);
                    guessButton.setVisible(false);
                    messageLabel.setVisible( false );
                    playButton.requestFocus();
                }
            }
            else  // process guessed char is found
            {
                // updated guessedWord as newGuessedWord
                stringBuilder = new StringBuilder();
                for (int i = 0; i < wordToFind.length(); i++) 
                {
                    if ( wordToFind.charAt(i) == guessedChar.charAt(0) ) 
                    {
                        stringBuilder.append( guessedChar ); // if guessedChar is in wordToFind
                    } 
                    else if ( guessedWord.charAt(i) != '*' )  
                    {
                        stringBuilder.append( guessedWord.charAt(i) ); // if guessedChar is in guessedWord
                    } 
                    else 
                    {
                        stringBuilder.append( "*" ); // make char as "*" if not found in wordToFind or guessedWord
                    }
                }
                newGuessedWord = stringBuilder.toString();

                guessedWord = newGuessedWord; // update guessedWord
                setGuessedWordDisplay();

                // You Won!, successfully guessed the word
                if (guessedWord.equals(wordToFind)) 
                {
                    // Won, word was guessed correctly
                    resultLabel.setText("You Won!");

                    //make the guess component invisible
                    guessedCharText.setVisible(false);
                    guessButton.setVisible(false);
                    messageLabel.setVisible( false );
                    playButton.requestFocus();
                } 
            } // end of guessedChar Found
        } // end of validInput
        
        guessedCharText.setText("                          "); // reset the guessedCharText
        guessedCharText.requestFocus();
        guessedCharText.setCaretPosition(1);
        revalidate();
        repaint();
        
    }// end of incorrectGuessProceedToHang function
    
    // reset previous game data
    private void resetGame()
    {
        // set wordToFind by randomly selecting from array of hangmanWords[]
        wordToFind = hangmanWords[(int) (Math.random() * (hangmanWords.length - 1))]; // select a random word from array
        wordToFind = wordToFind.trim(); // trim leading and trailing spaces
        wordToFind = wordToFind.replaceAll("\\s+", " "); // replace 1+ spaces with 1 space
        wordToFind = wordToFind.toUpperCase(); // convert to upper case
        
        // reset guessedWord
        guessedWord = wordToFind; // initialize to set as '_' replaced
        guessedWord = guessedWord.replaceAll("[A-Z]", "*"); // replace with '*'
        setGuessedWordDisplay(); // set guessedWordDisplay with embeded spaces

        incorrectLettersText.setText("        "); // reset incorrectLettersText
        
        //reset errorCount and hangmanPanel
        errorCount = 0;
        applyErrorCount();

        messageLabel.setText("                        " );  
        guessedCharText.setText("                        "); // reset the guessedCharText
        
        if ( repeatGameFlag )
        {
            guessedCharText.setVisible(true);
            guessButton.setVisible(true);
            resultLabel.setVisible( true );
            messageLabel.setVisible( true );

            guessedCharText.setText("                          "); // reset the guessedCharText
            guessedCharText.requestFocus();
            guessedCharText.setCaretPosition(1);
        }
        else
        {
            guessedCharText.setVisible(false);
            guessButton.setVisible(false);
            resultLabel.setVisible( false );
            messageLabel.setVisible( false );
        }
        
        revalidate();
        repaint();
        setVisible(true);
        setFocusable(true); 
    } //  end of reset function
    
}
