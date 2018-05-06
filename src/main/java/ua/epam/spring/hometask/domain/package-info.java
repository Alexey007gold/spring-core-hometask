/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeAdapter.class),
        @XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateAdapter.class)
})
@javax.xml.bind.annotation.XmlSchema(namespace = "http://spring.io/guides/gs-producing-web-service", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package ua.epam.spring.hometask.domain;

import ua.epam.spring.hometask.soap.xmladapter.LocalDateAdapter;
import ua.epam.spring.hometask.soap.xmladapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalDateTime;