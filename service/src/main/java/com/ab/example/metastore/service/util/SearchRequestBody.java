package com.ab.example.metastore.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author _amal
 *
 * POJO representing global search store POST request body.
 */
@SuppressWarnings("PMD.ImmutableField")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestBody implements Serializable {
    private static final long serialVersionUID = 46L;

    @JsonProperty("ms_id")
    private Long msId;

    @JsonProperty("es_id")
    private Long esId;
    private String type;
    private SearchCaptionEntity caption;
    private String text;

    public SearchRequestBody(long msId, String type, SearchCaptionEntity caption, String text) {
        this.msId = msId;
        this.type = type;
        this.caption = caption;
        this.text = text;
    }

    @Override
    public String toString()
    {
        return String.format("{msId: %d, type: %s, caption: %s, text: %s}", msId, type, caption.toString(), text);
    }
}
