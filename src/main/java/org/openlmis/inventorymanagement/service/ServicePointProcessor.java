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

import java.util.UUID;

import org.openlmis.inventorymanagement.domain.event.ServicePoint;
import org.openlmis.inventorymanagement.dto.ServicePointDto;
import org.openlmis.inventorymanagement.repository.ServicePointsRepository;
import org.openlmis.inventorymanagement.util.ServicePointProcessContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service that is in charge of saving point of delivery events
 * pod events.
 */
@Service
public class ServicePointProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(
          ServicePointProcessor.class);
  private static final XLogger XLOGGER = XLoggerFactory.getXLogger(
          ServicePointProcessor.class);

  @Autowired
  private ServicePointProcessContextBuilder contextBuilder;

  @Autowired
  private ServicePointsRepository servicePointsRepository;

  /**
   * Validate and persist pod event.
   *
   * @param servicePointDto point of delivery event dto.
   * @return the persisted event ids.
   */
  public UUID process(ServicePointDto servicePointDto) {
    XLOGGER.entry(servicePointDto);
    Profiler profiler = new Profiler("PROCESS");
    profiler.setLogger(XLOGGER);

    profiler.start("BUILD_CONTEXT");
    ServicePointProcessContext context = contextBuilder.buildContext(
            servicePointDto);
    servicePointDto.setContext(context);

    //to do validations

    UUID eventId = saveEventAndGenerateLineItems(
        servicePointDto, profiler.startNested("SAVE_AND_GENERATE_LINE_ITEMS")
    );

    return eventId;
  }

  private UUID saveEventAndGenerateLineItems(ServicePointDto servicePointDto,
                                             Profiler profiler) {
    profiler.start("CONVERT_TO_EVENT");
    ServicePoint servicePoint = servicePointDto
            .toServicePoint();

    profiler.start("DB_SAVE");
    UUID savedEventId = servicePointsRepository.save(
            servicePoint).getId();
    LOGGER.debug("Saved point of delivery event with id " + savedEventId);

    return savedEventId;
  }

}
