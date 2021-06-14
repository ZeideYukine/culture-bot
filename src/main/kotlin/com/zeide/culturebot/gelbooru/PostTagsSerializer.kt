package com.zeide.culturebot.gelbooru

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object PostTagsSerializer : KSerializer<PostTags> {
    override val descriptor = PrimitiveSerialDescriptor("CultureBot.PostRating", PrimitiveKind.CHAR)

    override fun deserialize(decoder: Decoder) = decoder.decodeString().split(" ").toSet()
    override fun serialize(encoder: Encoder, value: PostTags) = encoder.encodeString(value.joinToString(" "))
}