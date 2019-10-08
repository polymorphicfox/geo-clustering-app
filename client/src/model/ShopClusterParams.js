export default class ShopClusterParams {
    constructor(lat1, lon1, lat2, lon2, zoomLevel, categoryId, partnerId, halal){
        this.lat1=lat1;
        this.lon1=lon1;
        this.lat2=lat2;
        this.lon2=lon2;
        this.zoomLevel = zoomLevel;
        this.categoryId = categoryId;
        this.partnerId=partnerId;
        this.halal=halal;
    }

}