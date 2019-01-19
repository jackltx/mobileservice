package com.domain;

import javax.persistence.*;

@Entity
@Table(name = "orders", schema = "mobileservice", catalog = "")
public class OrdersEntity {
    private int orderid;
    private Integer userid;
    private Integer packageid;
    private String starttime;
    private String overtime;
    private AccountEntity accountByUserid;
    private PackageEntity packageByPackageid;

    @Id
    @Column(name = "orderid", nullable = false)
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    @Basic
    @Column(name = "userid", nullable = true)
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "packageid", nullable = true)
    public Integer getPackageid() {
        return packageid;
    }

    public void setPackageid(Integer packageid) {
        this.packageid = packageid;
    }

    @Basic
    @Column(name = "starttime", nullable = true, length = 10)
    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    @Basic
    @Column(name = "overtime", nullable = true, length = 10)
    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (orderid != that.orderid) return false;
        if (userid != null ? !userid.equals(that.userid) : that.userid != null) return false;
        if (packageid != null ? !packageid.equals(that.packageid) : that.packageid != null) return false;
        if (starttime != null ? !starttime.equals(that.starttime) : that.starttime != null) return false;
        if (overtime != null ? !overtime.equals(that.overtime) : that.overtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderid;
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        result = 31 * result + (packageid != null ? packageid.hashCode() : 0);
        result = 31 * result + (starttime != null ? starttime.hashCode() : 0);
        result = 31 * result + (overtime != null ? overtime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    public AccountEntity getAccountByUserid() {
        return accountByUserid;
    }

    public void setAccountByUserid(AccountEntity accountByUserid) {
        this.accountByUserid = accountByUserid;
    }

    @ManyToOne
    @JoinColumn(name = "packageid", referencedColumnName = "packageid")
    public PackageEntity getPackageByPackageid() {
        return packageByPackageid;
    }

    public void setPackageByPackageid(PackageEntity packageByPackageid) {
        this.packageByPackageid = packageByPackageid;
    }
}
