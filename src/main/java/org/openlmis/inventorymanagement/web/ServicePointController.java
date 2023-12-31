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

package org.openlmis.inventorymanagement.web;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.UUID;
import org.openlmis.inventorymanagement.dto.ServicePointDto;
// import org.openlmis.inventorymanagement.service.PermissionService;
import org.openlmis.inventorymanagement.service.ServicePointProcessor;
import org.openlmis.inventorymanagement.service.ServicePointService;
import org.openlmis.inventorymanagement.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller used to perform CRUD operations on service point.
 */
@Controller
@RequestMapping("/api/servicePoints")
public class ServicePointController extends BaseController {
  public static final String ID_PATH_VARIABLE = "/{id}";
  private static final Logger LOGGER = LoggerFactory.getLogger(ServicePointController.class);

  //   @Autowired
  //   private PermissionService permissionService;

  @Autowired
  private ServicePointProcessor servicePointProcessor;

  @Autowired
  private ServicePointService servicePointService;

  /**
   * Create service point.
   *
   * @param servicePointDto a pod event bound to request body.
   * @return created pod event's ID.
   */
  @Transactional
  @RequestMapping(method = POST)
  public ResponseEntity<UUID> createServicePoint(
        @RequestBody ServicePointDto servicePointDto) {

    LOGGER.debug("Try to create a service point");

    Profiler profiler = getProfiler("CREATE_SERVICE_POINT", servicePointDto);

    //checkPermission(ServicePointDto, profiler.startNested("CHECK_PERMISSION"));

    profiler.start("PROCESS");
    UUID createdPodId = servicePointProcessor.process(servicePointDto);

    profiler.start("CREATE_RESPONSE");
    ResponseEntity<UUID> response = new ResponseEntity<>(createdPodId, CREATED);

    return stopProfiler(profiler, response);
  }

  /**
   * List service point.
   *
   * @param facilityId a destination facility id.
   * @return List of pod events.
   */
  @RequestMapping(method = GET)
  public ResponseEntity<List<ServicePointDto>> getServicePoints(
      @RequestParam() UUID facilityId) {

    LOGGER.debug("Try to load service points");

    List<ServicePointDto> podsToReturn = 
        servicePointService.getServicePointsByFacilityId(facilityId);
    
    return new ResponseEntity<>(podsToReturn, OK);
    // Profiler profiler = getProfiler("LIST_SERVICE_POINTS", ServicePointDto);

    //checkPermission(ServicePointDto, profiler.startNested("CHECK_PERMISSION"));

    // profiler.start("PROCESS");
    // UUID createdPodId = servicePointProcessor.process(ServicePointDto);

    // profiler.start("CREATE_RESPONSE");
    // ResponseEntity<UUID> response = new ResponseEntity<>(createdPodId, CREATED);

    //return stopProfiler(profiler, response);
  }

  /**
   * Update a POD event.
   *
   * @param id POD event id.
   * @param dto POD dto.
   * @return created POD dto.
   */
  @Transactional
  @PutMapping(ID_PATH_VARIABLE)
  @ResponseStatus(OK)
  @ResponseBody
  public ResponseEntity<ServicePointDto> updateServicePoint(@PathVariable UUID id,
                                                    @RequestBody ServicePointDto dto) {
    ServicePointDto updatedPodEvent = servicePointService
        .updateServicePoint(dto, id);
    return new ResponseEntity<>(updatedPodEvent, OK);
  }

  /**
   * Delete a POD event.
   *
   * @param id POD event id.
   */
  @DeleteMapping(ID_PATH_VARIABLE)
  @ResponseStatus(NO_CONTENT)
  public void deleteServicePoint(@PathVariable UUID id) {
    servicePointService.deleteServicePoint(id);
  }

}
