package duke.commands;

import duke.taskmanager.Tasks;

import java.util.List;

public class ListCommand extends Command {
    private static final String format = "0O=-             %-60s-=O0%n";
    private static final String split = "=============================" +
            "====================================================";

    private static final String blankLine = "0O=-                      " +
            "                                                   -=O0";

    private static final String splitUpperBoundary = split +"\n000000000000000" +
            "00000000000000000000000000000000000000000000000000000000000" +
            "0000000\n" + blankLine;

    private static final String splitLowerBoundary = blankLine + "\n0000000" +
            "00000000000000000000000000000000000000000000000000000000" +
            "000000000000000000\n" + split;

    public ListCommand() {
    }

    public static void printIntro() {
        System.out.println(splitUpperBoundary);
        System.out.printf(format, "Your current task list:");
    }

    public static void printEmpty() {
        System.out.println(splitUpperBoundary);
        System.out.printf(format, "You have no ongoing task.");
        System.out.println(splitLowerBoundary);
    }

    public static void execute(List<Tasks> tasks) {
        printIntro();
        if (tasks.size() == 0){
            printEmpty();
        } else {
            int index = 0;
            for (Tasks task : tasks) {
                System.out.printf(format, index + ". "+ task.toString());
                index++;
            }
            System.out.println(splitLowerBoundary);
        }
    }
}