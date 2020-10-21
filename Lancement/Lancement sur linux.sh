export PATH_TO_FX=../ressources/JDK/javafx-sdk-15/lib
javac --module-path $PATH_TO_FX --add-modules javafx.controls ../src/main/java/outerhaven/$(find . -name "*.java")
java --module-path $PATH_TO_FX --add-modules javafx.controls App
