package com.sprint5.task2.fase3.mongo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ranking {


    @Schema(description = "This is the id of the Player. The id is autogenerated by the database")
    private String playerId;
    @Schema(description = "This is the name of the Player. The name is retrived by the database")
    private String playerName;
    @Schema(description = "This is the quantity of rolls won by the player.")
    private int win;
    @Schema(description = "This is the number of rolls played by the player")
    private int played;
    @Schema(description = "This is the ratio between rolls won and rolls played.")
    private int ratio;
}
