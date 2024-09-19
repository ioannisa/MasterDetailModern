package eu.anifantakis.project.library.masterdetailmodern.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun isValidUsername(username: String): Boolean {
        for (char in username.substring(1)) {
            if (!char.isLetterOrDigit() && char != '_') {
                return false
            }
        }

        return username.length >= 6 && username[0].isLetter()
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasNumber = password.any { it.isDigit() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasNumber,
            hasLowerCaseCharacter = hasLowerCase,
            hasUpperCaseCharacter = hasUpperCase,
            hasSymbolCharacter = hasSymbol
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}