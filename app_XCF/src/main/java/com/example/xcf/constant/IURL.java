package com.example.xcf.constant;

import okhttp3.HttpUrl;

/**
 * Created by pjy on 2017/6/1.
 */

public interface IURL {
     String ROOT="http://192.168.199.186:8080/EmployeeServer/";
     String REGIST_URL=ROOT+"regist.do";
     String CODE_URL=ROOT+"getCode.do";
     String LOGIN_URL=ROOT+"login.do";
     String ADDEMP_URL=ROOT+"addEmp";
     String EMPLOYEELIST_URL=ROOT+"listEmp";
     String FIRST="first";
     String BASEURL="http://apis.juhe.cn/cook/";
}
