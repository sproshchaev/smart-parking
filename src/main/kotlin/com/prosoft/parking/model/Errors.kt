package com.prosoft.parking.model

class SpotOccupiedException(spotId: String) :
    RuntimeException("Место $spotId уже занято")

class SessionNotFoundException(plate: String) :
    NoSuchElementException("Сессия $plate не найдена")