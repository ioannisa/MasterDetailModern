package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.scaffold

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScaffoldViewModel: ViewModel() {
    private val _title = MutableStateFlow<String?>(null)
    val title: StateFlow<String?> = _title

    private val _onBackPress = MutableStateFlow<(() -> Unit)?>(null)
    val onBackPress: StateFlow<(() -> Unit)?> = _onBackPress

    fun updateAppBar(title: String?, onBackPress: (() -> Unit)?) {
        _title.value = title
        _onBackPress.value = onBackPress
    }

    private val _topBarHeight = MutableStateFlow(0)
    val topBarHeight: StateFlow<Int> = _topBarHeight

    private val _bottomBarHeight = MutableStateFlow(0)
    val bottomBarHeight: StateFlow<Int> = _bottomBarHeight

    fun setTopBarHeight(height: Int) {
        _topBarHeight.value = height
    }

    fun setBottomBarHeight(height: Int) {
        _bottomBarHeight.value = height
    }
}