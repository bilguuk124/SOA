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
@NoArgsConstructor
@ToString
@JacksonXmlRootElement(localName = "flat_create_dto")
public class FlatCreateDTO {
    @JacksonXmlProperty(localName = "name")
    @NotNull
    String name;

    @JacksonXmlProperty(localName = "coordinateX")
    @NotNull
    Double coordinateX;

    @JacksonXmlProperty(localName = "coordinateY")
    @NotNull
    Integer coordinateY;

    @Min(1)
    @Max(527)
    @JacksonXmlProperty(localName = "area")
    Integer area;

    @JacksonXmlProperty(localName = "number_of_rooms")
    @Min(1)
    Long numberOfRooms;

    @JacksonXmlProperty(localName = "furnish")
    @NotEmpty
    String furnish;

    @JacksonXmlProperty(localName = "view")
    @NotEmpty
    String view;

    @JacksonXmlProperty(localName = "transport")
    @NotEmpty
    String transport;

    @JacksonXmlProperty(localName = "house_name")
    @NotEmpty
    String houseName;

    @JacksonXmlProperty(localName = "price")
    @Min(1)
    Long price;

    @JacksonXmlProperty(localName = "has_balcony")
    @NotNull
    Boolean hasBalcony;

    public boolean isFull(){
        return name != null && coordinateX != null && coordinateY != null && area != 0 && numberOfRooms != 0 && furnish != null && transport != null && view != null && price != null && hasBalcony != null;
    }
}
