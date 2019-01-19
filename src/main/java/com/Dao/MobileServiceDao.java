package com.Dao;

import java.sql.SQLException;

public interface MobileServiceDao {
    void queryPackage(int userid);

    void buyNewPackage(int userid, int packageid);

    void dropPackage(int userid, int packageid);

    float costByPhoneCall(int userid, int min) throws SQLException;

    float localTraffic(int userid, int traffic);

    float nationalTraffic(int userid, int traffic);

    void showMonthlyBill(int userid);
}
