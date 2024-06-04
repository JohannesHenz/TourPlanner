package com.FHTW.tourplanner.Model;

import com.FHTW.tourplanner.Model.Enums.TransportType;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tour {

    private String name;
    private String tourDescription;
    private String from;
    private String to;
    private TransportType transportType;
    private int tourDistance;
    private int estimatedTime;
    private Image routeInformation;
}
