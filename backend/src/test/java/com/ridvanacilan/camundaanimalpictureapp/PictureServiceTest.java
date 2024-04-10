package com.ridvanacilan.camundaanimalpictureapp;

import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;
import com.ridvanacilan.camundaanimalpictureapp.Repositories.PictureRepository;
import com.ridvanacilan.camundaanimalpictureapp.Service.PictureService;

import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PictureServiceTest extends CamundaAnimalPictureAppApplicationTests {

    @InjectMocks
    private PictureService pictureService;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private RestClient restClient;

    @Test
    public void testGetAnimalPicture() {
        String processInstanceKey = "testKey";
        Picture picture = new Picture();
        picture.setImage(new Binary(new byte[] { 1, 2, 3 }));
        when(pictureRepository.findById(processInstanceKey)).thenReturn(Optional.of(picture));

        byte[] result = pictureService.getAnimalPicture(processInstanceKey);

        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
    }

    @Test
    public void getNonExistingAnimalPicture() {
        String processInstanceKey = "testKey";
        when(pictureRepository.findById(processInstanceKey)).thenReturn(Optional.empty());

        byte[] result = pictureService.getAnimalPicture(processInstanceKey);

        assertNull(result);
    }

    @Test
    public void downloadAndSaveRandomAnimalPicture() {
        String processInstanceKey = "testKey";
        String animalType = "dog";

        pictureService.downloadAndSaveRandomAnimalPicture(processInstanceKey, animalType);

        verify(pictureRepository, times(1)).save(any(Picture.class));
    }

    @Test
    public void invalidAnimalTypeThrowsIllegalArgumentException() {
        String processInstance = "testKey";
        String animalType = "invalid";
        byte[] imageBytes = new byte[] { 1, 2, 3 };
        
        assertThrows(IllegalArgumentException.class, () -> {
            pictureService.downloadAndSaveRandomAnimalPicture(processInstance, animalType);
        });
    }

}