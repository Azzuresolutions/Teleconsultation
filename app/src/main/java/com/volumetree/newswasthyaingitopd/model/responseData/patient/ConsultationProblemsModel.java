package com.volumetree.newswasthyaingitopd.model.responseData.patient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsultationProblemsModel {

    @SerializedName("consultationProblemId")
    @Expose
    private Long consultationProblemId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("consultationId")
    @Expose
    private Long consultationId;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("sourceId")
    @Expose
    private Long sourceId;

    /**
     * No args constructor for use in serialization
     *
     */
    public ConsultationProblemsModel() {
    }

    /**
     *
     * @param sourceId
     * @param code
     * @param createdDate
     * @param name
     * @param consultationId
     * @param isActive
     * @param consultationProblemId
     */
    public ConsultationProblemsModel(Long consultationProblemId, String name, String code, String createdDate, Long consultationId, Boolean isActive, Long sourceId) {
        super();
        this.consultationProblemId = consultationProblemId;
        this.name = name;
        this.code = code;
        this.createdDate = createdDate;
        this.consultationId = consultationId;
        this.isActive = isActive;
        this.sourceId = sourceId;
    }

    public Long getConsultationProblemId() {
        return consultationProblemId;
    }

    public void setConsultationProblemId(Long consultationProblemId) {
        this.consultationProblemId = consultationProblemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

}