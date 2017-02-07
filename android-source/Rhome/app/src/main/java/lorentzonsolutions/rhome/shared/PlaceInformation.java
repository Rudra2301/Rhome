package lorentzonsolutions.rhome.shared;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Class representing a place parsed from JSON data collected from the Google Web API.
 */

public class PlaceInformation implements Comparable, Parcelable{
    public String name;
    public double latitude;
    public double longitude;
    public boolean isOpen;
    public String iconAddress;
    public String[] types;
    public String address;
    public String googlePlaceID;
    public double placeRating;
    public int distanceToStartLocation;
    public int minutesByCar;
    public int minutesByBicycle;
    public int minutesByWalk;

    // For calculation
    private int orderId;

    public void setOrderId(int id) {
        this.orderId = id;
    }
    public int getOrderId() {
        return this.orderId;
    }


    private PlaceInformation(BuildPlace builder) {
        this.name = builder.name;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.isOpen = builder.isOpen;
        this.iconAddress = builder.iconAddress;
        this.types = builder.types;
        this.address = builder.address;
        this.googlePlaceID = builder.googlePlaceID;
        this.placeRating = builder.placeRating;
        this.distanceToStartLocation = builder.distanceToStartLocation;
        this.minutesByCar = builder.minutesByCar;
        this.minutesByBicycle = builder.minutesByBicycle;
        this.minutesByWalk = builder.minutesByWalk;
    }

    // Used by the parcelable interface to recreate the object.
    public PlaceInformation(Parcel in) {

        this.name = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.isOpen = in.readByte() != 0;
        this.iconAddress = in.readString();
        this.types = in.createStringArray();
        this.address = in.readString();
        this.googlePlaceID = in.readString();
        this.placeRating = in.readDouble();
        this.distanceToStartLocation = in.readInt();
        this.minutesByCar = in.readInt();
        this.minutesByBicycle = in.readInt();
        this.minutesByWalk = in.readInt();
    }
    public static final Parcelable.Creator<PlaceInformation> CREATOR = new Parcelable.Creator<PlaceInformation>() {

        public PlaceInformation createFromParcel(Parcel in) {
            return new PlaceInformation(in);
        }

        public PlaceInformation[] newArray(int size) {
            return new PlaceInformation[size];
        }
    };


    @Override
    public int compareTo(Object o) {
        PlaceInformation obj = (PlaceInformation) o;
        return this.distanceToStartLocation - obj.distanceToStartLocation;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isOpen ? 1 : 0));
        dest.writeString(iconAddress);
        dest.writeStringArray(types);
        dest.writeString(address);
        dest.writeString(googlePlaceID);
        dest.writeDouble(placeRating);
        dest.writeInt(distanceToStartLocation);
        dest.writeInt(minutesByCar);
        dest.writeInt(minutesByBicycle);
        dest.writeInt(minutesByWalk);
    }



    public static class BuildPlace {
        private String name;
        private double latitude;
        private double longitude;
        private boolean isOpen;
        private String iconAddress;
        private String[] types;
        private String address;
        private String googlePlaceID;
        private double placeRating;
        private int distanceToStartLocation;
        private int minutesByCar;
        private int minutesByBicycle;
        private int minutesByWalk;

        public BuildPlace(String name, double builderLatitude, double builderLongitude) {
            this.name = name;
            this.latitude = builderLatitude;
            this.longitude = builderLongitude;
        }

        public BuildPlace isOpen(boolean openBoolean) {
            this.isOpen = openBoolean;
            return this;
        }

        public BuildPlace iconAddress(String iconAddress) {
            this.iconAddress = iconAddress;
            return this;
        }

        public BuildPlace types(String[] types) {
            this.types = types;
            return this;
        }

        public BuildPlace address(String address) {
            this.address = address;
            return this;
        }

        public BuildPlace googlePlaceID(String googlePlaceID) {
            this.googlePlaceID = googlePlaceID;
            return this;
        }
        public BuildPlace placeRating(double rating) {
            this.placeRating = rating;
            return this;
        }
        public BuildPlace distanceToStartLocation(int distanceToStartLocation) {
            this.distanceToStartLocation = distanceToStartLocation;
            return this;
        }
        public BuildPlace minutesByCar(int minutes) {
            this.minutesByCar = minutes;
            return this;
        }
        public BuildPlace minutesByBicycle(int minutes) {
            this.minutesByBicycle = minutes;
            return this;
        }
        public BuildPlace minutesByWalk(int minutes) {
            this.minutesByWalk = minutes;
            return this;
        }
        public PlaceInformation build() {
            return new PlaceInformation(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Place name: " + name + "\n");
        sb.append("Place address: " + address + "\n");
        sb.append("Place latitude: " + latitude + "\n");
        sb.append("Place longitude: " + longitude + "\n");
        sb.append("Place is open: " + isOpen + "\n");

        sb.append("Place types: [ ");
        for(int i = 0; i < types.length; i++) {
            sb.append(types[i]);
            if(i != types.length - 1) sb.append(", ");
        }
        sb.append(" ]\n");

        sb.append("Place GoogleID: " + googlePlaceID + "\n");
        sb.append("Place rating: " + placeRating + "\n");

        return sb.toString();
    }

    // Always override hashCode() when Overriding equals()
    @Override
    public int hashCode() {
        int result = 17;
        
        if(name != null) result = 31 * result + name.length();
        if(latitude != 0) result = 31 * result + (int) latitude;
        if(longitude != 0) result = 31 * result + (int) longitude;
        if(googlePlaceID != null) result = 31 * result + googlePlaceID.length();

        return result;
    }


    // Overriding because of object creation when fetching nearby places.
    // Only value
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof PlaceInformation)) return false;

        PlaceInformation test = (PlaceInformation) o;

        if(test.googlePlaceID == null || this.googlePlaceID == null) return false;
        return test.googlePlaceID.equals(this.googlePlaceID);

    }

}
