
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import InventoryInventoryManager from "./components/listers/InventoryInventoryCards"
import InventoryInventoryDetail from "./components/listers/InventoryInventoryDetail"


import OrderOrderManager from "./components/listers/OrderOrderCards"
import OrderOrderDetail from "./components/listers/OrderOrderDetail"

import DeliveryDeliveryManager from "./components/listers/DeliveryDeliveryCards"
import DeliveryDeliveryDetail from "./components/listers/DeliveryDeliveryDetail"


import DeadlineDeadlineManager from "./components/listers/DeadlineDeadlineCards"
import DeadlineDeadlineDetail from "./components/listers/DeadlineDeadlineDetail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"

import PaymentPaymentManager from "./components/listers/PaymentPaymentCards"
import PaymentPaymentDetail from "./components/listers/PaymentPaymentDetail"

import AccountAccountManager from "./components/listers/AccountAccountCards"
import AccountAccountDetail from "./components/listers/AccountAccountDetail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"
import Manager from "./components/listers/Cards"
import Detail from "./components/listers/Detail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/inventories/inventories',
                name: 'InventoryInventoryManager',
                component: InventoryInventoryManager
            },
            {
                path: '/inventories/inventories/:id',
                name: 'InventoryInventoryDetail',
                component: InventoryInventoryDetail
            },


            {
                path: '/orders/orders',
                name: 'OrderOrderManager',
                component: OrderOrderManager
            },
            {
                path: '/orders/orders/:id',
                name: 'OrderOrderDetail',
                component: OrderOrderDetail
            },

            {
                path: '/deliveries/deliveries',
                name: 'DeliveryDeliveryManager',
                component: DeliveryDeliveryManager
            },
            {
                path: '/deliveries/deliveries/:id',
                name: 'DeliveryDeliveryDetail',
                component: DeliveryDeliveryDetail
            },


            {
                path: '/deadlines/deadlines',
                name: 'DeadlineDeadlineManager',
                component: DeadlineDeadlineManager
            },
            {
                path: '/deadlines/deadlines/:id',
                name: 'DeadlineDeadlineDetail',
                component: DeadlineDeadlineDetail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },

            {
                path: '/payments/payments',
                name: 'PaymentPaymentManager',
                component: PaymentPaymentManager
            },
            {
                path: '/payments/payments/:id',
                name: 'PaymentPaymentDetail',
                component: PaymentPaymentDetail
            },

            {
                path: '/accounts/accounts',
                name: 'AccountAccountManager',
                component: AccountAccountManager
            },
            {
                path: '/accounts/accounts/:id',
                name: 'AccountAccountDetail',
                component: AccountAccountDetail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },
            {
                path: '//',
                name: 'Manager',
                component: Manager
            },
            {
                path: '///:id',
                name: 'Detail',
                component: Detail
            },



    ]
})
