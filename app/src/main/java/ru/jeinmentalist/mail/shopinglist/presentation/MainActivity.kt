package ru.jeinmentalist.mail.shopinglist.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.jeinmentalist.mail.shopinglist.R


class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mViewModel.shopList.observe(this){
            mAdapter.submitList(it)
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener{
            val intent = ShopItemActivity.newIntentAdd(this)
            startActivity(intent)
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
            val intent = ShopItemActivity.newIntentEdit(this, it.id)
            startActivity(intent)
        }
    }

    private fun setLongClickListener() {
        mAdapter.onShopItemOnLongClickListener = {
            mViewModel.changeEnabledState(it)
        }
    }
}