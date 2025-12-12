package com.example.guitarstore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val title: String,
    val price: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,
    val fCh: Int,
    val sCh: Int,
    val tCh: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (price != other.price) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}