package com.itheamc.meatprocessing.models.external;

public class Info {
    private String infoId;
    private String infoTitle;
    private String infoDesc;
    private String buttonText;

    // Empty Constructor
    public Info() {
    }

    // Constructors with parameters
    public Info(String infoId, String infoTitle, String infoDesc, String buttonText) {
        this.infoId = infoId;
        this.infoTitle = infoTitle;
        this.infoDesc = infoDesc;
        this.buttonText = buttonText;
    }

    // Getters and Setters
    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "info{" +
                "infoId='" + infoId + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                ", buttonText='" + buttonText + '\'' +
                '}';
    }
}
