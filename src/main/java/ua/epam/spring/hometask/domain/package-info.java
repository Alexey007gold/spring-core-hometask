/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeAdapter.class),
        @XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateAdapter.class)
})
package ua.epam.spring.hometask.domain;

import ua.epam.spring.hometask.xmladapter.LocalDateAdapter;
import ua.epam.spring.hometask.xmladapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalDateTime;