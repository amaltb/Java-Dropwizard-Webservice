package com.ab.example.metastore.service.util;

import lombok.Data;

import java.util.List;

/**
 * @author _amal
 *
 * POJO representing global response of a search store get api call.
 */
@Data
public class SearchGetResponse {
    private boolean success;
    private List<SearchResult> results;
}
