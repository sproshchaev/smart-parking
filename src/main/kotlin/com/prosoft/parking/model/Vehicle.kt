package com.prosoft.parking.model

// class Vehicle(val plate: String) {
abstract class Vehicle(val plate: String) {
    abstract val sizeFactor: Double
    open fun describe(): String = "ТС $plate"
}

// Авто - стандартное место
class Car(plate: String) : Vehicle(plate) {
    override val sizeFactor = 1.0
}

// Truck - грузовое место
class Truck(plate: String, val axles: Int): Vehicle(plate) {
    override val sizeFactor = 2.5
    override fun describe() = super.describe() + ", грузовик на $axles осях"
}

// Moto - компакт место
class Motorcycle(plate: String): Vehicle(plate) {
    override val sizeFactor = 0.5
    override fun describe() = "Мотоцикл $plate"
}