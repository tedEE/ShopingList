package ru.jeinmentalist.mail.shopinglist.domain.usecase

import ru.jeinmentalist.mail.shopinglist.domain.ShopListRepository
import ru.jeinmentalist.mail.shopinglist.domain.model.ShopItem

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): List<ShopItem>{
        return shopListRepository.getShopList()
    }
}