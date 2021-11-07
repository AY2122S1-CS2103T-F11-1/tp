package seedu.times.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.times.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.times.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.times.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.times.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.times.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.times.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.times.commons.core.index.Index;
import seedu.times.logic.commands.AddCommand;
import seedu.times.logic.commands.EditCommand;
import seedu.times.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.times.logic.parser.exceptions.ParseException;
import seedu.times.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Tokenize twice, once for everything before nok and everything after it
        String argsBeforeNok = args;
        String argsAfterNok = "";

        if (args.contains("nok/")) {
            String[] splitArgs = args.split("nok/");
            if (splitArgs.length != 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
            argsBeforeNok = splitArgs[0];
            argsAfterNok = splitArgs[1];
        }

        ArgumentMultimap argMultimapBeforeNok =
                ArgumentTokenizer
                        .tokenize(argsBeforeNok, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        ArgumentMultimap argMultimapAfterNok =
                ArgumentTokenizer.tokenize(argsAfterNok, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimapBeforeNok.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        // For before nok
        if (argMultimapBeforeNok.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimapBeforeNok.getValue(PREFIX_NAME).get()));
        }
        if (argMultimapBeforeNok.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimapBeforeNok.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimapBeforeNok.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimapBeforeNok.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimapBeforeNok.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor
                    .setAddress(ParserUtil.parseAddress(argMultimapBeforeNok.getValue(PREFIX_ADDRESS).get()));
        }

        parseTagsForEdit(argMultimapBeforeNok.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        // For after nok
        if (argMultimapAfterNok.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setNokName(ParserUtil.parseName(argMultimapAfterNok.getValue(PREFIX_NAME).get()));
        }
        if (argMultimapAfterNok.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setNokPhone(ParserUtil.parsePhone(argMultimapAfterNok.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimapAfterNok.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setNokEmail(ParserUtil.parseEmail(argMultimapAfterNok.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimapAfterNok.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor
                    .setNokAddress(ParserUtil.parseAddress(argMultimapAfterNok.getValue(PREFIX_ADDRESS).get()));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
