/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.saga.demo.pack.hotel;

import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HotelBookingService {
  private ConcurrentHashMap<Integer, HotelBooking> bookings = new ConcurrentHashMap<>();

  @Compensable(compensationMethod = "cancel")
  public void order(HotelBooking booking) {
    if (booking.getRooms() > 2) {
      throw new IllegalArgumentException("can not order the rooms large than two");
    }
    booking.setConfirm(true);
    booking.setCancel(false);
    bookings.put(booking.getId(), booking);
  }

  public void cancel(HotelBooking booking) {
    Integer id = booking.getId();
    if (bookings.containsKey(id)) {
      HotelBooking origin = bookings.get(id);
      origin.setConfirm(false);
      origin.setCancel(true);
    }
  }

  public Collection<HotelBooking> getAllBookings() {
    return bookings.values();
  }
}