package materialtest.example.user.materialtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Plants implements Parcelable{

    private Long id;
    private String genus;
    private String species;
    private String cultivar;
    private String common;

    public Plants(){

    }

    public Plants(Parcel input){
        id = input.readLong();
        genus = input.readString();
        species = input.readString();
        cultivar = input.readString();
        common = input.readString();
    }

    public Plants (long id, String genus, String species, String cultivar, String common){
        this.id = id;
        this.genus = genus;
        this.species = species;
        this.cultivar = cultivar;
        this.common = common;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getCultivar() {
        return cultivar;
    }

    public void setCultivar(String cultivar) {
        this.cultivar = cultivar;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String toString(){
        return"ID"+id+"GENUS"+genus;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(genus);
        dest.writeString(species);
        dest.writeString(cultivar);
        dest.writeString(common);
    }

    public static final Creator<Plants> CREATOR = new Creator<Plants>(){
        public  Plants createFromParcel(Parcel in){
            return new Plants(in);
        }

        public Plants[] newArray(int size){
            return new Plants[size];
        }
    };
}
