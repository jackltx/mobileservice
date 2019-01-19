package com.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "account", schema = "mobileservice", catalog = "")
public class AccountEntity {
    private int userid;
    private String teleid;
    private Integer callFree;
    private Integer callUsed;
    private Integer messageFree;
    private Integer messageUsed;
    private Integer nationaltrafficFree;
    private Integer nationaltrafficUsed;
    private Integer localtrafficFree;
    private Integer localtrafficUsed;
    private BigDecimal accountBalance;
    private BigDecimal initialAccount;
    private Collection<OrdersEntity> ordersByUserid;

    @Id
    @Column(name = "userid", nullable = false)
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "teleid", nullable = true, length = 11)
    public String getTeleid() {
        return teleid;
    }

    public void setTeleid(String teleid) {
        this.teleid = teleid;
    }

    @Basic
    @Column(name = "call_free", nullable = true)
    public Integer getCallFree() {
        return callFree;
    }

    public void setCallFree(Integer callFree) {
        this.callFree = callFree;
    }

    @Basic
    @Column(name = "call_used", nullable = true)
    public Integer getCallUsed() {
        return callUsed;
    }

    public void setCallUsed(Integer callUsed) {
        this.callUsed = callUsed;
    }

    @Basic
    @Column(name = "message_free", nullable = true)
    public Integer getMessageFree() {
        return messageFree;
    }

    public void setMessageFree(Integer messageFree) {
        this.messageFree = messageFree;
    }

    @Basic
    @Column(name = "message_used", nullable = true)
    public Integer getMessageUsed() {
        return messageUsed;
    }

    public void setMessageUsed(Integer messageUsed) {
        this.messageUsed = messageUsed;
    }

    @Basic
    @Column(name = "nationaltraffic_free", nullable = true)
    public Integer getNationaltrafficFree() {
        return nationaltrafficFree;
    }

    public void setNationaltrafficFree(Integer nationaltrafficFree) {
        this.nationaltrafficFree = nationaltrafficFree;
    }

    @Basic
    @Column(name = "nationaltraffic_used", nullable = true)
    public Integer getNationaltrafficUsed() {
        return nationaltrafficUsed;
    }

    public void setNationaltrafficUsed(Integer nationaltrafficUsed) {
        this.nationaltrafficUsed = nationaltrafficUsed;
    }

    @Basic
    @Column(name = "localtraffic_free", nullable = true)
    public Integer getLocaltrafficFree() {
        return localtrafficFree;
    }

    public void setLocaltrafficFree(Integer localtrafficFree) {
        this.localtrafficFree = localtrafficFree;
    }

    @Basic
    @Column(name = "localtraffic_used", nullable = true)
    public Integer getLocaltrafficUsed() {
        return localtrafficUsed;
    }

    public void setLocaltrafficUsed(Integer localtrafficUsed) {
        this.localtrafficUsed = localtrafficUsed;
    }

    @Basic
    @Column(name = "account_balance", nullable = true, precision = 2)
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Basic
    @Column(name = "initial_account", nullable = true, precision = 2)
    public BigDecimal getInitialAccount() {
        return initialAccount;
    }

    public void setInitialAccount(BigDecimal initialAccount) {
        this.initialAccount = initialAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        if (userid != that.userid) return false;
        if (teleid != null ? !teleid.equals(that.teleid) : that.teleid != null) return false;
        if (callFree != null ? !callFree.equals(that.callFree) : that.callFree != null) return false;
        if (callUsed != null ? !callUsed.equals(that.callUsed) : that.callUsed != null) return false;
        if (messageFree != null ? !messageFree.equals(that.messageFree) : that.messageFree != null) return false;
        if (messageUsed != null ? !messageUsed.equals(that.messageUsed) : that.messageUsed != null) return false;
        if (nationaltrafficFree != null ? !nationaltrafficFree.equals(that.nationaltrafficFree) : that.nationaltrafficFree != null)
            return false;
        if (nationaltrafficUsed != null ? !nationaltrafficUsed.equals(that.nationaltrafficUsed) : that.nationaltrafficUsed != null)
            return false;
        if (localtrafficFree != null ? !localtrafficFree.equals(that.localtrafficFree) : that.localtrafficFree != null)
            return false;
        if (localtrafficUsed != null ? !localtrafficUsed.equals(that.localtrafficUsed) : that.localtrafficUsed != null)
            return false;
        if (accountBalance != null ? !accountBalance.equals(that.accountBalance) : that.accountBalance != null)
            return false;
        if (initialAccount != null ? !initialAccount.equals(that.initialAccount) : that.initialAccount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userid;
        result = 31 * result + (teleid != null ? teleid.hashCode() : 0);
        result = 31 * result + (callFree != null ? callFree.hashCode() : 0);
        result = 31 * result + (callUsed != null ? callUsed.hashCode() : 0);
        result = 31 * result + (messageFree != null ? messageFree.hashCode() : 0);
        result = 31 * result + (messageUsed != null ? messageUsed.hashCode() : 0);
        result = 31 * result + (nationaltrafficFree != null ? nationaltrafficFree.hashCode() : 0);
        result = 31 * result + (nationaltrafficUsed != null ? nationaltrafficUsed.hashCode() : 0);
        result = 31 * result + (localtrafficFree != null ? localtrafficFree.hashCode() : 0);
        result = 31 * result + (localtrafficUsed != null ? localtrafficUsed.hashCode() : 0);
        result = 31 * result + (accountBalance != null ? accountBalance.hashCode() : 0);
        result = 31 * result + (initialAccount != null ? initialAccount.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "accountByUserid")
    public Collection<OrdersEntity> getOrdersByUserid() {
        return ordersByUserid;
    }

    public void setOrdersByUserid(Collection<OrdersEntity> ordersByUserid) {
        this.ordersByUserid = ordersByUserid;
    }
}
