package uk.ac.ebi.pride.archive.px;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import uk.ac.ebi.pride.tools.ErrorHandlerIface;
import uk.ac.ebi.pride.tools.GenericSchemaValidator;
import uk.ac.ebi.pride.tools.ValidationErrorHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Class to validate a PX XML file according to the PX XML schema.
 *
 * @author Tobias Ternent
 */

public class ValidateMessage {

    private static final Logger logger = LoggerFactory.getLogger(ValidateMessage.class);
    private static final String SCHEMA_LOCATION = "http://proteomecentral.proteomexchange.org/schemas/proteomeXchange-1.2.0.xsd";

    /**
     * Method to validate a supplied PX XML file.
     *
     * @param file the PX XML file to be validated.
     * @return a String which lists any errors that occurred during validation. If there were none, the String will be empty.
     */
    public static String validateMessage(File file) throws SAXException, MalformedURLException, FileNotFoundException, URISyntaxException{
        StringBuilder errorOutput = new StringBuilder();
        GenericSchemaValidator genericValidator = new GenericSchemaValidator();
        genericValidator.setSchema(new URI(SCHEMA_LOCATION));

        logger.info("XML schema validation on " + file.getName());

        ErrorHandlerIface handler = new ValidationErrorHandler();
        genericValidator.setErrorHandler(handler);
        BufferedReader br = new BufferedReader(new FileReader(file));
        genericValidator.validate(br);
        List<String> errorMsgs = handler.getErrorMessages(); // ToDo: make ErrorHandlerIface type safe
        for (String content : errorMsgs) {
            errorOutput.append(content);
            errorOutput.append(System.getProperty("line.separator"));
        }

        return errorOutput.toString();
    }
}