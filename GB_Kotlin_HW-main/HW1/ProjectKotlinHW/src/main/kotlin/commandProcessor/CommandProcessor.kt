package commandProcessor

import java.util.regex.Pattern

class CommandProcessor {
    /*
    Создается шаблон для проверки номера телефона. Регулярное выражение ^\\+\\d+$ означает,
    что строка должна начинаться с символа “+”, за которым следуют одна или более цифр, и ничего больше.
     */
    private val phonePattern = Pattern.compile("^\\+\\d+$")


    /*
    Создается шаблон для проверки адреса электронной почты.
    Регулярное выражение ^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+$ означает,
    что строка должна начинаться с одной или более букв, затем символ “@”,
    затем еще одна или более букв, затем точка “.”, и наконец еще одна или более букв.
     */
    private val emailPattern = Pattern.compile("^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+$")


    /**
     * Основная функция класса управления процессом
     * Проверка введенной команды на соответствие одному из 3-х вариантов
     */
    fun processCommand(command: String) {
        val parts = command.split(" ")
        when {
            parts.size == 1 && parts[0] == "help" -> printHelp()
            parts.size == 4 && parts[0] == "add" && parts[2] == "phone" -> processPhone(parts[1], parts[3])
            parts.size == 4 && parts[0] == "add" && parts[2] == "email" -> processEmail(parts[1], parts[3])
            else -> println("Неизвестная команда")
        }
    }


    /**
    Функция, которая выводит список доступных команд в консоль после ввода "help"
     */
    private fun printHelp() {
        println("Доступные команды:")
        println("exit - завершить работу программы")
        println("help - вывести список доступных команд")
        println("add <Имя> phone <Номер телефона> - добавить пользователя и его телефон")
        println("add <Имя> email <Адрес электронной почты> - добавить пользователя и его email")
    }

    /**
     * Функция добавления пользователя с номером телефона (на данном этапе вывод сообщения в консоль)
     */
    private fun processPhone(name: String, phone: String) {
        if (phonePattern.matcher(phone).matches()) {
            println("Добавлен контакт: $name, телефон: $phone")
        } else {
            println("Ошибка: неверный формат номера телефона")
        }
    }

    /**
     * Функция добавления пользователя с его email (на данном этапе вывод сообщения в консоль)
     */
    private fun processEmail(name: String, email: String) {
        if (emailPattern.matcher(email).matches()) {
            println("Добавлен контакт: $name, email: $email")
        } else {
            println("Ошибка: неверный формат адреса электронной почты")
        }
    }
}