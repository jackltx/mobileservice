package com.Main;
import com.DaoImlp.MobileServiceDaoImpl;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
//        queryPackage(1);//按用户编号对某用户进行套餐的查询（包括历史订购纪录）
//        buyNewPackage(1,5);//用户订购新套餐（若用户正在使用其他套餐，则新套餐下月初生效；否则立即生效）
//        dropPackage(1,5);//用户退订套餐(或取消下个月的套餐订)
//        costByPhoneCall(1,10);//用户单次通话后产生的费用
//        costByTraffic(1,2048,false);//用户使用一定流量后产生的费用(第三个参数代表使用的流量是本地或全国流量)
//        showMonthlyBill(1);//某用户月账单的生成
    }

    private static void showMonthlyBill(int userid) {
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        mobileServiceDao.showMonthlyBill(userid);
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
    }

    private static void costByTraffic(int userid, int traffic, boolean islocal) {
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        if(islocal) {
            //使用本地流量的情况
            float cost = mobileServiceDao.localTraffic(userid, traffic);
            System.out.println("用户" + userid + "本次上网使用了本地流量" + traffic + "M，共产生费用" + cost + "元！");
        }else{
            //使用全国流量的情况
            float cost = mobileServiceDao.nationalTraffic(userid, traffic);
            System.out.println("用户" + userid + "本次上网使用了全国流量" + traffic + "M，共产生费用" + cost + "元！");
        }
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
    }


    private static void costByPhoneCall(int userid, int min) {
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        float cost=mobileServiceDao.costByPhoneCall(userid,min);
        System.out.println("用户"+userid+"本次通话时长为"+min+"分钟，共产生费用"+cost+"元！");
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
    }
    private static void dropPackage(int userid, int packageid) {
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        mobileServiceDao.dropPackage(userid,packageid);
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
}

    private static void buyNewPackage(int userid, int packageid) {
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        mobileServiceDao.buyNewPackage(userid,packageid);
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
    }

    public static void queryPackage(int userid){
        long t1=System.currentTimeMillis();
        MobileServiceDaoImpl mobileServiceDao=new MobileServiceDaoImpl();
        mobileServiceDao.queryPackage(userid);
        long t2=System.currentTimeMillis();
        System.out.println("操作用时为："+(t2-t1)+"ms");
    }
}
