package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingInteger(initialValue: Int = 0) {
    private val _value = MutableStateFlow(initialValue)
    val value: StateFlow<Int> = _value.asStateFlow()

    private val atomicInteger = AtomicInteger(initialValue)

    fun incrementAndGet(): Int {
        val newValue = atomicInteger.incrementAndGet()
        _value.value = newValue
        return newValue
    }

    fun decrementAndGet(): Int {
        // decrement to newValue if counter more than 0 else stay at 0
        val newValue = if (atomicInteger.get() == 0) atomicInteger.get() else atomicInteger.decrementAndGet()
        _value.value = newValue
        return newValue
    }

    fun updateAndGet(newValue: Int) {
        atomicInteger.updateAndGet { newValue }
        _value.value = newValue
    }

    fun get(): Int {
        return atomicInteger.get()
    }
}