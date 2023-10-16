package org.dasensio.calendario.repository;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ClientRepositoryTests {

    private ClientRepository repository;

    @BeforeEach
    void beforeEach() {
        repository = new ClientRepository(ClientRepositoryTests.class.getClassLoader().getResource("clientes.txt").getFile().toString());
    }

    @Test
    void getClients() throws IOException {
        var clients = repository.getClients();
        Assertions.assertNotNull(clients);
    }

    @Test
    void getClient_returnsNull() throws IOException {
        try (MockedStatic<Files> files = Mockito.mockStatic(Files.class)) {
            List<String> list = new ArrayList<>();
            list.add(null);
            list.add("");
            list.add("  no id");
            list.add("noId");
            list.add("1 ");
            list.add("1");
            files.when(() -> Files.readAllLines(any())).thenReturn(list);
            var clients = repository.getClients();
            Assertions.assertNotNull(clients);
            Assertions.assertTrue(clients.isEmpty());
        }
    }

}
