package atem11.test.storage.impl;

import atem11.test.model.DbConnection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemConnectionStorageTest {

    private InMemConnectionStorage storage;
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
        storage = new InMemConnectionStorage();
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