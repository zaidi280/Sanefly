package com.backend.sanfely.common.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class GeoUtilsTest {

    @Test
    void distanceKm_returnsZeroForSamePoint() {
        BigDecimal lat = new BigDecimal("36.8065");
        BigDecimal lon = new BigDecimal("10.1815");

        double distance = GeoUtils.distanceKm(lat, lon, lat, lon);

        assertThat(distance).isCloseTo(0.0, within(0.001));
    }

    @Test
    void distanceKm_calculatesKnownDistanceBetweenTunisAndAriana() {
        // Tunis center
        BigDecimal tunisLat = new BigDecimal("36.8065");
        BigDecimal tunisLon = new BigDecimal("10.1815");
        // Ariana center
        BigDecimal arianaLat = new BigDecimal("36.8625");
        BigDecimal arianaLon = new BigDecimal("10.1956");

        double distance = GeoUtils.distanceKm(tunisLat, tunisLon, arianaLat, arianaLon);

        // Real-world distance is roughly 6-7km - allow a reasonable margin
        assertThat(distance).isBetween(5.0, 8.0);
    }

    @Test
    void distanceKm_isSymmetric() {
        BigDecimal pointA_lat = new BigDecimal("36.8065");
        BigDecimal pointA_lon = new BigDecimal("10.1815");
        BigDecimal pointB_lat = new BigDecimal("36.8625");
        BigDecimal pointB_lon = new BigDecimal("10.1956");

        double aToB = GeoUtils.distanceKm(pointA_lat, pointA_lon, pointB_lat, pointB_lon);
        double bToA = GeoUtils.distanceKm(pointB_lat, pointB_lon, pointA_lat, pointA_lon);

        assertThat(aToB).isCloseTo(bToA, within(0.0001));
    }
}