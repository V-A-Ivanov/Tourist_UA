package com.example.touristua

import java.io.Serializable

class Location : Serializable {
    var imageUrl: String = ""
    var id: Int = 0
    var name: String = ""
    var region: String = ""
    var type: String = ""
    var year: Int = 0
    var description: String = ""

    constructor()

    constructor(id: Int, name: String, region: String, type: String, year: Int, description: String, imageUrl: String = "") {
        this.id = id
        this.name = name
        this.region = region
        this.type = type
        this.year = year
        this.description = description
        this.imageUrl = imageUrl
    }
}
