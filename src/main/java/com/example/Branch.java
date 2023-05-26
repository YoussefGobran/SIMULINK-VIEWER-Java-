package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;

public class Branch {
    private int dstId;
    private boolean isDstInput;
    private int dstPortNumber;

    // for drawing lines with corners
    private List<Point2D> corners;

    /**
     * @param dstId
     * @param isDstInput
     * @param dstPortNumber
     * @param branchPoint
     * @param corners
     */
    public Branch(int dstId, boolean isDstInput, int dstPortNumber, List<Point2D> corners) {
        this.dstId = dstId;
        this.isDstInput = isDstInput;
        this.dstPortNumber = dstPortNumber;

        this.corners = corners;
    }

    Branch() {
        this(0, false, 0, new ArrayList<Point2D>());
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

    @Override
    public String toString() {
        return "Branch [dstId=" + dstId + ", isDstInput=" + isDstInput + ", dstPortNumber=" + dstPortNumber
                + ", corners=" + corners + "]";
    }

    public void generateBranchDrawing(Pane pane, Map<Integer, Block> blockMap, Point2D startingPoint) {
        Polyline line = new Polyline();
        ArrayList<Double> list = new ArrayList<Double>();

        Block dstBlock = blockMap.get(dstId);
        list.add(startingPoint.getX());
        list.add(startingPoint.getY());
        for (int i = 0; i < corners.size(); i++) {
            startingPoint = startingPoint.add(corners.get(i));
            list.add(startingPoint.getX());
            list.add(startingPoint.getY());
        }
        if (dstBlock.isMirroed()) {
            isDstInput = !isDstInput;
        }
        if (isDstInput) {
            list.add(dstBlock.getStart().getX());
            list.add(dstBlock.getStart().getY()
                    + Line.getRatio(dstPortNumber, dstBlock.getnInputPorts())
                            * dstBlock.getHeight());
        } else {
            list.add(dstBlock.getEnd().getX());
            list.add(dstBlock.getEnd().getY()
                    - Line.getRatio(dstPortNumber, dstBlock.getnOutputPorts())
                            * dstBlock.getHeight());
        }

        line.getPoints().addAll(list);
        double finalX = list.get(list.size() - 2);
        double finalY = list.get(list.size() - 1);
        pane.getChildren().add(Line.generateArrowHeadDrawing(isDstInput, new Point2D(finalX, finalY)));

        pane.getChildren().add(line);
    }
}
