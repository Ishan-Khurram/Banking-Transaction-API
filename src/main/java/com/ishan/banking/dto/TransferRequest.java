package com.ishan.banking.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private Long sourceId;
    private Long destinationId;
    private BigDecimal amount;

    public Long getSourceId(){
        return this.sourceId;
    }

    public Long getDestinationId(){
        return this.destinationId;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }

    public void setSourceId(Long id){
        this.sourceId = id;
    }

    public void setDestinationId(Long id){
        this.destinationId = id;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
}
