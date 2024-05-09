package mx.edu.potros.petcare.model

class Pet {
    var name: String = ""
        get() = field
        set(value) {
            field = value
        }

    var edad: String = ""
        get() = field
        set(value) {
            field = value
        }

    var color: String = ""
        get() = field
        set(value) {
            field = value
        }

    constructor()

    constructor(name: String, edad: String, color: String) {
        this.name = name
        this.edad = edad
        this.color = color
    }
}
