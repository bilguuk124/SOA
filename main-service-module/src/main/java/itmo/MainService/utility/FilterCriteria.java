package itmo.MainService.utility;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "filter_criteria")
public class FilterCriteria {
    @JacksonXmlProperty(localName = "field_name")
    private String fieldName;
    @JacksonXmlProperty(localName = "operator")
    private FilterOperator operator; // Operator: MORE, LESS, MOREANDEQUALS, LESSANDEQUALS
    @JacksonXmlProperty(localName = "value")
    private String value;

}
