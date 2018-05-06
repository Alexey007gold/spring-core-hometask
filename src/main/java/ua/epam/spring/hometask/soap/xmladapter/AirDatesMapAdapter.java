package ua.epam.spring.hometask.soap.xmladapter;

import ua.epam.spring.hometask.domain.EventDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AirDatesMapAdapter extends XmlAdapter<AdaptedMap, Map<LocalDateTime, EventDate>> {

    @Override
    public Map<LocalDateTime, EventDate> unmarshal(AdaptedMap adaptedMap) throws Exception {
        Map<LocalDateTime, EventDate> map = new HashMap<>();
        for(Entry entry : adaptedMap.getEntry()) {
            map.put(LocalDateTime.parse(entry.getKey()), entry.getValue());
        }
        return map;
    }

    @Override
    public AdaptedMap marshal(Map<LocalDateTime, EventDate> map) throws Exception {
        AdaptedMap adaptedMap = new AdaptedMap();
        for(Map.Entry<LocalDateTime, EventDate> mapEntry : map.entrySet()) {
            Entry entry = new Entry();
            entry.setKey(mapEntry.getKey().toString());
            entry.setValue(mapEntry.getValue());
            adaptedMap.getEntry().add(entry);
        }
        return adaptedMap;
    }

}