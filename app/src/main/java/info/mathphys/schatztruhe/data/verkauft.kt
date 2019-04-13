package info.mathphys.schatztruhe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "verkauft_table", foreignKeys = arrayOf(ForeignKey(entity = Product::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("product_id"),
    onDelete = ForeignKey.NO_ACTION),
    ForeignKey(entity = Theke::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("theke_id"),
        onDelete = ForeignKey.NO_ACTION)
))
data class verkauft(@PrimaryKey(autoGenerate = true) var ID:Long?=null,
                    @ColumnInfo(name = "anzahl") var anzahl:Long,
                     @ColumnInfo(name="product_id") var product_id:Long,
                     @ColumnInfo(name = "theke_id") var theke_id:Long,
                    @ColumnInfo(name = "verschenkt") var verschenkt:Boolean,
                    @ColumnInfo(name="zeitpunkt") var zeitpunkt: Date,
                    @ColumnInfo(name="tablet_imei") var tablet_imei:String
) {
}