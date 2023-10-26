package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "house")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "house")
public class House {
    @Id
    @NotEmpty
    @JacksonXmlProperty(localName = "name")
    private String name;

    @Min(1)
    @Max(634)
    @JacksonXmlProperty(localName = "year")
    private Integer year;

    @Min(1)
    @JacksonXmlProperty(localName = "numberOfFloors")
    private long numberOfFloors;

}
