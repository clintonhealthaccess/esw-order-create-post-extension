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

import static org.openlmis.fulfillment.service.request.RequestHelper.createUri;

import java.net.URI;
import java.util.UUID;
import org.openlmis.fulfillment.service.request.RequestHeaders;
import org.openlmis.fulfillment.service.request.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class RequisitionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String REQUISITION_API = "/api/v2/requisitions/";

  @Autowired
  protected AuthService authService;

  protected RestOperations restTemplate = new RestTemplate();

  @Value("${requisition.url}")
  private String requisitionUrl;

  /**
   * Return one object from Reference data service.
   *
   * @param id UUID of requesting object.
   * @return Requesting reference data object.
   */
  public RequisitionDto findOne(UUID id) {
    String url = getServiceUrl() + getUrl() + id;

    try {
      ResponseEntity<RequisitionDto> responseEntity = restTemplate.exchange(
          buildUri(url), HttpMethod.GET, createEntity(), getResultClass());
      return responseEntity.getBody();
    } catch (HttpStatusCodeException ex) {
      // rest template will handle 404 as an exception, instead of returning null
      if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
        logger.warn("{} with id {} does not exist. ", getResultClass().getSimpleName(), id);
        return null;
      } else {
        throw buildDataRetrievalException(ex);
      }
    }
  }

  protected URI buildUri(String url) {
    return createUri(url);
  }

  protected  <E> HttpEntity<E> createEntity() {
    return RequestHelper.createEntity(createHeadersWithAuth());
  }

  private RequestHeaders createHeadersWithAuth() {
    return RequestHeaders.init().setAuth(authService.obtainAccessToken());
  }

  protected DataRetrievalException buildDataRetrievalException(HttpStatusCodeException ex) {
    return new DataRetrievalException(getResultClass().getSimpleName(), ex);
  }

  protected String getServiceUrl() {
    return requisitionUrl;
  }

  protected String getUrl() {
    return REQUISITION_API;
  }

  protected Class<RequisitionDto> getResultClass() {
    return RequisitionDto.class;
  }

  protected Class<RequisitionDto[]> getArrayResultClass() {
    return RequisitionDto[].class;
  }
}
