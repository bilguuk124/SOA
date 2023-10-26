package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "coordinates")
public class Coordinates implements Serializable {

    @Max(548)
    @NotNull
    private double x;
    @NotNull
    private Integer y;
}
