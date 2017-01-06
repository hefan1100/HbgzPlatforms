package com.webservice;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DemdService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
        String mobileNumber = request.getParameter("mobileNumber") == null ? "" : request.getParameter("mobileNumber");
        String demdId = request.getParameter("demdId") == null ? "" : request.getParameter("demdId");

        Service service = new Service();
//        String endpoint = "http://133.0.160.87:8001/services/DemdService?wsdl";
        String endpoint = "http://10.36.1.50:8001/services/DemdService?wsdl";
//        String endpoint = "http://127.0.0.1:7001/services/DemdService?wsdl";
        String jsonStr = "";

        if (domain.equals("queryDemdEventInfo")) {
            try {
                String ws = request.getParameter("ws") == null ? "" : request.getParameter("ws");
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                //ws方法名
                call.setOperationName("queryDemdEventInfo");
                //一个输入参数,如果方法有多个参数,复制多条该代码即可,参数传入下面new Object后面
                call.addParameter("mobileNumber", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("ws", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                //插入的参数
                jsonStr = (String) call.invoke(new Object[]{mobileNumber,ws});
            } catch (Exception e) {
                System.out.println("queryDemdEventInfo调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("queryDemdInfo")) {
            try {
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("queryDemdInfo");
                call.addParameter("demdId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{demdId});
            } catch (Exception e) {
                System.out.println("queryDemdInfo调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("queryDemdEventLog")) {
            try {
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("queryDemdEventLog");
                call.addParameter("demdId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{demdId});
            } catch (Exception e) {
                System.out.println("queryDemdEventLog调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("initDemdEvent")) {
            try {
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("initDemdEvent");
                call.addParameter("demdId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("mobileNumber", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{demdId,mobileNumber});
            } catch (Exception e) {
                System.out.println("initDemdEvent调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("queryNextDealManager")) {
            try {
                String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("queryNextDealManager");
                call.addParameter("demdId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("mobileNumber", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("nodeId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{demdId,mobileNumber,nodeId});
            } catch (Exception e) {
                System.out.println("queryNextDealManager调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("queryWorkLoadManager")) {
            try {
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("queryWorkLoadManager");
                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{});
            } catch (Exception e) {
                System.out.println("queryWorkLoadManager调用异常!");
                e.printStackTrace();
            }
        }

        if (domain.equals("doDemdEvent")) {
            try {
                String doWhat = request.getParameter("doWhat") == null ? "" : request.getParameter("doWhat");
                String nodeId = request.getParameter("nodeId") == null ? "" : request.getParameter("nodeId");
                String nextNodeId = request.getParameter("nextNodeId") == null ? "" : request.getParameter("nextNodeId");
                String nextDealManager = request.getParameter("nextDealManager") == null ? "" : request.getParameter("nextDealManager");
                String textarea = new String((request.getParameter("textarea") == null ? "" : request.getParameter("textarea")).getBytes("ISO-8859-1"),"UTF-8");
                String evaluate = request.getParameter("evaluate") == null ? "" : request.getParameter("evaluate");
                String manager_1 = request.getParameter("manager_1") == null ? "" : request.getParameter("manager_1");
                String number_1 = request.getParameter("number_1") == null ? "" : request.getParameter("number_1");
                String manager_2 = request.getParameter("manager_2") == null ? "" : request.getParameter("manager_2");
                String number_2 = request.getParameter("number_2") == null ? "" : request.getParameter("number_2");
                String manager_3 = request.getParameter("manager_3") == null ? "" : request.getParameter("manager_3");
                String number_3 = request.getParameter("number_3") == null ? "" : request.getParameter("number_3");
                String manager_4 = request.getParameter("manager_4") == null ? "" : request.getParameter("manager_4");
                String number_4 = request.getParameter("number_4") == null ? "" : request.getParameter("number_4");
                String manager_5 = request.getParameter("manager_5") == null ? "" : request.getParameter("manager_5");
                String number_5 = request.getParameter("number_5") == null ? "" : request.getParameter("number_5");

                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(endpoint);
                call.setOperationName("doDemdEvent");
                call.addParameter("demdId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("mobileNumber", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("doWhat", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("nodeId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("nextNodeId", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("nextDealManager", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("textarea", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("evaluate", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("manager_1", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("number_1", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("manager_2", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("number_2", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("manager_3", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("number_3", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("manager_4", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("number_4", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("manager_5", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
                call.addParameter("number_5", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

                call.setReturnType(XMLType.XSD_STRING);
                call.setUseSOAPAction(true);
                jsonStr = (String) call.invoke(new Object[]{demdId,mobileNumber,doWhat,nodeId,nextNodeId,nextDealManager,textarea,evaluate,
                        manager_1,number_1,manager_2,number_2,manager_3,number_3,manager_4,number_4,manager_5,number_5});
            } catch (Exception e) {
                System.out.println("doDemdEvent调用异常!");
                e.printStackTrace();
            }
        }

        System.out.println(jsonStr);
        out.println(jsonStr);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
