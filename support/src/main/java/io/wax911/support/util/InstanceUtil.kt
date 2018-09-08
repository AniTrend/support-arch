package io.wax911.support.util

open class InstanceUtil<out T, in A>(private var creator: (A) -> T)  {

    fun newInstance(arg: A): T =
            creator(arg)
}