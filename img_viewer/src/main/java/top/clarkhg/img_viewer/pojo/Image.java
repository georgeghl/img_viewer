package top.clarkhg.img_viewer.pojo;

public class Image implements java.io.Serializable{
    private int imageId;
    private String imageName;
    private String description;
    private int typeId;
    private String typeName;
    private int public_;
    private int categoryId;
    private String categoryName;
    private String originalFilename;
    private String gps;

    //Depreciated
    private String ownerUsername;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPublic_() {
        return public_;
    }

    public void setPublic_(int public_) {
        this.public_ = public_;
    }
    

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



    @Override
    public String toString() {
        return "Image [imageId=" + imageId + ", imageName=" + imageName + ", description=" + description + ", typeId="
                + typeId + ", typeName=" + typeName + ", public_=" + public_ + ", categoryId=" + categoryId
                + ", categoryName=" + categoryName + ", originalFilename=" + originalFilename + ", gps=" + gps
                + ", ownerUsername=" + ownerUsername + "]";
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}
