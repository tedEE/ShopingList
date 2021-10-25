package ru.jeinmentalist.mail.shopinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.jeinmentalist.mail.shopinglist.R
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var mTilName: TextInputLayout
    private lateinit var mTilCount: TextInputLayout
    private lateinit var mEtName: EditText
    private lateinit var mEtCount: EditText
    private lateinit var mButtonSave: Button
    private lateinit var mViewModel: ShopItemViewModel

    private var mScreenMode = MODE_UNKNOWN
    private var mShopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        mViewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews()
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel(){
        mViewModel.errorInputCount.observe(this){
            val message = if (it){
                getString(R.string.error_input_count)
            }else{
                null
            }
            mTilCount.error = message
        }
        mViewModel.errorInputName.observe(this){
            val message = if (it){
                getString(R.string.error_input_name)
            }else{
                null
            }
            mTilName.error = message
        }
        mViewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }

    private fun addTextChangeListener(){
        mEtName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        mEtCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun launchRightMode(){
        when(mScreenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode(){
        mViewModel.getShopItem(mShopItemId)
        mViewModel.shopItem.observe(this) {
            mEtName.setText(it.name)
            mEtCount.setText(it.count.toString())
        }
        mButtonSave.setOnClickListener{
            mViewModel.editShopItem(mEtName.text?.toString(), mEtCount.text?.toString())
        }
    }

    private fun launchAddMode(){
        mButtonSave.setOnClickListener{
            mViewModel.addShopItem(mEtName.text?.toString(), mEtCount.text?.toString())
        }
    }

    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        mScreenMode = mode
        if (mScreenMode == MODE_EDIT ){
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param shop item id is absent")
            }
            mShopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(){
        mTilName = findViewById(R.id.til_name)
        mTilCount = findViewById(R.id.til_count)
        mEtName = findViewById(R.id.et_name)
        mEtCount = findViewById(R.id.et_count)
        mButtonSave = findViewById(R.id.save_button)
    }

    companion object{
        private const val  EXTRA_SCREEN_MODE = "extra_mode"
        private const val  EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        // режим запуска экрана, будетли он добавлять элемент или редактировать
        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }
}