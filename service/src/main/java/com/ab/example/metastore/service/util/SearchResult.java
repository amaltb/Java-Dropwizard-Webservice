package com.ab.example.metastore.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author _amal
 *
 * POJO representing an element in global search store.
 */
@Data
public class SearchResult {
    @JsonProperty("esId")
    private long esId;
    @JsonProperty("msId")
    private long msId;
    private String type;
    private SearchCaptionEntity caption;
}
