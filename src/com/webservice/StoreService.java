package com.webservice;

import com.alibaba.fastjson.JSON;
import com.app.web.user.UserHttpImpl;
import com.commons.util.ExpandHanlerUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 王文海        app的后台



 * Date: 16-11-22
 * Time: 上午9:17
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "StoreService")
public class StoreService extends HttpServlet {

    UserHttpImpl ui = null;
    private static final int CODE_SUCCESS=          1;
    private static final int CODE_FAILURE=          2;



    private String getJsonSql(String mathod, String sql){
        return "{\""+mathod+"\":\""+sql+"\"}";
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","POST");
        response.setHeader("Access-Control-Max-Age","60");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
        HttpSession session=request.getSession();
        String openid=getOpenid();
        session.setAttribute("openid",openid);
        String jsonStr = "";
        addUserList(openid,session);
        //获取商品分类
        if(domain.equals("getClassifyTpye"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM STORE_DIC_DICTIONARY T WHERE T.DIC_TYPE='GOODS_TYPE' AND T.DIC_PARENTCODE='0'");
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
            System.out.println("jsonStr:"+jsonStr);    //获取list数据
        }   //insert into STORE_USER_INFO(U_USERID,U_PWD,U_USERNAME) values ('sfsfdsfdsfdsfds','sfdsfdsfdsfdsfs','sfsfdsfdsf')
        else if(domain.equals("doRegister"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String username=request.getParameter("username");
            String password=request.getParameter("password");
            String uuid= UUID.randomUUID().toString();
            uuid=uuid.replaceAll("-","");
            sql.append("INSERT INTO STORE_USER_INFO(U_USERID,U_PWD,U_USERNAME) VALUES ('"+uuid+"','"+password+"','"+username+"')");
            boolean isSuccess = Boolean.parseBoolean(ui.addAny(getJsonSql("addAnySQL", sql.toString())));
            System.out.println("doregister:"+isSuccess);
            JSONObject obj=new JSONObject();
            if(isSuccess)
            {
                obj.put("message","注册成功");
                obj.put("code",CODE_SUCCESS);
            }else
            {
                obj.put("message","注册失败");
                obj.put("code",CODE_FAILURE);
            }
            System.out.println("registerStr:"+jsonStr);    //获取list数据
        }
        else if(domain.equals("doLogin"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String username=request.getParameter("username");
            String password=request.getParameter("password");
            String uuid= UUID.randomUUID().toString();
            uuid=uuid.replaceAll("-","");
            sql.append("SELECT T.* FROM STORE_USER_INFO T WHERE t.u_username='"+username+"' and t.u_pwd='"+password+"'");
            jsonStr=ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));
            System.out.println("doLogin:"+jsonStr);
        }
        else if(domain.equals("getCartList"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String session_openid=(String)session.getAttribute("openid");
            if(session_openid!=null)
            {
                sql.append("SELECT * FROM STORE_GOODS_INFO T INNER JOIN STORE_USER_CAR C ON T.G_ID=C.G_ID INNER JOIN STORE_USER_INFO U " +
                                "ON C.U_USERID=U.U_USERID WHERE u.openid='"+session_openid+"'");
                jsonStr=ui.queryAnyList(getJsonSql("queryAnyListSQL", sql.toString()));
            }
            else
            {
                JSONObject infoobj=new JSONObject();
                infoobj.put("code","failure");
                infoobj.put("message","会话已关闭，请重新登入微信");
                jsonStr=infoobj.toString();
            }
             System.out.println("getCartList:"+jsonStr);
        }
        //getCategorylist
        else if(domain.equals("getCategorylist"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String dicid=request.getParameter("dicid");
            if(dicid!=null)
            {
              //  sql.append("select c.* from STORE_GOODS_CATEGORY c left join STORE_DIC_DICTIONARY d on c.categ_dicid=d.dic_id where d.dic_id='"+dicid+"'");
                sql.append("SELECT * FROM STORE_DIC_DICTIONARY T WHERE t.dic_parentcode='"+dicid+"'");
                jsonStr=ui.queryAnyList(getJsonSql("queryAnyListSQL", sql.toString()));
            }
            System.out.println("getCategorylist:"+jsonStr);
        }
        else if(domain.equals("getMain")) //主页里面调的接口
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
    // oracle        sql.append("select t.* from store_goods_info t where ROWNUM <= 3 order by t.g_price desc");
            sql.append("SELECT T.* FROM STORE_GOODS_INFO T ORDER BY T.G_PRICE DESC LIMIT 3");
            String infoarray=ui.queryAnyList(getJsonSql("queryAnyListSQL", sql.toString()));
            JSONArray arrayobj= JSONArray.fromObject(infoarray);

            JSONObject mainobj=new JSONObject();
            mainobj.put("commendlist",arrayobj);
            jsonStr=mainobj.toString();
            System.out.println("getMain:"+jsonStr);
        }
        else if(domain.equals("getGoodslist"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String clarifyId=request.getParameter("clarifyId");
            String orderparam=(request.getParameter("orderparam")==null?"default":request.getParameter("orderparam"));
            int startpage=Integer.parseInt(request.getParameter("startpage"));
            int curpage= Integer.parseInt(request.getParameter("endpage"));
            System.out.println("startpage:" + startpage);
            System.out.println("curpage:"+curpage);
            System.out.println("orderparam:"+orderparam);
            //还需判断库存，有的库存为零的要消失在列表上


          //根据销量排序


//            select s.g_id,s.g_name,s.g_price,s.g_amount,s.dicid,sup.sup_name,SUM(d.info_amount) as dsum from store_goods_info s
//            inner join store_sup_supply sup on s.sup_id=sup.sup_id inner join store_order_details d on s.g_id=d.g_id group by s.g_id,s.g_name,
//            s.g_price,s.g_amount,s.dicid,sup.sup_name having s.dicid='qwedsfsdssddsfdfd' order by SUM(d.info_amount)
            if(orderparam.equals("salesamount"))  //销售,求和订单详情列表中的销售数量来排销量


           {
               sql.append("SELECT S.G_ID,S.G_PIC,S.G_NAME,S.G_PRICE,S.G_AMOUNT,S.DICID,SUP.SUP_NAME,SUM(D.INFO_AMOUNT) AS DSUM FROM STORE_GOODS_INFO S LEFT JOIN STORE_SUP_SUPPLY SUP ON S.SUP_ID=SUP.SUP_ID LEFT JOIN STORE_ORDER_DETAILS D ON S.G_ID=D.G_ID " +
                       "GROUP BY S.G_ID,S.G_NAME,S.G_PRICE,S.G_PIC,S.G_AMOUNT,S.DICID,SUP.SUP_NAME HAVING s.dicid='"+clarifyId+"' and s.g_amount>0 order by dsum limit "+(startpage)+","+curpage);
               System.out.println("销售排列:"+sql.toString());
           }else if(orderparam.equals("default")) //默认  ,去重复的项


           {
//               sql.append("select * from (SELECT A.*, ROWNUM RN FROM (select s.g_id,s.g_pic,s.g_name,to_number(s.g_price) as G_PRICE,s.g_amount,s.dicid," +
//                       "sup.sup_name,SUM(d.info_amount) as dsum from store_goods_info s left join store_sup_supply sup " +
//                       "on s.sup_id=sup.sup_id left join store_order_details d on s.g_id=d.g_id group by s.g_id,s.g_name,s.g_price,s.g_pic," +
//                       "s.g_amount,s.dicid,sup.sup_name having s.dicid='"+clarifyId+"') A " +
//                       "WHERE ROWNUM <= "+(startpage+curpage)+") WHERE RN >= "+(startpage+1));

               sql.append("SELECT S.G_ID,S.G_PIC,S.G_NAME,S.G_PRICE,S.G_AMOUNT,S.DICID,SUP.SUP_NAME,SUM(D.INFO_AMOUNT) " +
                       "AS DSUM FROM STORE_GOODS_INFO S LEFT JOIN STORE_SUP_SUPPLY SUP ON S.SUP_ID=SUP.SUP_ID LEFT JOIN STORE_ORDER_DETAILS D ON S.G_ID=D.G_ID " +
                       "GROUP BY S.G_ID,S.G_NAME,S.G_PRICE,S.G_PIC,S.G_AMOUNT,S.DICID,SUP.SUP_NAME HAVING s.dicid='"+clarifyId+"' and s.g_amount>0 limit "+(startpage)+","+(curpage));
               //select s.g_id,s.g_pic,s.g_name,s.g_price as G_PRICE,s.g_amount,s.dicid,sup.sup_name from store_goods_info s left join store_sup_supply sup on s.sup_id=sup.sup_id left join store_order_details d on s.g_id=d.g_id group by s.g_id,s.g_name,s.g_price,s.g_pic,s.g_amount,s.dicid,sup.sup_name having s.dicid='' and s.g_amount>0
//               sql.append("select * from (SELECT A.*, ROWNUM RN FROM (select s.g_id,s.g_pic,s.g_name,to_number(s.g_price) as G_PRICE,s.g_amount,s.dicid," +
//                       "sup.sup_name from store_goods_info s left join store_sup_supply sup " +
//                       "on s.sup_id=sup.sup_id left join store_order_details d on s.g_id=d.g_id group by s.g_id,s.g_name,s.g_price,s.g_pic," +
//                       "s.g_amount,s.dicid,sup.sup_name having s.dicid='"+clarifyId+"' and s.g_amount>0) A " +
//                       "WHERE ROWNUM <= "+(startpage+curpage)+") WHERE RN >= "+(startpage+1));
               System.out.println("默认排列:"+sql.toString());
           }else if(orderparam.equals("salesprice")) //价格
           {
               sql.append("SELECT S.G_ID,S.G_PIC,S.G_NAME,S.G_PRICE,S.G_AMOUNT,S.DICID,SUP.SUP_NAME,SUM(D.INFO_AMOUNT) " +
                       "AS DSUM FROM STORE_GOODS_INFO S LEFT JOIN STORE_SUP_SUPPLY SUP ON S.SUP_ID=SUP.SUP_ID LEFT JOIN STORE_ORDER_DETAILS D ON S.G_ID=D.G_ID " +
                       "GROUP BY S.G_ID,S.G_NAME,S.G_PRICE,S.G_PIC,S.G_AMOUNT,S.DICID,SUP.SUP_NAME HAVING s.dicid='"+clarifyId+"' and s.g_amount>0 order by s.g_price limit "+(startpage+1)+","+(startpage+curpage));
//               sql.append("select * from (SELECT A.*, ROWNUM RN FROM (select s.g_id,s.g_pic,s.g_name,to_number(s.g_price) as G_PRICE,s.g_amount,s.dicid," +
//                       "sup.sup_name from store_goods_info s left join store_sup_supply sup " +
//                       "on s.sup_id=sup.sup_id left join store_order_details d on s.g_id=d.g_id  group by s.g_id,s.g_name,s.g_price,s.g_pic," +
//                       "s.g_amount,s.dicid,sup.sup_name having s.dicid='"+clarifyId+"' and s.g_amount>0 order by G_PRICE) A  " +
//                       "WHERE ROWNUM <= "+(startpage+curpage)+") WHERE RN >= "+(startpage+1));
               System.out.println("价格排列:"+sql.toString());
           }
            jsonStr=ui.queryAnyList(getJsonSql("queryAnyListSQL", sql.toString()));
            System.out.println("getGoodslist:"+jsonStr);
        }
        else if(domain.equals("getGoodsdetail"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String gid=request.getParameter("gid");
            System.out.println("gid:"+gid);
            sql.append("SELECT * FROM STORE_GOODS_INFO S LEFT JOIN STORE_SUP_SUPPLY SUP ON S.SUP_ID=SUP.SUP_ID" +
                    " LEFT JOIN STORE_ORDER_COMMENT C ON S.G_ID=C.G_ID WHERE s.g_id='"+gid+"'");
            jsonStr=ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));
            System.out.println("getGoodsdetail:"+jsonStr);
        }
        else if(domain.equals("addCart"))     //首先判断gid,uid有没有，如果有就执行更新语句，没有就执行插入语句
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();

            String gid=request.getParameter("gid");
            String uid=(String)session.getAttribute("uid");
            if(uid!=null)
            {
               // select count(*) as amount from store_user_car u where u.u_userid= and u.g_id=
                sql.append("SELECT COUNT(*) AS AMOUNT FROM STORE_USER_CAR U WHERE u.u_userid='"+uid+"' and u.g_id='"+gid+"'");
                String json = ui.queryAny(getJsonSql("queryAnySQL",sql.toString()));
                JSONObject result= JSONObject.fromObject(json);

                int digital=Integer.parseInt(result.getString("AMOUNT"));
                Boolean addstate=false;
                String jsonresult="";
                if(digital==0)  //没有符合条件的就插入
                {
                    //插入语句
                    sql=new StringBuffer();
                    java.util.Date date=new java.util.Date();
                    java.sql.Date sqlDate=new java.sql.Date(date.getTime());
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("gid:"+gid);
                    String ssid=UUID.randomUUID().toString();
                    ssid=ssid.replaceAll("-","");
                    sql.append("INSERT INTO STORE_USER_CAR VALUES('"+ssid+"','"+uid+"','"+gid+"',1,'"+dateFormat.format(sqlDate)+"')");
                    jsonresult=ui.addAny(getJsonSql("addAnySQL", sql.toString()));
                    System.out.println("addCart:"+jsonStr);

                }
                else      //又符合条件的就执行更新语句


                {
                    sql = new StringBuffer();
                    sql.append("UPDATE STORE_USER_CAR C SET C.COUNT=C.COUNT+1 WHERE c.u_userid='"+uid+"' and c.g_id='"+gid+"'");
                    jsonresult=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
                    System.out.println("updateCart:"+jsonStr);
                }
                addstate=Boolean.parseBoolean(jsonresult);
                if(addstate)
                {
                    JSONObject obj=new JSONObject();
                    obj.put("code","success");
                    jsonStr=obj.toString();
                }
                else
                {
                    JSONObject obj=new JSONObject();
                    obj.put("code","failure");
                    obj.put("message","系统出了问题，待修复");
                    jsonStr=obj.toString();
                }
             }
            else
            {
                JSONObject obj=new JSONObject();
                obj.put("code","failure");
                obj.put("message","系统出了问题，待修复");
                jsonStr=obj.toString();
            }
        }
        else if(domain.equals("addCartNumber"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();

            String gid=request.getParameter("gid");
            String uid=(String)session.getAttribute("uid");

            sql.append("UPDATE STORE_USER_CAR C SET C.COUNT=C.COUNT+1 WHERE c.u_userid='"+uid+"' and c.g_id='"+gid+"'");
            String jsonresult=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
            Boolean isDel=Boolean.parseBoolean(jsonresult);
            if(isDel)
            {
                JSONObject message=new JSONObject();
                message.put("code","success");
                message.put("message","添加购物车数量成功");
                jsonStr=message.toString();
            }
            else
            {
                JSONObject message=new JSONObject();
                message.put("code","failure");
                message.put("message","添加购物车数量失败");
                jsonStr=message.toString();
            }
            System.out.println("updateCart:"+jsonStr);
        }

        else if(domain.equals("minusCartNumber"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();

            String gid=request.getParameter("gid");
            String uid=(String)session.getAttribute("uid");

            sql.append("UPDATE STORE_USER_CAR C SET C.COUNT=C.COUNT-1 WHERE c.u_userid='"+uid+"' and c.g_id='"+gid+"'");
            String jsonresult=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
            Boolean isDel=Boolean.parseBoolean(jsonresult);
            if(isDel)
            {
                JSONObject message=new JSONObject();
                message.put("code","success");
                message.put("message","删减购物车数量成功");
                jsonStr=message.toString();
            }
            else
            {
                JSONObject message=new JSONObject();
                message.put("code","failure");
                message.put("message","删减购物车数量失败");
                jsonStr=message.toString();
            }
            System.out.println("updateCart:"+jsonStr);
        }
        else if(domain.equals("delCart"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();

            String gid=request.getParameter("gid");
            String uid=(String)session.getAttribute("uid");

            sql.append("DELETE FROM STORE_USER_CAR C WHERE c.u_userid='"+uid+"' and c.g_id='"+gid+"'");
            String jsonMessage=ui.delAny(getJsonSql("delAnySQL", sql.toString()));

            Boolean isDel=Boolean.parseBoolean(jsonStr);
            if(isDel)
            {
                JSONObject message=new JSONObject();
                message.put("code","success");
                message.put("message","删除购物车成功");
                jsonStr=message.toString();
            }
            else
            {
                JSONObject message=new JSONObject();
                message.put("code","failure");
                message.put("message","删除购物车失败");
                jsonStr=message.toString();
            }
            System.out.println("delCart:"+jsonStr);
        }
        else if(domain.equals("acquireConfirmOrderInfo"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String idlist=request.getParameter("idlist");
            if(oid==null||oid.equals(""))
            {
                JSONObject message=new JSONObject();
                message.put("code","NoLogin");
                message.put("message","用户信息不存在，请用户重新登录微信");
                jsonStr=message.toString();
            }

            else
            {
                //返回有无可用的优惠券
               //检查有无收货地址
                sql.append("SELECT * FROM STORE_USER_ADDRESS ADDR WHERE addr.a_userid=(select i.u_userid from " +
                "store_user_info i where i.openid='"+oid+"') and addr.a_default=1");
              //  String jsonMessage=ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));
                String jsonMessage=ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                JSONObject rootjson=new JSONObject();
                if(jsonMessage==null||jsonMessage.equals(""))
                {
                   rootjson.put("code","NoAddress");
                   rootjson.put("message","该用户没有地址");
                }
                else
                {
                    JSONArray json=JSONArray.fromObject(jsonMessage);
                    if(json.size()==0)
                    {
                        rootjson.put("code","NoAddress");
                        rootjson.put("message","该用户没有地址");
                    }
                    else
                    {      //获取收货地址
                        rootjson.put("code","success");
                        rootjson.put("addresslist",json);
                           //获取购物车商品


                        JSONObject obj=json.getJSONObject(0);
                        String userid=obj.getString("A_USERID");
                        System.out.println("cartidlist:"+idlist);
                        sql=new StringBuffer();
                        sql.append("SELECT * FROM STORE_GOODS_INFO GI INNER JOIN STORE_USER_CAR CA " +
                                "ON CA.G_ID=GI.G_ID WHERE ca.s_id in ("+idlist+")");
                        String jsonM=ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        JSONArray cartlistarray=JSONArray.fromObject(jsonM);
                        rootjson.put("cartlist",cartlistarray);
                    }
                }
                jsonStr=rootjson.toString();
            }

            //select * from store_user_discount u inner join store_sup_discount up on u.c_id=up.c_id where u.u_userid=(select su.u_userid from store_user_info su where su.u_userid='d8324c4b-9bd9-4dcc-b2dc-9401762fe606')
             System.out.println("acquireConfirmOrderInfo:"+jsonStr);
        }

        else if(domain.equals("getDiscountByUser"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String status=request.getParameter("status");    //0  未使用     1  已使用       2  已过期


            int discountstatus=0;
            if(status!=null&&(!status.equals("")))
               discountstatus=Integer.parseInt(status);
            if(oid==null||oid.equals(""))
            {
                JSONObject message=new JSONObject();
                message.put("code","NoLogin");
                message.put("message","用户信息不存在，请用户重新登录微信");
                jsonStr=message.toString();
            }else
            {
                sql.append("SELECT * FROM STORE_USER_DISCOUNT U INNER JOIN STORE_SUP_DISCOUNT UP ON U.C_ID=UP.C_ID " +
                        "INNER JOIN STORE_SUP_SUPPLY SUP ON UP.SUP_ID=SUP.SUP_ID " +
                        "WHERE U.U_USERID=(SELECT SU.U_USERID FROM STORE_USER_INFO SU WHERE su.openid='"+oid+"') " +
                        "and u.uc_status="+discountstatus);
                String jsonResult=ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                JSONArray array=JSONArray.fromObject(jsonResult);
                JSONObject couponobj=new JSONObject();
                couponobj.put("code","success");
                couponobj.put("data",array);
                jsonStr=couponobj.toString();
                System.out.println("jsonstr:"+jsonStr);
            }

        }

        //个人中心模块
        else if(domain.equals("getUserDetailinfo"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String uid=(String)session.getAttribute("uid");//从session里面获得uid
        //    String status=request.getParameter("status");    //0  未使用     1  已使用       2  已过期
            sql.append("select * from store_user_details d where d.U_USERID='"+uid+"'");
            jsonStr= ui.queryAny(getJsonSql("queryAnySQL",sql.toString()));
         }
        //修改用户个人详细信息
        else if(domain.equals("modifyUserDetailinfo"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String param=(String)request.getAttribute("param");
            String imgurl=(String)request.getAttribute("imgurl");
            String uid=(String)session.getAttribute("uid");//从session里面获得uid
            //    String status=request.getParameter("status");    //0  未使用     1  已使用       2  已过期
            if(param.equals("avatar"))//修改头像
              sql.append("update store_user_details t set t.D_CODEPIC='"+imgurl+"'");
            String jsonresult=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
            Boolean isDel=Boolean.parseBoolean(jsonresult);
            JSONObject rootobj=new JSONObject();
            rootobj.put("code","success");
            jsonStr=rootobj.toString();
        }
        //生成未支付订单，优惠券没有的情况还没有做
        else if(domain.equals("produceNotPayOrder"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String cartidlist=request.getParameter("cartidlist");//购物车字段


            System.out.println("cartidlist:"+cartidlist);
            String couponid=request.getParameter("couponid");//优惠券id,discount优惠券id值


            System.out.println("couponid:"+couponid);
 //           String couponprice=request.getParameter("couponprice");//优惠券价格


//            String conditiontype=request.getParameter("conditiontype");//优惠券条件，C_GOODTYPE  1  满减
//            String C_MONEY=request.getParameter("C_MONEY");//满减的话就代表减去多少价格，打折的话这个就是百分比


            String cartallprice=request.getParameter("cartallprice");//购物车总价
            System.out.println("cartallprice:"+cartallprice);
            String UC_ID=request.getParameter("UC_ID");      //user的打折ID值，用户使用的优惠券id值


            System.out.println("UC_ID:"+UC_ID);
            String paramlist=request.getParameter("paramlist");       //参数集合提交上来      格式是    //gid商品id-优惠券id-单个商品总价-数量,gid商品id-优惠券id-单个商品总价-数量...
            System.out.println("paramlist:"+paramlist);

            if(oid==null||oid.equals(""))
            {
                JSONObject rootobj=new JSONObject();
                rootobj.put("code","NoLogin");
                rootobj.put("message","用户信息已丢失，请用户重新登录微信");
                jsonStr=rootobj.toString();
            }
           //根据优惠券条件来判断总价
            else
            {
            //满减 1   折扣  2
            //已付款   1    待付款   0     已发货    2    已签收    3     已评论     4      已退款     7      已完成     8

//                    for(int i=0;i<params.length;i++)
//                    {
//                          String orderdetailid=UUID.randomUUID().toString();
//                          String[] param=params[i].split("-");
//                          //gid商品id-优惠券id-总价-数量
//                          String couponparam=couponid!=null?"'"+couponid+"'":null;
//                          sql.append("insert into store_order_details values ('"+orderdetailid+"','"+O_ORDERID+"',"+param[0]+","+couponparam+","+param[1]+","+param[2]+",'0','','');");
//                    }
//                    sql.append(" commit;");
//                    sql.append(" end;");
//                    String resultStr=ui.addAny(getJsonSql("addAnySQL", sql.toString()));
//                    Boolean isSuccess=Boolean.parseBoolean(resultStr);
//                    System.out.println("添加order_details表:"+resultStr);
//                   //将STORE_USER_DISCOUNT表中的优惠券UC_STATUS改变其状态，改未使用状态为已使用


//                   //将STORE_USER_DISCOUNT表中的优惠券O_ORDERID绑定新增的orderid值


//                   //0  未使用     1   已使用       2   已过期


//
                   //查库存   比较所有提交的购物车商品的库存，如果有库存不够的就把他存起来
                    String[] gparams=paramlist.split(",");
                    JSONArray jarray=new JSONArray();
                    for(int i=0;i<gparams.length;i++)
                    {
                        String[] contentparam=gparams[i].split("-");
                        String gid=contentparam[0];
                        System.out.println("GID:"+gid);
                        String gcount=contentparam[2];
                        System.out.println("GCOUNT:"+gcount);
                        sql=new StringBuffer();
                        sql.append("SELECT * FROM STORE_GOODS_INFO G WHERE g.g_id="+gid+" and g.g_amount<"+gcount+"");
                        String jsonMessage = ui.queryAny(getJsonSql("queryAnySQL",sql.toString()));
                        System.out.println("库存 json info:"+jsonMessage);
                        if(jsonMessage==null||jsonMessage.equals("")||jsonMessage.equals("{}"))
                                continue;
                        else
                        {
                            JSONObject dataobj=JSONObject.fromObject(jsonMessage);
                            jarray.add(dataobj);
                        }
                    }
                    if(jarray.size()>0)
                    {
                            JSONObject infoobj=new JSONObject();
                            infoobj.put("code","NoNumber");
                            infoobj.put("data",jarray);
                            infoobj.put("message","商品库存不够,不能生成订单");
                            jsonStr=infoobj.toString();
                    }
                    else        //如果库存都够，则让他生成订单
                    {
                           //删除购物车信息

                            ArrayList<String> sqllist=new ArrayList<String>();     //操作数据的集合


                            String O_ORDERID=UUID.randomUUID().toString().substring(0,10);
                            O_ORDERID=O_ORDERID.replaceAll("-","");
                            java.util.Date date=new java.util.Date();
                            java.sql.Date sqlDate=new java.sql.Date(date.getTime());
                            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            //删除购物车中的商品


                            sql=new StringBuffer();

                            sql.append("DELETE FROM STORE_USER_CAR WHERE S_ID IN ("+cartidlist+");  ");
                            String sqljson=getJsonSql("delAnySQL",sql.toString());
                            sqllist.add(sqljson);
                            //然后所有商品减库存
                            //total_price     使用优惠券   插入一条记录到STORE_ORDER_INFO

                            sql=new StringBuffer();

                            sql.append("INSERT INTO STORE_ORDER_INFO VALUES ('"+O_ORDERID+"'," +
                                    "(SELECT ui.U_USERID FROM STORE_USER_INFO ui WHERE ui.openid='"+oid+"'),'','"+cartallprice+"'" +
                                    ",'"+dateFormat.format(sqlDate)+"'," +
                                    "'"+dateFormat.format(sqlDate)+"','1','0',(SELECT U.A_ADDRESSID FROM " +
                                    "STORE_USER_ADDRESS U WHERE U.A_USERID=(SELECT SU.U_USERID FROM STORE_USER_INFO SU WHERE SU.OPENID='"+oid+"')  and U.A_DEFAULT=1),null); ");
                            sqljson=getJsonSql("addAnySQL",sql.toString());
                            sqllist.add(sqljson);

                            System.out.println("insertsql:"+sql.toString());
                            //生成所有明细STORE_ORDER_DETAIL表


                            //INFO_ID详情id   O_ORDERID订单id    G_ID商品id   C_ID优惠券   INFO_TOTALPRICE总价   INFO_AMOUNT数量   INFO_STATUS状态,判断收获否   COM_CODE,COM _STATUS

                            System.out.println("商品paramlist:"+paramlist);

                            String[] params=paramlist.split(",");

                            for(int i=0;i<params.length;i++)
                            {
                                String orderdetailid=UUID.randomUUID().toString();
                                orderdetailid=orderdetailid.replaceAll("-","");
                                String[] param=params[i].split("-");
                                //gid商品id-总价-数量
                                String couponparam=couponid!=null?"'"+couponid+"'":null;


                                sql=new StringBuffer();
                                sql.append("INSERT INTO STORE_ORDER_DETAILS VALUES ('"+orderdetailid+"','"+O_ORDERID+"',"+param[0]+","+couponparam+","+param[1]+","+param[2]+",'0','',''); ");
                                sqljson=getJsonSql("addAnySQL",sql.toString());
                                sqllist.add(sqljson);


                                //更新库存
                                sql=new StringBuffer();
                                sql.append("UPDATE STORE_GOODS_INFO INFO SET INFO.G_AMOUNT=INFO.G_AMOUNT-"+param[2]+" where INFO.G_ID="+param[0]+"; ");
                                sqljson=getJsonSql("updateAnySQL",sql.toString());
                                sqllist.add(sqljson);
                            }

                            //将STORE_USER_DISCOUNT表中的优惠券UC_STATUS改变其状态，改未使用状态为已使用


                            //将STORE_USER_DISCOUNT表中的优惠券O_ORDERID绑定新增的orderid值


                            //0  未使用     1   已使用       2   已过期


                            //update store_user_discount dis set dis.uc_status=1,dis.o_orderid='sdfsfdsfsfdssdfdsf' where dis.uc_id='sdfsfsfss'

                            if(UC_ID!=null)
                            {
                                sql=new StringBuffer();
                                sql.append("UPDATE STORE_USER_DISCOUNT DIS SET dis.uc_status=1,dis.o_orderid='"+O_ORDERID+"' where dis.uc_id='"+UC_ID+"'; ");
                                sqljson=getJsonSql("updateAnySQL",sql.toString());
                                sqllist.add(sqljson);
                                System.out.println("updateCart:"+jsonStr);
                            }
                        //     sql.append(" commit;");
                        //     sql.append(" end;");
                        //     sql.append(" DELIMITER;");

                        //     sString resultStr=ui.addAny(getJsonSql("addAnySQL", sql.toString()));
                      //       sql.append("  END");
                   //          String resultStr=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
                             Boolean isSuccess=false;
                            try
                            {
                                ExpandHanlerUtil.ExpandUtilTransaction(sqllist);
                                isSuccess=true;
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                                isSuccess=false;
                            }

                             System.out.println("总的操作:"+isSuccess);


                            JSONObject rootobj=new JSONObject();
                            JSONObject dataobj=new JSONObject();
                            dataobj.put("O_ORDERID",O_ORDERID);
                            rootobj.put("code","success");
                            rootobj.put("data",dataobj);
                            rootobj.put("message","生成未支付订单成功");
                            jsonStr=rootobj.toString();
                    }
            }


        }
        //
        else if(domain.equals("modifyPayOrder"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String O_ORDERID=request.getParameter("O_ORDERID");
            String orderstatus=request.getParameter("orderstatus");
            String allprice=request.getParameter("allprice");
            //info_status     待付款   0     作废    5     已评论    4     已退款     7      已完成      8
            //已发货      2       已付款      1     已签收      3

            String sqljson="";
            if(oid==null||oid.equals(""))
            {
                JSONObject message=new JSONObject();
                message.put("code","NoLogin");
                message.put("message","用户信息不存在，请用户重新登录微信");
                jsonStr=message.toString();
            }else if(orderstatus.equals("1"))  //订单status           1为已支付
            {
                if(allprice==null||allprice.equals("")||O_ORDERID==null||O_ORDERID.equals(""))
                {
                    JSONObject message=new JSONObject();
                    message.put("code","NoParam");
                    message.put("message","系统参数错误");
                    jsonStr=message.toString();
                }
                else
                {
                    Date date=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ArrayList<String> sqllist=new ArrayList<String>();
                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_INFO I SET i.o_realprice='"+allprice+"' where i.o_orderid='"+O_ORDERID+"';");
                     sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);


                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_INFO I SET i.o_rderstate='"+orderstatus+"' where i.o_orderid='"+O_ORDERID+"';");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_DETAILS D SET d.info_status='"+orderstatus+"' where d.o_orderid='"+O_ORDERID+"';");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);


                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_INFO I SET i.LASTMODIFY='"+format.format(date)+"',i.O_PAYDATE='"+format.format(date)+"' where i.O_ORDERID='"+O_ORDERID+"'");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

                    //             sql.append(" end;");
                 //   String resultStr=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
                    Boolean isSuccess=false;
                    try
                    {
                        ExpandHanlerUtil.ExpandUtilTransaction(sqllist);   //批处理，一次性执行多条语句
                        isSuccess=true;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        isSuccess=false;
                    }

                    JSONObject rootobj=new JSONObject();
                    rootobj.put("code","success");
                    rootobj.put("message","更新订单成功");
                    jsonStr=rootobj.toString();
                    System.out.println("jsonstr:"+jsonStr);
                }
            }
            else if(orderstatus.equals("5")||orderstatus.equals("11"))        //已取消


            {
                if(O_ORDERID==null||O_ORDERID.equals(""))//没有订单
                {
                    JSONObject message=new JSONObject();
                    message.put("code","NoParam");
                    message.put("message","系统参数错误");
                    jsonStr=message.toString();
                }
                else
                {
                    sql=new StringBuffer();
                    Date date=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ArrayList<String> sqllist=new ArrayList<String>();
                    //商家同意以后才能加库存 sql.append("update store_goods_info info set info.g_amount=info.g_amount+"+info_amount+" where info.g_id='"+gid+"'; ");
                    sql=new StringBuffer();
                    sql.append("UPDATE STORE_ORDER_DETAILS D SET d.info_status='"+orderstatus+"' where d.o_orderid='"+O_ORDERID+"'; ");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

                    sql=new StringBuffer();
                    sql.append("UPDATE STORE_ORDER_INFO INFO SET info.O_RDERSTATE='"+orderstatus+"' where info.O_ORDERID='"+O_ORDERID+"'; ");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);


                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_INFO I SET i.LASTMODIFY='"+format.format(date)+"' where i.O_ORDERID='"+O_ORDERID+"'");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

               //     String resultStr=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
                    Boolean isSuccess=false;
                    try
                    {
                        ExpandHanlerUtil.ExpandUtilTransaction(sqllist);
                        isSuccess=true;
                    }
                    catch(Exception e)
                    {
                         e.printStackTrace();
                        isSuccess=false;
                    }

                    JSONObject rootobj=new JSONObject();
                    rootobj.put("code","success");
                    rootobj.put("message","更新订单成功");
                    jsonStr=rootobj.toString();
                    System.out.println("jsonstr:"+jsonStr);
                }
            }
            else if(orderstatus.equals("10")||orderstatus.equals("12")||orderstatus.equals("8"))        //把订单调成已删除状态  或者加急发货状态   或者已完成状态


            {
                if(O_ORDERID==null||O_ORDERID.equals(""))//没有订单
                {
                    JSONObject message=new JSONObject();
                    message.put("code","NoParam");
                    message.put("message","系统参数错误");
                    jsonStr=message.toString();
                }
                else
                {
                    sql=new StringBuffer();
                    Date date=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ArrayList<String> sqllist=new ArrayList<String>();

                    sql=new StringBuffer();
                    sql.append("UPDATE STORE_ORDER_DETAILS D SET d.info_status='"+orderstatus+"' where d.o_orderid='"+O_ORDERID+"'; ");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

                    sql=new StringBuffer();
                    sql.append("UPDATE STORE_ORDER_INFO INFO SET info.O_RDERSTATE='"+orderstatus+"' where info.O_ORDERID='"+O_ORDERID+"'; ");
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);

                    sql=new StringBuffer();
                    sql.append(" UPDATE STORE_ORDER_INFO I SET i.LASTMODIFY='"+format.format(date)+"' where i.O_ORDERID='"+O_ORDERID+"'");
                           //更新修改日期
                    sqljson=getJsonSql("updateAnySQL",sql.toString());
                    sqllist.add(sqljson);



           //         String resultStr=ui.updateAny(getJsonSql("updateAnySQL", sql.toString()));
                    Boolean isSuccess=false;
                    try{
                         ExpandHanlerUtil.ExpandUtilTransaction(sqllist);
                         isSuccess=true;
                    }
                    catch(Exception e)
                    {
                         e.printStackTrace();
                        isSuccess=false;
                    }

                    JSONObject rootobj=new JSONObject();
                    rootobj.put("code","success");
                    rootobj.put("message","更新订单成功");
                    jsonStr=rootobj.toString();
                    System.out.println("jsonstr:"+jsonStr);
                }
            }
        }

        else if(domain.equals("getPayOrderByStatus"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String orderstatus=request.getParameter("orderstatus");
            //已付款   1    待付款   0     已发货    2    已签收    3     已评论     4      已退款     7      已完成     8
            if(oid==null||oid.equals(""))
            {
                JSONObject message=new JSONObject();
                message.put("code","NoLogin");
                message.put("message","用户信息不存在，请用户重新登录微信");
                jsonStr=message.toString();
            }else
            {
                //查所有订单id
//                String presql="select i.o_orderid from store_order_info i";
//                sql.append(presql);
//                String orderidmessage=ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));

               //10  已删除订单状态，对用户不可见
                String sqlinfo="SELECT * FROM STORE_ORDER_DETAILS D LEFT JOIN STORE_GOODS_INFO I ON D.G_ID=I.G_ID LEFT JOIN STORE_SUP_SUPPLY SUP ON I.SUP_ID=SUP.SUP_ID LEFT JOIN STORE_ORDER_INFO ORDERINFO ON D.O_ORDERID=ORDERINFO.O_ORDERID WHERE D.INFO_STATUS!='10'";
                sql=new StringBuffer();
                sql.append(sqlinfo);
                if(orderstatus!=null&&!(orderstatus.equals("")))
                    sql.append(" and d.info_status='"+orderstatus+"'");
                String jsonMessage = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                JSONArray jsonarrays= JSONArray.fromObject(jsonMessage);

                HashMap<String,JSONObject> ordermap=new HashMap<String,JSONObject>();    //order集合

                JSONArray resultarray=new JSONArray();//结果arr

                int totalcount=0;//订单商品总量

                //每一个元素都比较之前的id集合，如果相等就


                for(int i=0;i<jsonarrays.size();i++)
                {
                    JSONObject zuzhuanginfoobj=new JSONObject();//组装信息
                    boolean isequal=false;
                    JSONObject itemobject=jsonarrays.getJSONObject(i);

                    Set<String> orderidset=ordermap.keySet();
                    Iterator keyiterator=orderidset.iterator();
                    String O_ORDERID=itemobject.getString("O_ORDERID");
           //         int itemcount=Integer.parseInt(itemobject.getString("INFO_AMOUNT"));
           //         totalcount+=itemcount;                                                       //算出具体个数

                    while(keyiterator.hasNext())
                    {
                         String id=(String)keyiterator.next();
                        JSONObject itemresultobj=ordermap.get(id);//组装json信息

                        if(id.equals(O_ORDERID))   //如果与之前储存的orderid相等，那么把之前的存储的valueJSONObject取出来，再把里面的jsonarray取出来，往里面添加jsonobject
                        {
                            JSONArray array=itemresultobj.getJSONArray("datalist");
                            array.add(itemobject);
                            isequal=true;
                            break;
                        }
                    }
                    if(isequal==false)
                    {
                       JSONArray array=new JSONArray();
                       array.add(itemobject);
                       zuzhuanginfoobj.put("datalist",array);
                       zuzhuanginfoobj.put("O_ORDERID",O_ORDERID);
                       zuzhuanginfoobj.put("INFO_STATUS",itemobject.getString("INFO_STATUS"));
                       zuzhuanginfoobj.put("O_TOTALPRICE",itemobject.getString("O_TOTALPRICE"));
                       ordermap.put(O_ORDERID,zuzhuanginfoobj);
                    }
                }

                Set<String> set=ordermap.keySet();
                Iterator iterator=set.iterator();
                while(iterator.hasNext())
                {
                    JSONObject arrayitem=ordermap.get(iterator.next());
                     resultarray.add(arrayitem);
                }

                JSONObject rootobj=new JSONObject();
                rootobj.put("code","success");
                rootobj.put("data",resultarray);
                jsonStr=rootobj.toString();
                System.out.println("jsonstr:"+jsonStr);
            }
        }



        else if(domain.equals("getOrderDetailById"))//获取订单详情
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String oid=(String)session.getAttribute("openid");
            String O_ORDERID=request.getParameter("O_ORDERID");
            //已付款   1    待付款   0     已发货    2    已签收    3     已评论     4      已退款     7      已完成     8
            if(oid==null||oid.equals(""))
            {
                JSONObject message=new JSONObject();
                message.put("code","NoLogin");
                message.put("message","用户信息不存在，请用户重新登录微信");
                jsonStr=message.toString();
            }
            else
            {
                if(O_ORDERID==null||O_ORDERID.equals(""))//没有订单
                {
                    JSONObject message=new JSONObject();
                    message.put("code","NoParam");
                    message.put("message","系统参数错误");
                    jsonStr=message.toString();
                }else
                {
                    sql=new StringBuffer();
                    sql.append("SELECT * FROM STORE_ORDER_INFO I INNER JOIN STORE_USER_ADDRESS A ON I.A_ADDRESSID=A.A_ADDRESSID WHERE i.O_ORDERID='"+O_ORDERID+"'");
                    String json = ui.queryAny(getJsonSql("queryAnySQL",sql.toString()));
                    JSONObject objroot=JSONObject.fromObject(json);


                    String sqlinfo="SELECT * FROM STORE_ORDER_DETAILS D LEFT JOIN STORE_GOODS_INFO I ON D.G_ID=I.G_ID LEFT JOIN STORE_SUP_SUPPLY SUP ON I.SUP_ID=SUP.SUP_ID WHERE d.O_ORDERID='"+O_ORDERID+"'";
                    sql=new StringBuffer();
                    sql.append(sqlinfo);
                    String jsonMessage = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                    JSONArray resultarray = JSONArray.fromObject(jsonMessage);


                    JSONObject rootobj=new JSONObject();
                    JSONObject infoobj=new JSONObject();//总json根下面的data-json,包含商品列表信息，地址信息

                    infoobj.put("infojson",objroot);
                    infoobj.put("listjson",resultarray);

                    rootobj.put("code","success");
                    rootobj.put("data",infoobj);
                    jsonStr=rootobj.toString();
                    System.out.println("jsonstr:"+jsonStr);
                }
            }
        }
        out.println(jsonStr);
    }
    //减去所有购物车的id
    //生成未支付订单




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }


    private String getOpenid(){
        String[] openids=new String[]{"dsfsdfdsfdsfds","dsfdsfsdfdsfdsf","ertererewerweerr",
                "tyutuytuytuytytuytu","qewrwwqeefsdsfsfsds","sdfsfdsfdsfdsfds","sdfdsfdsfdsfds",
                "sfdsdfdsfdsfd","dsfdsfdsfdsfdsfds","sdfdsfssfsfsfswreew"};
        int index=(int)Math.random()*10;
        if(index>=10)
             index=9;
        return openids[index];
    }


    private void addUserList(String openid,HttpSession session) {
        ui=new UserHttpImpl();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT U.U_USERID FROM STORE_USER_INFO U WHERE u.openid='"+openid+"'");
        String jsonStr = ui.queryAny(getJsonSql("queryAnySQL",sql.toString()));
        JSONObject result= JSONObject.fromObject(jsonStr);
        if(result==null||(result.getString("U_USERID")==null))
        {
           ArrayList<String> sqllist=new ArrayList<String>();
           //addAnySQL
            sql=new StringBuffer();
            String userid=UUID.randomUUID().toString();
            userid=userid.replaceAll("-","");
            sql.append("INSERT INTO STORE_USER_INFO(U_USERID,OPENID) VALUES ('"+userid+"','"+openid+"')");
            System.out.println("insertsql:"+sql.toString());
            String sqlinfo=getJsonSql("addAnySQL", sql.toString());
            sqllist.add(sqlinfo);

      //      String info=ui.addAny(getJsonSql("addAnySQL", sql.toString()));

            String detailid=UUID.randomUUID().toString();
            sql=new StringBuffer();
            sql.append("insert into STORE_USER_DETAILS(D_DETAILID,U_USERID) values('"+detailid+"','"+userid+"')");
            sqlinfo=getJsonSql("addAnySQL", sql.toString());
            sqllist.add(sqlinfo);

            session.setAttribute("uid",userid);
      //      boolean isInsert=Boolean.parseBoolean(info);
      //      System.out.println("insert:"+isInsert);
            try
            {
               ExpandHanlerUtil.ExpandUtilTransaction(sqllist);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String userid=result.getString("U_USERID");
            session.setAttribute("uid",userid);
        }


    }
}
