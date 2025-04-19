package com.example.rakshasetu

data class EmergencyAlert(
    val time: String,
    val temperature: String,
    val location: String,
    val subLocation: String,
    val magnitude: String,
    val event: String,
    val eventTime: String,
    val coordinates: String,
    val affectedAreas: List<String>
)

// Sample data
val sampleAlert = EmergencyAlert(
    time = "3.00 PM",
    temperature = "32°",
    location = "South Delhi",
    subLocation = "Hauz Khas",
    magnitude = "2.8",
    event = "Earthquake",
    eventTime = "Today: 3.00 PM",
    coordinates = "33'/ 31\" Fees1 No. 42\"",
    affectedAreas = listOf("Delhi", "Noida", "Gurgaon", "Ghaziabad", "Meerut")
)
