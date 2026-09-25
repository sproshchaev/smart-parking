package com.prosoft.parking.repo

interface Identifiable<ID> {
    val id: ID
}

interface Repository<T : Identifiable<ID>, ID> {

    fun save(item: T): T
    fun findById(id: ID): T?
    fun findAll(): List<T>
    fun deleteById(id: ID): T?

    operator fun get(id: ID): T? = findById(id)

}

class InMemoryRepository<T: Identifiable<ID>, ID> : Repository<T, ID> {

    private val store = LinkedHashMap<ID, T>()

    override fun save(item: T): T = item.also {
        store[it.id] = it
    }

    override fun findById(id: ID): T? = store[id]

    override fun findAll(): List<T> = store.values.toList()

    override fun deleteById(id: ID): T? = store.remove(id)

}