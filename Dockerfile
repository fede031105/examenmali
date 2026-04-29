# ==========================================
# ETAPA 1: Construcción (Build)
# ==========================================
# Usamos una imagen de Maven con Java 17 (Cambia el 17 si usas Java 21)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente de tu proyecto
COPY src ./src

# Compilamos el proyecto y generamos el archivo .jar (saltando los tests para mayor rapidez)
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Ejecución (Run)
# ==========================================
# Usamos una imagen mucho más ligera solo con Java para correr la app
FROM eclipse-temurin:17-jre-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos el .jar generado en la Etapa 1
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que configuraste en tu application.properties
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]