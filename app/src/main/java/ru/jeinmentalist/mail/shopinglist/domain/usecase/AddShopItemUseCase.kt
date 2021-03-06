package ru.jeinmentalist.mail.shopinglist.domain.usecase

import ru.jeinmentalist.mail.shopinglist.domain.ShopListRepository
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun addShopItem(shopItem: ShopItem){
        shopListRepository.addShopItem(shopItem)
    }
}