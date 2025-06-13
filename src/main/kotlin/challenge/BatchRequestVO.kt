package challenge

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Schema(description = "A batch")
@JsonNaming(
    PropertyNamingStrategies.SnakeCaseStrategy::class,
)
data class BatchRequestVO(
    @Schema(description = "The list of image metadata to be processed")
    @field:Valid
    @field:NotNull
    val imageMetadata: List<ImageMetadataVO>,
)
