package com.model;

import com.enums.OperationType;

public class AdjustmentMessage extends Message {
    
	private OperationType operationType;

    public AdjustmentMessage() {}

    public AdjustmentMessage(OperationType operationType) {
        this.operationType = operationType;
    }

    public AdjustmentMessage(String type, Double sellingPrice, OperationType operationType) {
        super(type, sellingPrice);
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
