package ru.jeinmentalist.mail.shopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.jeinmentalist.mail.shopinglist.R
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: ShopListAdapter
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mViewModel.shopList.observe(this){
            mAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        mAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = mAdapter
            // ручная настройка пула вьюхолдеров
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setLongClickListener()
        setupOnClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val item =
//                    mAdapter.shopList[viewHolder.adapterPosition] // получение элемента на котором произашел свайп
                val item =
                    mAdapter.currentList[viewHolder.adapterPosition] // получение элемента на котором произашел свайп
                mViewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupOnClickListener() {
        mAdapter.onShopItemOnClickListener = {
            Log.d("sldfjdfkj", it.name)
        }
    }

    private fun setLongClickListener() {
        mAdapter.onShopItemOnLongClickListener = {
            mViewModel.changeEnabledState(it)
        }
    }
}