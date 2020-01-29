package com.helper.dataparser.parser;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @author sz_qiuhf@163.com
 **/
public class Position {
    private final int[] coordinate;
    private boolean isParentNode;
    private Position parentPosition;

    Position(int[] coordinate) {
        this.coordinate = coordinate;
    }

    Position(int level, int depth) {
        this.coordinate = new int[]{level, depth};
    }

    public int[] getCoordinate() {
        return coordinate;
    }

    public Position getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int[] pCoordinate) {
        this.parentPosition = new Position(pCoordinate);
    }

    public boolean isParentNode() {
        return isParentNode;
    }

    public void setParentNode(boolean parentNode) {
        isParentNode = parentNode;
    }

    public boolean isRoot() {
        return coordinate[0] + coordinate[1] == 0;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Position.class.getSimpleName() + "", "")
                .add(Arrays.toString(coordinate))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return Arrays.equals(coordinate, position.coordinate);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinate);
    }
}
