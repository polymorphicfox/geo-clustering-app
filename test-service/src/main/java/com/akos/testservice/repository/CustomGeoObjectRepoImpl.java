package com.akos.testservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;

public class CustomGeoObjectRepoImpl implements CustomGeoObjectRepo{

    private static final String SEARCH_OBJECTS_QUERY =
            "SELECT o.id, o.latitude, o.longitude, o.quadkey, o.name FROM geo_objects o " +
                    " WHERE" +
                    "  o.quadkey ~~ ANY (string_to_array(?, ','))";

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Override
    public List<Object[]> findAllObjectsInTiles(String quadkeys) {
        return jdbcTemplate.query(SEARCH_OBJECTS_QUERY,
                new Object[]{quadkeys},
                new int[]{Types.VARCHAR},
                (rs, rowNum) -> {
                    Object[] objects = new Object[5];
                    objects[0] = rs.getString("id");
                    objects[1] = rs.getDouble("latitude");
                    objects[2] = rs.getDouble("longitude");
                    objects[3] = rs.getString("quadkey");
                    objects[4] = rs.getString("name");
                    return objects;
                });
    }
}
