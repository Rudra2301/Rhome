package lorentzonsolutions.rhome.shared;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class representing a place parsed from JSON data collected from the Google Web API.
 */

public class GooglePlaceInformation implements Comparable, Parcelable{
    public String name;
    public double latitude;
    public double longitude;
    public boolean isOpen;
    public String iconAddress;
    public String[] types;
    public String address;
    public String googlePlaceID;
    public double placeRating;
    //Start location
    public int distanceToStartLocation;
    public int minutesByCarToStartLocation;
    public int minutesByBicycleToStartLocation;
    public int minutesByWalkToStartLocation;
    // End location
    public int distanceToEndLocation;
    public int minutesByCarToEndLocation;
    public int minutesByBicycleToEndLocation;
    public int minutesByWalkToEndLocation;

    // For calculation
    private int orderId;

    public boolean visited;


    private GooglePlaceInformation(BuildPlace builder) {
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
        this.minutesByCarToStartLocation = builder.minutesByCarToStartLocation;
        this.minutesByBicycleToStartLocation = builder.minutesByBicycleToStartLocation;
        this.minutesByWalkToStartLocation = builder.minutesByWalkToStartLocation;

        this.distanceToEndLocation = builder.distanceToEndLocation;
        this.minutesByCarToEndLocation = builder.minutesByCarToEndLocation;
        this.minutesByBicycleToEndLocation = builder.minutesByBicycleToEndLocation;
        this.minutesByWalkToEndLocation = builder.minutesByWalkToEndLocation;

        this.visited = false;
    }

    // Used by the parcelable interface to recreate the object.
    public GooglePlaceInformation(Parcel in) {

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
        this.minutesByCarToStartLocation = in.readInt();
        this.minutesByBicycleToStartLocation = in.readInt();
        this.minutesByWalkToStartLocation = in.readInt();
        this.distanceToEndLocation = in.readInt();
        this.minutesByCarToEndLocation = in.readInt();
        this.minutesByBicycleToEndLocation = in.readInt();
        this.minutesByWalkToEndLocation = in.readInt();
    }
    public static final Parcelable.Creator<GooglePlaceInformation> CREATOR = new Parcelable.Creator<GooglePlaceInformation>() {

        public GooglePlaceInformation createFromParcel(Parcel in) {
            return new GooglePlaceInformation(in);
        }

        public GooglePlaceInformation[] newArray(int size) {
            return new GooglePlaceInformation[size];
        }
    };


    @Override
    public int compareTo(Object o) {
        GooglePlaceInformation obj = (GooglePlaceInformation) o;
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
        dest.writeInt(minutesByCarToStartLocation);
        dest.writeInt(minutesByBicycleToStartLocation);
        dest.writeInt(minutesByWalkToStartLocation);
        dest.writeInt(distanceToEndLocation);
        dest.writeInt(minutesByCarToEndLocation);
        dest.writeInt(minutesByBicycleToEndLocation);
        dest.writeInt(minutesByWalkToEndLocation);
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
        private int minutesByCarToStartLocation;
        private int minutesByBicycleToStartLocation;
        private int minutesByWalkToStartLocation;
        private int distanceToEndLocation;
        private int minutesByCarToEndLocation;
        private int minutesByBicycleToEndLocation;
        private int minutesByWalkToEndLocation;

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
        public BuildPlace minutesByCarToStartLocation(int minutes) {
            this.minutesByCarToStartLocation = minutes;
            return this;
        }
        public BuildPlace minutesByBicycleToStartLocation(int minutes) {
            this.minutesByBicycleToStartLocation = minutes;
            return this;
        }
        public BuildPlace minutesByWalkToStartLocation(int minutes) {
            this.minutesByWalkToStartLocation = minutes;
            return this;
        }

        public BuildPlace distanceToEndLocation(int distanceToEndLocation) {
            this.distanceToEndLocation = distanceToEndLocation;
            return this;
        }
        public BuildPlace minutesByCarToEndLocation(int minutes) {
            this.minutesByCarToEndLocation = minutes;
            return this;
        }
        public BuildPlace minutesByBicycleToEndLocation(int minutes) {
            this.minutesByBicycleToEndLocation = minutes;
            return this;
        }
        public BuildPlace minutesByWalkToEndLocation(int minutes) {
            this.minutesByWalkToEndLocation = minutes;
            return this;
        }
        public GooglePlaceInformation build() {
            return new GooglePlaceInformation(this);
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
        if(types != null) {
            for (int i = 0; i < types.length; i++) {
                sb.append(types[i]);
                if (i != types.length - 1) sb.append(", ");
            }
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
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof GooglePlaceInformation)) return false;

        GooglePlaceInformation test = (GooglePlaceInformation) o;

        if(test.googlePlaceID == null || this.googlePlaceID == null) return false;
        return test.googlePlaceID.equals(this.googlePlaceID);

    }

}
