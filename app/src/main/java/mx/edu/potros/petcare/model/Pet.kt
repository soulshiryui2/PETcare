package mx.edu.potros.petcare.model

class Pet {
    var name: String = ""
        get() = field
        set(value) {
            field = value
        }
    var tipo: String = ""
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
    var salud: String = ""
        get() = field
        set(value) {
            field = value
        }

    constructor()

    constructor(name: String, tipo: String, edad: String, color: String, salud: String) {
        this.name = name
        this.tipo = tipo
        this.edad = edad
        this.color = color
        this.salud = salud
    }
}
