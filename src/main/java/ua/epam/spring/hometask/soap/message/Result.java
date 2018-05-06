package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Oleksii_Kovetskyi on 5/6/2018.
 */
@XmlType(name = "result")
@XmlEnum
public enum Result {
    SUCCESS,
    FAIL
}
