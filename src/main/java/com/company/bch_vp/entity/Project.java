package com.company.bch_vp.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    private String projectName;
    private String type;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    @NotBlank(message = "Storage is required")
    private String storage;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<DetailInfo> detailsInfo = new ArrayList<>();

    public void addDetailInfo(DetailInfo detailInfo){
        detailsInfo.add(detailInfo);
    }

    //delete!
    public Project(String projectName) {
        this.projectName = projectName;
    }

    public Project(String projectName, String type, Integer quantity, String storage) {
        this.projectName = projectName;
        this.type = type;
        this.quantity = quantity;
        this.storage = storage;
    }
}
