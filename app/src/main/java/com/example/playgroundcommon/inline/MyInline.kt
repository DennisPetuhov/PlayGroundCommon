package com.example.playgroundcommon.inline

//1. Inlining: The inlineFunction directly invokes the lambda, resulting in simpler bytecode.
//The commonFunction creates an anonymous class for the lambda, increasing bytecode complexity.
//2. Performance: Inline functions avoid the overhead of creating additional objects and method calls,
//leading to potentially better performance.
fun higherfunc(str: String, mycall: (String) -> Unit) {

    // inovkes the print() by passing the string str
    mycall(str)
}

val a = intArrayOf(1, 2, 3, 4, 5)
fun main(args: Array<String>) {
    print("GeeksforGeeks: ")
    higherfunc("A Computer Science portal for Geeks", ::print)
}
// make inline to make faster  avoid reflection
//1.Use inline for preventing object creation for example in loops
// 2.  Use inline for preventing variable capturing
//When you use the local variables inside the lambda, it is called variable capturing(closure):
//
//fun doSomething() {
//    val greetings = "Hello"                // Local variable
//    doSomethingElse {
//        println("$greetings from lambda")  // Variable capture
//    }
//}
//If our doSomethingElse() function here is not inline, the captured variables are passed to the lambda via the constructor while creating the anonymous object that we saw earlier:
//
//public static final void doSomething() {
//    String greetings = "Hello";
//    doSomethingElse(new Function(greetings) {
//        public final void invoke() {
//            System.out.println(this.$greetings + " from lambda");
//        }
//    });
//}
//If you have many local variables used inside the lambda or calling the lambda in a loop, passing every local variable through the constructor causes the extra memory overhead. Using the inline function in this case helps a lot, since the variable is directly used at the call-site.
//
//So, as you can see from the two examples above, the big chunk of performance benefit of inline functions is achieved when the functions take other functions as arguments. This is when the inline functions are most beneficial and worth using. There is no need to inline other general functions because the JIT compiler already makes them inline under the hood, whenever it feels necessary.
//
//3.Use inline for better control flow
//Since non-inline function type is converted to a class, we can't write the return statement inside the lambda:
//
//fun doSomething() {
//    doSomethingElse {
//        return    // Error: return is not allowed here
//    }
//}
//This is known as non-local return because it's not local to the calling function doSomething(). The reason for not allowing the non-local return is that the return statement exists in another class (in the anonymous class shown previously). Making the doSomethingElse() function inline solves this problem and we are allowed to use non-local returns because then the return statement is copied inside the calling function.
//
//Use inline for reified type parameters
//While using generics in Kotlin, we can work with the value of type T. But we can't work with the type directly, we get the error Cannot use 'T' as reified type parameter. Use a class instead:
//
//fun <T> doSomething(someValue: T) {
//    println("Doing something with value: $someValue")               // OK
//    println("Doing something with type: ${T::class.simpleName}")    // Error
//}
//This is because the type argument that we pass to the function is erased at runtime. So, we cannot possibly know exactly which type we are dealing with.
//
//Using an inline function along with the reified type parameter solves this problem:
//
//inline fun <reified T> doSomething(someValue: T) {
//    println("Doing something with value: $someValue")               // OK
//    println("Doing something with type: ${T::class.simpleName}")    // OK
//}
//Inlining causes the actual type argument to be copied in place of T. So, for example, the T::class.simpleName becomes String::class.simpleName, when you call the function like doSomething("Some String"). The reified keyword can only be used with inline functions.
//
//Avoid inline when calls are repetitive
//Let's say we have the following function that is called repetitively at different abstraction levels:
//
//inline fun doSomething() {
//    println("Doing something")
//}
//First abstraction level
//
//inline fun doSomethingAgain() {
//    doSomething()
//    doSomething()
//}
//Results in:
//
//public static final void doSomethingAgain() {
//    System.out.println("Doing something");
//    System.out.println("Doing something");
//}
//At first abstraction level, the code grows at: 21 = 2 lines.
//
//Second abstraction level
//
//inline fun doSomethingAgainAndAgain() {
//    doSomethingAgain()
//    doSomethingAgain()
//}
//At third abstraction level, the code grows at: 23 = 8 lines.
//
//Similarly, at the fourth abstraction level, the code grows at 24 = 16 lines and so on.
//
//The number 2 is the number of times the function is called at each abstraction level. As you can see the code grows exponentially not only at the last level but also at every level, so that's 16 + 8 + 4 + 2 lines. I have shown only 2 calls and 3 abstraction levels here to keep it concise but imagine how much code will be generated for more calls and more abstraction levels. This increases the size of your app. This is another reason why you shouldn't inline each and every function in your app.
//
//Avoid inline in recursive cycles
//Avoid using the inline function for recursive cycles of function calls as shown in the following code:
//
//// Don't use inline for such recursive cycles
//
//inline fun doFirstThing() { doSecondThing() }
//inline fun doSecondThing() { doThirdThing() }
//inline fun doThirdThing() { doFirstThing() }
//This will result in a never ending cycle of the functions copying the code. The compiler gives you an error: The 'yourFunction()' invocation is a part of inline cycle.
//
//Can't use inline when hiding implementation
//The public inline functions cannot access private functions, so they cannot be used for implementation hiding:
//
//inline fun doSomething() {
//    doItPrivately()  // Error
//}
//
//private fun doItPrivately() { }
//In the inline function shown above, accessing the private function doItPrivately() gives an error: Public-API inline function cannot access non-public API fun.
//
//        Checking the generated code
//Now, about the second part of your question:
//
//but I found that there is no function object created by kotlin for a non-inline function. why?
//
//The Function object is indeed created. To see the created Function object, you need to actually call your lock() function inside the main() function as follows:
//
//fun main() {
//    lock { println("Inside the block()") }
//}
//Generated class
//
//The generated Function class doesn't reflect in the decompiled Java code. You need to directly look into the bytecode. Look for the line starting with:
//
//final class your/package/YourFilenameKt$main$1 extends Lambda implements Function0 { }
//This is the class that is generated by the compiler for the function type that is passed to the lock() function. The main$1 is the name of the class that is created for your block() function. Sometimes the class is anonymous as shown in the example in the first section.
//
//Generated object
//
//In the bytecode, look for the line starting with:
//
//GETSTATIC your/package/YourFilenameKt$main$1.INSTANCE
//INSTANCE is the object that is created for the class mentioned above. The created object is a singleton, hence the name INSTANCE.


//public final class com.example.InlineFunctionKt {
//    public static final void inlineFunction(java.lang.String, kotlin.jvm.functions.Function1);
//    Code:
//    0: aload_0
//    1: ldc           #7                  // String str
//    3: invokestatic  #13                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
//    6: aload_1
//    7: ldc           #14                 // String mycall
//    9: invokestatic  #13                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
//    12: aload_1
//    13: aload_0
//    14: invokeinterface #20,  2           // InterfaceMethod kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
//    19: pop
//    20: return
//
//    public static final void main();
//    Code:
//    0: ldc           #26                 // String Hello, Inline!
//    2: astore_0
//    3: getstatic     #32                 // Field java/lang/System.out:Ljava/io/PrintStream;
//    6: aload_0
//    7: invokevirtual #38                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
//    10: return
//}


//public final class com.example.InlineFunctionKt {
//    public static final void inlineFunction(java.lang.String, kotlin.jvm.functions.Function1);
//    Code:
//    0: aload_0
//    1: ldc           #7                  // String str
//    3: invokestatic  #13                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
//    6: aload_1
//    7: ldc           #14                 // String mycall
//    9: invokestatic  #13                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
//    12: aload_1
//    13: aload_0
//    14: invokeinterface #20,  2           // InterfaceMethod kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
//    19: pop
//    20: return
//
//    public static final void main();
//    Code:
//    0: ldc           #26                 // String Hello, Inline!
//    2: astore_0
//    3: getstatic     #32                 // Field java/lang/System.out:Ljava/io/PrintStream;
//    6: aload_0
//    7: invokevirtual #38                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
//    10: return
//}