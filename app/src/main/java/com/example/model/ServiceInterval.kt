package com.example.model

enum class IntervalType {
    OIL_CHANGE,
    BRAKES_FRONT,
    BRAKES_REAR,
    CABIN_FILTER,
    ENGINE_FILTER,
    SPARK_PLUGS,
    TRANSMISSION_FLUID,
    COOLANT_FLUSH,
    BRAKE_FLUID
}
//mapping each manufacturer's intervals to this object
data class ServiceInterval(
    val partName: String,
    val intervalKm: Int,
    val detail: String,
    val type: IntervalType
)

data class ManufacturerInfo(
    val name: String,
    val logoAccentChar: String, // Short identifier character to draw (e.g. "M", "B", "T")
    val intervals: List<ServiceInterval>
)
