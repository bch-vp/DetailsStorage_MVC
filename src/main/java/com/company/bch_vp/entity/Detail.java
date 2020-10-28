package com.company.bch_vp.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Detail {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Detail Name is required")
    private String detailName;
    private String type;
    private String production;
    @NotNull(message="Quantity is required")
    @Pattern(regexp = "^[1-9]\\d*$",message = "gorba иди нахуй/не пройдешь")
    private Integer quantityOfAll;
    private Integer quantityOfAvailable;
    @NotNull(message="Price is required")
    @Pattern(regexp = "^[1-9]\\d*$",message = "gorba иди нахуй/не пройдешь")
    private Double price;
    @NotBlank(message="Storage is required")
    private String storage;

    @OneToMany(mappedBy = "detail", fetch = FetchType.EAGER)
    private List<DetailInfo> detailsInfo = new ArrayList<>();

    //delete!
    public Detail(String detailName) {
        this.detailName = detailName;
    }

    public Detail(String detailNamem, Integer quantityOfAll){
        this.detailName=detailNamem;
        this.quantityOfAll=quantityOfAll;
        this.quantityOfAvailable=quantityOfAll;
    }

    public Detail(String detailName, String type, String production, Integer quantityOfAll, Double price, String storage) {
        this.detailName = detailName;
        this.type = type;
        this.production = production;
        this.quantityOfAll = quantityOfAll;
        //construct new detail,that's why  quantityOfAvailable == quantityOfAll
        this.quantityOfAvailable=quantityOfAll;
        this.price = price;
        this.storage = storage;
    }

    public void addDetailInfo(DetailInfo detailInfo){
        detailsInfo.add(detailInfo);
    }

    public Detail addAvailableDetails(Integer quantityOfAvailable){
        this.quantityOfAvailable+=quantityOfAvailable;
        return this;
    }

    public Detail addQuantityOfDetails(Integer quantityOfAvailable){
        this.quantityOfAvailable+=quantityOfAvailable;
        this.quantityOfAll+=quantityOfAvailable;
        return this;
    }

    public void subtractAvailableDetails(Integer quantityOfAvailable){
        this.quantityOfAvailable-=quantityOfAvailable;
    }

}
