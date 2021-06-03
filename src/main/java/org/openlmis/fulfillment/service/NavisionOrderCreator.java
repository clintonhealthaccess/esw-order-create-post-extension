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

import org.openlmis.fulfillment.domain.Order;
import org.openlmis.fulfillment.extension.point.OrderCreatePostProcessor;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.stereotype.Component;

@Component(value = "NavisionOrderCreator")
public class NavisionOrderCreator implements OrderCreatePostProcessor {

  private static final XLogger XLOGGER = XLoggerFactory.getXLogger(NavisionOrderCreator.class);

  @Override
  public void process(Order order) {
    XLOGGER.entry(order);
    Profiler profiler = new Profiler("DEFAULT_ORDER_CREATE_POST_PROCESSOR");
    profiler.setLogger(XLOGGER);

    XLOGGER.info("Testing that we have gotten to the Navision extension point");

    profiler.stop().log();
    XLOGGER.exit();
  }
}
