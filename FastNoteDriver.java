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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import static java.lang.Thread.sleep;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Cory Buck
 */
public class FastNoteDriver{
    
    static private MenuListener new_listener;
    static private MenuListener[] close_listener = new MenuListener[10];
    
    private static FastNote[] notes = new FastNote[10];
    private static int num_notes = 0;
    
    public FastNoteDriver(){
        setCloseNoteListener(0);
        setNewNoteListener();
        
        notes[0] = new FastNote(new_listener, close_listener[0]);
        notes[0].start();
        num_notes++;
    }
    
    public void setCloseNoteListener(final int i){
        close_listener[i] = new MenuListener(){
            @Override
            public void menuSelected(final MenuEvent me) {
                new Timer(200, new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if(JOptionPane.showConfirmDialog(null, "Are you sure?","Close Fast Note",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            if(num_notes == 1){
                                notes[i].frame.dispatchEvent(new WindowEvent(notes[i].frame, WindowEvent.WINDOW_CLOSING));
                            }else{
                                notes[i].frame.setVisible(false);
                                num_notes--;
                            }
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
    }
    
    public void setNewNoteListener(){
        new_listener = new MenuListener(){
            @Override
            public void menuSelected(MenuEvent me) {
                ((JMenu)me.getSource()).setSelected(false);
                if(num_notes < notes.length){
                    for(int i = 0; i < notes.length; i++){
                        if(notes[i] != null){
                            if(!notes[i].frame.isVisible()){
                                setCloseNoteListener(i);
                                notes[i] = new FastNote(new_listener, close_listener[i]);
                                notes[i].start();
                                break;
                            }
                        }else{
                            setCloseNoteListener(i);
                            notes[i] = new FastNote(new_listener, close_listener[i]);
                            notes[i].start();
                            break;
                        }
                    }
                    num_notes++;
                }
                
            }

            @Override
            public void menuDeselected(MenuEvent me) {}

            @Override
            public void menuCanceled(MenuEvent me) {}
            
        };
    }

    public static void main(String[] args) {
        new FastNoteDriver();
        
    }

}
