package ru.jeinmentalist.mail.shopinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.jeinmentalist.mail.shopinglist.data.ShopListRepositoryImp
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem
import ru.jeinmentalist.mail.shopinglist.domain.usecase.AddShopItemUseCase
import ru.jeinmentalist.mail.shopinglist.domain.usecase.EditShopItemUseCase
import ru.jeinmentalist.mail.shopinglist.domain.usecase.GetShopItemUseCase

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImp
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    // следует закрыть экран
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItem: ShopItem) {
        val item = getShopItemUseCase.getShopItem(shopItem.id)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValidate = validateInput(name, count)
        if (fieldsValidate) {
            addShopItemUseCase.addShopItem(ShopItem(name, count, enabled = true))
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValidate = validateInput(name, count)
        if (fieldsValidate) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    // сброс ошибки инпутов
    fun resetErrorInputName() {
        _errorInputName.value = false
    }
    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isEmpty()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    private fun finishWork(){
        _shouldCloseScreen.value = Unit
    }
}