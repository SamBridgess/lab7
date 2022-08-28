package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;

/**
 * help command
 */
public class HelpCommand extends Command {
    public HelpCommand() {
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     */
    @Override
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) {
        String help = "help : вывести справку по доступным командам\n"
                + "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n"
                + "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n"
                + "add {element} : добавить новый элемент в коллекцию\n"
                + "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n"
                + "remove_by_id id : удалить элемент из коллекции по его id\n"
                + "clear : очистить коллекцию\n"
                + "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n"
                + "exit : завершить программу (без сохранения в файл)\n"
                + "remove_first : удалить первый элемент из коллекции\n"
                + "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n"
                + "sort : отсортировать коллекцию в естественном порядке\n"
                + "filter_less_than_distance distance : вывести элементы, значение поля distance которых меньше заданного\n"
                + "print_ascending : вывести элементы коллекции в порядке возрастания\n"
                + "print_field_descending_distance : вывести значения поля distance всех элементов в порядке убывания";
        return new ServerResponse(help, false);
    }



}
