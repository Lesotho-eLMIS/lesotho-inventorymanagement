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


package org.openlmis.inventorymanagement.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.openlmis.inventorymanagement.domain.event.ServicePoint;
import org.openlmis.inventorymanagement.util.ServicePointProcessContext;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePointDto {

  private UUID id;

  private String name;

  private UUID facilityId;

  private ServicePointProcessContext context;

  /**
   * Convert dto to jpa model.
   *
   * @return the converted jpa model object.
   */
  public ServicePoint toServicePoint() {
    ServicePoint servicePoint = new ServicePoint(
        name, facilityId);
    return servicePoint;
  }

  /**
   * Create from jpa model.
   *
   * @param servicePoints inventory jpa model.
   * @return created dto.
   */
  public static List<ServicePointDto> servicePointToDto(
        Collection<ServicePoint> servicePoints) {

    List<ServicePointDto> servicePointDtos = new ArrayList<>(servicePoints.size());
    servicePoints.forEach(i -> servicePointDtos.add(servicePointToDto(i)));
    return servicePointDtos;
  }

  /**
   * Create from jpa model.
   *
   * @param servicePoint inventory jpa model.
   * @return created dto.
   */
  public static ServicePointDto servicePointToDto(ServicePoint servicePoint) {
    return ServicePointDto.builder()
      .id(servicePoint.getId())
      .name(servicePoint.getName())
      .facilityId(servicePoint.getFacilityId())
      .build();
  }


}
