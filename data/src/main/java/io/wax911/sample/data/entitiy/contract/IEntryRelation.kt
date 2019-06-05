package io.wax911.sample.data.entitiy.contract

interface IEntryRelation<E, R> {
    var entry: E?
    var reference: List<R>
}