package com.ab.example.metastore.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author _amal
 *
 * POJO representing the caption added to an element in global search store (ES).
 */

@Data
@AllArgsConstructor
public class SearchCaptionEntity implements Serializable {
    private static final long serialVersionUID = 47L;

    private String ownerName;
    private String ownerTeamName;
    private String description;

    @Override
    public String toString()
    {
        return String.format("{owner: %s, team: %s, description: %s}", ownerName, ownerTeamName, description);
    }
}
