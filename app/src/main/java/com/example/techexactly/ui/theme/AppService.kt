import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.POST

import retrofit2.http.Body

interface AppService {
    @POST("api/v1/apps/list")
    fun getAppList(@Body requestBody: RequestBody): Call<AppListResponse>
}

