package duke.e.chat_language_translate.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class TranslationService {
   private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build();

   public static CompletableFuture<TranslationService.TranslationResult> translate(String text, String targetLang) {
      return CompletableFuture.supplyAsync(() -> {
         try {
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
             String encodedTarget = URLEncoder.encode(targetLang, StandardCharsets.UTF_8);
             String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + encodedTarget + "&dt=t&dt=ld&q=" + encoded;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("User-Agent", "Mozilla/5.0").GET().build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
               return null;
            } else {
               JsonArray root = JsonParser.parseString((String)response.body()).getAsJsonArray();
               StringBuilder translated = new StringBuilder();
               JsonArray sentences = root.get(0).getAsJsonArray();

               for(int i = 0; i < sentences.size(); ++i) {
                  try {
                     String segment = sentences.get(i).getAsJsonArray().get(0).getAsString();
                     translated.append(segment);
                  } catch (Exception ignored) {
                  }
               }

               String detectedLang = "unknown";

               try {
                  detectedLang = root.get(2).getAsString();
               } catch (Exception ignored) {
               }

               return new TranslationService.TranslationResult(detectedLang, translated.toString().trim());
            }
         } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            return null;
         } catch (Exception e) {
            return null;
         }
      });
   }

   public static record TranslationResult(String detectedLang, String translatedText) {
      public TranslationResult(String detectedLang, String translatedText) {
         this.detectedLang = detectedLang;
         this.translatedText = translatedText;
      }

      public String detectedLang() {
         return this.detectedLang;
      }

      public String translatedText() {
         return this.translatedText;
      }
   }
}
