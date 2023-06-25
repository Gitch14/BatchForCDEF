package com.example.batchforcdef.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "transactionRecord")
public class Transaction {

    @XmlElement(name = "stringNumber")
    private String stringNumber;

    @XmlElement(name = "svc2Agr2")
    private String svc2Agr2;

    @XmlElement(name = "company")
    private String company;

    @XmlElement(name = "bmoServAcc2Agr2")
    private String bmoServAcc2Agr2;

    @XmlElement(name = "ptc")
    private String ptc;

    @XmlElement(name = "abc")
    private String abc;
}
