package com.model;

public class DetailedMessage extends Message {
    private long instanceCount;

    public DetailedMessage() {}

    public DetailedMessage(long instanceCount) {
        this.instanceCount = instanceCount;
    }

    public DetailedMessage(String type, Double sellingPrice, long instanceCount) {
        super(type, sellingPrice);
        this.instanceCount = instanceCount;
    }

    public long getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(long instanceCount) {
        this.instanceCount = instanceCount;
    }
}
