package io.wax911.support.core.factory

@Deprecated(
    message = "This view model creator will be deprecated in v1.2 see [Koin](https://insert-koin.io/docs/2.0/getting-started/android-viewmodel/)",
    replaceWith = ReplaceWith(
        expression = "by viewmodel delegate, this is part of the Koin modules"
    ),
    level = DeprecationLevel.WARNING
)
open class SingletonCreator<out T, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null)
            return i


        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            }
            else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}