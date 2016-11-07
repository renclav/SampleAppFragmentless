package com.touchnote.renclav.touchnotesampleapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renclav on 2016/11/04.
 */

public class Clue implements Parcelable {

    private String id;
    private String description;
    private String date;
    private List<String> tags = new ArrayList<String>();
    private String title;
    private String image;

    //NOTE: This kind of logic should exist in model-view
    private int morphStep = 0;

    private Clue() {
        //Leave empty for Moshi
    }

    protected Clue(Parcel in) {
        id = in.readString();
        description = in.readString();
        date = in.readString();
        tags = new ArrayList<>();
        if (in.readByte() == 0x01) {
            in.readList(tags, String.class.getClassLoader());
        }
        title = in.readString();
        image = in.readString();
        morphStep = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(date);
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeString(title);
        dest.writeString(image);
        dest.writeInt(morphStep);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Clue> CREATOR = new Parcelable.Creator<Clue>() {
        @Override
        public Clue createFromParcel(Parcel in) {
            return new Clue(in);
        }

        @Override
        public Clue[] newArray(int size) {
            return new Clue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void copyFrom(Clue clue)
    {
        this.id = clue.id;
        this.description = clue.description;
        this.date = clue.date;
        this.tags = new ArrayList<>();
        if(clue.tags != null)
        {
            this.tags.addAll(clue.tags);
        }
        this.title = clue.title;
        this.image = clue.image;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    public int getMorphStep() {
        return morphStep;
    }

    public void setMorphStep(int morphStep) {
        this.morphStep = morphStep;
    }
}
