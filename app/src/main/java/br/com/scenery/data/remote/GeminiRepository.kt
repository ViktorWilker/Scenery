package br.com.scenery.data.remote

import br.com.scenery.BuildConfig
import br.com.scenery.data.model.SceneResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GeminiRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(GeminiApi::class.java)

    private val sceneAdapter = moshi.adapter(SceneResponse::class.java)

    suspend fun generateScene(userPrompt: String): Result<SceneResponse> {
        return try {
            val response = api.generateScene(
                apiKey = API_KEY,
                request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = buildPrompt(userPrompt)))
                        )
                    )
                )
            )

            val rawText = response.candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: return Result.failure(Exception("Resposta vazia da IA"))

            val cleaned = rawText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val scene = sceneAdapter.fromJson(cleaned)
                ?: return Result.failure(Exception("Erro ao parsear JSON"))

            Result.success(scene)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPrompt(userPrompt: String): String = """
        Você é um compositor de ambientes sonoros.
        O usuário vai descrever uma cena e você deve montar um mix de sons para ela.

        Sons disponíveis (use apenas estes IDs):
        rain_light, rain_heavy, wind_soft, wind_howling, thunder_distant,
        fire_crackling, river_flowing, ocean_waves, forest_birds, night_crickets,
        cafe_ambience, city_traffic, train_rolling, subway_station, crowd_murmur,
        fireplace_indoor, library_quiet, clock_ticking, vinyl_crackle,
        space_drone, cave_echo, tavern_ambience

        Responda APENAS com um JSON válido, sem markdown, sem texto extra:
        {
          "scene_name": "nome criativo e evocativo da cena (máx 5 palavras)",
          "mood": "2-3 adjetivos que descrevem o clima",
          "sounds": [
            { "id": "ID_DO_SOM", "volume": 0.0 a 1.0 }
          ]
        }

        Use entre 2 e 4 sons. Volumes devem fazer sentido — o som principal mais alto, os secundários mais baixos.

        Cena: $userPrompt
    """.trimIndent()

    companion object {
        private const val API_KEY = BuildConfig.GEMINI_API_KEY
    }
}