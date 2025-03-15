package com.example.playgroundcommon.di

//https://blog.kotlin-academy.com/dependency-injection-the-pattern-without-the-framework-33cfa9d5f312
class MyDependencyInjection {
}

interface CatApi
interface CatService
interface CatRepository
interface Scheduler
interface Context
abstract class Application {
    abstract fun onCreate()
}
abstract class AppCompatActivity


fun app(): AppComponent = TODO("")
fun common(): CommonComponent = TODO()
fun api(): ApiComponent = TODO()


interface AppComponent {
//    val moshi: Moshi
    val context: Context
//    val retrofit: Retrofit
//    val okHttpClient: OkHttpClient
    val catApi: CatApi
    val catRepository: CatRepository
    val catService: CatService
    val mainThread: Scheduler
    val backgroundThread: Scheduler

    companion object {
        lateinit var instance: AppComponent
    }

}
interface ApiComponent: AppComponent {

    companion object {
        lateinit var instance: ApiComponent
    }
}
interface  CommonComponent : ApiComponent {

    companion object {
        lateinit var instance: CommonComponent
    }
}

// Implement the union of all properties
abstract class ComponentsImplementation(
//    override val moshi: Moshi,
    override val context: Context
) : CommonComponent, AppComponent