package challenge

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class BatchImportController(val batchImportService: BatchImportService) {

    @Operation(
        summary = "Receives image metadata batch from the importer, stores them in the database" +
            " and creates a job to start the asynchronous processing of the batch.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "No Content, when image batch was accepted and saved or when we already " +
                    "have an image batch with the given id.",
            ), ApiResponse(responseCode = "400", description = "Bad Request"),
        ],
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = ["/batches"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun receiveBatch(
        @RequestBody @Valid
        batch: BatchRequestVO,
    ) {
        batchImportService.saveIdempotently(batch)
    }
}
