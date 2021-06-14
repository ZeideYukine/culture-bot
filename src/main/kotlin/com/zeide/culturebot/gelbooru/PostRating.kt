package com.zeide.culturebot.gelbooru

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PostRating.Serializer::class)
enum class PostRating {
    SAFE,
    QUESTIONABLE,
    EXPLICIT;

    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.titlecase() }

    internal companion object Serializer : KSerializer<PostRating> {
        override val descriptor = PrimitiveSerialDescriptor("CultureBot.PostRating", PrimitiveKind.CHAR)

        override fun deserialize(decoder: Decoder) = when (decoder.decodeChar()) {
            's' -> SAFE
            'q' -> QUESTIONABLE
            'e' -> EXPLICIT

            else -> EXPLICIT /* If rating is invalid, mark it as explicit to avoid false nsfw */
        }

        override fun serialize(encoder: Encoder, value: PostRating) = encoder.encodeChar(when (value) {
            SAFE -> 's'
            QUESTIONABLE -> 'q'
            EXPLICIT -> 'e'
        })
    }
}