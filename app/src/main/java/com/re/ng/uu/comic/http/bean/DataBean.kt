package com.re.ng.uu.comic.http.bean

import com.google.gson.annotations.SerializedName
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.util.*

/**
 * Date    : 2020-10-29
 */
data class TypeList(val tags: List<TypeBean>) : BaseBean()

data class BookList(val books: List<BookBean>) : BaseBean()

data class NewestBooks(val newest: List<BookBean>) : BaseBean()

data class EndBooks(val ends: List<BookBean>) : BaseBean()

data class Channel(val date: List<ChannelBeen>) : BaseBean()

data class RecommendBooks(val recommends: List<BookBean>) : BaseBean()

data class HotBooks(val hots: List<BookBean>) : BaseBean()

data class updateBooks(val books: List<BookBean>) : BaseBean()

data class mostChargedBooks(val most: List<BookBean>) : BaseBean()

data class BookDetail(val book: BookBean) : BaseBean()

data class ChapterList(val chapters: List<ChapterBean>) : BaseBean()

data class ChapterDetail(
    val chapter: ChapterBean,
    val chapters: List<ChapterBean>,
    val money: String
) : BaseBean()

data class LoginData(val userInfo: UserInfo) : BaseBean()

data class FavorInfo(val isfavor: Int) : BaseBean()

data class FavorList(val favors: List<FavorBean>) : BaseBean()

data class BannerData(val banners: List<BannerBean>) : BaseBean()

data class SearchBean(
    val hot_search: List<String>,
    val books: List<BookBean>
) : BaseBean()

data class CommentData(val comments: List<CommentBean>) : BaseBean()

data class PayResult(val balance: Int) : BaseBean()

data class OrderData(val spendings: List<OrderBean>) : BaseBean()

data class ChargeHistoryData(val charges: List<OrderBean>) : BaseBean()

data class OrderBean(
    val create_time: String,
    val id: Int,
    val money: String,
    val summary: String,
    val update_time: String,
    val usage: Int,
    val user_id: Int
)

data class CommentBean(
    val book_id: Int,
    val content: String,
    val create_time: String,
    val id: Int,
    val update_time: String,
    val user_id: Int,
    val user: UserInfo
)

data class ChannelBeen(
    val type: Int,
    val code: Int,
    val img: String,
    val title: String,
    val switch: Int,
    val name: String,
    var click:Boolean
): LitePalSupport()

data class FavorBean(
    val book: BookBean,
    val book_id: Int,
    val create_time: String,
    val id: Int,
    val update_time: String,
    val user_id: Int
)

data class UserInfo(
    var balance: Int,
    val email: String,
    val mobile: String,
    val nick_name: String,
    val uid: Int,
    val username: String,
    val utoken: String
) : LitePalSupport() {
    var vip_expire_time: Int = 0
    var password: String? = null
}

data class TypeBean(
    val cover_url: String,
    val id: Int,
    val tag_name: String,
    var click: Boolean
)

data class BookBean(
    val area_id: Int,
    val author_id: Int,
    val author_name: String,
    val banner_url: String,
    val book_name: String,
    val chapter_count: Int,
    val cover_url: String,
    val create_time: String,
    val delete_time: Int,
    val end: Int,
    @SerializedName("id")
    @Column(nullable = false, unique = true)
    val book_id: Int,
    val is_copyright: Int,
    val is_top: Int,
    val last_chapter: String,
    val last_chapter_id: Int,
    val last_time: Int,
    val money: String,
    val nick_name: String,
    val src_url: String,
    val start_pay: Int,
    val summary: String,
    val tags: String,
    val unique_id: String,
    val update_time: String,
    val start: Int,
    val area: Area,
    var firstOpenLastChapterId: Long = 0,
    var recentChapter: String? = null,
    var recentChapterId: Long = 0,
    var lastRecordChapter: String? = null,
    var lastRecordChapterId: Long = 0,
    var time: Date? = null
) : LitePalSupport() {

    fun getPrimaryKey(): Long {
        return baseObjId
    }
}

data class Area(
    val area_name: String,
    val create_time: String,
    val id: Int,
    val update_time: String
)

data class VerifyImg(
    val `data`: String,
    val key: String
)

data class BannerBean(
    val banner_order: Int,
    val book_id: Int,
    val create_time: String,
    val id: Int,
    val pic_name: String,
    val title: String,
    val update_time: String
)

data class RechargeMoney(
    val show: String,
    val month: Int,
    val value: Int,
    var isSelected: Boolean,
    var desc: String = ""
)
