/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2021 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.fulfillment.service;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.tomcat.util.codec.binary.Base64;
import org.openlmis.fulfillment.domain.Order;
import org.openlmis.fulfillment.domain.OrderLineItem;
import org.openlmis.fulfillment.extension.point.OrderCreatePostProcessor;
import org.openlmis.fulfillment.service.referencedata.FacilityDto;
import org.openlmis.fulfillment.service.referencedata.FacilityReferenceDataService;
import org.openlmis.fulfillment.service.referencedata.OrderableDto;
import org.openlmis.fulfillment.service.referencedata.OrderableReferenceDataService;
import org.openlmis.fulfillment.service.referencedata.PeriodReferenceDataService;
import org.openlmis.fulfillment.service.referencedata.ProcessingPeriodDto;
import org.openlmis.fulfillment.service.referencedata.ProgramDto;
import org.openlmis.fulfillment.service.referencedata.ProgramReferenceDataService;
import org.openlmis.fulfillment.service.request.RequestHelper;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Component(value = "NavisionOrderCreator")
public class NavisionOrderCreator implements OrderCreatePostProcessor {

  private static final XLogger XLOGGER = XLoggerFactory.getXLogger(NavisionOrderCreator.class);

  @Autowired
  private FacilityReferenceDataService facilityReferenceDataService;

  @Autowired
  private ProgramReferenceDataService programReferenceDataService;

  @Autowired
  private OrderableReferenceDataService orderableReferenceDataService;

  @Autowired
  private RequisitionService requisitionService;

  @Autowired
  private PeriodReferenceDataService periodReferenceDataService;

  private final RestOperations restTemplate = new RestTemplate();

  @Value("${navision.url}")
  private String navisionUrl;

  @Value("${navision.username}")
  private String navisionUsername;

  @Value("${navision.password}")
  private String navisionPassword;

  @Override
  public void process(Order order) {
    XLOGGER.entry(order);
    Profiler profiler = new Profiler("NAVISION_ORDER_CREATE_POST_PROCESSOR");
    profiler.setLogger(XLOGGER);

    XLOGGER.debug("Getting reference data to create order to Navision API");
    FacilityDto supplyingFacility = facilityReferenceDataService
        .findOne(order.getSupplyingFacilityId());

    ProgramDto program = programReferenceDataService
        .findOne(order.getProgramId());

    List<UUID> orderableIds = new ArrayList<>();
    for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
      orderableIds.add(orderLineItem.getOrderable().getId());
    }
    List<OrderableDto> orderables = orderableReferenceDataService
        .findByIds(orderableIds);
    Map<UUID, OrderableDto> orderablesMap = orderables.stream()
        .collect(Collectors.toMap(OrderableDto::getId, Function.identity()));

    XLOGGER.debug("Getting requisition data to create order to Navision API");
    RequisitionDto requisition = requisitionService.findOne(order.getExternalId());
    ProcessingPeriodDto processingPeriod = periodReferenceDataService
        .findOne(requisition.getProcessingPeriod().getId());
    Map<UUID, RequisitionLineItemDto> lineItemsMap = requisition.getRequisitionLineItems().stream()
        .collect(Collectors.toMap(lineItem -> lineItem.getOrderable().getId(), lineItem -> lineItem));

    String plainCreds = navisionUsername + ":" + navisionPassword;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + base64Creds);

    URI uri = RequestHelper.createUri(navisionUrl);

    XLOGGER.debug("Sending requests");
    for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
      try {
        NavisionOrderLineItemDto requestBody = new NavisionOrderLineItemDto(
            supplyingFacility.getCode(),
            program.getCode(),
            processingPeriod.getEndDate().format(DateTimeFormatter.ISO_INSTANT),
            order.getId(),
            order.getEmergency(),
            orderablesMap.get(orderLineItem.getOrderable().getId()).getProductCode(),
            orderLineItem.getOrderable().getId(),
            orderLineItem.getOrderable().getVersionNumber(),
            orderablesMap.get(orderLineItem.getOrderable().getId()).getDispensable().getDisplayUnit(),
            orderLineItem.getOrderedQuantity(),
            lineItemsMap.get(orderLineItem.getOrderable().getId()).getRequestedQuantityExplanation()
        );
        HttpEntity<NavisionOrderLineItemDto> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.postForObject(uri, entity, Object.class);

      } catch (HttpStatusCodeException ex) {
        XLOGGER.warn(
            "Unable to create order in Navision. Error code: {}, response message: {}",
            ex.getStatusCode(), ex.getResponseBodyAsString()
        );
      } catch (Exception ex) {
        XLOGGER.warn("Unable to create order in Navision. Error message: {}", ex.getMessage());
      }
    }
    profiler.stop().log();
    XLOGGER.exit();
  }
}
