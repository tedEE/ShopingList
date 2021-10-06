package ru.jeinmentalist.mail.shopinglist.domain.usecase

import ru.jeinmentalist.mail.shopinglist.domain.ShopListRepository
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItem(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}