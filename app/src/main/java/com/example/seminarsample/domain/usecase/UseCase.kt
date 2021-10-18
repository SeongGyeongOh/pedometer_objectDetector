package com.example.seminarsample.domain.usecase

abstract class UseCaseWithParams<T, in Params> {
    protected abstract suspend fun buildUseCase(params: Params): T
}

abstract class UseCaseWithoutParams<T> {
    protected abstract suspend fun buildUseCase(): T
}