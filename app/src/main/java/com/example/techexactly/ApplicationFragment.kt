import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techexactly.R

import com.example.techexactly.ui.theme.DataLoadListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter
    private lateinit var progressBar: ProgressBar
    lateinit var constraintLayout : ConstraintLayout
    private lateinit var dataLoadListener: DataLoadListener
    private lateinit var searchView: SearchView

    private var originalAppList: List<AppItem> = listOf()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_application, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        dataLoadListener = requireActivity() as DataLoadListener
        searchView = view.findViewById(R.id.searchView)
        constraintLayout = view.findViewById(R.id.constraint)
        recyclerView.visibility = View.GONE
        constraintLayout.visibility = View.VISIBLE

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://navkiraninfotech.com/g-mee-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val appService = retrofit.create(AppService::class.java)

        // Making the API request using POST method
        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            """{"kid_id": 378}"""
        )

        val call = appService.getAppList(requestBody)

        call.enqueue(object : Callback<AppListResponse> {
            override fun onResponse(
                call: Call<AppListResponse>,
                response: Response<AppListResponse>
            ) {
                if (response.isSuccessful) {
                    recyclerView.visibility = View.VISIBLE
                    dataLoadListener.isLoaded(true)
                    constraintLayout.visibility = View.GONE
                    val appList = response.body()?.data?.appList
                    originalAppList = appList ?: emptyList()
                    showAppList(originalAppList)
                } else {
                    showError("API request failed: ${response.message()}")
                    // Log the complete server response for further inspection
                    Log.d("API_RESPONSE_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<AppListResponse>, t: Throwable) {
                constraintLayout.visibility = View.GONE
                showError("Network request failed")
                Log.e("NETWORK_ERROR", t.message ?: "Unknown error")
            }
        })

        // Set up the SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()  // Cancel the previous job if still running
                searchJob = lifecycleScope.launch {
                    delay(300)  // Debounce time
                    filterAppList(newText.orEmpty())
                }
                return true
            }
        })

        return view
    }

    private fun showAppList(appList: List<AppItem>) {
        appAdapter = AppAdapter(requireContext(), appList)
        recyclerView.adapter = appAdapter
    }

    private fun filterAppList(query: String) {
        val filteredList = originalAppList.filter { app ->
            app.app_name.contains(query, ignoreCase = true)
        }
        showAppList(filteredList)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
