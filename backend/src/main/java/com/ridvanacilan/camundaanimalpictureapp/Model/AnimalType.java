package com.ridvanacilan.camundaanimalpictureapp.Model;

public enum AnimalType {
    CAT("https://placekitten.com/"),
    DOG("https://place.dog/"),
    BEAR("https://placebear.com/");

    private final String url;

    AnimalType(String url) {
        this.url = url;
    }

    public String getUrl(int width, int height) {
        return url + width + "/" + height;
    }

    public static AnimalType fromString(String animalType) {
        return AnimalType.valueOf(animalType.toUpperCase());
    }
}
