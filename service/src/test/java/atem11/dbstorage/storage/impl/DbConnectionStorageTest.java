package atem11.dbstorage.storage.impl;

import atem11.dbstorage.model.DbConfig;
import atem11.dbstorage.model.DbConnection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DbConnectionStorageTest {

    private DbConnectionStorage storage;
    private final DbConnection testConnection1 = new DbConnection(
            "url1",
            "username",
            "passwd"
    );
    private final DbConnection testConnection2 = new DbConnection(
            "url2",
            "username",
            "passwd"
    );

    @Before
    public void setUp() {
        storage = new DbConnectionStorage(
                new DbConfig(
                        "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false",
                        "test",
                        "",
                        2
                )
        );
    }

    @Test
    public void saveAndGetTest() {
        storage.saveConnection("1", testConnection1);
        storage.saveConnection("2", testConnection1);
        storage.saveConnection("3", testConnection1);
        storage.saveConnection("3", testConnection2);
        assertEquals(testConnection1, storage.getConnection("1"));
        assertEquals(testConnection1, storage.getConnection("2"));
        assertEquals(testConnection1, storage.getConnection("3"));
    }

    @Test
    public void saveRemoveAndGetTest() {
        storage.saveConnection("1", testConnection1);
        storage.saveConnection("2", testConnection1);
        storage.saveConnection("3", testConnection1);
        assertEquals(testConnection1, storage.getConnection("1"));
        assertEquals(testConnection1, storage.getConnection("2"));
        assertEquals(testConnection1, storage.getConnection("3"));
        storage.removeConnection("2");
        storage.saveConnection("2", testConnection2);
        assertEquals(testConnection1, storage.getConnection("1"));
        assertEquals(testConnection2, storage.getConnection("2"));
        assertEquals(testConnection1, storage.getConnection("3"));
    }

}