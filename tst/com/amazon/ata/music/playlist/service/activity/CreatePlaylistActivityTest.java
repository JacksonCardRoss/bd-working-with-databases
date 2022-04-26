package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Playlist playlist = new Playlist();
        List<AlbumTrack> emptySongList = new ArrayList<>();
        Set<String> tags = new HashSet<>();
        playlist.setCustomerId(expectedCustomerId);
        playlist.setName(expectedName);
        playlist.setId(expectedId);
        playlist.setSongList(emptySongList);
        playlist.setSongCount(0);
        playlist.setTags(tags);

        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                        .withName(expectedName)
                        .withCustomerId(expectedCustomerId)
                        .withTags(expectedTags)
                        .build();

        when(playlistDao.savePlaylist(any(Playlist.class))).thenReturn(playlist);

        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request, null);

        // THEN
        assertEquals(expectedId, result.getPlaylist().getId());
        assertEquals(expectedName, result.getPlaylist().getName());
        assertEquals(expectedCustomerId, result.getPlaylist().getCustomerId());
        assertEquals(expectedTags, result.getPlaylist().getTags());

    }

}
