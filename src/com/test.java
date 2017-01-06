package com;

import com.alibaba.fastjson.JSON;
import com.app.web.user.UserHttpImpl;
import com.commons.util.GetAccessToken;
import com.commons.util.JsonUtil;
import com.commons.util.WeChatUtil;

import java.util.*;
import com.commons.util.WeChatUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 16-10-11
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
public class test {

    public static void main(String[] args) throws Exception {
        String a="[{\"MENU_NAME\":\"商品管理\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":11,\"minutes\":2,\"month\":10,\"nanos\":0,\"seconds\":4,\"time\":1479898924000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":null,\"MENU_ID\":\"D81A73DEDA4D45D0B8736F2AAB57E6B6\"},{\"MENU_NAME\":\"分佣管理\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":11,\"minutes\":2,\"month\":10,\"nanos\":0,\"seconds\":17,\"time\":1479898937000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":null,\"MENU_ID\":\"96CEDCBE47264E5FA77E3F53E8163F98\"},{\"MENU_NAME\":\"订单管理\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":11,\"minutes\":2,\"month\":10,\"nanos\":0,\"seconds\":26,\"time\":1479898946000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":null,\"MENU_ID\":\"E589E12CD4B74FDC93F3B5BC42BE52D5\"},{\"MENU_NAME\":\"收支\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":11,\"minutes\":2,\"month\":10,\"nanos\":0,\"seconds\":34,\"time\":1479898954000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":null,\"MENU_ID\":\"C079441976E147CA89D8292B04062C8B\"},{\"MENU_NAME\":\"权限\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":11,\"minutes\":2,\"month\":10,\"nanos\":0,\"seconds\":39,\"time\":1479898959000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":null,\"MENU_ID\":\"EE162F3060B642DF8634CBDF11F4A3FC\"},{\"MENU_NAME\":\"AAAAAA\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":17,\"minutes\":28,\"month\":10,\"nanos\":0,\"seconds\":1,\"time\":1479922081000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":\"96CEDCBE47264E5FA77E3F53E8163F98\",\"MENU_ID\":\"asdfaSSDASAdsafDSFA\"},{\"MENU_NAME\":\"BBBBBB\",\"MENU_CODE\":null,\"INSERTDATE\":{\"date\":23,\"day\":3,\"hours\":17,\"minutes\":28,\"month\":10,\"nanos\":0,\"seconds\":1,\"time\":1479922081000,\"timezoneOffset\":0,\"year\":116},\"MENU_PARENTID\":\"D81A73DEDA4D45D0B8736F2AAB57E6B6\",\"MENU_ID\":\"QERFSBDAEQGRDASDFSDAQREF\"}]";
        Object[] ac=JsonUtil.getObjectArray(a);
        StringBuffer sb=new StringBuffer("[");
        for(int i=0;i<ac.length;i++){
            JSONObject parent=JSONObject.fromObject(ac[i]);
            if(parent.get("MENU_PARENTID").equals("null")){
                sb.append("{\"text\": \""+parent.get("MENU_NAME")+"\"");
                int haschild=0;
                for(int b=0;b<ac.length;b++){
                    JSONObject child=JSONObject.fromObject(ac[b]);
                    if(child.get("MENU_PARENTID").equals(parent.get("MENU_ID"))){
                        if(haschild==0){
                             sb.append(",\"nodes\": [");
                        }
                        sb.append("{\"text\": \""+child.get("MENU_NAME")+"\"},");
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
    }
}
