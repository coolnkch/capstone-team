
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ParkingManager from "./components/ParkingManager"

import ParkAreaManager from "./components/ParkAreaManager"
import ParkingZoneStatusManager from "./components/ParkingZoneStatusManager"

import PaymentManager from "./components/PaymentManager"


import MyParkingInfo from "./components/MyParkingInfo"
export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/parkings',
                name: 'ParkingManager',
                component: ParkingManager
            },

            {
                path: '/parkAreas',
                name: 'ParkAreaManager',
                component: ParkAreaManager
            },
            {
                path: '/parkingZoneStatuses',
                name: 'ParkingZoneStatusManager',
                component: ParkingZoneStatusManager
            },

            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },


            {
                path: '/myParkingInfos',
                name: 'MyParkingInfo',
                component: MyParkingInfo
            },


    ]
})
