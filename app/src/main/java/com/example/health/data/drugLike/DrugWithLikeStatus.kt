package com.example.health.data.drugLike

import com.example.health.data.drugs.DrugsDTO

data class DrugWithLikeStatus(
    val drug: DrugsDTO,
    val isLiked: Boolean
)