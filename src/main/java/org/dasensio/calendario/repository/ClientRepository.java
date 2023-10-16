package org.dasensio.calendario.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.dasensio.calendario.domain.Client;

public class ClientRepository {

    private static final Logger LOGGER = Logger.getLogger(ClientRepository.class.getName());

    private static final String SEPARATOR = " ";

    private final Function<String, Client> parseClient = idName -> {
        if (!validateInput(idName)) {
            LOGGER.log(Level.INFO, "invalid input for client: {0}", idName);
            return null;
        }
        List<String> fields = Arrays.asList(idName.split(SEPARATOR));

        Integer id;
        try {
            id = Integer.valueOf(fields.get(0));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.INFO, "invalid integer: {0}", fields.get(0));
            return null;
        }

        String name = String.join(SEPARATOR, fields.subList(1, fields.size()));

        return new Client(id, name);
    };

    private final String sourceFile;

    public ClientRepository(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Collection<Client> getClients() throws IOException {
        List<String> strClients = Files.readAllLines(new File(sourceFile).toPath());
        return strClients.stream().map(parseClient).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private boolean validateInput(String idName) {
        return idName != null
            && idName.contains(SEPARATOR)
            && idName.split(SEPARATOR).length > 1;
    }

}
