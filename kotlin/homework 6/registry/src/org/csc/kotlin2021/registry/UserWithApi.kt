package org.csc.kotlin2021.registry

import org.csc.kotlin2021.UserAddress

data class UserWithApi(
    val userAddress: UserAddress,
    val userApi: UserApi,
)
