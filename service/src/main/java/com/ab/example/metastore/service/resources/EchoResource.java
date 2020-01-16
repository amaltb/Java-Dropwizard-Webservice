package com.ab.example.metastore.service.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/echo")
@Api("Echo")
public class EchoResource {
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Echo back your name")
    @Timed
    public Response echo(@PathParam("name") final String name) {
        final JsonObject jsonElement = new JsonObject();
        jsonElement.addProperty("name", name);
        return Response.status(200).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(jsonElement)).build();
    }
}
