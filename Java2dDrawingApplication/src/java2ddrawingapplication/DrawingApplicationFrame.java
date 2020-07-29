/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

/**
 *
 * @author mohitbhushan
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    
    // line and one to contain both of those panels.
    private final JPanel firstLinePanel;
    private final JPanel secondLinePanel;
    private final JPanel topPanel;
    

    // create the widgets for the firstLine Panel.
    private final JButton Undo;
    private final JButton Clear;
    private final JLabel textShape;
    private final String[] ThreeShapes = {"line", "oval", "rectangle"};
    
    private final JComboBox Shape;
    private final JCheckBox Filled;

    //create the widgets for the secondLine Panel.
    private final JCheckBox UseGradient;
    private final JButton firstColor;
    private final JButton secondColor;
    private final JLabel LineText;
    private final JTextField LineWidth;
    private String lineWidth;
    private final JLabel DashText;
    private String dashLength;
    private final JTextField DashLength;
    private final JCheckBox Dashed;
    
    // Variables for drawPanel.
    ArrayList < MyShapes > shapes = new ArrayList<>(); 
    private DrawPanel drawPanel;
   
    private BasicStroke stroke = new BasicStroke();
    private Color color1 = Color.LIGHT_GRAY;
    private Color color2 = Color.LIGHT_GRAY;

    // add status label
    private final JPanel statusLabel;
    public final JLabel statusLabelText;
  
    
    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawnings");
        
        
        // add widgets to panels
        firstLinePanel = new JPanel();
        secondLinePanel = new JPanel();
        topPanel = new JPanel();
        drawPanel = new DrawPanel();
        
        statusLabel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        
        statusLabel.setLayout(new BorderLayout());
        
             
        // firstLine widgets
        Undo = new JButton("Undo");
        Clear = new JButton("Clear");
        textShape = new JLabel("Shape:");
        Shape = new JComboBox(ThreeShapes);
        Filled = new JCheckBox("Filled");
        
        firstLinePanel.add(Undo);
        firstLinePanel.add(Clear);
        firstLinePanel.add(textShape);
        firstLinePanel.add(Shape);
        firstLinePanel.add(Filled);


        // secondLine widgets
        UseGradient = new JCheckBox("Use Gradient");
        firstColor = new JButton("1st Color");
        secondColor = new JButton("2nd Color");
        LineWidth = new JTextField();
        LineText = new JLabel("Line Width");
        DashLength = new JTextField();
        DashText = new JLabel("Dash Length");
        Dashed = new JCheckBox("Dashed");
        
        
        secondLinePanel.add(UseGradient);
        secondLinePanel.add(firstColor);
        secondLinePanel.add(secondColor);
        secondLinePanel.add(LineText);
        secondLinePanel.add(LineWidth);
        secondLinePanel.add(DashText);
        secondLinePanel.add(DashLength);
        secondLinePanel.add(Dashed);

        //statusLabel widgets
        statusLabelText = new JLabel("Test");
        statusLabel.add(statusLabelText);
        
        // add top panel of two panels
        topPanel.add(firstLinePanel,BorderLayout.NORTH);
        topPanel.add(secondLinePanel,BorderLayout.SOUTH);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel,BorderLayout.CENTER);
        add(statusLabelText,BorderLayout.SOUTH);
        
        
        
        //add listeners and event handlers
        //first color
        ButtonHandler handler = new ButtonHandler();
        
        
        firstColor.addActionListener(handler);
        secondColor.addActionListener(handler);
        LineWidth.addActionListener(handler);
        DashLength.addActionListener(handler);
        Undo.addActionListener(handler);
        Clear.addActionListener(handler);
        
        
    }

    // Create event handlers, if needed
    private class ButtonHandler implements ActionListener{
        @Override
        //when action performed method is called 
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == firstColor){
                color1 = JColorChooser.showDialog(DrawingApplicationFrame.this,"Choose a Color", color1);
            
                
            } 
            else if(e.getSource() == LineWidth){
                lineWidth = LineWidth.getText();
                
            }
            else if(e.getSource() == DashLength){
                dashLength= DashLength.getText();
            }
            else if(e.getSource() == secondColor){
                color2 = JColorChooser.showDialog(DrawingApplicationFrame.this,"Choose a Color", color2);
            }
            else if(e.getSource() == Clear){
                shapes.clear();
                repaint();
            }
            else if (e.getSource() == Undo){
                
                shapes.remove(shapes.size() -1);
                repaint();
            }
            if (color1 == null){
                color1 = Color.LIGHT_GRAY;
                
            
                //clear: clear arraylist and repaint
                //undo: remove last elememtn in arraylist and repaint
                
            }
            
            
        }
        
    }
    

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {

            
            MouseHandler mousehandler = new MouseHandler();
            
            setBackground(Color.white);
            
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseHandler());
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for(MyShapes shape : shapes){
                shape.draw(g2d);
            }

        }


        private class MouseHandler extends MouseAdapter implements MouseListener, MouseMotionListener
        {
            
            
            @Override
            public void mousePressed(MouseEvent event)
                    
            {
                Paint paint;
                int matchShape;
                float lineLength = Float.parseFloat(LineWidth.getText());
                float[] dashWidth = {Float.parseFloat(DashLength.getText())};

                int startX = event.getX();
                int startY = event.getY();
                Point startPoint = new Point(startX,startY);
                
                Boolean filled = false;
                // if gradient is selected add new paint 
                if (UseGradient.isSelected()){
                     paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                    }
                else{
                    paint = color1;
                }
                
                //creates dashed line if selected
                if (Dashed.isSelected()){
                     
                    stroke = new BasicStroke(lineLength, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashWidth, 0);
                    } 
              
                else{
                    stroke = new BasicStroke(lineLength, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                
                   
                    }
                if (Filled.isSelected()){
                    filled = true;
                }
                
                
                matchShape = Shape.getSelectedIndex();
                //if shape is line
                if(matchShape == 0){
                    shapes.add(new MyLine(event.getPoint(), event.getPoint(),paint, stroke));
                                       
                }
                //if shape is oval
                if(matchShape == 1){
                    shapes.add(new MyOval(event.getPoint(), event.getPoint(), paint, stroke, filled ));
                }
                //if shape is a rectangle
                if(matchShape == 2){
                    shapes.add(new MyRectangle(event.getPoint(), event.getPoint(), paint, stroke, filled));
                }
                
                
                }   
                
        
            @Override
            public void mouseReleased(MouseEvent event)
            {
                //update status position
                String position = "(" + event.getPoint().x +","+ event.getPoint().y +")";
                statusLabelText.setText(position);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                
               
                
                
                shapes.get(shapes.size()-1).setEndPoint(event.getPoint());
                
                
                String position = "(" + event.getPoint().x +","+ event.getPoint().y +")";
                statusLabelText.setText(position);
                
                
                repaint();
            }

            
            
            
            @Override
            public void mouseMoved(MouseEvent event)
            {
               
                //String position = "(" + event.getPoint().x +","+ event.getPoint().y +")";
                statusLabelText.setText(String.format("(%d,%d)", event.getX(), event.getY()));
            }
        }
    }
}

    
    

