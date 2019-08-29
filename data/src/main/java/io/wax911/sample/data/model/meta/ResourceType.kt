package io.wax911.sample.data.model.meta

import androidx.annotation.StringDef

@StringDef(
    ResourceType.FULL, ResourceType.META
)
@Retention(AnnotationRetention.SOURCE)
annotation class ResourceType {
    companion object {
        const val META = "meta"
        const val FULL = "full"

        const val ALL = "$META,$FULL"
    }
}