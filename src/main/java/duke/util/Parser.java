package duke.util;

import duke.commands.*;
import duke.exceptions.IllegalClearException;
import duke.exceptions.IllegalDeleteException;
import duke.exceptions.IllegalDoneTaskException;
import duke.taskmanager.Deadline;
import duke.taskmanager.Event;
import duke.taskmanager.Tasks;
import duke.taskmanager.ToDo;

import java.io.IOException;
import java.util.List;

public class Parser {
    private static UI ui;
    private static List<Tasks> list;
    public Parser(UI ui, List<Tasks> list) {
        Parser.ui = ui;
        Parser.list = list;
    }

    /**
     * Main parser for user commands, it parses the command to its
     * corresponding action: add task, print task, mark as done,
     * delete task, find task or clear task. Then, it carries out the
     * action. It also print an error message when the user has entered
     * a wrong command at the main page.
     * @param  exeCommand   string input command by user
     * @throws IOException  when writing data to the file fails
     */
    public void parseCommand(String exeCommand) throws IOException {
        try {
            CommandType[] commandType = CommandType.values();
            int index = Integer.parseInt(exeCommand)-1;
            CommandType command = commandType[index];
            switch (command) {
            case ADD_TASK:
                Parser.ui.printTaskType();
                parseAddCommand();
                break;
            case PRINT_TASKS:
                ListCommand.execute(list);
                break;
            case MARK_AS_DONE:
                try {
                    DoneCommand doneCommand = new DoneCommand(ui);
                    list = doneCommand.execute(list);
                } catch (IllegalDoneTaskException | IndexOutOfBoundsException e) {
                    Parser.ui.printExceptionInstruction();
                    String doneExceptionInput = Parser.ui.getStringInput();
                    if (doneExceptionInput.equals("1") || doneExceptionInput.equals("Yes")) {
                        parseCommand("3");
                    }
                }
                break;
            case DELETE_TASK:
                try {
                    DeleteCommand deleteCommand = new DeleteCommand(ui);
                    list = deleteCommand.execute(list);
                } catch (IllegalDeleteException e) {
                    Parser.ui.printExceptionInstruction();
                    String deleteExceptionInput = Parser.ui.getStringInput();
                    if (deleteExceptionInput.equals("1") || deleteExceptionInput.equals("Yes")) {
                        parseCommand("3");
                    }
                }
                break;
            case FIND_TASK:
                FindCommand findCommand = new FindCommand(ui);
                findCommand.execute(list);
                break;
            case CLEAR_TASK:
                try {
                    list.clear();
                    Parser.ui.printRespondToClearTask(list);
                } catch (IllegalClearException e) {
                    Parser.ui.printClearErrorMessage();
                }
                break;
            default:
                Parser.ui.printErrorMessage();
            }
            Storage.writeData(list);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            Parser.ui.printErrorMessage();
        }
    }

    /**
     * Parser for add command, and checks whether the task
     * entered was already in the list. It parses the type
     * of task to get different information from the user
     * and print an error message when the user has entered
     * a wrong command when selecting the type.
     */
    private static void parseAddCommand() {
        String task = Parser.ui.getStringInput();
        TaskType[] taskType = TaskType.values();
        int index = Integer.parseInt(task)-1;
        TaskType selectTask = taskType[index];

        String by;
        System.out.println("    Please enter the task: ");
        task = Parser.ui.getStringInput();
        boolean isRepeat = checkRepeat(task);
        if (!isRepeat) {
            switch (selectTask) {
            case TODO:
                ToDo t = new ToDo(task);
                list.add(t);
                break;
            case DEADLINE:
                Parser.ui.printTaskInstruction("deadline ");
                by = Parser.ui.getStringInput();
                Deadline d = new Deadline(task, by);
                list.add(d);
                break;
            case EVENT:
                Parser.ui.printTaskInstruction("venue ");
                by = Parser.ui.getStringInput();
                Event e = new Event(task, by);
                list.add(e);
                break;
            default:
                Parser.ui.printErrorMessage();
                parseAddCommand();
            }
            Parser.ui.printRespondToAddTask(task);
        }
    }

    /**
     * This checks repeat for the input task. Returns the
     * boolean of whether the task entered was already in the
     * task list. The task argument is a String entered
     * by the user.
     * @param task the task name to be checked
     * @return     <code>true</code> if the task is already present
     *             in the task list
     *             <code>false</code> otherwise.
     */
    private static boolean checkRepeat(String task) {
        if (list!=null && !list.isEmpty()) {
            for (Tasks i : list) {
                if (i != null && i.task.equals(task)) {
                    Parser.ui.printRepeatMessage();
                    return true;
                }
            }
        }
        return false;
    }
}
