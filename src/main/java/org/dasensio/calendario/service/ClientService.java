package org.dasensio.calendario.service;

import java.io.IOException;
import java.util.Collection;
import org.dasensio.calendario.domain.Client;
import org.dasensio.calendario.repository.ClientRepository;
import org.dasensio.calendario.util.CalendarException;

public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Collection<Client> getClients() {
        try {
            return repository.getClients();
        } catch (IOException e) {
            throw new CalendarException(e);
        }
    }

}
