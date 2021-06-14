package com.zeide.culturebot

import com.zeide.culturebot.gelbooru.GelbooruAPI
import com.zeide.culturebot.gelbooru.GelbooruPost.Companion.forPost
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.entity.User
import io.ktor.client.features.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.net.ConnectException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class TaskQueue {
    private val queueScope = CoroutineScope(Dispatchers.Default)
    private val taskScope = CoroutineScope(Dispatchers.Default)

    private val activeTasks: ConcurrentMap<Snowflake, PostTask> = ConcurrentHashMap()
    private val tasksChannel = Channel<Pair<Snowflake, PostTask>>(capacity = Channel.UNLIMITED)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun startProcessing() = queueScope.launch {
        while (!tasksChannel.isClosedForReceive) {
            val (issuerId, postTask) = tasksChannel.receive()

            taskScope.launch {
                processTask(issuerId, postTask)
            }

            delay(200)
        }
    }

    private suspend fun processTask(issuerId: Snowflake, postTask: PostTask) {
        activeTasks[issuerId] = postTask

        try {
            val channel = postTask.channel

            channel.withTyping {
                val posts = try {
                    GelbooruAPI.fetchPosts(limit = postTask.limit, tags = postTask.tags)
                } catch (e: ResponseException) {
                    channel.createMessage("Impossible d'atteindre l'API de Gelbooru.")
                    return
                } catch (e: ConnectException) {
                    channel.createMessage("Impossible d'atteindre l'API de Gelbooru.")
                    return
                }

                if (posts.isEmpty()) {
                    channel.createMessage("Aucun résultat n'a été trouvé.")
                    return
                }

                val firstPost = posts.first()

                createEmbed {
                    forPost(firstPost)

                    if (posts.size >= 2) {
                        footer {
                            text = "Posts: 1/${posts.size}"
                        }
                    }
                }.also { if (posts.size >= 2) CultureBot.registerMultiPost(it, MultiPostDescriptor(posts, 0)) }
            }
        } finally {
            activeTasks -= issuerId
        }
    }

    suspend fun offer(issuer: User, postTask: PostTask): Boolean {
        if (issuer.id in activeTasks)
            return false

        tasksChannel.send(Pair(issuer.id, postTask))
        return true
    }
}