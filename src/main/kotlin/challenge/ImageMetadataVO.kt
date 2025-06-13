package challenge

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ImageMetadataVO(

    @Schema(
        description = "A unique ID of this shipment. Should be used in log messages to find relevant information about this shipment.",
        example = "811769e0-69f8-11e6-91aa-02000ab20f88",
    )
    @field:Size(max = 36)
    @field:NotNull
    val imageMetadataId: String,

    @Schema(
        description = "The blob storage container name the corresponding image is stored in.",
        example = "bbs-images-057",
    )
    @field:Size(max = 64)
    @field:NotNull
    val containerName: String,

    @Schema(
        description = "The original 'SID' of this shipment. When the original filename on the delivery ZIP was a1234p.png.enc, " +
            "then the originalId is a1234p",
        example = "a1234p",
    )
    @field:Size(max = 64)
    @field:NotNull
    val originalId: String,
)
