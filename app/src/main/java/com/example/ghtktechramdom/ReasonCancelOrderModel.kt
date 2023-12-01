package com.example.ghtktechramdom

class ReasonCancelOrderModel {
    var id = 0
    var name = ""
    var actionType = TYPE_NORMAL

    constructor(id: Int, reson: String, type: Int) {
        this.id = id
        name = reson
        actionType = type
    }

    constructor() {}

    fun setActionType(actionType: Int): ReasonCancelOrderModel {
        this.actionType = actionType
        return this
    }

    val isSelect: Boolean
        get() = actionType == TYPE_SELECTED

    fun changeStatus() {
        actionType = if (actionType == TYPE_SELECTED) TYPE_NORMAL else TYPE_SELECTED
    }

    companion object {
        var TYPE_SELECTED = 1
        var TYPE_NORMAL = 2
        var TYPE_ADD_ITEM = 3
    }
}
