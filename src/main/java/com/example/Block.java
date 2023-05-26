package com.example;


import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Block {
    private String name;
    private int id;
    private Point2D start;
    private Point2D end;
    private int nInputPorts;
    private int nOutputPorts;
    private boolean isMirroed;

    public boolean isMirroed() {
        return isMirroed;
    }

    public void setMirroed(boolean isMirroed) {
        this.isMirroed = isMirroed;
    }

    /**
     * @param name
     * @param id
     * @param start
     * @param end
     * @param nInputPorts
     * @param nOutputPorts
     */
    public Block(String name, int id, Point2D start, Point2D end, int nInputPorts, int nOutputPorts,boolean isMirroed) {
        this.name = name;
        this.id = id;
        this.start = start;
        this.end = end;
        this.nInputPorts = nInputPorts;
        this.nOutputPorts = nOutputPorts;
        this.isMirroed=isMirroed;
    }

    Block() {
        this("", 0, new Point2D(0, 0), new Point2D(0, 0), 0, 0,false);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the start
     */
    public Point2D getStart() {
        return start;
    }
public int getWidth(){
    return (int)Math.abs(end.getX()-start.getX());
}
public int getHeight(){
    return (int)Math.abs(end.getY()-start.getY());
}
    /**
     * @param start the start to set
     */
    public void setStart(Point2D start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Point2D getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Point2D end) {
        this.end = end;
    }

    /**
     * @return the nInputPorts
     */
    public int getnInputPorts() {
        return nInputPorts;
    }

    /**
     * @param nInputPorts the nInputPorts to set
     */
    public void setnInputPorts(int nInputPorts) {
        this.nInputPorts = nInputPorts;
    }

    /**
     * @return the nOutputPorts
     */
    public int getnOutputPorts() {
        return nOutputPorts;
    }

    /**
     * @param nOutputPorts the nOutputPorts to set
     */
    public void setnOutputPorts(int nOutputPorts) {
        this.nOutputPorts = nOutputPorts;
    }
    Rectangle getRectangleDrawing(){
        // block setting
        Rectangle rectangle = new Rectangle();
        rectangle.setX(start.getX()); 
        rectangle.setY(start.getY()); 
        rectangle.setWidth(Math.abs(end.getX()-start.getX())); 
        rectangle.setHeight(Math.abs(end.getY()-start.getY())); 
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLUE);

        return rectangle;
    }
    Label getLabelDrawing(){
        //Block label setting
            Label text = new Label(name);
            text.setLayoutX(start.getX());
            text.setLayoutY(end.getY()+2);
            // text.setAlignment(Pos.CENTER);
            text.setTextFill(Color.BLACK);
            text.setFont(new Font("Consolas",12));
        
        return text;
    }
    @Override
    public String toString() {
        return "Block [name=" + name + ", id=" + id + ", start=" + start + ", end=" + end + ", nInputPorts="
                + nInputPorts + ", nOutputPorts=" + nOutputPorts + "]";
    }

}
