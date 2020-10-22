package com.company.bch_vp.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Entity
public class DetailInfo {

//classes
//    @Embeddable
//    @NoArgsConstructor
//    public static class Id implements Serializable {
//
//        @Column(name = "detail_id")
//        protected Long detailId;
//        @Column(name = "project_id")
//        protected Long projectId;
//
//        public Id(Long detailyId, Long projectId) {
//            this.detailId = detailyId;
//            this.projectId = projectId;
//        }
//
//        public boolean equals(Object o) {
//            if (o != null && o instanceof Id) {
//                Id that = (Id) o;
//                return this.detailId.equals(that.detailId)
//                        && this.projectId.equals(that.projectId);
//            }
//            return false;
//        }
//
//        public int hashCode() {
//            return detailId.hashCode() + projectId.hashCode();
//        }
//    }

//fields
    @EmbeddedId
    @Getter @Setter(AccessLevel.NONE)
    private IdDetailInfo id = new IdDetailInfo();

    @Getter @Setter
    private Integer quantityDetailsUsed;

    @Getter @Setter
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="detail_id",insertable = false, updatable = false)
    private Detail detail;

    @Getter @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="project_id", insertable = false, updatable = false)
    private Project project;

    public DetailInfo(Integer quantityDetailsUsed, Detail detail, Project project) {
        this.quantityDetailsUsed = quantityDetailsUsed;
        this.detail = detail;
        this.project = project;

        this.id.detailId=detail.getId();
        this.id.projectId=project.getId();

        project.getDetailsInfo().add(this);
        detail.getDetailsInfo().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof DetailInfo)) return false;
        DetailInfo detailInfo = (DetailInfo) o;
        return Objects.equals(getDetail(), detailInfo.getDetail()) &&
                Objects.equals(getProject(), detailInfo.getProject()) &&
                Objects.equals(getId().getDetailId(), detailInfo.getId().getDetailId()) &&
                Objects.equals(getId().getProjectId(), detailInfo.getId().getProjectId()) &&
                Objects.equals(getQuantityDetailsUsed(), detailInfo.getQuantityDetailsUsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
