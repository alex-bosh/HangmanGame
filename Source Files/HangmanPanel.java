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

// Draw Hangman Body Parts

package hangmangame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class HangmanPanel extends JPanel
{
    private int errorCount;
    private static final int MAXERRORS = 6;
    
    // Constructor for HangmanPanel here...
    public HangmanPanel()
    {
        errorCount = 0; // initialize errorCount to zero in the beginning
    }
    
    // set errorCount
    public void setErrorCount( int i_errorCount )
    {
        errorCount = i_errorCount;
    }
    
    // get errorCountl
    public int getErrorCount()
    {
        return errorCount;
    }
    
    // draw Hangman frame and body parts
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g ); // call superclass's paintComponent
        
        Graphics2D g2d = ( Graphics2D ) g; // cast g to Graphics2D
        
        // Erect a frame to hang
        g2d.setPaint( Color.BLACK);
            
        // draw frame rectangle boxes
        // base horizontal rectangle
        g2d.fillRect(8, 144, 56, 4);

        // pole vertical rectangle
        g2d.fillRect(12, 12, 4, 136);

        // top horizontal rectangle
        g2d.fillRect(16, 12, 32, 4);

        // hanging pole vertical rectangle
        g2d.fillRect(44, 12, 4, 24);

        
        // Add Body Parts if more than 1 error
        //
        // body part stroke size = 4 and when alive color is blue otherwise red
        g2d.setStroke( new BasicStroke ( 2.0f ) ); // set stroke width here
        
        // if alive color the body blue; after death color body red;
        if ( errorCount < MAXERRORS ) // alive color is blue
        {
            g2d.setColor( Color.BLUE );
        }
        else // died color is red
        {
            g2d.setColor( Color.RED );
        }
        
        // On second error add body part - head
        if ( errorCount > 0 )
        {
            g2d.drawOval(38, 36, 16, 16);
        }
        
        // On third error add body part - torso
        if ( errorCount > 1 )
        {
            g2d.drawLine(46, 52, 46, 84);
        }
        
        // On forth error add body part - left leg
        if ( errorCount > 2 )
        {
            g2d.drawLine(46, 84, 34, 100);
        }
        
        // On fifth error add body part - right leg
        if ( errorCount > 3 )
        {
            g2d.drawLine(46, 84, 58, 100);
        }
        
        // On sixth error add body part - left hand
        if ( errorCount > 4 )
        {
            g2d.drawLine(46, 68, 34, 58);
        }
        
        // on seventh error add body part - right hand 
        if ( errorCount > 5 )
        {
            g2d.drawLine(46, 68, 58, 58);
        }
    }
}