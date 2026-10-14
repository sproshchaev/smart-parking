package com.prosoft.parking.jpa

import com.prosoft.parking.model.Session
import com.prosoft.parking.repo.Repository
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Entity
@Table(name = "session")
class SessionEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "plate", nullable = false, unique = true)
    var plate: String = "",

    @Column(name = "spotId", nullable = false)
    var spotId: String = "",

    @Column(name = "startedAt", nullable = false)
    var startedAt: Long = 0
    ) {
    fun toDomain() = Session(plate, spotId, startedAt)
}

interface SessionJpaRepository : JpaRepository<SessionEntity, Long> {
    fun findByPlate(plate: String): SessionEntity?
}

@Component
class JpaSessionRepository(private val jpa: SessionJpaRepository) : Repository<Session, String> {

    override fun save(item: Session): Session {
        val entity = jpa.findByPlate(item.plate) ?: SessionEntity()
        entity.plate = item.plate
        entity.spotId = item.spotId
        entity.startedAt = item.startedAt
        jpa.save(entity)
        return item
    }

    override fun findById(id: String): Session? = jpa.findByPlate(id)?.toDomain()

    override fun findAll(): List<Session> = jpa.findAll().map { it.toDomain() }

    override fun deleteById(id: String): Session? {
        val entity = jpa.findByPlate(id) ?: return null
        jpa.delete(entity)
        return entity.toDomain()
    }

}