<template>
    <v-container>
        <v-layout
                text-center
                wrap
        >
            <v-flex offset-xs1 xs10>
                <GmapMap
                        ref="map"
                        :center="center"
                        :zoom="7"

                        map-type-id="terrain"
                        style="width: auto; height: 500px"
                >
                </GmapMap>
                <v-btn @click="clearMarkers">Clear</v-btn>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import ShopClusterParams from "../model/ShopClusterParams";
    import ObjectService from "../service/ObjectService";

    export default {
        name: "google-map-component",
        mounted() {

            this.$refs.map.$mapPromise.then((map) => {
                map.setMapTypeId('roadmap');
                map.addListener('zoom_changed',this.onZoomChanged);
                map.addListener('dragend',this.onDragend);
                this.map = map;
            });
            this.updateObjects()
        },
        data() {
            return {
                map: {},
                center: {
                    lat: 55.74624616610336,
                    lng: 37.60707194274821
                },
                markers: []
            }
        },
        methods: {
            initMap() {

            },

            onZoomChanged(){
                console.log(this.map.getZoom());
                this.updateObjects()
            },
            onDragend() {
                this.updateObjects();
            },

            updateObjects() {
                let params = new ShopClusterParams(
                    this.map.getBounds().getNorthEast().lat(),
                    this.map.getBounds().getSouthWest().lng(),
                    this.map.getBounds().getSouthWest().lat(),
                    this.map.getBounds().getNorthEast().lng(),
                    this.map.getZoom()
                );
                console.log(params);
                this.clearMarkers();


                ObjectService.getObjects(params)
                    .then(response => {
                        console.log(response);
                        response.objects.forEach(object => {
                            var marker = new google.maps.Marker({
                                position: {
                                    lat: object.coordinates.lat,
                                    lng: object.coordinates.lon
                                },
                                map: this.map,
                                title: object.id + ", lat: " + object.coordinates.latitude + ", lon: " + object.coordinates.longitude,
                                icon: {
                                    url: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
                                }
                            });
                            this.markers.push(marker);
                        });
                        response.clusters.forEach(cluster => {
                            var marker = new google.maps.Marker({
                                position: {
                                    lat: cluster.coordinates.latitude,
                                    lng: cluster.coordinates.longitude
                                },
                                map: this.map,
                                label: {
                                    text: ""+ cluster.shops_count,
                                    color: "#ffff00",
                                    fontSize: "12px",
                                    fontWeight: "bold"
                                },
                            });
                            this.markers.push(marker);
                        })
                    });

            },

            clearMarkers(){
                this.markers.forEach(marker => {
                   marker.setMap(null);
                });
                this.markers.splice(0, this.markers.length)
            }
        }
    }
</script>

<style scoped>

</style>