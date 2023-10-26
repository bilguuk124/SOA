package itmo.MainService.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="flat")
@JacksonXmlRootElement(localName = "flat")
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false )
    @NotBlank
    private String name;

    @JacksonXmlElementWrapper(localName = "coordinates")
    @Embedded
    @NotNull
    private Coordinates coordinates;

    @JacksonXmlElementWrapper(localName = "creation-date")
    @NotNull
    private LocalDate creationDate;


    @Min(1)
    @Max(527)
    private int area;
    @Min(1)
    private long numberOfRooms;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Furnish furnish;

    @Enumerated(EnumType.STRING)
    @NotNull
    private View view;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Transport transport;

    @ManyToOne
    @JoinColumn(name = "house_id")
    @NotNull
    private House house;

    @Min(1)
    private Long price;

    @NotNull
    private boolean hasBalcony;

    public Flat(String name, Coordinates coordinates, int area, long numberOfRooms, Furnish furnish, View view, Transport transport, House house, Long price, boolean hasBalcony) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.price = price;
        this.hasBalcony = hasBalcony;
    }
}
