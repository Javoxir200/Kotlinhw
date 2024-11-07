import commandProcessor.CommandProcessor


fun main() {
    println("Введите одну из команд:\n" +
            "1. exit\n" +
            "2. help\n" +
            "3. add <Имя> phone <Номер телефона>\n" +
            "4. add <Имя> email <Адрес электронной почты>\n ")
    val commandProcessor = CommandProcessor()
    while (true) {
        print("Введите команду: ")
        val command = readLine() ?: continue
        if (command == "exit") break
        commandProcessor.processCommand(command)
    }
}