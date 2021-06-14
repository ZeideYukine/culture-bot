package com.zeide.culturebot

import com.zeide.culturebot.gelbooru.PostTags
import dev.kord.core.behavior.channel.MessageChannelBehavior

data class PostTask(
    val channel: MessageChannelBehavior,
    val limit: Int,
    val tags: PostTags
)