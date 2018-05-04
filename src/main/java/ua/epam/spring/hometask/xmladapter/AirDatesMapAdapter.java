package ua.epam.spring.hometask.xmladapter;

import ua.epam.spring.hometask.domain.EventDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirDatesMapAdapter extends XmlAdapter<AirDatesMapAdapter.AdaptedMap, Map<LocalDateTime, EventDate>> {

    public static class AdaptedMap {

        public List<Entry> entry = new ArrayList<>();
    }

    public static class Entry {

        public String key;

        public EventDate value;

    }

    @Override
    public Map<LocalDateTime, EventDate> unmarshal(AdaptedMap adaptedMap) throws Exception {
        Map<LocalDateTime, EventDate> map = new HashMap<>();
        for(Entry entry : adaptedMap.entry) {
            map.put(LocalDateTime.parse(entry.key), entry.value);
        }
        return map;
    }

    @Override
    public AdaptedMap marshal(Map<LocalDateTime, EventDate> map) throws Exception {
        AdaptedMap adaptedMap = new AdaptedMap();
        for(Map.Entry<LocalDateTime, EventDate> mapEntry : map.entrySet()) {
            Entry entry = new Entry();
            entry.key = mapEntry.getKey().toString();
            entry.value = mapEntry.getValue();
            adaptedMap.entry.add(entry);
        }
        return adaptedMap;
    }

}