package io.wax911.support.factory

open class InstanceCreator<out T, in A>(private var creator: (A) -> T) {
    fun newInstance(arg: A): T = creator(arg)
}