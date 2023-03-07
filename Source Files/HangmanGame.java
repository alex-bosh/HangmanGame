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

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.*;
 
public class HangmanGame 
{
    // Attributes
    private static final int MAXERRORS = 6;
    
    public static void main( String[] args )
    {
       
        // Variable declaration for database file name
        String fileName;
        String[] hangmanWords = {""};
        ArrayList<String> hangmanWordsArrayList;
        int size;
        
        // Declare scanners
        Scanner input = new Scanner(System.in);;
        Scanner fileScanner;

        // Get name of database file from user and populate hangmanWords[]
        do
        {
            //intialize 
            fileName = null;
            hangmanWordsArrayList = new ArrayList<>();
  
            System.out.println("\nEnter the name of the database file => ");
            //fileName = input.nextLine(); 
            fileName = "words.txt";
            try 
            {
                fileScanner = new Scanner(new File(fileName));
                
                // Copy words from database into ArrayList
                while (fileScanner.hasNextLine())
                {
                    hangmanWordsArrayList.add(fileScanner.nextLine().trim());
                }
                // Close scanners
                fileScanner.close();
                input.close();
                
                size = hangmanWordsArrayList.size();
                if ( size < 1 )
                {
                    System.out.println( "Error: File is empty or Hangman words could not be read from the databse/file provided" );
                    fileName = null;   
                }
                else
                {
                    //populate hangmanWords array
                    hangmanWords= new String[ size ];
                    for(int i =0; i < size; i++)
                    {
                        hangmanWords[i] = hangmanWordsArrayList.get(i);
                    }
                }
            }
            catch (FileNotFoundException ex) 
            {
                System.out.println( "Error: File not found" );
                fileName = null;
            }
            catch (IndexOutOfBoundsException ex)
            {
                System.out.println( "Error: Hangman Words could not be read from the databse/file provided" );
                fileName = null;
            }
            catch (Exception ex)
            {
                Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (fileName == null);
        
        //
        //Instantiate the Hangman 
        try
        {
            Hangman hangman = new Hangman( hangmanWords ); 
        }
        catch (Exception ex)
        {
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } // end main 
}