package itmo.MainService.DTO;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JacksonXmlRootElement(localName = "house_create_dto")
public class HouseCreateDTO {
    @NotNull
    @NotEmpty
    @JacksonXmlProperty(localName = "name")
    private String name;
    @Min(1)
    @Max(634)
    @JacksonXmlProperty(localName = "year")
    private Integer year;
    @Min(1)
    @JacksonXmlProperty(localName = "number_of_floors")
    private Long numberOfFloors;


    public boolean isFull(){
        return name != null && year != null && numberOfFloors != null;
    }
}
