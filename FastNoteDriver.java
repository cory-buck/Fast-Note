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

import java.awt.event.WindowEvent;
import static java.lang.Thread.sleep;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Cory Buck
 */
public class FastNoteDriver extends Thread{
    
    static private MenuListener new_listener;
    
    private static FastNote[] notes = new FastNote[10];
    private static int num_notes = 0;
    
    @Override
    public void run() {
        notes[num_notes] = new FastNote(new_listener);
        notes[num_notes].start();
        num_notes++;
     
        while(true){
            if(!notes[0].frame.isVisible() && num_notes == 1) break;
            for(int i = 0; i < num_notes; i++){
                if(!notes[i].frame.isVisible()){
                    if(num_notes == 1) break;
                    for(int j = i; j < num_notes -1; j++){
                        notes[j] = notes[j+1];
                    }
                    num_notes--;
                }
            }
            try{sleep(0);}catch(InterruptedException e){}
        }
        notes[0].frame.dispatchEvent(new WindowEvent(notes[0].frame, WindowEvent.WINDOW_CLOSING));
        
    }
    
    public static void main(String[] args) {
        new_listener = new MenuListener(){

            @Override
            public void menuSelected(MenuEvent me) {
                if(num_notes < notes.length){
                    notes[num_notes] = new FastNote(new_listener);
                    notes[num_notes].start();
                    num_notes++;
                }
            }

            @Override
            public void menuDeselected(MenuEvent me) {}

            @Override
            public void menuCanceled(MenuEvent me) {}
            
        };
        
        (new FastNoteDriver()).start();
        
    }

}
