package org.dasensio.calendario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dasensio.calendario.domain.Client;
import org.dasensio.calendario.repository.ClientRepository;
import org.dasensio.calendario.service.CalendarService;
import org.dasensio.calendario.service.ClientService;
import org.dasensio.calendario.service.CollectionsService;
import org.dasensio.calendario.service.DocumentService;
import org.dasensio.calendario.util.CalendarException;

public class CalendarioCesta {

    private static final Logger LOGGER = Logger.getLogger(CalendarioCesta.class.getName());

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "# input arguments: {0}", args.length);
        LOGGER.log(Level.ALL, "input arguments: {0}", args);
        if (args.length < 3) {
            LOGGER.log(Level.SEVERE, "You must provide 3 arguments: source file (complete path), group size (>0) and start date (dd-MM-yyyy)");
            System.exit(1);
        }

        String sourceFile = getSourceFile(args[0]);
        int groupSize = getGroupSize(args[1]);
        LocalDate startDate = getStartDate(args[2]);

        if (sourceFile == null || groupSize <= 0 || startDate == null) {
            LOGGER.log(Level.SEVERE, "Invalid input arguments");
            System.exit(1);
        }

        //init
        ClientService clientService = new ClientService(new ClientRepository(sourceFile));
        CollectionsService<Client> collectionsService = new CollectionsService<>();
        CalendarService calendarService = new CalendarService();
        DocumentService documentService = new DocumentService();

        //logic
        Collection<Client> clients = clientService.getClients();
        List<Client> orderedClients = collectionsService.shuffle(clients);
        Collection<Collection<Client>> groups = collectionsService.group(orderedClients, groupSize);
        Map<LocalDate, Collection<Client>> calendar = calendarService.generateCalendar(groups, startDate);
        LOGGER.log(Level.INFO, "Calendar: {0}", calendar);
        ByteArrayOutputStream baos = documentService.generateDocument(calendar);

        //output
        try (OutputStream out = new FileOutputStream("calendar-" + LocalDateTime.now().getNano() + ".docx")) {
            baos.writeTo(out);
        } catch (IOException e) {
            throw new CalendarException(e);
        }
    }

    private static String getSourceFile(String path) {
        File file = Paths.get(path).toFile();
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("incorrect file " + path);
        } else {
            return path;
        }
    }

    private static int getGroupSize(String size) {
        return Integer.parseInt(size);
    }

    private static LocalDate getStartDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
