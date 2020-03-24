package org.kafkahq.rest;


import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.micrometer.core.lang.Nullable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;
import org.kafkahq.service.SchemaService;
import org.kafkahq.service.dto.SchemaRegistry.SchemaListDTO;
import org.kafkahq.service.dto.SchemaRegistry.DeleteSchemaDTO;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller("${kafkahq.server.base-path:}/api")
public class SchemaResource {

    private SchemaService schemaService;

    @Inject
    public SchemaResource(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Get("/schema")
    public SchemaListDTO fetchAllSchemaRegistry(String clusterId, @Nullable String search, Optional<Integer> pageNumber) throws ExecutionException, InterruptedException, IOException, RestClientException {
        log.debug("Fetch all Schema Registry");

        return schemaService.getSchema(clusterId,  Optional.ofNullable(search), pageNumber);
    }

    @Delete("/schema/delete")
    public SchemaListDTO deleteSchema(@Body DeleteSchemaDTO deleteSchemaDTO) throws ExecutionException, InterruptedException, IOException, RestClientException {
        log.debug("Delete schema: {}", deleteSchemaDTO.getSubject());
        schemaService.deleteSchema(deleteSchemaDTO.getClusterId(), deleteSchemaDTO.getSubject());
        return schemaService.getSchema(deleteSchemaDTO.getClusterId(), null, Optional.empty());
    }
}
