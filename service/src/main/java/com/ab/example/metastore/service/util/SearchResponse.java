package com.ab.example.metastore.service.util;

import lombok.Data;

/**
 * @author _amal
 *
 * POJO representing global search store API call response (other than GET).
 */
@Data
public class SearchResponse {

    private boolean success;
    private String message;
}
