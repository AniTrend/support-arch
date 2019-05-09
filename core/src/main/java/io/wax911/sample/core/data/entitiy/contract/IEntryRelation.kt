package io.wax911.sample.core.data.entitiy.contract

interface IEntryRelation<E, R> {
    var entry: E?
    var reference: List<R>
}