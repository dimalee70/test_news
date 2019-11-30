package kz.test.testnews.android.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kz.testwhether.android.modules.base.domain.BaseItem
import java.util.ArrayList

/**
 * Created by wokrey@gmail.com on 8/20/19.
 * It's not wokrey, if the code smells bad. Somebody set me up.
 */
class RecyclerBindingAdapter<T: BaseItem>(
    private val variableId: Int,
    private val context: Context
) : RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder>() {
    private var items: List<T> = ArrayList()
    private var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerBindingAdapter.BindingHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return BindingHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerBindingAdapter.BindingHolder, position: Int) {
        val item = items[position]

        holder.binding!!.root.setOnClickListener { v ->
            if (onItemClickListener != null)
                onItemClickListener!!.onItemClick(position, item)
        }
        holder.binding.setVariable(variableId, item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
//            ?: return R.layout.item_reguest_title
//        if (item is BaseMission){
//            if (item is PartnerMission)
//                return R.layout.item_partner_mission
//            else if(item is PhotogradMission)
//                return R.layout.item_photograd_mission
//        }
//        return holderLayout
        return item.getHolderLayout()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(position: Int, item: T)
    }

    class BindingHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding?

        init {
            binding = DataBindingUtil.bind(v)
        }
    }

    fun setItems(items: List<T>?) {
        if (items != null) {
            this.items = items
            notifyDataSetChanged()
        }
    }
}