package info.mathphys.schatztruhe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="product_table")
data class Product(@ColumnInfo(name = "name") var name: String, @PrimaryKey(autoGenerate = true) var id:Long?=null) {
}