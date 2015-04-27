/*
 * The MIT License
 *
 * Copyright 2015 Cory Buck.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package FastNote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Cory Buck
 */
public class FastNote extends Thread{
    final static int MAX_SIZE = 250;
    protected JFrame frame;
    private JTextArea text;
    
    private MenuListener new_listener;
    private MenuListener close_listener;
    
    private int posX, posY;
    int[] poop = {0,1,3};
    final private int[][] colors = { { 165, 255, 100}, { 255, 255, 100}, {125, 255, 255}, {125,160,255},{255,125,125}};
    final private int color_option = (int) (Math.random() * 5);
    final private Color color = new Color(colors[color_option][0],colors[color_option][1],colors[color_option][2]);
    
    public FastNote(MenuListener ml){
        new_listener = ml;
        createQuickNote();
        addDraggable();
    }
    
    private void createQuickNote(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(150,150);
        frame.setLocation((int)(Math.random()* 500),(int) (Math.random() * 500));
        frame.setUndecorated(true);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.lightGray));
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createContents());
        frame.setVisible(true);       
    }
    
    private JMenuBar createMenuBar(){
        JMenuBar menu_bar = new JMenuBar();
        menu_bar.setBackground(color);
        JMenu menu = new JMenu("New");
        
        menu.setMnemonic(KeyEvent.VK_N);
        menu.addMenuListener(new_listener);
        menu_bar.add(menu);
        menu_bar.add(Box.createHorizontalGlue());
        menu = new JMenu("Close");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent me) {
                if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your Quick Note?","WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    frame.setVisible(false);
                }
            }
            @Override
            public void menuDeselected(MenuEvent me) {}
            @Override
            public void menuCanceled(MenuEvent me) {}
        });
        menu_bar.add(menu);
        
        return menu_bar;
    }
    
    private Container createContents(){
        JPanel contents = new JPanel();
        contents.setBackground(color);
        contents.setLayout(new BorderLayout());   
        text = new JTextArea();
        text.setLineWrap(true);
        text.setSize(MAX_SIZE, MAX_SIZE);
        text.setMaximumSize(new Dimension(MAX_SIZE, MAX_SIZE));
        text.setFont(text.getFont().deriveFont(18.0f)); 
        text.setBackground(color);
        text.setEditable(true);
        contents.add(text,BorderLayout.CENTER);
        return contents;        
    }
    
    private void addDraggable(){
        frame.addMouseListener(new MouseAdapter(){
           @Override
           public void mousePressed(MouseEvent e){
               posX = e.getX();
               posY = e.getY();
           } 
        });
        frame.addMouseMotionListener(new MouseAdapter(){
           @Override
           public void mouseDragged(MouseEvent e){
               frame.setLocation(e.getXOnScreen()-posX, e.getYOnScreen()-posY);
           } 
        });
    }
}
