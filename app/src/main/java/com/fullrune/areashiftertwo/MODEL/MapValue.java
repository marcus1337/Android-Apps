package com.fullrune.areashiftertwo.MODEL;

public enum MapValue {
    LINE(2), WALL(1), EMPTY(0), EDGE(3);

    private final int id;
    MapValue(int id) { this.id = id; }
    public int getValue() { return id; }
}
