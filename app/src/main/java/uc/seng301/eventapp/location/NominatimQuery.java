/*
 * Created on Wed Apr 07 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */
package uc.seng301.eventapp.location;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles call to the MapQuest REST API
 */
public class NominatimQuery implements LocationService {

  private static final Logger LOGGER = LogManager.getLogger(NominatimQuery.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String MAPQUEST_SEARCH = "http://open.mapquestapi.com/nominatim/v1/search.php";

  // remember that you should never put API keys in your source code as this
  // but for the purpose of this lab, we make it easy
  // get your key on
  // https://developer.mapquest.com/plan_purchase/steps/business_edition/business_edition_free/register
  // see https://developer.mapquest.com/documentation/open/nominatim-search/
  private static final String MAPQUEST_KEY = "ipOy58aqmtKrnTGmjASL0OfEhYC2YYBX";

  /**
   * Retrieve the first plausible city from a query string using the mapquest
   * Nominatim API.
   * 
   * See <https://developer.mapquest.com>
   *
   * @param query a query
   * @return the NominatimResult object of a city corresponding to the search
   *         query, null if none found
   */
  @Override
  public NominatimResult getCityFromString(String query) {
    List<NominatimResult> rawResults = retrieveLocations(query);
    return rawResults.stream().filter(n -> "city".equals(n.getLocationType())).findFirst().orElse(null);
  }

  /**
   * Call the external REST API from Mapquest to retrieve a location from a given
   * string
   *
   * @param query a location to look for
   * @return the list of NominatimResult corresponding to given search query, an
   *         empty list if none are retrieved
   */
  private List<NominatimResult> retrieveLocations(String query) {
    List<NominatimResult> results = null;
    try {
      URL url = new URL(MAPQUEST_SEARCH + "?key=" + MAPQUEST_KEY + "&format=json&q=" + query);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();

      int responseCode = connection.getResponseCode();

      if (responseCode != 200) {
        LOGGER.error("unable to process request to MapRequest, response code is '{}'", responseCode);
        return Collections.emptyList();
      }

      Scanner scanner = new Scanner(url.openStream());
      StringBuilder stringResult = new StringBuilder();
      while (scanner.hasNext()) {
        stringResult.append(scanner.nextLine());
      }
      scanner.close();
      results = convertToNominatimResult(stringResult.toString());
      LOGGER.debug(results);
    } catch (IOException e) {
      LOGGER.error("error processing the request", e);
    }
    return null == results ? Collections.emptyList() : results;
  }

  /**
   * Convert given json string into a list of NominatimResult
   *
   * @param jsonString a json string complying to the NominatimResult
   * @return the converted json into a list of NominatimResult
   * @throws JsonProcessingException if the deserialisation resulted in an error
   */
  private List<NominatimResult> convertToNominatimResult(String jsonString) throws JsonProcessingException {
    return Arrays.asList(objectMapper.readValue(jsonString, NominatimResult[].class));
  }
}
