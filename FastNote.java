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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Cory Buck
 */
public class FastNote extends Thread{
    final static int MAX_SIZE = 250;
    final static String folder = "FastNotes/";
    protected JFrame frame;
    private JTextField note_name;
    private JTextArea text;
    
    private static int color_counter = 0;
    
    final private MenuListener new_listener, save_listener, load_listener,close_listener;
    
    private int posX, posY;
    int[] poop = {0,1,3};
    final private int[][] colors = { { 165, 255, 100}, { 255, 255, 100}, {125, 255, 255}, {125,160,255},{255,125,125}};
    final private Color color = new Color(colors[color_counter % colors.length][0],colors[color_counter % colors.length][1],colors[color_counter % colors.length][2]);
    
    public FastNote(MenuListener ml, MenuListener cl){
        color_counter++;
        new_listener = ml;
        save_listener = new MenuListener(){
            @Override
            public void menuSelected(final MenuEvent me) {
                new Timer(200, new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        
                        String file_name = note_name.getText().replace(" ","_");
                        if(!file_name.equals("New_Note")){
                            
                            File dir = new File(folder);
                            if(!dir.exists()){
                                dir.mkdir();
                            }

                            File file = new File(folder + file_name + ".fn");
                            try{
                                if(file.exists()){
                                    if(JOptionPane.showConfirmDialog(null,"Are you sure?","Overwrite Existing Note",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                        FileWriter writer = new FileWriter(file);
                                        writer.write(text.getText());
                                        writer.flush();
                                        writer.close();
                                    }
                                }else{
                                    file.createNewFile();
                                    FileWriter writer = new FileWriter(file);
                                    writer.write(text.getText());
                                    writer.flush();
                                    writer.close();                        
                                }
                            }catch(Exception e){
                                JOptionPane.showMessageDialog(frame,"The note could note be saved.");
                            }
                        }else{
                            JOptionPane.showMessageDialog(frame,"Please enter a valid note title.");
                        }
                        ((JMenu)me.getSource()).setSelected(false);
                        ((Timer)ae.getSource()).stop();
                    }                    
                }).start();
                
            }

            @Override
            public void menuDeselected(MenuEvent me) {}

            @Override
            public void menuCanceled(MenuEvent me) {}
            
        };
        load_listener = new MenuListener(){
            @Override
            public void menuSelected(final MenuEvent me) {
                new Timer(200,new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                        File fn = new File(folder);
                        File[] file_list = fn.listFiles();
                        if(file_list.length > 0){
                            ArrayList<String>  file_names = new ArrayList();
                            for(int i = 0; i < file_list.length; i++){
                                String file = file_list[i].toString();
                                if(file_list[i].isFile()){
                                    file = file.replace("_"," ").split("\\\\")[1].split("\\.")[0];
                                    file_names.add(file);
                                }
                            }
                            String[] files = new String[file_names.size()];
                            file_names.toArray(files);
                           
                            loadNote((String)JOptionPane.showInputDialog(frame,"Note:","Which Note Do You Want To Open?",JOptionPane.QUESTION_MESSAGE,null,files, files[0]));
          
                            
                        }else{
                            JOptionPane.showMessageDialog(frame,"There are no files to open.");
                        }  
                        
                        ((JMenu)me.getSource()).setSelected(false);
                        ((Timer)ae.getSource()).stop();
                    }
                    
                }).start();
            }

            @Override
            public void menuDeselected(MenuEvent me) {}

            @Override
            public void menuCanceled(MenuEvent me) {}
            
        };
        close_listener = cl;
        createQuickNote();
        addDraggable();
        text.requestFocus(true);
    }
    
    private void createQuickNote(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MAX_SIZE,MAX_SIZE);
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
        menu = new JMenu("Save");
        menu.setMnemonic(KeyEvent.VK_S);
        menu.addMenuListener(save_listener);
        menu_bar.add(menu);
        menu = new JMenu("Load");
        menu.setMnemonic(KeyEvent.VK_L);
        menu.addMenuListener(load_listener);
        menu_bar.add(menu);
        menu_bar.add(Box.createHorizontalGlue());
        menu = new JMenu("Close");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.addMenuListener(close_listener);
        menu_bar.add(menu);
        
        return menu_bar;
    }
    
    private Container createContents(){
        JPanel contents = new JPanel();
        contents.setBackground(color);
        contents.setLayout(new BorderLayout());   
        note_name = new JTextField();
        note_name.setSize(MAX_SIZE,(int)(MAX_SIZE * .3));
        note_name.setBackground(color);
        note_name.setBorder(BorderFactory.createMatteBorder(0, 0, 1,0, Color.LIGHT_GRAY));
        note_name.setText("New Note");
        note_name.setHorizontalAlignment(JTextField.CENTER);
        contents.add(note_name,BorderLayout.NORTH);
        text = new JTextArea();
        text.setLineWrap(true);
        text.setSize(MAX_SIZE, (int)(MAX_SIZE *.6));
        text.setMaximumSize(new Dimension(MAX_SIZE, MAX_SIZE));
        text.setFont(text.getFont().deriveFont(16.0f)); 
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
    
    private boolean isValidFileName(String fn){
        return true;
    }
    
    private void loadNote(String note){
        if(note != null){ 
            try{

                BufferedReader reader = new BufferedReader(new FileReader(folder + note.replace(" ","_") + ".fn"));
                String note_text = "";
                String line;
                while((line = reader.readLine())!= null) note_text += line;
                reader.close();
                note_name.setText(note);
                text.setText(note_text);
            }catch(Exception e){
                JOptionPane.showMessageDialog(frame,"Could Not Load Note.");
            }
        }        
    }
}
