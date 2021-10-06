package ru.jeinmentalist.mail.shopinglist.data

import ru.jeinmentalist.mail.shopinglist.domain.ShopListRepository
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem
import java.lang.RuntimeException

object ShopListRepositoryImp : ShopListRepository {

    // времменное решение пока не добавлено бд
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = autoIncrementId ++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        // можно убрать exception и вернуть нулабельный тип
        return shopList.find { it.id == shopItemId } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()// возвращене немутабельного листа
    }
}