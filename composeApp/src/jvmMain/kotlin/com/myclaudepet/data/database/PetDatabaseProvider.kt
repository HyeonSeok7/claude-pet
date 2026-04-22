package com.myclaudepet.data.database

import com.myclaudepet.db.PetDatabase

object PetDatabaseProvider {

    fun create(driverFactory: DriverFactory): PetDatabase =
        PetDatabase(driverFactory.create())
}
