package org.dasensio.calendario.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollectionsService<T> {

    private static final Logger LOGGER = Logger.getLogger(CollectionsService.class.getName());

    public List<T> shuffle(Collection<T> list) {
        List<T> orderedList = new ArrayList<>(list);
        Collections.shuffle(orderedList);
        return orderedList;
    }

    public Collection<Collection<T>> group(List<T> list, int groupSize) {
        Collection<Collection<T>> result = new HashSet<>();
        Set<T> group = new HashSet<>();
        for (T element : list) {
            group.add(element);
            if (group.size() == groupSize) {
                result.add(group);
                group = new HashSet<>();
            }
        }
        if (!group.isEmpty()) {
            LOGGER.log(Level.INFO, "NO FULL GROUP {0}", group);
            result.add(group);
        }
        return result;
    }

}
