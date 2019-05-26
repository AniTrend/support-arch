package io.wax911.support.data.factory.contract

interface IRetrofitFactory {

    /**
     * Generates retrofit service classes
     *
     * @param serviceClass The interface class method representing your request to use
     */
    fun <S> createService(serviceClass: Class<S>): S
}