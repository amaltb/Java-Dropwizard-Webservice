package com.ab.example.metastore.service.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MetaStoreExceptionMapper implements ExceptionMapper<MetaStoreException> {
    @Override
    public Response toResponse(MetaStoreException e)
    {
        return Response.status(e.getStatus()).entity(e).build();
    }
}
