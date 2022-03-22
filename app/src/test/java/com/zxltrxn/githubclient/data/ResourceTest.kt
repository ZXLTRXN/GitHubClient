package com.zxltrxn.githubclient.data

import com.google.common.truth.Truth.assertThat
import com.zxltrxn.githubclient.data.model.User
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class ResourceTest(
    private val actualAfter: NetworkResource<Any>,
    private val expected: Resource<Any>
    ) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>> {
            val data = User(name = "Alfred", login = "dwqqfqwfDD")
            val message = "Wrong token"
            val code = 401
            return listOf(
                arrayOf(
                    NetworkResource.Success(data = data),
                    Resource.Success(data = data)),
                arrayOf(
                    NetworkResource.Error<Any>(message = message),
                    Resource.Error<Any>(message = message)),
                arrayOf(
                    NetworkResource.Error<Any>(message = message, code = code),
                    Resource.Error<Any>(message = message)),
            )
        }
    }

    @Test
    fun mapping_is_correct() {
        val actual = actualAfter.toResource()
        assertThat(actual.javaClass == expected.javaClass).isTrue()
        assertThat(actual.data).isEqualTo(expected.data)
        assertThat(actual.message).isEqualTo(expected.message)

    }
}