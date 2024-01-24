package ru.netology.nmedia

import java.math.BigDecimal
import java.math.RoundingMode

object Counter {
    fun reduce(count: Int): String =
        when (count) {
            in 0..999 -> count.toString()
            in 1000..9_999 -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000), 1, RoundingMode.FLOOR)
                result.toString() + "K"
            }
            in 10_000..999_999 -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000), 0, RoundingMode.FLOOR)
                result.toString() + "K"
            }
            else -> {
                val number = BigDecimal(count)
                val result = number.divide(BigDecimal(1000000), 1, RoundingMode.FLOOR)
                result.toString() + "M"
            }
        }
}