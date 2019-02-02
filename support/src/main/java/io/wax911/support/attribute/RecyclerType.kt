package io.wax911.support.attribute

import androidx.annotation.IntDef

@IntDef(RecyclerType.RECYCLER_TYPE_CONTENT, RecyclerType.RECYCLER_TYPE_HEADER,
        RecyclerType.RECYCLER_TYPE_LOADING, RecyclerType.RECYCLER_TYPE_EMPTY,
        RecyclerType.RECYCLER_TYPE_ERROR)
annotation class RecyclerType {

    companion object {
        const val RECYCLER_TYPE_CONTENT = 0x00000010
        const val RECYCLER_TYPE_HEADER = 0x00000011
        const val RECYCLER_TYPE_LOADING = 0x00000100
        const val RECYCLER_TYPE_EMPTY = 0x00000101
        const val RECYCLER_TYPE_ERROR = 0x00000110
    }
}