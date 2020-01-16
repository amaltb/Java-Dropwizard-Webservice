package com.expedia.www.doppler.metastore.unit.resources;

import com.expedia.www.doppler.metastore.commons.entities.Team;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("PMD.SystemPrintln")
public class TeamResourceTest {
    private TeamResourceTest() {
    }

    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final InputStream input = new FileInputStream("/Users/ambabu/Documents/office/projects/doppler/doppler-metastore-service/service/src/test/resources/data/team.json");

        final Team team = mapper.readValue(input, Team.class);

        System.out.println(team.getId());
    }
}