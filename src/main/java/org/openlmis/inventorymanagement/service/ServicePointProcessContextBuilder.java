/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
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

package org.openlmis.inventorymanagement.service;

import static org.slf4j.LoggerFactory.getLogger;
import static org.slf4j.ext.XLoggerFactory.getXLogger;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.openlmis.inventorymanagement.dto.ServicePointDto;
import org.openlmis.inventorymanagement.service.referencedata.FacilityReferenceDataService;
import org.openlmis.inventorymanagement.util.AuthenticationHelper;
import org.openlmis.inventorymanagement.util.ServicePointProcessContext;

import org.slf4j.Logger;
import org.slf4j.ext.XLogger;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Before we process a pod event, this class will run first, to get all things we need from
 * reference data. So that network traffic will be concentrated at one place rather than scattered
 * all around the place.
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
public class ServicePointProcessContextBuilder {

  private static final Logger LOGGER = getLogger(
          ServicePointProcessContextBuilder.class);
  private static final XLogger XLOGGER = getXLogger(
          ServicePointProcessContextBuilder.class);
  
  @Autowired
  private AuthenticationHelper authenticationHelper;
  
  @Autowired
  private FacilityReferenceDataService facilityService;
  
  /**
   * Before processing events, put all needed ref data into context so
   * we don't have to do frequent network requests.
   *
   * @param servicePointDto servicePointDto.
   * @return a context object that includes all needed ref data.
   */
  public ServicePointProcessContext buildContext(
          ServicePointDto servicePointDto) {
    XLOGGER.entry(servicePointDto);
    Profiler profiler = new Profiler("BUILD_CONTEXT");
    profiler.setLogger(XLOGGER);

    LOGGER.info("build stock event process context");
    ServicePointProcessContext context = new ServicePointProcessContext();

    // profiler.start("CREATE_LAZY_USER");
    // OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
    //     .getContext()
    //     .getAuthentication();

    // Supplier<UUID> userIdSupplier;

    // if (authentication.isClientOnly()) {
    //   userIdSupplier = servicePointDto::getReceivedByUserId;
    // } else {
    //   userIdSupplier = () -> authenticationHelper.getCurrentUser().getId();
    // }

    // LazyResource<UUID> userId = new LazyResource<>(userIdSupplier);
    // context.setCurrentUserId(userId);

    // profiler.start("CREATE_LAZY_FACILITY");
    // UUID facilityId = servicePointDto.getFacilityId();
    // Supplier<FacilityDto> facilitySupplier = new ReferenceDataSupplier<>(
    //     facilityService, facilityId
    // );
    // LazyResource<FacilityDto> facility = new LazyResource<>(facilitySupplier);
    // context.setFacility(facility);

    return context;
  }

}
