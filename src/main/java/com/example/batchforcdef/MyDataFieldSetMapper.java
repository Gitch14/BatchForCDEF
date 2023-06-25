package com.example.batchforcdef;

import com.example.batchforcdef.model.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class MyDataFieldSetMapper implements FieldSetMapper<Transaction> {

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) {
        Transaction myData = new Transaction();
        myData.setStringNumber(fieldSet.readString(0));
        myData.setSvc2Agr2(fieldSet.readString(1));
        myData.setCompany(fieldSet.readString(2));
        myData.setBmoServAcc2Agr2(fieldSet.readString(3));
        myData.setPtc(fieldSet.readString(4));
        myData.setAbc(fieldSet.readString(5));

        return myData;
    }
}
