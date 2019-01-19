package com.domain;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "package", schema = "mobileservice", catalog = "")
public class PackageEntity {
    private int packageid;
    private Integer cost;
    private String detail;
    private Collection<OrdersEntity> ordersByPackageid;

    @Id
    @Column(name = "packageid", nullable = false)
    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    @Basic
    @Column(name = "cost", nullable = true)
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "detail", nullable = true, length = -1)
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageEntity that = (PackageEntity) o;

        if (packageid != that.packageid) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (detail != null ? !detail.equals(that.detail) : that.detail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = packageid;
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "packageByPackageid")
    public Collection<OrdersEntity> getOrdersByPackageid() {
        return ordersByPackageid;
    }

    public void setOrdersByPackageid(Collection<OrdersEntity> ordersByPackageid) {
        this.ordersByPackageid = ordersByPackageid;
    }
}
