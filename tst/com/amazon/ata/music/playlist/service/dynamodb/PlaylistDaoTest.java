package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PlaylistDaoTest {

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    private PlaylistDao playlistDao;



    @BeforeEach
    public void setUp() {
        initMocks(this);
        playlistDao = new PlaylistDao(dynamoDbMapper);
    }

    @Test
    public void getPlaylist_validPlaylistId_returnsPlaylist() {
        // GIVEN
        String playlistId = "validId";
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);

        when(dynamoDbMapper.load(Playlist.class, playlistId)).thenReturn(playlist);

        // WHEN
        Playlist returnedPlaylist = playlistDao.getPlaylist(playlistId);

        // THEN
        assertEquals(playlist, returnedPlaylist);
    }

    @Test
    public void getPlaylist_invalidPlaylistId_returnsNull() {
        // GIVEN
        String invalidPlaylistId = "invalidId";

        when(dynamoDbMapper.load(Playlist.class, invalidPlaylistId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(PlaylistNotFoundException.class, () -> {
            playlistDao.getPlaylist(invalidPlaylistId);
        });
    }

    @Test
    public void savePlaylist_validPlaylist_returnsPlaylistId() {
        // GIVEN
        Playlist playlist = new Playlist();
        String validCustomerId = "validId";
        String validName = "validName";
        playlist.setName(validName);
        playlist.setCustomerId(validCustomerId);

        // WHEN
        Playlist returnedPlaylist = playlistDao.savePlaylist(playlist);

        // THEN
        assertEquals(5, returnedPlaylist.getId().length(),
                "Expected an Id to be generated and to be 5 characters");
    }

    @Test
    public void savePlaylist_invalidPlaylist_returnsPlaylistId() {
        // GIVEN
        Playlist playlist = new Playlist();
        String validCustomerId = "invalid' Id";
        String invalidName = "invalid' name";
        playlist.setName(invalidName);
        playlist.setCustomerId(validCustomerId);

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> {
            playlistDao.savePlaylist(playlist);
        } );
    }
}
