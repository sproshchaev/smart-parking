package com.prosoft.parking.plate

fun recognizePlate(raw: String?): String? =
    raw?.trim()?.uppercase()?.takeIf { it.length in 8..9 }
