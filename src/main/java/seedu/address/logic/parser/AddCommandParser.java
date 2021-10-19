package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSTIMING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nok;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;
import seedu.address.model.tuitionclass.ClassName;
import seedu.address.model.tuitionclass.ClassTiming;
import seedu.address.model.tuitionclass.Location;
import seedu.address.model.tuitionclass.Rate;
import seedu.address.model.tuitionclass.TuitionClass;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String argsBeforeNok = args;
        String argsAfterNok = "";

        if (args.contains("nok/")) {
            String[] splitArgs = args.split("nok/");
            argsBeforeNok = splitArgs[0];
            argsAfterNok = splitArgs[1];
        }

        Student student = parseStudent(argsBeforeNok);
        TuitionClass tuitionClass = parseClass(argsBeforeNok);
        Nok nok = parseNok(argsAfterNok);
        student.setNok(nok);

        return new AddCommand(student, tuitionClass);
    }

    private TuitionClass parseClass(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer
                        .tokenize(args, PREFIX_NAME, PREFIX_RATE, PREFIX_CLASSTIMING, PREFIX_LOCATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_RATE, PREFIX_CLASSTIMING, PREFIX_LOCATION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Rate rate = ParserUtil.parseRate(argMultimap.getValue(PREFIX_RATE).get());
        ClassTiming classTiming = ParserUtil.parseClassTiming(argMultimap.getValue(PREFIX_CLASSTIMING).get());
        Location location = ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get());
        // TODO: Add a token for adding class name when doing the add command

        return new TuitionClass(new ClassName("Placeholder"), classTiming, location, rate);
    }

    private Student parseStudent(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer
                        .tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                                PREFIX_RATE, PREFIX_CLASSTIMING, PREFIX_LOCATION, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE,
                PREFIX_RATE, PREFIX_CLASSTIMING, PREFIX_LOCATION, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Rate rate = ParserUtil.parseRate(argMultimap.getValue(PREFIX_RATE).get());
        ClassTiming classTiming = ParserUtil.parseClassTiming(argMultimap.getValue(PREFIX_CLASSTIMING).get());
        Location location = ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Student(name, phone, email, address, null, tagList);
    }

    private Nok parseNok(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        return new Nok(
                ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()),
                ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()),
                ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()),
                ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get())
        );
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
