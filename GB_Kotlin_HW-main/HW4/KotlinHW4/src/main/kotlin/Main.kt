import java.io.File

sealed interface Command {
    fun isValid(): Boolean // Метод для проверки валидности команды
}

// Команда добавления телефона
data class AddPhoneCommand(val name: String, val phone: String) : Command {
    override fun isValid() = phone.matches(Regex("\\+?\\d+")) // Проверка валидности номера телефона
    override fun toString(): String {
        return "Вызвана команда добавления с указанием телефона"
    }
}

// Команда добавления email
data class AddEmailCommand(val name: String, val email: String) : Command {
    override fun isValid() = email.matches(Regex("\\w+@\\w+\\.\\w+")) // Проверка валидности email
    override fun toString(): String {
        return "Вызвана команда добавления с указанием почты"
    }
}

// Команда выхода
object ExitCommand : Command {
    override fun isValid() = true
}

// Команда вызова меню помощи
object HelpCommand : Command {
    override fun isValid() = true
}

// Команда показа информации о пользователе по имени
data class ShowCommand(val name: String) : Command {
    override fun isValid() = true
}

// Команда поиска данных по телефону или почте
data class FindCommand(val info: String) : Command {
    override fun isValid() = true
}

// Класс Person для хранения информации о человеке
data class Person(
    var name: String, // Имя
    var phones: MutableList<String> = mutableListOf(), // Список телефонов
    var emails: MutableList<String> = mutableListOf() // Список адресов почты
)

// Телефонная книга для хранения записей о людях
val phoneBook = mutableMapOf<String, Person>()

// Функция для чтения команды пользователя из консоли (доработана в region ДЗ №4)
/*
fun readCommand(input: String): Command {
    val parts = input.split(" ")
    return when {
        parts[0] == "exit" -> ExitCommand
        parts[0] == "help" -> HelpCommand
        parts.size == 2 -> when (parts[0]) {
            "show" -> ShowCommand(parts[1])
            "find" -> FindCommand(parts[1])
            else -> HelpCommand
        }

        parts.size == 4 && parts[0] == "add" -> when (parts[2]) {
            "phone" -> AddPhoneCommand(parts[1], parts[3])
            "email" -> AddEmailCommand(parts[1], parts[3])
            else -> HelpCommand
        }

        else -> HelpCommand
    }
}
 */

// endregion

// region Реализация DSL для домашнего задания №4

class JsonObject {
    private val map = mutableMapOf<String, Any>()

    fun addProperty(key: String, value: Any) {
        map[key] = value
    }

    override fun toString(): String {
        val properties = map.entries.joinToString(",\n    ") { (key, value) ->
            "\"$key\": ${if (value is String) "\"$value\"" else value}"
        }
        return "{\n    $properties\n}"
    }
}

fun json(init: JsonObject.() -> Unit): JsonObject {
    return JsonObject().apply(init)
}

// Команда экспорта в JSON
data class ExportCommand(val path: String) : Command {
    override fun isValid() = true
}

// Функция для чтения команды пользователя из консоли (в регионе ДЗ №3 -закомментирована)
fun readCommand(input: String): Command {
    val parts = input.split(" ")
    return when {
        parts[0] == "exit" -> ExitCommand
        parts[0] == "help" -> HelpCommand
        parts.size == 2 -> when (parts[0]) {
            "show" -> ShowCommand(parts[1])
            "find" -> FindCommand(parts[1])
            "export" -> ExportCommand(parts[1])
            else -> HelpCommand
        }

        parts.size == 4 && parts[0] == "add" -> when (parts[2]) {
            "phone" -> AddPhoneCommand(parts[1], parts[3])
            "email" -> AddEmailCommand(parts[1], parts[3])
            else -> HelpCommand
        }

        else -> HelpCommand
    }
}

private val comandsStartMenu = "Введите одну из команд:\n" +
        "1. exit\n" +
        "2. help\n" +
        "3. add <Имя> phone <Номер телефона>\n" +
        "4. add <Имя> email <Адрес электронной почты>\n" +
        "5. show <Имя>\n" +
        "6. find <Телефон или Email>\n" +
        "7. export <Путь к файлу .json>"

// endregion


//------------------------------------------------------------------------------------------------------------------
// Точка входа и начало работы программы (Выполнил: Лысков Никита Алексеевич)
//------------------------------------------------------------------------------------------------------------------
fun main() {
    println(comandsStartMenu)
    while (true) {
        val command = readCommand(readLine()!!.lowercase())
        println(command)
        if (command.isValid()) {
            when (command) {
                // Новая команда export
                is ExportCommand -> {
                    val jsonObjects = phoneBook.values.map { person ->
                        json {
                            addProperty("name", person.name)
                            addProperty("phones", person.phones)
                            addProperty("emails", person.emails)
                        }
                    }
                    val json = "[${jsonObjects.joinToString(", ")}]"
                    File(command.path).writeText(json)
                    println("Данные экспортированы в файл ${command.path}")
                }

                is AddPhoneCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.phones.add(command.phone)
                    println("Добавлено: ${it.name}, телефон: ${command.phone}")
                }

                is AddEmailCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.emails.add(command.email)
                    println("Добавлено: ${it.name}, email: ${command.email}")
                }

                is ShowCommand -> {
                    phoneBook[command.name]?.let {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Запись не найдена")
                }

                is FindCommand -> {
                    phoneBook.values.filter {
                        it.phones.contains(command.info)
                                || it.emails.contains(command.info)
                    }.takeIf { it.isNotEmpty() }?.forEach {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Записи не найдены")
                }

                is HelpCommand -> {
                    println(comandsStartMenu)
                }

                is ExitCommand -> return
//                else -> println("Неизвестная команда")
            }
        } else {
            println("Неверный формат команды, попробуйте еще раз")
        }
    }
}


/*
Для проверки выполнил следующие команды:

add nikita phone 123312123
add nikita phone 8976567
add anna phone 2352535
add dima phone 33442333777
add nikita email slsfjd@mail.ru
add anna email anna@mail.ru
add dima email dima@mail.ru
add dima email dima2@mail.ru

export D:\GeekBrains\file1.json
export D:\GeekBrains\file2.txt

Данные файлы копировал в папку resources для проверки

 */


