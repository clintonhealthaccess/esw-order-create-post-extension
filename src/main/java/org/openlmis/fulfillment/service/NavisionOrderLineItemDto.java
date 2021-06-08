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

  private String Facility_No;
  private String Program_Code;
  private String Period_End_Date;
  private UUID Order_ID;
  private String Order_No;
  private Boolean Emergency;
  private String Product_Code;
  private UUID Product_ID;
  private Long Product_Version_No;
  private String Product;
  private String Dispencing_UOM;
  private Long Requested_Quantity;
  private String Requested_Quantity_Explanation;

  @JsonProperty("Facility_No")
  public String getFacility_No() {
    return Facility_No;
  }

  @JsonProperty("Program_Code")
  public String getProgram_Code() {
    return Program_Code;
  }

  @JsonProperty("Period_End_Date")
  public String getPeriod_End_Date() {
    return Period_End_Date;
  }

  @JsonProperty("Order_ID")
  public UUID getOrder_ID() {
    return Order_ID;
  }

  @JsonProperty("Order_No")
  public String getOrder_No() {
    return Order_No;
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

  @JsonProperty("Product_Version_No")
  public Long getProduct_Version_No() {
    return Product_Version_No;
  }

  @JsonProperty("Product")
  public String getProduct() {
    return Product;
  }

  @JsonProperty("Dispencing_UOM")
  public String getDispencing_UOM() {
    return Dispencing_UOM;
  }

  @JsonProperty("Requested_Quantity")
  public Long getRequested_Quantity() {
    return Requested_Quantity;
  }

  @JsonProperty("Requested_Quantity_Explanation")
  public String getRequested_Quantity_Explanation() {
    return Requested_Quantity_Explanation;
  }
}
