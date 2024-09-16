package eu.anifantakis.project.library.masterdetailmodern.auth.data

import android.util.Patterns
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.PatternValidator

object EmailPatternValidator: PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}