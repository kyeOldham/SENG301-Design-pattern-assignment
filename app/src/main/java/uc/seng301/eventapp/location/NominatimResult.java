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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Plain Old Java Object (POJO) class with no logic to be used to deserialize
 * Nominatim requests objects retrieved from the MapQuest REST API.
 *
 * Note the @JsonDeserialize annotations used by the Jackson engine to populate
 * the fields with values from the Json string retrieved.
 *
 * Note also getters are not documented. In a production system, even if they
 * are simple getters, at least this should be stated clearly in their
 * docstrings.
 */
public class NominatimResult implements LocationServiceResult {

  @JsonDeserialize
  @JsonProperty("place_id")
  private String placeID;

  @JsonDeserialize
  private String licence;

  @JsonDeserialize
  @JsonProperty("osm_type")
  private String osmType;

  @JsonDeserialize
  @JsonProperty("osm_id")
  private String osmId;

  @JsonDeserialize
  @JsonProperty("boundingbox")
  private String[] boundingBox;

  @JsonDeserialize
  @JsonProperty("lat")
  private String latitude;

  @JsonDeserialize
  @JsonProperty("lon")
  private String longitude;

  @JsonDeserialize
  @JsonProperty("display_name")
  private String displayName;

  @JsonDeserialize
  @JsonProperty("class")
  private String locationClass;

  @JsonDeserialize
  @JsonProperty("type")
  private String locationType;

  @JsonDeserialize
  private Double importance;

  @JsonDeserialize
  private String icon;

  public String getPlaceID() {
    return this.placeID;
  }

  public String getLicence() {
    return this.licence;
  }

  public String getOsmType() {
    return this.osmType;
  }

  public String getOsmId() {
    return this.osmId;
  }

  public String[] getBoundingBox() {
    return this.boundingBox;
  }

  @Override
  public String getLatitude() {
    return this.latitude;
  }

  /**
   * This setter is used for mocking purpose
   *
   * @param latitude a latitude (assumed valid)
   */
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @Override
  public String getLongitude() {
    return this.longitude;
  }

  /**
   * This setter is used for mocking purpose
   *
   * @param longitude a longitude (assumed valid)
   */
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @Override
  public String getName() {
    return this.displayName;
  }

  /**
   * This setter is used for mocking purpose
   *
   * @param name a display name to set (assumed not null)
   */
  public void setDisplayName(String name) {
    this.displayName = name;
  }

  public String getLocationClass() {
    return this.locationClass;
  }

  public String getLocationType() {
    return locationType;
  }

  public Double getImportance() {
    return this.importance;
  }

  public String getIcon() {
    return this.icon;
  }

  @Override
  public String toString() {
    return ("NominatimResult {\n" + "\t placeID='" + getPlaceID() + "'\n" + "\t licence='" + getLicence() + "'\n"
        + "\t osmType='" + getOsmType() + "'\n" + "\t osmId='" + getOsmId() + "'\n" + "\t boundingBox='"
        + getBoundingBox() + "'\n" + "\t latitude='" + getLatitude() + "'\n" + "\t longitute='" + getLongitude() + "'\n"
        + "\t displayName='" + getName() + "'\n" + "\t locationClass='" + getLocationClass() + "'\n"
        + "\t locationType='" + getLocationType() + "'\n" + "\t importance='" + getImportance() + "'\n" + "\t icon='"
        + getIcon() + "'\n" + "}\n");
  }
}
