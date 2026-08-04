package com.didwanatechcare.app.navigation

object Routes {
    const val HOME = "home"
    const val REPAIR_FORM = "repair_form"
    const val BUY_FORM = "buy_form"
    const val CONFIRMATION = "confirmation/{requestId}"
    const val ADMIN_LOGIN = "admin_login"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val REQUEST_DETAIL = "request_detail/{requestId}"
    const val CUSTOMER_HISTORY = "customer_history/{mobile}"
    const val SETTINGS = "settings"
    fun confirmation(requestId: String) = "confirmation/$requestId"
    fun requestDetail(requestId: String) = "request_detail/$requestId"
    fun customerHistory(mobile: String) = "customer_history/$mobile"
}