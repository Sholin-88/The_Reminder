package com.sholin.the_reminder.domain.use_case

class CalculateIdealWeightUseCase {
    operator fun invoke(heightCm: Double?, gender: String?): String {
        if (heightCm == null) return "Enter valid height"
        
        val inches = heightCm / 2.54
        return if (gender == "Male") {
            val devine = 50 + 2.3 * (inches - 60)
            "Ideal weight ≈ %.1f kg".format(devine)
        } else {
            val devine = 45.5 + 2.3 * (inches - 60)
            "Ideal weight ≈ %.1f kg".format(devine)
        }
    }
}
