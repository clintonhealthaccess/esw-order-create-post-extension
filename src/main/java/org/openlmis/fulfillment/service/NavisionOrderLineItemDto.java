/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2021 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation either
 * version 3 of the License or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.fulfillment.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NavisionOrderLineItemDto {

  private String Facility_Code;
  private String Program_Code;
  private String Period;
  private UUID Order_ID;
  private Boolean Emergency;
  private String Product_Code;
  private UUID Product_ID;
  private Long Product_Version;
  private Long Requested_Quantity;
  private String Requested_Quantity_Explanation;

  @JsonProperty("Facility_Code")
  public String getFacility_Code() {
    return Facility_Code;
  }

  @JsonProperty("Program_Code")
  public String getProgram_Code() {
    return Program_Code;
  }

  @JsonProperty("Period")
  public String getPeriod() {
    return Period;
  }

  @JsonProperty("Order_ID")
  public UUID getOrder_ID() {
    return Order_ID;
  }

  @JsonProperty("Emergency")
  public Boolean getEmergency() {
    return Emergency;
  }

  @JsonProperty("Product_Code")
  public String getProduct_Code() {
    return Product_Code;
  }

  @JsonProperty("Product_ID")
  public UUID getProduct_ID() {
    return Product_ID;
  }

  @JsonProperty("Product_Version")
  public Long getProduct_Version() {
    return Product_Version;
  }

  @JsonProperty("Requested_Quantity")
  public Long getRequested_Quantity() {
    return Requested_Quantity;
  }

  @JsonProperty("Requested_Quantity_Explanation")
  public String getRequested_Quantity_Explanation() {
    return Requested_Quantity_Explanation;
  }

  @Override
  public String toString() {
    return "NavisionOrderLineItemDto{" +
        "Facility_Code='" + Facility_Code + '\'' +
        ", Program_Code='" + Program_Code + '\'' +
        ", Period='" + Period + '\'' +
        ", Order_ID=" + Order_ID +
        ", Emergency=" + Emergency +
        ", Product_Code='" + Product_Code + '\'' +
        ", Product_ID=" + Product_ID +
        ", Product_Version=" + Product_Version +
        ", Requested_Quantity=" + Requested_Quantity +
        ", Requested_Quantity_Explanation='" + Requested_Quantity_Explanation + '\'' +
        '}';
  }
}
