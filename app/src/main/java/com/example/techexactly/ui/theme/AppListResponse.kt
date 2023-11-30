import com.google.gson.annotations.SerializedName

data class AppListResponse(
    val success: Boolean,
    val data: AppListData?,
    val message: String
)

data class AppListData(
    @SerializedName("app_list") val appList: List<AppItem>
)

data class AppItem(
    val app_id: Int,
    val fk_kid_id: Int,
    val kid_profile_image: String,
    val app_name: String,
    val app_icon: String,
    val app_package_name: String,
    var status: String
)
