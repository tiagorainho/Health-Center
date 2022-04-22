/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HCP.entities;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Font;

import HCP.enums.AgeGroup;
import HCP.enums.DoS;

/**
 *
 * @author BlacKugar
 */
public class GuiBox extends javax.swing.JTextField{
    
    //Border Settings and Colors
    private static final int borderWidth=2;
    private static final Border black=BorderFactory.createLineBorder(Color.black, borderWidth);;
    private static final Border red = BorderFactory.createLineBorder(Color.red,borderWidth);
    private static final Border yellow = BorderFactory.createLineBorder(Color.decode("#eeee00"),borderWidth);
    private static final Border blue = BorderFactory.createLineBorder(Color.blue,borderWidth);

    //fonts
    private static final Font seatFont = new Font(Font.SANS_SERIF,Font.PLAIN,20);
    private static final Font waitFont = new Font(Font.SANS_SERIF,Font.BOLD,10);

    //background colors
    private static final String childColor = "#FAFAFA";
    private static final String adultColor = "#E0E0E0";
    private static final String anyColor = "#EAEAEA";
    
    
    public GuiBox(AgeGroup ageGroup, int x, int y, boolean square){
        this.setHorizontalAlignment(JTextField.CENTER);
        this.colorBackground(ageGroup);
        this.setFocusable(false);
        this.setBorder(black);
        if(square){
            this.setBounds(x, y, 40, 40);
            this.setFont(seatFont);
        }else{
            this.setBounds(x,y,40,20);
            this.setFont(waitFont);
        }
    }
    
    public void colorPatient(DoS degreeOfSeverity){
        switch(degreeOfSeverity){
            case YELLOW:
                this.setBorder(yellow);
                break;
            case RED:
                this.setBorder(red);
                break;
            case BLUE:
                this.setBorder(blue);
                break;
            case BLACK:
                this.setBorder(black);
                break;
        }
    }

    public void colorBackground(AgeGroup ageGroup){
        switch(ageGroup){
            case ADULT:
                this.setBackground(Color.decode(adultColor));
                break;
            case CHILD:
                this.setBackground(Color.decode(childColor));
                break;
            default:
                this.setBackground(Color.decode(anyColor));
                break;
        }
    }
    
    public void updateBox(int id, DoS degreeOfSeverity, AgeGroup ageGroup){
        this.setText(Integer.toString(id));
        this.colorPatient(degreeOfSeverity);
        this.colorBackground(ageGroup);
    }

    public void empty(AgeGroup ageGroup){
        this.setText("");
        this.colorBackground(ageGroup);
        this.colorPatient(DoS.BLACK);
    }
}
