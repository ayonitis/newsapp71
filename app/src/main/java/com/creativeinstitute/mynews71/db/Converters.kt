package com.creativeinstitute.mynews71.db

import androidx.room.TypeConverter
import com.creativeinstitute.mynews71.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }

}