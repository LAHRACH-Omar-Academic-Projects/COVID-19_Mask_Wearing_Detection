package app.models;

public class Person {
    private int id;
    private String photo;
    private int numberOfDetections;

    public Person() {

    }

    public Person(int id, String photo, int numberOfDetections) {
        this.id = id;
        this.photo = photo;
        this.numberOfDetections = numberOfDetections;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getNumberOfDetections() {
        return numberOfDetections;
    }

    public void setNumberOfDetections(int numberOfDetections) {
        this.numberOfDetections = numberOfDetections;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", photo='" + photo + '\'' +
                ", numberOfDetections=" + numberOfDetections +
                '}';
    }
}
