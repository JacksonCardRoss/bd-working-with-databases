package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CreatePlaylistActivityTest {
    @Mock
    private PlaylistDao playlistDao;

    private CreatePlaylistActivity createPlaylistActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        createPlaylistActivity = new CreatePlaylistActivity(playlistDao);
    }


    @Test
    public void handleRequest_validPlaylist_savesPlaylist() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        String expectedId = "expectedId";
        List<String> expectedTags = Lists.newArrayList("tag");

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                        .withName(expectedName)
                        .withCustomerId(expectedCustomerId)
                        .withTags(expectedTags)
                        .build();

        when(playlistDao.savePlaylist(any(Playlist.class))).thenReturn(expectedId);

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request, null);

        // THEN
        assertEquals(expectedId, result.getPlaylist().getId());
        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedTags, result.getPlaylist().getTags());

    }

}
