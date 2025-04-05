package tasklist

enum class Priority(val namePriority: String, val code: String) {
    C("C", "\u001B[101m \u001B[0m"),
    H("H", "\u001B[103m \u001B[0m"),
    N("N", "\u001B[102m \u001B[0m"),
    L("L", "\u001B[104m \u001B[0m")
}