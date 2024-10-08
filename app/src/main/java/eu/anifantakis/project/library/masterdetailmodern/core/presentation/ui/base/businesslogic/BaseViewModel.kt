package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    private val loadingCounter: ObservableLoadingInteger by inject(ObservableLoadingInteger::class.java)

    // Method to manually inject ObservableLoadingInteger for testing
    fun testInjects(newLoadingCounter: ObservableLoadingInteger) {
        // This is now a local function that can modify the injected instance
        // Use with caution, preferably only in tests
        (this::loadingCounter as Lazy<*>).apply {
            val field = Lazy::class.java.getDeclaredField("_value")
            field.isAccessible = true
            field.set(this, newLoadingCounter)
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val mutex = Mutex()

    fun setLoading(boolean: Boolean) {
        when (boolean) {
            true -> if (loadingCounter.incrementAndGet() >= 1) {
                _isLoading.value = true
            }
            false -> if (loadingCounter.decrementAndGet() == 0) {
                _isLoading.value = false
            }
        }
    }

    // Scoped function for loading
    suspend inline fun loading(block: () -> Unit) = mutex.withLock {
        setLoading(true)
        try {
            block.invoke()
        } finally {
            setLoading(false)
        }
    }

    private val _isLoadingPull = MutableStateFlow(false)
    val isLoadingPull: StateFlow<Boolean> = _isLoadingPull.asStateFlow()

    fun setLoadingPull(boolean: Boolean) {
        _isLoadingPull.value = boolean

        // adjust the counter to block rotation through main activity
        if (boolean) {
            loadingCounter.incrementAndGet()
        } else {
            loadingCounter.decrementAndGet()
        }
    }

    // Scoped function for pull to refresh loading
    suspend inline fun loadingPull(block: () -> Unit) = mutex.withLock {
        setLoadingPull(true)
        try {
            block.invoke()
        } finally {
            setLoadingPull(false)
        }
    }
}