package org.dasensio.calendario.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import org.dasensio.calendario.domain.Client;
import org.dasensio.calendario.repository.ClientRepository;
import org.dasensio.calendario.util.CalendarException;
import org.dasensio.calendario.util.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class ClientServiceTests {

    private ClientService service;
    private ClientRepository repository;

    @BeforeEach
    void beforeEach() {
        repository = mock(ClientRepository.class);
        service = new ClientService(repository);
    }

    @RepeatedTest(5)
    void getClients_returnsDataFromRepository() throws IOException {
        Random random = new Random();
        Collection<Client> list = TestDataUtils.getClients(random.nextInt(100));
        when(repository.getClients()).thenReturn(list);
        var clients = service.getClients();
        Assertions.assertEquals(list, clients);
    }

    @Test
    void getClients_catchExceptionFromRepository() throws IOException {
        when(repository.getClients()).thenThrow(new IOException("test exception"));
        Assertions.assertThrows(CalendarException.class, () -> service.getClients());
    }

}
