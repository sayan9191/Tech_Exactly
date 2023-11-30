import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techexactly.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.squareup.picasso.Picasso

class AppAdapter(private val context: Context, private val appList: List<AppItem>) :
    RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val switch: SwitchMaterial = itemView.findViewById(R.id.switch2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appItem = appList[position]

        holder.titleTextView.text = appItem.app_name
        Picasso.get()
            .load(appItem.app_icon)
            .into(holder.iconImageView)

        // Set the switch state based on the 'status' property
        holder.switch.isChecked = appItem.status.equals("Active", ignoreCase = true)

        // Disable the switch if needed
        holder.switch.isClickable = appItem.status.equals("disabled", ignoreCase = true)

        // Change the appearance based on the 'status' property
        if (appItem.status.equals("disabled", ignoreCase = true)) {
            holder.switch.thumbDrawable.setTint(context.resources.getColor(R.color.grey)) // Set the color to grey
            holder.switch.trackDrawable.setTint(context.resources.getColor(R.color.grey)) // Set the color to grey
        } else {
            holder.switch.thumbDrawable.setTint(context.resources.getColor(R.color.green)) // Set the color to green
            holder.switch.trackDrawable.setTint(context.resources.getColor(R.color.green)) // Set the color to green
        }

        // Set a listener to handle switch changes
        holder.switch.setOnCheckedChangeListener(null) // Clear existing listener to avoid unwanted calls
        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            // Do nothing since the switch is non-interactive
        }
    }


    override fun getItemCount(): Int {
        return appList.size
    }
}
