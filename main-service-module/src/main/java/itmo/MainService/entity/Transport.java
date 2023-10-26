package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "transport")

public enum Transport {
    FEW,
    NONE,
    LITTLE,
    NORMAL,
    ENOUGH
}
