package org.dasensio.calendario.service;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CollectionsServiceTests {

    Random random = new Random();

    private CollectionsService<String> service;

    @BeforeEach
    void beforeEach() {
        service = new CollectionsService<>();
    }

    @RepeatedTest(5)
    void shuffleClients_returnsAllClients() {
        List<String> list = getExample(random.nextInt(100));
        var clients = service.shuffle(list);
        Assertions.assertTrue(clients.containsAll(list));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void groupClients_returnClientsGrouped(int groupSize) {
        List<String> list = getExample(random.nextInt(100));
        var groups = service.group(list, groupSize);
        Assertions.assertEquals(groups.size(), list.size() / groupSize + (list.size() % groupSize > 0 ? 1 : 0));
        List<String> clients = groups.stream().flatMap(Collection::stream).collect(Collectors.toList());
        Assertions.assertEquals(list.size(), clients.size());
        Assertions.assertTrue(list.containsAll(clients));
        Assertions.assertTrue(groups.stream().map(Collection::size).max(Integer::compareTo).orElse(Integer.MAX_VALUE) <= groupSize);
    }

    private List<String> getExample(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> "test" + i)
            .collect(Collectors.toList());
    }

}
