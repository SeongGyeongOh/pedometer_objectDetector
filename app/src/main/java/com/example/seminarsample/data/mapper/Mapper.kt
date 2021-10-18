package com.example.architecturekotlin.data.mapper

import com.example.seminarsample.data.entity.StepCountEntity
import com.example.seminarsample.domain.model.StepCountModel

fun StepCountEntity.map() = StepCountModel(
    id = id,
    date = date,
    count = count
)


fun StepCountModel.map() = StepCountEntity(
    id = id,
    date = date,
    count = count
)