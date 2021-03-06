package com.re.ng.uu.comic.http;

import com.re.ng.uu.comic.http.bean.Amount;
import com.re.ng.uu.comic.http.bean.AreaList;
import com.re.ng.uu.comic.http.bean.BannerData;
import com.re.ng.uu.comic.http.bean.BaseBean;
import com.re.ng.uu.comic.http.bean.BookDetail;
import com.re.ng.uu.comic.http.bean.BookList;
import com.re.ng.uu.comic.http.bean.Channel;
import com.re.ng.uu.comic.http.bean.ChapterDetail;
import com.re.ng.uu.comic.http.bean.ChapterList;
import com.re.ng.uu.comic.http.bean.ChargeHistoryData;
import com.re.ng.uu.comic.http.bean.CommentData;
import com.re.ng.uu.comic.http.bean.EndBooks;
import com.re.ng.uu.comic.http.bean.FavorInfo;
import com.re.ng.uu.comic.http.bean.FavorList;
import com.re.ng.uu.comic.http.bean.HotBooks;
import com.re.ng.uu.comic.http.bean.LoginData;
import com.re.ng.uu.comic.http.bean.NewestBooks;
import com.re.ng.uu.comic.http.bean.OrderData;
import com.re.ng.uu.comic.http.bean.PayResult;
import com.re.ng.uu.comic.http.bean.RecommendBooks;
import com.re.ng.uu.comic.http.bean.SearchBean;
import com.re.ng.uu.comic.http.bean.ShareData;
import com.re.ng.uu.comic.http.bean.TypeList;
import com.re.ng.uu.comic.http.bean.mostChargedBooks;
import com.re.ng.uu.comic.http.bean.updateBooks;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Date    : 2020-10-28
 */
public interface ApiService {

    @GET("books/getNewest")
    Observable<NewestBooks> newestBooks();

    @GET("books/getmostcharged")
    Observable<mostChargedBooks> mostCharged();

    @GET("books/getEnds")
    Observable<EndBooks> endBooks();

    @GET("books/getTops")
    Observable<BaseBean> recommendBooks();

    @GET("books/getHot")
    Observable<HotBooks> hotBooks();

    @GET("books/getupdate")
    Observable<BaseBean> updateBooks();

    @GET("getUpdate")
    Observable<updateBooks> getUpdate(@Query("page") String page,
                                      @Query("date") String date);

    @GET("books/search")
    Observable<SearchBean> search(@Query("keyword") String keyword);

    @GET("tag/getBanners")
    Observable<BannerData> banners(@Query("num") int num);

    @GET("tag/getList")
    Observable<TypeList> tagList();

    @GET("tag/getAreaList")
    Observable<AreaList> getArea();

    @GET("Users/getshare")
    Observable<ShareData> getShare(
            @Query("utoken") String token);

    @GET("tag/getBookList")
    Observable<BookList> tagBooks(@Query("tag") String tag,
                                  @Query("startItem") int page,
                                  @Query("pageSize") int pageSize);

    @GET("tag/getBookList")
    Observable<BookList> tagBooks(@Query("area") int area,
                                  @Query("tag") String tag,
                                  @Query("end") int end,
                                  @Query("startItem") int page,
                                  @Query("pageSize") int pageSize);

    @GET("tag/getBookList")
    Observable<BookList> tagBooks(@Query("end") int end,
                                  @Query("startItem") int page,
                                  @Query("pageSize") int pageSize);


    @GET("books/detail")
    Observable<BookDetail> bookDetail(@Query("id") String id);

    @GET("books/getRecommend")
    Observable<RecommendBooks> recommendBooks(@Query("book_id") String book_id);

    @GET("Finance/payment")
    Observable<Channel> getChannel(@Query("utoken") String utoken);

    @GET("Finance/paymoney")
    Observable<Amount> getAmount(@Query("utoken") String utoken);

    @GET("chapters/getList")
    Observable<ChapterList> chapterList(@Query("book_id") String bookId);

    @GET("chapters/detail")
    Observable<ChapterDetail> chapterDetail(@Query("id") String chapterId,
                                            @Query("utoken") String utoken);

    @GET("account/register")
    Observable<BaseBean> register(@Query("username") String username,
                                  @Query("password") String password,
                                  @Query("pid") String pid);

    @GET("account/login")
    Observable<LoginData> login(@Query("username") String username,
                                @Query("password") String password);

    @GET("users/bookshelf")
    Observable<FavorList> bookShelf(@Query("utoken") String utoken);

    @GET("users/isfavor")
    Observable<FavorInfo> hasFavor(@Query("book_id") String book_id,
                                   @Query("utoken") String utoken);

    //ids: ??????id????????????????????????????????????
    @GET("users/delfavors")
    Observable<BaseBean> delFavors(@Query("ids") String ids,
                                   @Query("utoken") String utoken);

    //isfavor??????????????????0????????????1?????????
    @GET("users/switchfavor")
    Observable<BaseBean> switchFavor(@Query("book_id") String book_id,
                                     @Query("isfavor") int isfavor,
                                     @Query("utoken") String utoken);

    @GET("users/history")
    Observable<BaseBean> history(@Query("utoken") String utoken);

    @GET("books/getComments")
    Observable<CommentData> comments(@Query("book_id") String book_id);

    @GET("users/subComment")
    Observable<CommentData> subComments(@Query("book_id") String book_id,
                                        @Query("comment") String comment,
                                        @Query("utoken") String utoken);

    @GET("Finance/buychapter")
    Observable<PayResult> buyChapter(@Query("utoken") String utoken,
                                     @Query("chapter_id") String chapter_id);

    @FormUrlEncoded
    @POST("Finance/vip")
    Observable<BaseBean> buyVip(@Field("utoken") String utoken,
                                @Field("month") int month,
                                @Field("time") String time,
                                @Field("token") String token);

    @FormUrlEncoded
    @POST("Finance/charge")
    Observable<BaseBean> recharge(@Field("utoken") String utoken,
                                  @Field("money") String money,
                                  @Field("pay_type") int pay_type,
                                  @Field("time") String time,
                                  @Field("token") String token);

    @GET("Finance/getSpendings")
    Observable<OrderData> orderHistory(@Query("utoken") String utoken);

    @GET("Finance/getCharges")
    Observable<ChargeHistoryData> rechargeHistory(@Query("utoken") String utoken);

    @GET("Users/sendcms")
    Observable<BaseBean> sendcms(@Query("utoken") String utoken,
                                 @Query("phone") String mobile);

    @GET("Finance/buyhistory")
    Observable<BaseBean> buyhistory(@Query("utoken") String utoken);

    @GET("Finance/vipexchange")
    Observable<BaseBean> exchangeVip(@Query("utoken") String utoken,
                                     @Query("code") String code);

    @GET("Users/updateUser")
    Observable<LoginData> refresh(@Query("utoken") String utoken);

    @GET("Users/update")
    Observable<BaseBean> updateNickName(@Query("utoken") String utoken,
                                        @Query("nickname") String nickname);

    @GET("Users/resetpwd")
    Observable<BaseBean> resetPassword(@Query("utoken") String utoken,
                                       @Query("password") String password);

    @GET("Users/bindphone")
    Observable<BaseBean> bindphone(@Query("utoken") String utoken,
                                   @Query("phonecode") String phonecode,
                                   @Query("phone") String phone);

    @GET("users/getVipExpireTime")
    Observable<BaseBean> vipExpireTime(@Query("utoken") String utoken);

    @GET("Finance/getBalance")
    Observable<BaseBean> userBalance(@Query("utoken") String utoken);


//    @GET("users/resetpwd")
//    Observable<BaseBean> resetpwd(@Query("password") String password);

}
