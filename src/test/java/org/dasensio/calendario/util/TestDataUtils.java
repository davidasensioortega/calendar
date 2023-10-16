package org.dasensio.calendario.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.dasensio.calendario.domain.Client;

public class TestDataUtils {

    public static Collection<Client> getClients(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> new Client(i, "test" + i))
            .collect(Collectors.toSet());
    }

    public static Collection<Collection<Client>> getGroups(int size, int groupSize) {
        return IntStream.range(0, size)
            .mapToObj(i -> getClients(groupSize))
            .collect(Collectors.toList());
    }

    public static Map<LocalDate, Collection<Client>> getCalendar() {
        return Map.of(LocalDate.now(), getClients(2));
    }

    public static Map<LocalDate, Collection<Client>> getCalendarUngrouped() {
        return Map.of(LocalDate.MAX, getClients(1));
    }

}
