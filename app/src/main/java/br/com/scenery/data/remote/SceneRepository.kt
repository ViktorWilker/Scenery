package br.com.scenery.data.remote

import br.com.scenery.data.model.SceneResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SceneryApi {
    @POST("scene")
    suspend fun createScene(@Body request: SceneRequest): SceneResponse
}

data class SceneRequest(val prompt: String)

class SceneRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://scenery-backend.onrender.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SceneryApi::class.java)

    suspend fun generateScene(prompt: String): Result<SceneResponse> {
        return try {
            val scene = api.createScene(SceneRequest(prompt))
            Result.success(scene)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}