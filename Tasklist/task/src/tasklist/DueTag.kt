package tasklist

enum class DueTag(val nameTag: String, val code: String) {
    I("I", "\u001B[102m \u001B[0m"),
    T("T", "\u001B[103m \u001B[0m"),
    O("O", "\u001B[101m \u001B[0m")
}