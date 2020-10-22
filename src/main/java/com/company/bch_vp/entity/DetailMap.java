package com.company.bch_vp.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DetailMap {
    private List<DetailForm> details=new ArrayList<>();

    public void addDetail(DetailForm detailForm){
        this.details.add(detailForm);
    }
}
