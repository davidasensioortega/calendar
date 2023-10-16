package org.dasensio.calendario.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.dasensio.calendario.domain.Client;

public class CalendarService {

    public Map<LocalDate, Collection<Client>> generateCalendar(final Collection<Collection<Client>> groups, final LocalDate startDate) {
        LocalDate nextDate = startDate;
        int maxGroupSize = groups.stream().mapToInt(Collection::size).max().orElse(Integer.MAX_VALUE);
        Map<LocalDate, Collection<Client>> calendar = new HashMap<>();
        for (Collection<Client> group : groups) {
            if (group.size() == maxGroupSize) {
                calendar.put(nextDate, group);
                nextDate = getNextDate(nextDate);
            } else {
                calendar.put(LocalDate.MAX, group);
            }
        }
        return calendar;
    }

    private LocalDate getNextDate(final LocalDate previousDate) {
        return previousDate.plusDays(7);
    }

}
