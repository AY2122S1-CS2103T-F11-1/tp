package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindNameCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectClassCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.commands.classcommands.AddClassCommand;
import seedu.address.logic.commands.classcommands.AddToClassCommand;
import seedu.address.logic.commands.classcommands.DeleteClassCommand;
import seedu.address.logic.commands.classcommands.EditClassCommand;
import seedu.address.logic.commands.classcommands.FindClassCommand;
import seedu.address.logic.commands.classcommands.FindClassNameCommand;
import seedu.address.logic.commands.classcommands.ListClassCommand;
import seedu.address.logic.parser.classcommandparsers.AddClassCommandParser;
import seedu.address.logic.parser.classcommandparsers.AddToClassCommandParser;
import seedu.address.logic.parser.classcommandparsers.DeleteClassCommandParser;
import seedu.address.logic.parser.classcommandparsers.EditClassCommandParser;
import seedu.address.logic.parser.classcommandparsers.FindClassCommandParser;
import seedu.address.logic.parser.classcommandparsers.FindClassNameCommandParser;
import seedu.address.logic.parser.classcommandparsers.SelectClassCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        //// student commands

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case FindNameCommand.COMMAND_WORD:
            return new FindNameCommandParser().parse(arguments);

        case FindTagCommand.COMMAND_WORD:
            return new FindTagCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        //// tuition class commands

        case ListClassCommand.COMMAND_WORD:
            return new ListClassCommand();

        case AddClassCommand.COMMAND_WORD:
            return new AddClassCommandParser().parse(arguments);

        case AddToClassCommand.COMMAND_WORD:
            return new AddToClassCommandParser().parse(arguments);

        case EditClassCommand.COMMAND_WORD:
            return new EditClassCommandParser().parse(arguments);

        case SelectClassCommand.COMMAND_WORD:
            return new SelectClassCommandParser().parse(arguments);

        case DeleteClassCommand.COMMAND_WORD:
            return new DeleteClassCommandParser().parse(arguments);

        case FindClassCommand.COMMAND_WORD:
            return new FindClassCommandParser().parse(arguments);

        case FindClassNameCommand.COMMAND_WORD:
            return new FindClassNameCommandParser().parse(arguments);

        //// general application command

        case ViewCommand.COMMAND_WORD:
            return new ViewCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
