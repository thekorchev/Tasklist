package tasklist
import kotlin.system.exitProcess
import kotlinx.datetime.*
import java.io.File
import com.squareup.moshi.*


class AddTask {

    private val taskMap = mutableMapOf<String, MutableList<String>>()

    private val jsonFile = File("tasklist.json")
    private val moshi = Moshi.Builder().build()
    private val adapter = moshi.adapter(Map::class.java)

    init {
        loadTasksFromFile()
    }

    fun makeInput() {

        while(true) {
            println("Input an action (add, print, edit, delete, end):")
            val input = readln().lowercase().trim()
            when(input) {
                "add" -> addFunction()
                "print" -> printTaskMap()
                "edit" -> editTask()
                "delete" -> deleteTask()
                "end" -> endFunction()
                else -> println("The input action is invalid")
            }
        }
    }

    private fun finalKeyTask(date: String, time: String, letter: String, dueTag: String): String {
        return "$date $time $letter $dueTag"
    }

    private var date: String = ""
    private var time: String = ""
    private var letter: String = ""
    private var dueTag: String = ""
    private var finalKeyTask: String = ""
    private var number = 1

    private fun addFunction() {
        while (true) {
            println("Input the task priority (C, H, N, L):")
            val letterInput = readln().uppercase().trim()
            if (letterInput in setOf("C", "H", "N", "L")) {
                letter = letterInput
                break
            }
        }

        while(true) {
            date = readValidDate()
            break
        }

        while(true) {
            time = readValidTime()
            break
        }

        val taskDate = LocalDate.parse(date)
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        val numberOfDays = currentDate.daysUntil(taskDate)

        if (numberOfDays < 0) {
            dueTag = "O"
        } else if (numberOfDays > 0 ) {
            dueTag = "I"
        } else {
            dueTag = "T"
        }

        finalKeyTask = finalKeyTask(date, time, letter, dueTag)
        val count = number++
        val final = count.toString()

        println("Input a new task (enter a blank line to end):")
        val lines = mutableListOf<String>()
        lines.add(finalKeyTask)
        taskMap[final] = lines

        while(true) {
            val valueLine = readln().trim()
            lines.add(valueLine)

            if (lines.size == 2 && valueLine.isEmpty()) {
                println("The task is blank")
                taskMap.remove(final)
                break
            } else {
                if (valueLine.isEmpty()) {
                    val lastLine = lines.size
                    lines.removeAt(lastLine - 1)
                    break
                }
            }
        }
    }

    private fun readValidTime(): String {
        return try {
            println("Input the time (hh:mm):")
            val (hour, minute) = readln().split(":").map { it.toInt() }
            if (hour in 0..23 && minute in 0..59) {
                "%02d:%02d".format(hour, minute)
            } else throw Exception("The input time is invalid")
        } catch (e: Exception) {
            println("The input time is invalid")
            readValidTime()
        }
    }

    private fun readValidDate(): String {
        return try {
            println("Input the date (yyyy-mm-dd):")
            val input = readln().split("-")
            val date = LocalDate(input[0].toInt(), input[1].toInt(), input[2].toInt())
            date.toString()
        } catch (e: Exception) {
            println("The input date is invalid")
            readValidDate()
        }
    }

    private var taskNumbers: Int = 0
    private fun deleteTask() {
        if (taskMap.isEmpty()) {
            println("No tasks have been input")
            return
        }
        printTaskMap()
        val calculateNumber = (taskNumbers - 1)
        val firstNumber = taskNumbers - calculateNumber
        while(true) {
            println("Input the task number ($firstNumber-$taskNumbers):")
            var numberInput: Int
            val taskNumberInput = readln()
            if (taskNumberInput.all { it.isDigit()}) {
                numberInput = taskNumberInput.toInt()
                if (numberInput !in firstNumber..taskNumbers) {
                    println("Invalid task number")
                } else {
                    val deleteKey = taskMap.keys.elementAtOrNull(numberInput - 1)
                    taskMap.remove(deleteKey)
                    println("The task is deleted")
                    break
                }
            } else {
                println("Invalid task number")
            }

        }
    }

    private fun editTask() {
        if (taskMap.isEmpty()) {
            println("No tasks have been input")
            return
        }
        printTaskMap()
        val calculateNumber = (taskNumbers - 1)
        val firstNumber = taskNumbers - calculateNumber
        var numberInput: Int
        while(true) {
            println("Input the task number ($firstNumber-$taskNumbers):")
            val taskNumberInput = readln()
            if (taskNumberInput.all { it.isDigit()}){
                numberInput = taskNumberInput.toInt()
                if (numberInput !in firstNumber..taskNumbers) {
                    println("Invalid task number")
                } else {
                    break
                }
            } else {
                println("Invalid task number")
            }
        }

        while(true) {
            println("Input a field to edit (priority, date, time, task):")
            val editInput = readln().lowercase().trim()
            if (editInput in setOf("priority", "date", "time", "task")) {
                when (editInput) {
                    "date" -> {
                        val newDate = readValidDate()
                        if (taskMap.keys.any { it == numberInput.toString() }) {
                            val check = taskMap[numberInput.toString()] // [2000-02-02 02:02 C O, adasd, asd, asd]
                            val check2 = check!![0] // 2000-02-02 02:02 C O
                            val check3 = check2.split(" ").toMutableList() // [2000-02-02, 02:02, C, O]
                            check3[0] = newDate // [1888-02-02, 02:02, C, O]

                            taskMap[numberInput.toString()] = check.mapIndexed { index, value ->
                                if (index == 0) {
                                    check3.joinToString(" ")
                                } else {
                                    value
                                }
                            }.toMutableList()
                            println("The task is changed")
                            break
                        }
                    }
                    "time" -> {
                        val newTime = readValidTime()
                        if (taskMap.keys.any { it == numberInput.toString() }) {
                            val check = taskMap[numberInput.toString()]
                            val check2 = check!![0]
                            val check3 = check2.split(" ").toMutableList()
                            check3[1] = newTime
                            taskMap[numberInput.toString()] = check.mapIndexed { index, value ->
                                if (index == 0) {
                                    check3.joinToString(" ")
                                } else {
                                    value
                                }
                            }.toMutableList()
                            println("The task is changed")
                            break
                        }
                    }
                    "priority" -> {
                        val newLetter: String
                        while (true) {
                            println("Input the task priority (C, H, N, L):")
                            val letterInput = readln().uppercase().trim()
                            if (letterInput in setOf("C", "H", "N", "L")) {
                                newLetter = letterInput
                                break
                            }
                        }
                        if (taskMap.keys.any { it == numberInput.toString() }) {
                            val check = taskMap[numberInput.toString()]
                            val check2 = check!![0]
                            val check3 = check2.split(" ").toMutableList()
                            check3[2] = newLetter

                            taskMap[numberInput.toString()] = check.mapIndexed { index, value ->
                                if (index == 0) {
                                    check3.joinToString(" ")
                                } else {
                                    value
                                }
                            }.toMutableList()
                            println("The task is changed")
                            break
                        }
                    }
                    "task" -> {
                        if (taskMap.keys.any { it == numberInput.toString() }) {
                            val check = taskMap[numberInput.toString()] // [2000-02-02 02:02 C O, adasd, asd, asd]
                            val check2 = check!![0] // 2000-02-02 02:02 C O
                            val check3 = check2.split(" ").toMutableList() // [2000-02-02, 02:02, C, O]

                            val newChcek = mutableListOf(check[0], check[1])
                            check.clear()
                            check.addAll(newChcek)

                            println("Input a new task (enter a blank line to end):")
                            val newTask = readln().trim()

                            taskMap[numberInput.toString()] = check.mapIndexed { index, value ->
                                if (index == 0) {
                                    check3.joinToString(" ")
                                } else {
                                    newTask
                                }
                            }.toMutableList()
                            println("The task is changed")
                            break
                        }
                    }
                }
            } else {
                println("Invalid field")
            }
        }
    }

    private fun saveTasksToFile() {
        val json = adapter.toJson(taskMap)
        jsonFile.writeText(json)
    }

    private fun loadTasksFromFile() {
        if (!jsonFile.exists()) return

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val taskMapType = Types.newParameterizedType(Map::class.java, String::class.java, List::class.java)
        val taskMapAdapter = moshi.adapter<Map<String, MutableList<String>>>(taskMapType)
        val newTaskMap = taskMapAdapter.fromJson(jsonFile.readText())
        taskMap.clear()
        if (newTaskMap != null) {
            taskMap.putAll(newTaskMap)
        }
    }

    private fun printTaskMap() {
        if (taskMap.isEmpty()) {
            println("No tasks have been input")
            return
        }
        println("+----+------------+-------+---+---+--------------------------------------------+")
        println("| N  |    Date    | Time  | P | D |                   Task                     |")
        println("+----+------------+-------+---+---+--------------------------------------------+")
        var index = 1

        taskMap.forEach { (task, lines) ->
            val taskInfo = lines[0].split(" ").toMutableList() // Первая строка содержит информацию о задаче
            val indexString = if (index < 10) " " else ""

            var priority = "X"
            if (taskInfo[2] == Priority.C.namePriority) {
                priority = Priority.C.code
            } else if (taskInfo[2] == Priority.H.namePriority) {
                priority = Priority.H.code
            } else if (taskInfo[2] == Priority.N.namePriority) {
                priority = Priority.N.code
            } else if (taskInfo[2] == Priority.L.namePriority) {
                priority = Priority.L.code
            }

            var dueTagColor = "Y"
            if (taskInfo[3] == DueTag.I.nameTag) {
                dueTagColor = DueTag.I.code
            } else if (taskInfo[3] == DueTag.O.nameTag) {
                dueTagColor = DueTag.O.code
            } else if (taskInfo[3] == DueTag.T.nameTag) {
                dueTagColor = DueTag.T.code
            }

            val taskFieldWidth = 44

            print("| $index $indexString| ${taskInfo[0]} | ${taskInfo[1]} | $priority | $dueTagColor |")

            lines.subList(1, lines.size).forEachIndexed { lineIndex, line ->
                val chunks = line.chunked(taskFieldWidth)
                chunks.forEachIndexed { chunkIndex, chunk ->

                    var xz = ""
                    if (chunk.length < taskFieldWidth) {
                        val check = taskFieldWidth - chunk.length
                        repeat(check) {
                            xz+= " "
                        }
                    }
                    if (chunkIndex == 0 && lineIndex == 0) {
                        println("$chunk$xz|")
                    } else {
                        println("|    |            |       |   |   |$chunk$xz|")
                    }
                }
            }

            println("+----+------------+-------+---+---+--------------------------------------------+")
            index++
        }
        taskNumbers = index - 1
    }

    private fun endFunction() {
        saveTasksToFile()
        println(jsonFile.readText())
        println("Tasklist exiting!")
        exitProcess(0)
    }
}