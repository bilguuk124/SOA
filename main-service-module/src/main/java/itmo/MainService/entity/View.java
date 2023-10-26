package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "view")

public enum View {
    STREET,
    YARD,
    BAD,
    NORMAL,
    TERRIBLE
}
