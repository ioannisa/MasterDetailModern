package eu.anifantakis.project.library.masterdetailmodern.auth.domain

interface PatternValidator {

    fun matches(value: String): Boolean

}