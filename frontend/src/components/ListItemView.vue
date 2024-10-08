<template>

    <v-data-table
        :headers="headers"
        :items="listItem"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'ListItemView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
                { text: "name", value: "name" },
                { text: "stock", value: "stock" },
            ],
            listItem : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/listItems'))

            temp.data._embedded.listItems.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.listItem = temp.data._embedded.listItems;
        },
        methods: {
        }
    }
</script>

