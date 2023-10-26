package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
