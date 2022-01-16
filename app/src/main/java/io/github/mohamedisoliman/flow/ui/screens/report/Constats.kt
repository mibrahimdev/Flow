package io.github.mohamedisoliman.flow.ui.screens.report

import io.github.mohamedisoliman.flow.R

enum class Scale(val stringResId: Int) {
    DAY(R.string.day), WEEK(R.string.week), MONTH(R.string.month)
}


fun Int.axis() = when (this) {
    Scale.DAY.ordinal -> Axis(
        max = 24,//Hour
        skipRate = 6
    )
    Scale.WEEK.ordinal -> Axis(
        max = 7,
        skipRate = 1
    )
    Scale.MONTH.ordinal -> Axis(
        max = 30,
        skipRate = 7
    )
    else -> throw NotImplementedError(
        "This scale is not handled check its ordinal $this"
    )
}

fun Int.maxTasksCounter() = when (this) {
    Scale.DAY.ordinal -> 20
    Scale.WEEK.ordinal -> 80
    Scale.MONTH.ordinal -> 100
    else -> throw NotImplementedError(
        "This scale is not handled check its ordinal $this"
    )
}
