package org.dasensio.calendario.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import org.dasensio.calendario.domain.Client;
import org.dasensio.calendario.util.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalendarServiceTests {

    private CalendarService service;

    @BeforeEach
    void beforeEach() {
        service = new CalendarService();
    }

    @Test
    void generateCalendar_returnsCalendar() {
        Map<LocalDate, Collection<Client>> calendar = service.generateCalendar(TestDataUtils.getGroups(3, 2), LocalDate.now());
        Assertions.assertNotNull(calendar);
        Assertions.assertEquals(3, calendar.size());
        Assertions.assertTrue(calendar.containsKey(LocalDate.now()));
        Assertions.assertFalse(calendar.containsKey(LocalDate.MAX));
    }

    @Test
    void generateCalendar_noFullGroup_returnsCalendar() {
        var groups = TestDataUtils.getGroups(3, 2);
        groups.add(TestDataUtils.getClients(1));
        Map<LocalDate, Collection<Client>> calendar = service.generateCalendar(groups, LocalDate.now());
        Assertions.assertNotNull(calendar);
        Assertions.assertEquals(4, calendar.size());
        Assertions.assertTrue(calendar.containsKey(LocalDate.now()));
        Assertions.assertTrue(calendar.containsKey(LocalDate.MAX));
    }

}
