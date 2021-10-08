package ru.jeinmentalist.mail.shopinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.jeinmentalist.mail.shopinglist.data.ShopListRepositoryImp
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem
import ru.jeinmentalist.mail.shopinglist.domain.usecase.DeleteShopItemUseCase
import ru.jeinmentalist.mail.shopinglist.domain.usecase.EditShopItemUseCase
import ru.jeinmentalist.mail.shopinglist.domain.usecase.GetShopListUseCase

class MainViewModel : ViewModel() {

    // временное решение, presentation ничего не должен знать о data слое
    private val repository = ShopListRepositoryImp
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList(){
        shopList.value = getShopListUseCase.getShopList()
    }

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
        getShopList()
    }

    fun changeEnabledState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
        getShopList()
    }
}