package com.example.kt2

import android.content.Intent

fun main() {
    val role = UserRole.valueOf("Support")
if (role == UserRole.User)
    println(role)
    AeSimpleSHA1.sHA1("jnk")
    Intent()

}


open class User(firstName: String, var lastName: String) {

    var firstName = firstName
        get() = "name : $field"
        set(value) {
            field = value + "z"
        }

}

class Student(): User(firstName = "", lastName = "") {
protected fun test (){}
}
enum class UserRole(val age:Int){
    User(10) {
        val num = 50
         fun test() {
            TODO("Not yet implemented")
        }
    },
    Support(20) {
         fun test() {
            TODO("Not yet implemented")
        }
    },
    Admin(30) {
         fun test() {
            TODO("Not yet implemented")
        }
    };
}

