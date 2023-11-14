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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.openlmis.inventorymanagement.domain.event.ServicePoint;
import org.openlmis.inventorymanagement.dto.ServicePointDto;
import org.openlmis.inventorymanagement.exception.ResourceNotFoundException;
import org.openlmis.inventorymanagement.repository.ServicePointsRepository;
import org.openlmis.inventorymanagement.util.Message;
import org.openlmis.inventorymanagement.util.ServicePointProcessContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicePointService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServicePointService.class);

  @Autowired
  private ServicePointsRepository servicePointsRepository;

  @Autowired
  private ServicePointProcessContextBuilder contextBuilder;

  /**
   * Get a list of Point of Delivery events.
   *
   * @param destinationId destination id.
   * @return a list of pod events.
   */
  public List<ServicePointDto> getServicePointsByDestinationId(UUID destinationId) {
    List<ServicePoint> servicePoints = servicePointsRepository
        .findByDestinationId(destinationId);
    
    if (servicePoints == null) {
      return Collections.emptyList();
    }
    return ServicePointDto.podToDto(servicePoints);
  }

  /**
   * Save or update POD.
   *
   * @param dto POD event dto.
   * @return the saved POD event.
   */
  public ServicePointDto updateServicePoint(ServicePointDto dto, UUID id) {
    //LOGGER.info("update POS event");
    //physicalInventoryValidator.validateDraft(dto, id);
    //checkPermission(dto.getProgramId(), dto.getFacilityId());

    //checkIfDraftExists(dto, id);
    
    LOGGER.info("Attempting to fetch pod event with id = " + id);
    Optional<ServicePoint> existingPodEventOpt = 
        servicePointsRepository.findById(id);

    if (existingPodEventOpt.isPresent()) {
      ServicePoint existingPodEvent = existingPodEventOpt.get();
      ServicePointProcessContext context = contextBuilder.buildContext(dto);
      dto.setContext(context);
      ServicePoint incomingPodEvent = dto.toServicePoint();

      // Update the Existing PodEvent object with values incoming DTO data
      existingPodEvent = copyAttributes(existingPodEvent, incomingPodEvent);
    
      //save updated pod event
      servicePointsRepository.save(existingPodEvent);
      return ServicePointDto.podToDto(existingPodEvent);
    } else {
      return null;
    }
  }

  private ServicePoint copyAttributes(
      ServicePoint existingPodEvent, ServicePoint incomingPodEvent) {
    if (incomingPodEvent.getSourceId() != null) {
      existingPodEvent.setSourceId(incomingPodEvent.getSourceId());
    }
    if (incomingPodEvent.getSourceFreeText() != null) {
      existingPodEvent.setSourceFreeText(incomingPodEvent.getSourceFreeText());
    }
    if (incomingPodEvent.getDestinationId() != null) {
      existingPodEvent.setDestinationId(incomingPodEvent.getDestinationId());
    }
    if (incomingPodEvent.getDestinationFreeText() != null) {
      existingPodEvent.setDestinationFreeText(incomingPodEvent.getDestinationFreeText());
    }
    if (incomingPodEvent.getReferenceNumber() != null) {
      existingPodEvent.setReferenceNumber(incomingPodEvent.getReferenceNumber());
    }
    if (incomingPodEvent.getPackingDate() != null) {
      existingPodEvent.setPackingDate(incomingPodEvent.getPackingDate());
    }
    if (incomingPodEvent.getPackedBy() != null) {
      existingPodEvent.setPackedBy(incomingPodEvent.getPackedBy());
    }
    if (incomingPodEvent.getNumberOfCartons() != null) {
      existingPodEvent.setNumberOfCartons(incomingPodEvent.getNumberOfCartons());
    }
    if (incomingPodEvent.getNumberOfContainers() != null) {
      existingPodEvent.setNumberOfContainers(incomingPodEvent.getNumberOfContainers());
    }
    if (incomingPodEvent.getRemarks() != null) {
      existingPodEvent.setRemarks(incomingPodEvent.getRemarks());
    }
    return existingPodEvent;
  }

  /**
   * Delete POD.
   *
   * @param id POD event id.
   */
  public void deleteServicePoint(UUID id) {
    //LOGGER.info("update POS event");
    //physicalInventoryValidator.validateDraft(dto, id);
    //checkPermission(dto.getProgramId(), dto.getFacilityId());

    //checkIfDraftExists(dto, id);
    
    LOGGER.info("Attempting to fetch pod event with id = " + id);
    Optional<ServicePoint> existingPodEventOpt = 
        servicePointsRepository.findById(id);

    if (existingPodEventOpt.isPresent()) {
      //delete pod event
      servicePointsRepository.delete(existingPodEventOpt.get());
    } else {
      throw new ResourceNotFoundException(new Message("Point of delivery event not found ", id));
    }
  }
}
