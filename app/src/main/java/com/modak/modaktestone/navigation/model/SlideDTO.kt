package com.modak.modaktestone.navigation.model

data class SlideDTO(
    var title: String? = null,
    var explain: String? = null,
    var url: String? = null,
    var region: String? = null,
    var timestamp: Long? = null,
    var kind: Int? = null
//1: event
//2: information
)
