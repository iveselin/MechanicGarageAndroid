package hr.ferit.iveselin.mechanicgarageandroid.model;

import hr.ferit.iveselin.mechanicgarageandroid.utils.StringUtils;

public class AppointmentRequest {

    private String[] requestType;
    private String extraNote;

    public AppointmentRequest() {
    }

    public AppointmentRequest(String[] requestType, String extraNote) {
        this.requestType = requestType;
        this.extraNote = extraNote;
    }

    public String[] getRequestType() {
        return requestType;
    }

    public void setRequestType(String[] requestType) {
        this.requestType = requestType;
    }

    public String getExtraNote() {
        return StringUtils.getValueOrEmpty(extraNote);
    }

    public void setExtraNote(String extraNote) {
        this.extraNote = extraNote;
    }

    @Override
    public String toString() {
        String requests = "";
        for (String s : requestType) {
            requests.concat(s + ", ");
        }
        return "AppointmentRequest{" +
                "requestType='" + requests +'\'' +
                ", extraNote='" + extraNote + '\'' +
                '}';
    }
}
