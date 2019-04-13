package info.mathphys.schatztruhe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="theke_table")
data class Theke(@ColumnInfo(name="name") var name:String,@PrimaryKey(autoGenerate = true) var id:Long?=null) {
}