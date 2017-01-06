package com.webservice;

import com.app.web.user.UserHttpImpl;
import com.commons.util.JsonUtil;
import com.commons.util.StringUtil;
import com.commons.util.WeChatUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by swt on 2015/10/13.
 * sql处理方式：






 * 1、列表：queryAnyListSQL
 * 2、查询返回单行：queryAnySQL
 * 3、新增：addAnySQL
 * 4、修改：updateAnySQL
 * 5、删除：delAnySQL
 * url: http://localhost:7001/serv?domain=queryLogin
 */

public class ManageService extends HttpServlet {
    UserHttpImpl ui = null;
    private static final int CODE_SUCCESS=          1;
    private static final int CODE_FAILURE=          2;

    private String getJsonSql(String mathod, String sql){
        return "{\""+mathod+"\":\""+sql+"\"}";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
//        String whereSql = new String((request.getParameter("whereSql") == null ? "" : request.getParameter("whereSql")).getBytes("ISO-8859-1"),"UTF-8");
        HttpSession session=request.getSession();
        String params = new String((request.getParameter("params") == null ? "" : request.getParameter("params")));
        String jsonStr = "";
        JSONObject objs = JSONObject.fromObject(params);

        if(domain.equals("login")){
            JSONObject obj = JSONObject.fromObject(params);
            String username=obj.get("username").toString();
            String pwd=obj.get("pwd").toString();
            String platform =obj.get("platform").toString();
            ui = new UserHttpImpl();
            if(platform.equals("store")){
                //商户登录
                String sql = "select * from store_sup_supply where sup_username='"+username+"' and sup_pwd='"+pwd+"'";
                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                JSONArray  ja=JSONArray.fromObject(jsonStr);
                JSONObject rejson = ja.getJSONObject(0);
                String userid=rejson.get("SUP_ID").toString();
                session.setAttribute(userid,"store");
            }
        } else if(objs.has("user")){
            //微信平台入口
            if(session.getAttribute(objs.get("user").toString())!=null){
                if(session.getAttribute(objs.get("user").toString()).equals("store")){
                    //商户接口
                    String user=objs.get("user").toString();

                    if(domain.equals("goodsCount"))
                    {
                        //商品列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select count(*) as count from store_goods_info a join store_dic_dictionary b on a.g_type=b.dic_code and dic_type='goods_type' where sup_id='"+user+"'");
                        if(obj.has("g_id")){
                            sql.append(" and g_id='"+objs.get("g_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("goodsList"))
                    {
                        //商品列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        int page=Integer.parseInt(obj.get("page").toString());
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT g_id,g_name,g_price,g_type,g_amount,status,creater,sup_id,(select dic_name from store_dic_dictionary where dic_type='goods_type' and DIC_CODE=t.g_type) as dicname FROM store_goods_info t where 1=1 ");
                        if(obj.has("g_id")){
                            sql.append(" and g_id='"+objs.get("g_id").toString()+"'");
                        }
                        sql.append("limit "+(page-1)*20+","+page*20+"");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("getDicTpye"))
                    {
                        //获取字典类型，获得类型信息
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select * from store_dic_dictionary where 1=1 ");
                        if(obj.has("dic_type")){
                            sql.append(" and dic_type='"+objs.get("dic_type").toString()+"'");
                        }
                        if(obj.has("dic_parentcode")){
                            sql.append(" and dic_parentcode='"+objs.get("dic_parentcode").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateGoods"))
                    {
                        //新增或保存商品
                        JSONObject obj = JSONObject.fromObject(params);
                        String type=obj.get("type").toString();
                        if(type.equals("saveorupdate")){
                            String userid=obj.get("user").toString();
                            String g_name=obj.get("g_name").toString();
                            String g_price=obj.get("g_price").toString();
                            String g_amount=obj.get("g_amount").toString();
                            String g_status=obj.get("g_status").toString();
                            String g_type=obj.get("g_type").toString();
                            if(obj.get("g_id").toString().length()!=32){
                                UUID uuid=UUID.randomUUID();
                                String id=uuid.toString().replaceAll("-","");
                                ui = new UserHttpImpl();
                                String sql = "insert into STORE_GOODS_INFO (g_id,g_name,g_price,g_type,g_amount,status,creater,sup_id) values('"+id+"','"+g_name+"','"+g_price+"','"+g_type+"','"+g_amount+"','"+g_status+"','"+userid+"','"+userid+"')";
                                jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                            }else{
                                String g_id=obj.get("g_id").toString();
                                String sql="update STORE_GOODS_INFO set g_name='"+g_name+"',g_price='"+g_price+"',g_amount='"+g_amount+"',status='"+g_status+"',g_type='"+g_type+"' where g_id='"+g_id+"'" ;
                                jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                            }
                        }else if(type.equals("puton")){
                            //商品上架
                            String gids=obj.getString("g_id").toString();
                            String gid[]=gids.split(",");
                            for(int i=0;i<gid.length;i++){
                                String sql = "update STORE_GOODS_INFO set status=1 where g_id='"+gid[i]+"'";
                                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                            }
                        }else if(type.equals("putoff")){
                            //商品下架
                            String gids=obj.getString("g_id").toString();
                            String gid[]=gids.split(",");
                            for(int i=0;i<gid.length;i++){
                                String sql = "update STORE_GOODS_INFO set status=0 where g_id='"+gid[i]+"'";
                                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                            }
                        }

                    }
                    if(domain.equals("discountList"))
                    {
                        //优惠卷列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select s.*,(select dic_name from STORE_DIC_DICTIONARY d where s.c_type=d.dic_code and d.dic_type='discount_type') as c_typename,(select dic_name from STORE_DIC_DICTIONARY d where s.c_goodtype=d.dic_code and d.dic_type='goods_type') as c_goodstypename from store_sup_discount s where s.sup_id='"+user+"'");
                        if(obj.has("c_id")){
                            sql.append(" and c_id='"+objs.get("c_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("discountCount"))
                    {
                        //优惠卷列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select count(*) as count from store_sup_discount s where s.sup_id='"+user+"'");
                        if(obj.has("c_id")){
                            sql.append(" and c_id='"+objs.get("c_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateDiscount"))
                    {
                        //新增或更新优惠卷
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        String name=obj.get("name").toString();
                        String money=obj.get("money").toString();
                        String detail=obj.get("detail").toString();
                        String startmoney=obj.get("startmoney").toString();
                        String startdate=obj.get("startdate").toString();
                        String deadline=obj.get("deadline").toString();
                        String goodtype=obj.get("goodtype").toString();
                        String discounttype=obj.get("discounttype").toString();
                        if(obj.get("id").toString().length()!=32){
                            UUID uuid=UUID.randomUUID();
                            String newid=uuid.toString().replaceAll("-","");
                            ui = new UserHttpImpl();
                            String sql = "insert into store_sup_discount (c_id,sup_id,c_name,c_money,c_type,c_detail,c_startmoney,c_startdate,c_deadline,c_goodtype) values('"+newid+"','"+user+"','"+name+"','"+money+"','"+discounttype+"','"+detail+"','"+startmoney+"',to_date('"+startdate+"','yyyy-mm-dd'),to_date('"+deadline+"','yyyy-mm-dd'),'"+goodtype+"')";
                            jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                        }else{
                            String sql="update store_sup_discount set c_name='"+name+"',c_money='"+money+"',c_type='"+discounttype+"',c_detail='"+detail+"',c_startmoney='"+startmoney+"',c_startdate=to_date('"+startdate+"','yyyy-mm-dd'),c_deadline=to_date('"+deadline+"','yyyy-mm-dd'),c_goodtype='"+goodtype+"'  where c_id='"+id+"' and sup_id='"+user+"'" ;
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                        }

                    }
                    if(domain.equals("deldiscounts"))
                    {
                        //删除优惠卷
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String cids=obj.getString("cids").toString();
                        String cid[]=cids.split(",");
                        for(int i=0;i<cid.length;i++){
                            String sql = "delete from store_sup_discount where c_id='"+cid[i]+"'";
                            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        }
                    }
                    if(domain.equals("commissionList")){
                        //分佣列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select a.o_orderid,(select g_name from store_goods_info where g_id = a.g_id) as g_name,a.info_amount,info_totalprice,b.lastmodify,(select u_name from store_user_info where u_userid in(select u_userid from store_commission where com_code = a.com_code) ) as username from store_order_details a, store_order_info b where a.g_id in (select g_id from store_goods_info where sup_id = '"+user+"')");
                        if(obj.has("orderid")&& !obj.get("orderid").toString().equals("null") && !StringUtil.isBlank(obj.get("orderid").toString())){
                            sql.append(" and a.o_orderid like '%"+objs.get("orderid").toString()+"%'");
                        }
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and lastmodify>to_date('"+objs.get("startDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and lastmodify<to_date('"+objs.get("endDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("goodname")&& !obj.get("goodname").toString().equals("null")&& !StringUtil.isBlank(obj.get("goodname").toString())){
                            sql.append(" and a.g_id in (select g_id from store_goods_info where g_name like '%"+objs.get("goodname").toString()+"%')");
                        }
                        if(obj.has("username")&& !obj.get("username").toString().equals("null")&& !StringUtil.isBlank(obj.get("username").toString())){
                            sql.append("and a.com_code in(select a.com_code from store_order_details a, store_order_info b where b.u_userid in (select u_userid from store_user_info where u_name like '%"+obj.get("username").toString()+"%'))");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("orderDetailList")){
                        //订单明细列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select b.*,(select dic_name from store_dic_dictionary where dic_code=b.info_status and dic_type='info_status') as statusname,(select g_name from store_goods_info where g_id=b.g_id) as goodsname,(select d_name from store_user_details where u_userid=a.u_userid) as username,a.lastmodify from store_order_info a,store_order_details b where b.g_id in (select g_id from store_goods_info where sup_id = '"+user+"')");
                        if(obj.has("infoid")&& !obj.get("infoid").toString().equals("null") && !StringUtil.isBlank(obj.get("infoid").toString())){
                            sql.append(" and b.info_id like '%"+objs.get("infoid").toString()+"%'");
                        }
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and lastmodify>to_date('"+objs.get("startDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and lastmodify<to_date('"+objs.get("endDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("goodname")&& !obj.get("goodname").toString().equals("null")&& !StringUtil.isBlank(obj.get("goodname").toString())){
                            sql.append(" and b.g_id in (select g_id from store_goods_info where g_name like '%"+objs.get("goodname").toString()+"%')");
                        }
                        if(obj.has("username")&& !obj.get("username").toString().equals("null")&& !StringUtil.isBlank(obj.get("username").toString())){
                            sql.append("and a.u_userid in (select u_userid from store_user_info where u_userid=a.u_userid and u_name  like '%"+objs.get("username").toString()+"%')");
                        }
                        if(obj.has("status")&& !obj.get("status").toString().equals("null")&& !StringUtil.isBlank(obj.get("status").toString())){
                            sql.append("and b.info_status = '"+obj.get("status").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("approveOrder")){
                        //退款单审批
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String infoids=obj.getString("infoids").toString();
                        String infoid[]=infoids.split(",");
                        for(int i=0;i<infoid.length;i++){
                            String aprovesql = "update store_order_details set info_status=7 where info_id='"+infoid[i]+"'";
                            String financialsql="insert  into  store_order_expenses (sup_id,o_orderid,exp_id,exp_expenses,exp_Date,exp_Checkid,exp_checkdate) values((select sup_id from store_goods_info where g_id=(select g_id from store_order_details where info_id='"+infoid[i]+"')),'"+infoid[i]+"',sys_guid(),(select info_totalprice from store_order_details where info_id='"+infoid[i]+"'),sysdate,'"+user+"',sysdate)";
                            ui.addAny(getJsonSql("addAnySQL",financialsql));
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",aprovesql));

                        }
                    }
                    if(domain.equals("expensesList")){
                        //支出
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select a.*,(select sup_name from store_sup_supply where sup_id=a.exp_checkid) as checkname from store_order_expenses a where sup_id='"+user+"'");
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and exp_date>to_date('"+objs.get("startDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and exp_date<to_date('"+objs.get("endDate").toString()+"','yyyy-MM-dd')");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("incomeList")){
                        //收入
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select * from store_order_income  where sup_id='"+user+"'");
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and in_date>to_date('"+objs.get("startDate").toString()+"','yyyy-MM-dd')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and in_date<to_date('"+objs.get("endDate").toString()+"','yyyy-MM-dd')");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("supMenuList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select menu_id,menu_name,menu_parentid,menu_code,menu_url,to_char(insertdate,'yyyy-mm-dd') as ins,(select menu_name from store_sup_menu where t.menu_parentid=menu_id) as menu_parent from store_sup_menu t");
                        if(obj.has("id")){
                            sql.append(" where menu_id='"+obj.get("id")+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateSupMenu")){
                        //保存或更新页面
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        String menucode=obj.get("menucode").toString();
                        String menuname=obj.get("menuname").toString();
                        String menuurl=obj.get("menuurl").toString();
                        String parentmenu=obj.get("parentmenu").toString();
                        if(obj.get("id").toString().length()!=32){
                            ui = new UserHttpImpl();
                            String sql = "insert into store_sup_menu(menu_id,menu_name,menu_parentid,menu_code,menu_url) values (sys_guid(),'"+menuname+"','"+parentmenu+"','"+menucode+"','"+menuurl+"')";
                            jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                        }else{
                            String sql="update store_sup_menu set menu_name='"+menuname+"',menu_parentid='"+parentmenu+"',menu_code='"+menucode+"',menu_url='"+menuurl+"' where menu_id='"+id+"' " ;
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                        }
                    }
                    if(domain.equals("supplyTypeList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select * from store_supply_type where sup_id='"+user+"'");
                        if(obj.has("id")){
                            String id=obj.get("id").toString();
                            sql.append(" and sup_type_id='"+id+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateSupplyType")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        if(obj.get("id").toString().length()!=32){
                            String typeName=obj.get("typename").toString();
                            StringBuffer sql = new StringBuffer();
                            UUID uuid=UUID.randomUUID();
                            String type_id=uuid.toString().replaceAll("-","").toUpperCase();
                            sql.append("insert into store_supply_type (sup_type_id,sup_id,sup_type_name) values ('"+type_id+"','"+user+"','"+typeName+"')");
                            jsonStr = ui.addAny(getJsonSql("addAnySQL", sql.toString()));
                            String menuids=obj.get("menuid").toString();
                            String[] menuid=menuids.split(",");
                            for(int i=0;i<menuid.length;i++){
                                String authsql="insert into store_auth_info (menu_id,sup_type_id) values ('"+menuid[i]+"','"+type_id+"')";
                                ui.addAny(getJsonSql("addAnySQL", authsql.toString()));
                            }
                        }else{
                            String delsql="delete from store_auth_info where sup_type_id='"+id+"'";
                            ui.delAny(getJsonSql("delAnySQL",delsql));
                            String menuids=obj.get("menuid").toString();
                            String[] menuid=menuids.split(",");
                            for(int i=0;i<menuid.length;i++){
                                String authsql="insert into store_auth_info (menu_id,sup_type_id) values ('"+menuid[i]+"','"+id+"')";
                                ui.addAny(getJsonSql("addAnySQL", authsql.toString()));
                            }
                        }
                        StringBuffer sql = new StringBuffer();
                        sql.append("select * from store_supply_type where sup_id='"+user+"'");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("getUserMenu")){
                        //获取用户界面
                        ui=new UserHttpImpl();
                        StringBuffer sql = new StringBuffer();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id="";
                        if(obj.has("id")){
                            id=obj.get("id").toString();
                        }
                        if(obj.has("id")){
                            sql.append("select MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL from store_sup_menu where 1=1 and menu_id in (select menu_id from store_auth_info where sup_type_id ='"+id+"')");
                        }else if(user.equals("112314asdadas")){
                            sql.append("select MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL from store_sup_menu") ;
                        } else{
                            sql.append("select MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL from store_sup_menu where 1=1 and menu_id in (select menu_id from store_auth_info where sup_type_id in (select sup_type_id from store_sup_supply where sup_id='"+user+"'))");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(sql.toString());
                        System.out.println(jsonStr);
                    }
                    if(domain.equals("getMenuJson")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        if(user.equals("112314asdadas")){
                            sql.append("select MENU_PARENTID,MENU_NAME,MENU_ID,MENU_URL from store_sup_menu") ;
                        } else{
                            sql.append("select MENU_PARENTID,MENU_NAME,MENU_ID,MENU_URL from store_sup_menu where menu_id in (select menu_id from store_auth_info where sup_type_id in (select sup_type_id from store_sup_supply where sup_id='"+user+"'))");
                        }
                        String data = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));

                        Object[] ac= JsonUtil.getObjectArray(data);
                        StringBuffer sb=new StringBuffer("[");
                        for(int i=0;i<ac.length;i++){
                            JSONObject parent=JSONObject.fromObject(ac[i]);
                            if(parent.get("MENU_PARENTID").equals("null")){
                                sb.append("{\"text\": \""+parent.get("MENU_NAME")+"\",\"id\":\""+parent.get("MENU_ID")+"\"");
                                int haschild=0;
                                for(int b=0;b<ac.length;b++){
                                    JSONObject child=JSONObject.fromObject(ac[b]);
                                    if(child.get("MENU_PARENTID").equals(parent.get("MENU_ID"))){
                                        if(haschild==0){
                                            sb.append(",\"nodes\": [");
                                        }
                                        sb.append("{\"text\": \""+child.get("MENU_NAME")+"\",\"id\":\""+child.get("MENU_ID")+"\"},");
                                        haschild++;
                                    }
                                }
                                if(haschild>0){
                                    sb.deleteCharAt(sb.length()-1);
                                    sb.append("]");
                                }
                                sb.append("},");
                            }
                        }
                        sb.deleteCharAt(sb.length()-1);
                        sb.append("]");
                        System.out.println(sb.toString());
                        jsonStr=sb.toString();
                    }
                    if(domain.equals("commentList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("select (select g_name from store_goods_info where g_id=t.g_id) as g_name,to_char(t.c_comment) as C_COMMENT,to_char(t.c_insertdate,'yyyy-mm-dd') as c_insertdate,u_name from store_order_comment t join store_order_info o on o.o_orderid= t.o_orderid join store_user_info u on u.u_userid=o.u_userid where 1=1 and g_id in(select g_id from store_goods_info where sup_id='"+user+"')");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                    }

                }
            }else{
                jsonStr="needlogin";
            }
        }else{
            jsonStr="needlogin";
        }

        System.out.println(jsonStr);
        out.println(jsonStr);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
