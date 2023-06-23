package com.example.batchforcdef.model;



import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuppressWarnings("restriction")
@XmlRootElement(name = "transactionRecord")
public class Transaction {


    private String stringNumber;
    private String svc2Agr2;
    private String company;
    private String bmoServAcc2Agr2;
    private String ptc;
    private String abc;


}
