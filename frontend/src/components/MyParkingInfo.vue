<template>

<v-data-table
    :headers="headers"
    :items="myParkingInfo"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'MyParkingInfo',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "carNo", value: "carNo" },
            { text: "inTime", value: "inTime" },
            { text: "outTime", value: "outTime" },
            { text: "carStatus", value: "carStatus" },
            { text: "price", value: "price" },
            { text: "paymentStatus", value: "paymentStatus" },
            { text: "parkingAreaId", value: "parkingAreaId" },
            { text: "parkingZoneNo", value: "parkingZoneNo" },
        ],
        myParkingInfo : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/myparkinginfos')

      this.myParkingInfo = temp.data._embedded.myparkinginfos;

    },
    methods: {
    }
  }
</script>

