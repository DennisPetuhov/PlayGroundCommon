@file:Suppress("UNCHECKED_CAST")

package com.example.playgroundcommon.di

object MySimpleServiceLocator {
private val myDependencies: MutableMap<String, Any> = mutableMapOf()

    @Synchronized
    fun <T : Any> addDependency(key: String, dependency: T) {
        if (myDependencies.containsKey(key)) {
            throw IllegalArgumentException("Dependency already exists for key: $key")
        }
        myDependencies[key] = dependency
    }


    internal inline fun <reified  T> getDependency(key: String): T {
        return myDependencies[key] as? T
            ?: throw IllegalArgumentException("Dependency not found for key: $key")
    }

    @Synchronized
    fun removeDependency(key: String) {
        if (!myDependencies.containsKey(key)) {
            throw IllegalArgumentException("No dependency found for key: $key to remove")
        }
        myDependencies.remove(key)
    }
}

class MyDependency {
    val myDependency = "My Dependency"
}





interface ServiceRegistry {
    fun <T : Any> registerService(clazz: Class<*>, service: T)
    fun <T : Any> getService(clazz: Class<T>): T
}

object ServiceLocator : ServiceRegistry {
    private val services = mutableMapOf<Class<*>, Any>()

    override fun <T : Any> registerService(clazz: Class<*>, service: T) {
        services[clazz] = service
    }

    override fun <T : Any> getService(clazz: Class<T>): T {
        return services[clazz] as T
    }
}

inline fun <reified T : Any> ServiceRegistry.register(service: T) {
    this.registerService(T::class.java, service)
}

inline fun <reified I : Any> ServiceRegistry.get(): I {
    return this.getService(I::class.java)
}

fun main() {
    val myDependency = MyDependency() // create an instance of MyDependency in App
    MySimpleServiceLocator.addDependency("firstDependency", myDependency) // save the instance in the service locator in App

    val myDependencyFromServiceLocator = MySimpleServiceLocator.getDependency<MyDependency>("firstDependency") // get the instance from the service locator in any place of the app
    println(myDependencyFromServiceLocator.myDependency)

    MySimpleServiceLocator.removeDependency("firstDependency") // remove the instance from the service locator



    //or
    // Register PreferenceService
//    ServiceLocator.register<UserService>(ActualUserService())
//    val userService = ServiceLocator.get<UserService>()
//    userService.loadUserData()
}