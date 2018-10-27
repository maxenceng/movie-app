package app.utils;

import app.api.CallApis;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static CallApis buildCallApis() {
        // Indicate the base URL and the converter used to bring the json into objects, here gson.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create());
        // Builds the retrofit object
        Retrofit retrofit = builder.build();
        // Get the functions containing the API calls from the CallApis interface
        return retrofit.create(CallApis.class);
    }

}
