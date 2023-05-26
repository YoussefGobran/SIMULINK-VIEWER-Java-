/*  
 This Line class represents a line connection between Block elements in a flowchart.
 It contains information such as:
 - Source and destination block IDs  
 - Whether the connection is from/to an input/output port  
 - The specific port number on the source/destination block  
 - A list of corner points if the line is not straight  
 - A list of branch lines extending from this main line  

 The Line class can generate a JavaFX Polyline and Arrowhead node to display the line
 on screen, using information from a Block map to determine the proper source and
 destination coordinates.  

 Methods:  
 - getters and setters for all fields  
 - generateLineDrawing(): Generates the Polyline and Arrowhead nodes to display the line  
 - generateArrowHeadDrawing(): Helper to generate just the Arrowhead node  
 - getRatio(): Helper to calculate the y-coordinate ratio for a given port number  

 Example Usage:  
 Line line = new Line(1, 2, true, false, 0, 1, corners, branches);  
 line.generateLineDrawing(pane, blockMap); // Adds nodes to pane to draw the line   

 This class represents one possible way to model line connections in a flowchart, using  
 Block and Branch classes to represent the nodes/blocks and branch lines. Please let me
 know if you have any questions or need clarification on this code.
 */
package com.example;
//Line.java

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class Line {
    // if line has branches set dstId to -1
    // so values of isDstInput, dstPortNumber will be redundant
    private int srcId;
    private int dstId;
    // is the arrow from the source is from the input or output of the block
    private boolean isSrcInput;
    private boolean isDstInput;

    private int srcPortNumber;
    private int dstPortNumber;

    // for drawing lines with corners
    private List<Point2D> corners;
    private List<Branch> branches;

    /**
     * @param srcId
     * @param dstId
     * @param isSrcInput
     * @param isDstInput
     * @param srcPortNumber
     * @param dstPortNumber
     * @param corners
     */
    public Line(int srcId, int dstId, boolean isSrcInput, boolean isDstInput, int srcPortNumber, int dstPortNumber,
            List<Point2D> corners, List<Branch> branches) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.isSrcInput = isSrcInput;
        this.isDstInput = isDstInput;
        this.srcPortNumber = srcPortNumber;
        this.dstPortNumber = dstPortNumber;
        this.corners = corners;
        this.branches = branches;
    }

    Line() {
        this(0, 0, false, false, 0, 0, new ArrayList<Point2D>(), new ArrayList<Branch>());
    }

    /**
     * @return the srcId
     */
    public int getSrcId() {
        return srcId;
    }

    /**
     * @param srcId the srcId to set
     */
    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    /**
     * @return the dstId
     */
    public int getDstId() {
        return dstId;
    }

    /**
     * @param dstId the dstId to set
     */
    public void setDstId(int dstId) {
        this.dstId = dstId;
    }

    /**
     * @return the isSrcInput
     */
    public boolean isSrcInput() {
        return isSrcInput;
    }

    /**
     * @param isSrcInput the isSrcInput to set
     */
    public void setSrcInput(boolean isSrcInput) {
        this.isSrcInput = isSrcInput;
    }

    /**
     * @return the isDstInput
     */
    public boolean isDstInput() {
        return isDstInput;
    }

    /**
     * @param isDstInput the isDstInput to set
     */
    public void setDstInput(boolean isDstInput) {
        this.isDstInput = isDstInput;
    }

    /**
     * @return the srcPortNumber
     */
    public int getSrcPortNumber() {
        return srcPortNumber;
    }

    /**
     * @param srcPortNumber the srcPortNumber to set
     */
    public void setSrcPortNumber(int srcPortNumber) {
        this.srcPortNumber = srcPortNumber;
    }

    /**
     * @return the dstPortNumber
     */
    public int getDstPortNumber() {
        return dstPortNumber;
    }

    /**
     * @param dstPortNumber the dstPortNumber to set
     */
    public void setDstPortNumber(int dstPortNumber) {
        this.dstPortNumber = dstPortNumber;
    }

    /**
     * @return the corners
     */
    public List<Point2D> getCorners() {
        return corners;
    }

    /**
     * @param corners the corners to set
     */
    public void setCorners(List<Point2D> corners) {
        this.corners = corners;
    }

    /**
     * @return the branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "Line [srcId=" + srcId + ", dstId=" + dstId + ", isSrcInput=" + isSrcInput + ", isDstInput=" + isDstInput
                + ", srcPortNumber=" + srcPortNumber + ", dstPortNumber=" + dstPortNumber + ", corners=" + corners
                + ", branches=" + branches + "]";
    }

    static public double getRatio(int portWanted, int nPorts) {
        return ((2 * portWanted) - 1) / (2.0 * nPorts);
    }

    static public Polygon generateArrowHeadDrawing(boolean isInput, Point2D point) {
        Polygon arrowHead = new Polygon();
        if (isInput) {
            arrowHead.getPoints().addAll(new Double[] {
                    point.getX(), point.getY(),
                    point.getX() - 4, point.getY() + 4,
                    point.getX() - 4, point.getY() - 4
            });
        } else {
            arrowHead.getPoints().addAll(new Double[] {
                    point.getX(), point.getY(),
                    point.getX() + 4, point.getY() - 4,
                    point.getX() + 4, point.getY() + 4
            });
        }

        arrowHead.setFill(Color.BLACK);
        return arrowHead;
    }

    public void generateLineDrawing(Pane pane, Map<Integer, Block> blockMap) {
        Block srcBlock = blockMap.get(srcId);
        Block dstBlock = blockMap.getOrDefault(dstId, null);

        // POLYLINE DECLERATION
        Polyline line = new Polyline();
        ArrayList<Double> list = new ArrayList<Double>();
        Point2D srcPoint;

        if (srcBlock.isMirroed()) {
            isSrcInput = !isSrcInput;
        }

        if (isSrcInput) {
            srcPoint = new Point2D(srcBlock.getStart().getX(),
                    srcBlock.getStart().getY()
                            + getRatio(srcPortNumber, srcBlock.getnInputPorts())
                                    * srcBlock.getHeight());
        } else {
            srcPoint = new Point2D(srcBlock.getEnd().getX(),
                    srcBlock.getEnd().getY()
                            - getRatio(srcPortNumber, srcBlock.getnOutputPorts())
                                    * srcBlock.getHeight());

        }
        list.add(srcPoint.getX());
        list.add(srcPoint.getY());
        for (int i = 0; i < corners.size(); i++) {
            srcPoint = srcPoint.add(corners.get(i));
            list.add(srcPoint.getX());
            list.add(srcPoint.getY());
        }
        if (dstBlock != null) {
            if (dstBlock.isMirroed()) {
                isDstInput = !isDstInput;
            }
            if (isDstInput) {
                list.add(dstBlock.getStart().getX());
                list.add(dstBlock.getStart().getY()
                        + getRatio(dstPortNumber, dstBlock.getnInputPorts())
                                * dstBlock.getHeight());
            } else {
                list.add(dstBlock.getEnd().getX());
                list.add(dstBlock.getEnd().getY()
                        - getRatio(dstPortNumber, dstBlock.getnOutputPorts())
                                * dstBlock.getHeight());
            }
            double finalX = list.get(list.size() - 2);
            double finalY = list.get(list.size() - 1);
            pane.getChildren().add(generateArrowHeadDrawing(isDstInput, new Point2D(finalX, finalY)));
        } else {
            Circle circle = new Circle();
            circle.setCenterX(srcPoint.getX());
            circle.setCenterY(srcPoint.getY());
            circle.setRadius(2);
            circle.setFill(Color.BLACK);
            pane.getChildren().add(circle);
        }
        line.getPoints().addAll(list);

        pane.getChildren().add(line);
        if (!branches.isEmpty()) {
            for (int i = 0; i < branches.size(); i++) {
                branches.get(i).generateBranchDrawing(pane, blockMap, srcPoint);
            }
        }
    }

}
