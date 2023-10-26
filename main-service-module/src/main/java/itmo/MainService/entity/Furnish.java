package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "furnish")

public enum Furnish {
    NONE,
    FINE,
    BAD,
    LITTLE
}
