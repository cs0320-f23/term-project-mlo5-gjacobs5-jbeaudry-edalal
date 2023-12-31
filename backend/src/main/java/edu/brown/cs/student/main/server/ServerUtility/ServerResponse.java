package edu.brown.cs.student.main.server.ServerUtility;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.Map;

/** The Response class is responsible for serializing a response object to JSON format. */
public class ServerResponse {
  // The response object to be serialized
  private final Map<String, Object> response;

  /**
   * Constructs a Response instance with the provided response object.
   *
   * @param response The response object to be serialized.
   */
  public ServerResponse(Map<String, Object> response) {
    this.response = response;
  }

  /**
   * Serialize the response object to JSON.
   *
   * @return A JSON representation of the response.
   * @throws RuntimeException If there is an issue with the serialization process.
   */
  public String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);

      return adapter.toJson(response);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize response.", e);
    }
  }
}
