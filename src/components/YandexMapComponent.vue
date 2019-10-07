<template>
    <v-container>
        <v-layout
                text-center
                wrap
        >
            <v-flex offset-xs1 xs10>
                <div id="map" style="width: auto; height: 500px"></div>
            </v-flex>
            <v-flex xs6>
                <v-text-field
                        :value="leftBottom"
                        readonly
                        label="Left bottom corner:">
                </v-text-field>
            </v-flex>
            <v-flex xs6>
                <v-text-field
                        :value="rightTop"
                        readonly
                        label="Right top corner:">
                </v-text-field>
            </v-flex>
            <v-flex xs3>
                <v-text-field
                        :value="zoomLevel"
                        label="Zoom level:">
                </v-text-field>
            </v-flex>
            <v-flex xs3>
                <v-text-field
                        :value="precision"
                        label="Precision:">
                </v-text-field>
            </v-flex>
            <v-flex xs3>
                <v-btn @click="clear">Clear</v-btn>
                <v-switch
                        v-model="useBackendData"
                        :label="`Use back data: ${useBackendData.toString()}`"
                ></v-switch>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import ShopClusterParams from "../model/ShopClusterParams";
    import ObjectService from "../service/ObjectService";

    export default {
        name: "yandex-map-component",
        created() {
            ymaps.ready(this.initMap);
        },
        data() {
            return {
                leftBottom: null,
                rightTop: null,
                selectedPoint: [],
                map: {},
                zoomLevel: null,
                useBackendData: false
            }
        },
        computed: {
            precision() {

                let levelOfDetail = this.zoomLevel;
                if (this.zoomLevel <= 3)
                    levelOfDetail = 3;

                if (this.zoomLevel > 3 && this.zoomLevel <= 6)
                    levelOfDetail = 4;

                if (this.zoomLevel === 6 || this.zoomLevel === 7)
                    levelOfDetail = 6;

                if (this.zoomLevel === 8 || this.zoomLevel === 9)
                    levelOfDetail = 8;

                if (this.zoomLevel === 13 || this.zoomLevel === 14)
                    levelOfDetail = 13;

                if (this.zoomLevel>22)
                    levelOfDetail=22;

                return levelOfDetail + 1;
            }
        },
        methods: {
            initMap() {
                let map = new ymaps.Map("map", {
                    center: [55.76, 37.64],
                    zoom: 7,
                });

                map.events.add('boundschange', this.onBoundsChange);
                map.events.add('click', this.addObject);

                this.leftBottom = map.getBounds()[0];
                this.rightTop = map.getBounds()[1];
                this.map = map;
                this.zoomLevel = this.map.getZoom();
                this.updateShops();
            },
            addObject(e) {
                // let newObject = {
                //     lat: e.get('coords')[0],
                //     lon: e.get('coords')[1]
                // };
                // ObjectService.save(newObject)
                //     .then(response => {
                //         this.map.geoObjects.add(this.getPlacemark(response.lat, response.lon, response.title))
                //     });

                this.map.geoObjects.add(this.getPlacemark(e.get('coords')[0], e.get('coords')[1], "Test"))
            },
            clear() {
                this.map.geoObjects.removeAll();
            },
            onBoundsChange(e) {
                this.leftBottom = e.get('newBounds')[0];
                this.rightTop = e.get('newBounds')[1];
                this.updateShops();
                this.zoomLevel = this.map.getZoom();
            },

            getPlacemark(lat, lon, content) {
                return new ymaps.Placemark([lat, lon], {
                    balloonContent: content
                }, {
                    preset: 'islands#icon',
                    iconColor: '#b62c83'
                })
            },

            updateShops() {
                let params = new ShopClusterParams(
                    this.rightTop[0],
                    this.leftBottom[1],
                    this.leftBottom[0],
                    this.rightTop[1],
                    this.map.getZoom(),
                    null,
                    null,
                    null
                );
                console.log(params);
                if (this.useBackendData){
                    ObjectService.getObjects(params)
                        .then(response => {
                            console.log(response);
                            this.map.geoObjects.removeAll();
                            response.shops.forEach(shop => {
                                let placemark = new ymaps.Placemark([shop.coordinates.lat, shop.coordinates.lon], {
                                    balloonContent: shop.id + ", lat: " + shop.coordinates.lat + ", lon: " + shop.coordinates.lon
                                }, {
                                    preset: 'islands#icon',
                                    iconColor: '#0095b6'
                                });
                                this.map.geoObjects.add(placemark)
                            });
                            response.clusters.forEach(cluster => {
                                let placemark = new ymaps.Placemark([cluster.coordinates.lat, cluster.coordinates.lon], {
                                    iconCaption: cluster.shops_count,
                                    balloonContent: this.getClusterBalloonText(cluster)
                                }, {
                                    preset: 'islands#icon',
                                    iconColor: '#b62d2c'
                                });
                                this.map.geoObjects.add(placemark)
                            })
                        });
                } else {
                    ///////// Not implemented
                }
            },
            getClusterBalloonText(cluster) {
                let text = "";
                if (cluster.shops_count > 0) {
                    cluster.shops.forEach(shopId => {
                        text = text + shopId + "<br/>"
                    })
                }
                return text
            }
        }
    }
</script>

<style scoped>

</style>