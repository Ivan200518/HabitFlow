package com.example.habitflow.db

class ListItem(
    dataHabit: String?,
    dataImportance: String?,
    dataReward: String?,
    dataUri: String?,
    dataCategory: String?
) {
    var habit = dataHabit
    var importance = dataImportance
    var reward = dataReward
    var category = dataCategory
    var uri = dataUri
    var id: Int? = 0

}