package com.example.playgroundcommon.sobes.sber

//// Load the drawable
//val drawable = AppCompatResources.getDrawable(this, R.drawable.my_drawable)
//
//// Change the color of the drawable
//drawable?.let {
//    val wrappedDrawable: Drawable = DrawableCompat.wrap(it)
//    DrawableCompat.setTint(wrappedDrawable, getColor(R.color.new_color))
//}

//Pseudocode
//Open the proguard-rules.pro file.
//Add a -keep rule for MyClass.
//Code
//# Keep the MyClass class from being obfuscated
//-keep class com.example.MyClass {
//    *;
//}

//# Keep the MyClass class and its properties annotated with @Serialized from being obfuscated
//-keep class com.example.MyClass {
//    @com.google.gson.annotations.SerializedName <fields>;
//}
fun main() {
    val list= listOf("a","b","c")
    list+ "d"
  val myMutableList = mutableListOf("a", "b")
   myMutableList += "d"
    println(list)
    println(myMutableList)
    list.filter { it == "a" }
}