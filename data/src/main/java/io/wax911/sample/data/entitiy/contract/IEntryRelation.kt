package io.wax911.sample.data.entitiy.contract

interface IEntryRelation<E, R> {
    val entry: E?
    val reference: List<R>
}