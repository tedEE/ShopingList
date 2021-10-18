package ru.jeinmentalist.mail.shopinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jeinmentalist.mail.shopinglist.R
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>
    (ShopItemDiffCallback()) {

    var count = 0
    // необходимо при использовании ShopListDiffCallback
//    var shopList = listOf<ShopItem>()
//        set(value) {
//            val callback = ShopListDiffCallback(shopList, value)
//            val diffResult = DiffUtil.calculateDiff(callback)
//            diffResult.dispatchUpdatesTo(this)
//            field = value
//        }
    var onShopItemOnLongClickListener : ((ShopItem)->Unit)? = null
    var onShopItemOnClickListener : ((ShopItem)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder, count: ${++count}")
        val layout = when (viewType) {
            ITEM_DISABLED -> R.layout.item_shop_disabled
            ITEM_ENABLED -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
//        val shopItem = shopList[position]
        viewHolder.view.setOnLongClickListener {
            onShopItemOnLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.view.setOnClickListener{
            onShopItemOnClickListener?.invoke(shopItem)
        }
        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()
    }

    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
        super.onViewRecycled(viewHolder)
        viewHolder.tvName.text = ""
        viewHolder.tvCount.text = ""

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            ITEM_ENABLED
        } else {
            ITEM_DISABLED
        }
    }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

//    interface OnShopItemOnLongClickListener{
//        fun onShopItemOnLongClick(shopItem: ShopItem)
//    }

    companion object{
        const val ITEM_ENABLED = 1
        const val ITEM_DISABLED = 3

        const val MAX_POOL_SIZE = 15
    }
}