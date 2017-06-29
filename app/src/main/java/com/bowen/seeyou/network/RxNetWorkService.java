package com.bowen.seeyou.network;

import com.bowen.seeyou.bean.BookResult;
import com.bowen.seeyou.bean.SearchResult;
import com.bowen.seeyou.bean.TicketNumberResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 肖稳华 on 2016/11/23.
 * 通用的网络请求框架,用户模块
 */

public interface RxNetWorkService {

    //访问百度后面的空不能省略
    @FormUrlEncoded
    @POST("bc/phone/surplus/ticket/new")
    Observable<TicketNumberResult> checkTick(@FieldMap Map<String, Object> filedMap);

    //预订票
    @FormUrlEncoded
    @POST("order/phone/create")
    Observable<BookResult> bookTicket(@FieldMap Map<String, Object> filedMap);

    //搜索线路
    @FormUrlEncoded
    @POST("bc/phone/data")
    Observable<SearchResult> search(@FieldMap Map<String, Object> filedMap);

    //搜索线路
    @FormUrlEncoded
    @POST("order/phone/main/data")
    Observable<String> getOrderList(@FieldMap Map<String, Object> filedMap);

}
