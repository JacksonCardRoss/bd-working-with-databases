package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.exceptions.AlbumTrackNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AlbumTrackDaoTest {

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    private AlbumTrackDao albumTrackDao;



    @BeforeEach
    public void setUp() {
        initMocks(this);
        albumTrackDao = new AlbumTrackDao(dynamoDbMapper);
    }

    @Test
    public void getAlbumTrack_validAsinAndTrackNumber_returnsAlbumTrack() {
        // GIVEN
        String asin = "asin";
        Integer trackNumber = 0;
        AlbumTrack albumTrack = new AlbumTrack();
        albumTrack.setAsin(asin);
        albumTrack.setTrackNumber(trackNumber);

        when(dynamoDbMapper.load(AlbumTrack.class, asin, trackNumber)).thenReturn(albumTrack);

        // WHEN
        AlbumTrack returnedAlbumTrack = albumTrackDao.getAlbumTrack(asin, trackNumber);

        // THEN
        assertEquals(albumTrack, returnedAlbumTrack);
    }

    @Test
    public void getAlbumTrack_invalidAsinAndTrackNumber_returnsNull() {
        // GIVEN
        String invalidAsin = "invalidAsin";
        Integer invalidTrackNumber = 1000;

        when(dynamoDbMapper.load(AlbumTrack.class, invalidAsin, invalidTrackNumber)).thenReturn(null);

        // WHEN + THEN
        assertThrows(AlbumTrackNotFoundException.class, () -> {
            albumTrackDao.getAlbumTrack(invalidAsin, invalidTrackNumber);
        });
    }
}
