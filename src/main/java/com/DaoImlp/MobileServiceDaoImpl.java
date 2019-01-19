package com.DaoImlp;
import com.Dao.MobileServiceDao;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class MobileServiceDaoImpl implements MobileServiceDao {
    //初始化，加载数据库类对象
    public MobileServiceDaoImpl(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取数据库连接
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/mobileservice","root","930908");
    }
    //查询套餐（及历史套餐）
    public void queryPackage(int userid) {
        try {
            Connection con=getConnection();
            String s="select * from orders where userid="+userid;//该用户所购买过的订单
            Statement statement=con.createStatement();
            ResultSet resultSet=statement.executeQuery(s);
            //记录用户订购的套餐数量
            int rowCount = 0;
            //列出用户历史上每次订购的套餐
            while(resultSet.next()){
                rowCount++;
                String s1=resultSet.getString("starttime");
                String s2=resultSet.getString("overtime");
                if(s2==null){
                    s2="现在";
                }
                int pid=resultSet.getInt("packageid");
                String s3="select detail from package where packageid="+pid;//对应套餐的详情
                Statement statement1=con.createStatement();
                ResultSet resultSet1=statement1.executeQuery(s3);
                String detail=null;
                if(resultSet1.next()){
                    detail=resultSet1.getString("detail");
                }
                //输出套餐起始于终止时间，并显示详情
                System.out.println("用户"+userid+"从"+s1+"到"+s2+"订购的套餐是"+pid+"号套餐。套餐内容如下：\n\t\t"+detail);
            }
            //如果用户订过的套餐数为0
            if(rowCount==0){
                System.out.println("用户"+userid+"没有订购套餐");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //购买、换购新套餐
    public void buyNewPackage(int userid, int packageid) {
        try {
            Connection con=getConnection();
            Statement s=con.createStatement();
            String date=getPresentDate();
            String lastDateOfMonth=getLastDateOfMonth(date);
            String firstdateOfNextmonth=getFirstdateOfNextmonth(date);
            //找出该用户目前正在使用的套餐
            ResultSet resultSet=s.executeQuery("select * from orders where userid="+userid+" and overtime is null");
            //如果目前有正进行的套餐
            if(resultSet.next()){
                //如果用户重复订购同一个套餐
                if(resultSet.getInt("packageid")==packageid){
                    System.out.println("此用户正在使用套餐"+packageid+"，无需再次订购！");
                }else {//为用户进行换购套餐服务
                    Statement statement = con.createStatement();
                    //将用户正在使用的套餐设置为月底失效
                    String update = "update orders set overtime='" + lastDateOfMonth + "'where userid=" + userid + " and overtime is null";
                    ////int money=packageCost(packageid);
                    ////String payMoney="update account set account_balance=account_balance-"+money+" where userid="+userid;
                    statement.addBatch(update);
                    ////statement.addBatch(payMoney);
                    statement.executeBatch();
                    //产生新的套餐订单插入订单表中
                    String insert = "INSERT INTO orders(userid,packageid,starttime,overtime) VALUES(?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(insert);
                    ps.setInt(1, userid);
                    ps.setInt(2, packageid);
                    ps.setString(3, firstdateOfNextmonth);
                    ps.setString(4, null);
                    ps.execute();
                    System.out.println("您已经换购了新套餐" + packageid + "，原套餐将于本月底失效，新套餐将于下月初生效！");
                }
                //如果目前没有正在使用的套餐
            } else{
                int money=packageCost(packageid);
                //扣除话费
                String payMoney="update account set account_balance=account_balance-"+money+" where userid="+userid;
                //将套餐优惠通话时间、免费流量加入该用户账户中
                addFree(userid,packageid);
                s.executeUpdate(payMoney);
                //产生的新订单插入订单表中
                String update="insert into orders(userid,packageid,starttime,overtime) values(?,?,?,?)";
                PreparedStatement ps=con.prepareStatement(update);
                ps.setInt(1,userid);
                ps.setInt(2,packageid);
                ps.setString(3,date);
                ps.setString(4,null);
                ps.execute();
                System.out.println("用户"+userid+"从即日起订购了套餐"+packageid+",立刻生效!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //把选择套餐内的免费通话时间和流量加入用户账户中
    private void addFree(int userid, int packageid) {
        String s=null;
        if(packageid==1){
            s="update account set call_free=call_free+100 where userid="+userid;
        }else if(packageid==2){
            s="update account set message_free=message_free+200 where userid="+userid;
        }else if(packageid==3){
            s="update account set localtraffic_free=localtraffic_free+2048 where userid="+userid;
        }else if(packageid==4){
            s="update account set nationaltraffic_free=nationaltraffic_free+2048 where userid="+userid;
        }else{
            s="update account set call_free=call_free+100,message_free=message_free+200,nationaltraffic_free=nationaltraffic_free+2048 where userid="+userid;
        }
        try {
            Connection con = getConnection();
            Statement statement=con.createStatement();
            statement.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//各套餐的收费情况
    private int packageCost(int packageid) {
        try {
            Connection con=getConnection();
            String s="select cost from package where packageid="+packageid;
            Statement statement=con.createStatement();
            ResultSet resultSet=statement.executeQuery(s);
            if(resultSet.next()){
                int cost=resultSet.getInt("cost");
                return cost;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
//退订（或取消下月的预订）
    public void dropPackage(int userid, int packageid) {
        String date=getPresentDate();
        try {
            Connection con=getConnection();
            Statement statement=con.createStatement();
            //将用户所要退订套餐的失效时间设为本月底
            String s="update orders set overtime=? where userid="+userid+" and packageid="+packageid+" and overtime is null";
            PreparedStatement ps=con.prepareStatement(s);
            ps.setString(1,getLastDateOfMonth(date));
            int n=ps.executeUpdate();
            //若用户并没有正在使用改套餐
            if(n==0){
                System.out.println("用户没有订购该套餐，无需退订!");
            }else {
                //判断要退订的套餐是否为下个月才开始生效的，如果是的话，直接取消该条预定记录即可
                Statement statement1=con.createStatement();
                String s1="select * from orders where userid="+userid+" and packageid="+packageid+" order by orderid DESC";
                ResultSet rr=statement1.executeQuery(s1);
                String start=null;
                if(rr.next()){
                    start=rr.getString("starttime");
                }
                String s2="delete from orders where userid="+userid+" and packageid="+packageid+" order by orderid DESC limit 1";
                int startMonth=Integer.parseInt(start.split("-")[1]);
                int presentMonth=Integer.parseInt(date.split("-")[1]);
                //取消下月的预订
                if(startMonth>presentMonth){
                    statement.executeUpdate(s2);
                    System.out.println("用户" + userid + "已经取消了预订套餐" + packageid);
                    //退订当下的套餐
                }else {
                    System.out.println("用户" + userid + "已经退订了套餐" + packageid + "，套餐月底失效!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//单次通话后产生的费用
    public float costByPhoneCall(int userid, int min) {
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            //找出用户账户内的剩余免费通话时长
            String free="select call_free from account where userid="+userid;
//            String used="select call_used from account where userid="+userid;
            int ftime=0;
//            int utime=0;
            ResultSet rs1=statement.executeQuery(free);
            if(rs1.next()){
                ftime=rs1.getInt("call_free");
            }
            rs1.close();
//            ResultSet rs2=statement.executeQuery(used);
//            if(rs2.next()){
//                utime=rs2.getInt("call_used");
//            }
//            rs2.close();
            String s1="update account set call_free=call_free-"+min+" where userid="+userid;//减少剩余免费通话时间
            String s2="update account set call_used=call_used+"+min+" where userid="+userid;//增加累计通话时间
            String s3="update account set call_free=0 where userid="+userid;//将免费通话时长清零
            String s4=null;
            String s = "select cost from charges where cid=1";//通话基准资费
            ResultSet rs = statement.executeQuery(s);
            float costPerMin=0;
            if(rs.next()) {
                costPerMin = rs.getFloat("cost");
                rs.close();
            }
            //若免费时长多于本次通话时长
            if(ftime>=min){
                statement.addBatch(s1);
                statement.addBatch(s2);
                statement.executeBatch();
                return 0;
                //若还有一些免费通话时间
            }else if(ftime>0){
                int payTime=min-ftime;
                statement.addBatch(s2);
                statement.addBatch(s3);
                s4="update account set account_balance=account_balance-"+payTime*costPerMin+" where userid="+userid;
                statement.addBatch(s4);
                statement.executeBatch();
                return payTime*costPerMin;
                //纯自费的情况
            }else {
                s4="update account set account_balance=account_balance-"+min*costPerMin+" where userid="+userid;
                statement.addBatch(s4);
                statement.addBatch(s2);
                statement.executeBatch();
                return costPerMin*min;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
//使用本地流量后产生的费用
    public float localTraffic(int userid, int traffic) {
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String free="select localtraffic_free from account where userid="+userid;//找出该用户剩余的本地流量
            int ftime=0;
            ResultSet rs1=statement.executeQuery(free);
            if(rs1.next()){
                ftime=rs1.getInt("localtraffic_free");
            }
            rs1.close();
            String s1="update account set localtraffic_free=localtraffic_free-"+traffic+" where userid="+userid;//扣除剩余流量
            String s2="update account set localtraffic_used=localtraffic_used+"+traffic+" where userid="+userid;//增加总使用流量
            String s3="update account set localtraffic_free=0 where userid="+userid;//流量清零
            String s4=null;
            String s = "select cost from charges where cid=3";//本地流量基准资费
            ResultSet rs = statement.executeQuery(s);
            float costPerM=0;
            if(rs.next()) {
                costPerM = rs.getFloat("cost");
                rs.close();
            }
            if(ftime>=traffic){
                statement.addBatch(s1);
                statement.addBatch(s2);
                statement.executeBatch();
                return 0;
            }else if(ftime>0){
                int payTime=traffic-ftime;
                statement.addBatch(s2);
                statement.addBatch(s3);
                s4="update account set account_balance=account_balance-"+payTime*costPerM+" where userid="+userid;//扣话费
                statement.addBatch(s4);
                statement.executeBatch();
                return payTime*costPerM;
            }else {
                s4="update account set account_balance=account_balance-"+traffic*costPerM+" where userid="+userid;//扣话费
                statement.addBatch(s4);
                statement.addBatch(s2);
                statement.executeBatch();
                return costPerM*traffic;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
//使用全国流量后产生的费用
    public float nationalTraffic(int userid, int traffic) {
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String free="select nationaltraffic_free from account where userid="+userid;//找出该用户剩余的全国流量
            int ftime=0;
            ResultSet rs1=statement.executeQuery(free);
            if(rs1.next()){
                ftime=rs1.getInt("nationaltraffic_free");
            }
            rs1.close();
            String s1="update account set nationaltraffic_free=nationaltraffic_free-"+traffic+" where userid="+userid;//扣除剩余国内流量
            String s2="update account set nationaltraffic_used=nationaltraffic_used+"+traffic+" where userid="+userid;//增加总使用流量
            String s3="update account set nationaltraffic_free=0 where userid="+userid;//清零
            String s4=null;
            String s = "select cost from charges where cid=4";//国内流量基准资费
            ResultSet rs = statement.executeQuery(s);
            float costPerM=0;
            if(rs.next()) {
                costPerM = rs.getFloat("cost");
                rs.close();
            }
            if(ftime>=traffic){
                statement.addBatch(s1);
                statement.addBatch(s2);
                statement.executeBatch();
                return 0;
            }else if(ftime>0){
                int payTime=traffic-ftime;
                statement.addBatch(s2);
                statement.addBatch(s3);
                s4="update account set account_balance=account_balance-"+payTime*costPerM+" where userid="+userid;//扣话费
                statement.addBatch(s4);
                statement.executeBatch();
                return payTime*costPerM;
            }else {
                s4="update account set account_balance=account_balance-"+traffic*costPerM+" where userid="+userid;//扣话费
                statement.addBatch(s4);
                statement.addBatch(s2);
                statement.executeBatch();
                return costPerM*traffic;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    //展示用户的月账单
    public void showMonthlyBill(int userid) {
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            String sql1 = "select * from account where userid = "+userid;//找出该用户账户中的所有情况
            ResultSet rs=s.executeQuery(sql1);
            if(rs.next()){
                int call_free=rs.getInt("call_free");
                int call_used=rs.getInt("call_used");
                int message_free=rs.getInt("message_free");
                int message_used=rs.getInt("message_used");
                int nationalTra_free=rs.getInt("nationaltraffic_free");
                int nationalTra_used=rs.getInt("nationaltraffic_used");
                int localTra_free=rs.getInt("localtraffic_free");
                int localTra_used=rs.getInt("localtraffic_used");
                float account_balance=rs.getFloat("account_balance");
                float initial=rs.getFloat("initial_account");
                String sql2 = "select * from orders where userid = "+userid;//找出该用户的套餐订购情况
                ResultSet rs2=s.executeQuery(sql2);
                while (rs2.next()){
                    String start=rs2.getString("starttime");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startTime = dateFormat.parse(start);
                        Date now=dateFormat.parse(getPresentDate());
                        if(startTime.compareTo(now)==0){
                            int id=rs2.getInt("packageid");
                            System.out.println("用户"+userid+"本月订购了套餐"+id+"；");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("用户"+userid+"本月通话时长为"+call_used+"分钟，剩余免费通话时间为"+call_free+"分钟；\n" +
                        "本月发送短信"+message_used+"条，剩余免费短信为"+message_free+"条；\n" +
                        "使用了本地流量"+localTra_used+"M，剩余本地流量"+localTra_free+"M；\n" +
                        "使用了全国流量"+nationalTra_used+"M，剩余全国流量"+nationalTra_free+"M；\n" +
                        "本月共计消费了"+(initial-account_balance)+"元；\n" +
                        "账户余额为"+account_balance+"元");
            }else{
                System.out.println("系统中没有此用户!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//获取当前日期
    public String getPresentDate(){
        Calendar cal=Calendar.getInstance();
        int m=cal.get(Calendar.MONTH)+1;
        int y=cal.get(Calendar.YEAR);
        int d=cal.get(Calendar.DATE);
        String s=y+"-"+m+"-"+d;
        return s;
    }
    //获取某月最后一天的日期（旧套餐失效日）
    public String getLastDateOfMonth(String s){
        String[] ss=s.split("-");
        String y=ss[0];
        String m=ss[1];
        String d=ss[2];
        String lastdate=y+"-"+m+"-"+30;
        return  lastdate;
    }
    //获取某月下个月第一天的日期（新套餐生效日）
    public String getFirstdateOfNextmonth(String s){
        String[] ss=s.split("-");
        String y=ss[0];
        String m=ss[1];
        String d=ss[2];
        int next=Integer.parseInt(m)+1;
        String fdate=y+"-"+next+"-"+1;
        return  fdate;
    }
}
