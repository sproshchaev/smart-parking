package com.prosoft.parking.web

import com.prosoft.parking.ParkingService
import com.prosoft.parking.Receipt
import com.prosoft.parking.model.AdjustableClock
import com.prosoft.parking.model.Session
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class EnterRequest(val plate: String, val axles: Int? = null)

data class SessionDto(val plate: String, val spotId: String, val startedAt: Long)

data class ReceiptDto(val plate: String, val spotId: String, val minutes: Int, val amount: Int)

private fun Session.toDto() = SessionDto(plate, spotId, startedAt)

private fun Receipt.toDto() = ReceiptDto(plate, spotId, minutes, amount)

@RestController
@RequestMapping("/api/sessions")
class SessionController(
    private val service: ParkingService,
    private val clock: AdjustableClock,
) {

    // POST
    @PostMapping
    fun enter(@RequestBody request: EnterRequest): ResponseEntity<SessionDto> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.enter(request.plate, request.axles).toDto())

    // GET
    @GetMapping
    fun active():List<SessionDto> = service.active().map {
        it.toDto()
    }

    // DELETE
    @DeleteMapping
    fun leave(@RequestParam plate: String) : ReceiptDto = service.exit(plate).toDto()

    // POST
    @PostMapping("/travel")
    fun travel(@RequestParam minutes: Long): Map<String, Long> {
        clock.travel(minutes)
        return mapOf("offsetMinutes" to clock.offsetMillis / 60_000)
    }

}

@RestControllerAdvice
class ErrorHandler {

    // 400
    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequest(e: IllegalArgumentException) =
        ResponseEntity.badRequest().body(mapOf("error" to e.message))

    // 409
    @ExceptionHandler(IllegalStateException::class)
    fun conflict(e: IllegalStateException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to e.message))

    // 404
    @ExceptionHandler(NoSuchElementException::class)
    fun notFound(e: NoSuchElementException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
}