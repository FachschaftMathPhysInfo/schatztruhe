package info.mathphys.schatztruhe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "bietet_an_table", foreignKeys = arrayOf(ForeignKey(entity = Product::class,
        parentColumns = arrayOf("id"),
    childColumns = arrayOf("product_id"),
    onDelete = ForeignKey.NO_ACTION),
    ForeignKey(entity = Theke::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("theke_id"),
        onDelete = ForeignKey.NO_ACTION)
    ))
data class bietet_an(@PrimaryKey(autoGenerate = true) var ID:Long?=null,
                     @ColumnInfo(name="product_id") var product_id:Long,
                     @ColumnInfo(name = "theke_id") var theke_id:Long) {
}