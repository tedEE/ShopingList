package ru.jeinmentalist.mail.shopinglist.domain.usecase

import ru.jeinmentalist.mail.shopinglist.domain.ShopListRepository
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun deleteShopItem(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}