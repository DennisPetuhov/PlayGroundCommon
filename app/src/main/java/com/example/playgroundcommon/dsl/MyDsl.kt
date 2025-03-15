package com.example.playgroundcommon.dsl

fun house(block: House.() -> Unit): House {
    val house = House()
    house.block()
    return house
}

class House {
    var rooms = 0
    var floors = 0
    fun rooms(quantity: Int) {
        rooms = quantity
    }

    fun floors(quantity: Int) {floors = quantity }

    override fun toString() = "House(rooms=$rooms, floors=$floors)"

}

val myHouse = house {
    this.rooms(4)
    this.floors(2)
}
val myHouse1 = house(block = {
    rooms(8)
    floors(8)
})

fun main() {
    println(myHouse) // House(rooms=4, floors=0)
    println(myHouse1)
}