[![Build Status](https://travis-ci.org/amcomaschi/mutants.svg?branch=master)](https://travis-ci.org/amcomaschi/mutants)
[![codecov](https://codecov.io/gh/amcomaschi/mutants/branch/master/graph/badge.svg)](https://codecov.io/gh/amcomaschi/mutants)
# Ejercicio Mutantes - Mercadolibre

Ejercicio práctico para MercadoLibre. 

Se puede ver una version estable del proyecto aqui:

- [servicio es mutante](http://ec2-13-58-238-161.us-east-2.compute.amazonaws.com:4567/mutants/).
- [servicio estadisticas](http://ec2-13-58-238-161.us-east-2.compute.amazonaws.com:4567/stats)

![Magneto_image](doc/images/magneto.jpg)

- [Ejercicio](#ejercicio)
  - [Especificaciones](#especificaciones)
  - [Implementación y tecnologias usadas](#implementaci%C3%B3n-y-tecnologias-usadas)
  - [Comentarios relevantes](#comentarios-relevantes)
- [Setup](#setup)
  - [Instrucciones](#instrucciones)
  - [Uso](#uso)
  - [API Url](#api)
  - [Servicios](#servicios)
    - [Es mutante](#es-mutante)
    - [Estadisticas](#estadisticas)
- [Test](#test)
  - [Automaticos](#automaticos)
  - [Scripts](#scripts)

## Ejercicio

### Especificaciones

Los archivos correspondientes a la especificación del ejercicio se encuentran en la carpeta `spec`. Dentro de ella
se encuentra [un pdf](./spec/Examen%20Mercadolibre%202017.pdf) que describe la funcionalidades y requisitos esperados
que contenga el proyecto.

### Implementacion y tecnologias usadas

- [SparkJava](http://sparkjava.com)
- [MongoDB](https://www.mongodb.com)
- [Morphia](https://mongodb.github.io/morphia/)
- [Gson](https://github.com/google/gson)
- [Log4j](https://logging.apache.org/log4j/2.x/)
- [jUnit](http://junit.org/junit5/)

### Comentarios relevantes
Cuando comence a realizar el ejercicio no estaba familiarizado con SparkJava, por lo que tuve la oportunidad de aprender
los conceptos basicos de este framework utilizando la [documentacion oficial de sparkjava](http://sparkjava.com/documentation) y 
[tutoriales](http://sparkjava.com/tutorials/).

Tampoco estaba familiarizado con [Morphia](https://mongodb.github.io/morphia/) para el mapeo Object/Document, utilice 
tambien la [documentacion oficial](http://mongodb.github.io/morphia/1.3/) y la 
[documentacion de la API](http://mongodb.github.io/morphia/1.3/javadoc/).


## Setup

### Instrucciones
Para compilar y ejecutar proyecto es necesario contar con la version 1.8 de la JDK y Maven para la gestion de las dependencias.

Tambien es necesario contar con una instancia de MongoDB en caso de querer ejecutarlo localmente, se utilizan los datos de conexion por default de MongoDB, 
si la instancia se encuentra levantada en un host/port distinto se debe actualizar en el componente
_[DbServiceImpl](./src/main/java/ar/com/mercadolibre/mutants/services/impl/DbServiceImpl.java)_


Los distintos logs de la aplicacion se generan en el directorio del proyecto.
En caso de querer loguear en otra ubicacion es necesario actualizar la propiedad _*dir*_ del archivo de configuracion _[log4j2](./src/main/resources/log4j2.xml)_.

Clonar este repositorio: https://github.com/amcomaschi/mutants

Una vez levantada la aplicacion se puede realizar invocaciones a la API.

El puerto por defecto de la API es 4567.

### Uso

Para iniciar la aplicación, asegúrese de cumplir con las instrucciones anteriores. 

Una vez listo, ejecutar la clase principal _MutantsApp_ en su IDE preferido y espere hasta que se inicie la aplicación.

Tambien se puede iniciar la aplicacion con el siguiente comando en linea de comandos posicionandose en el directorio raiz
del proyecto:
```
mvn exec:java -Dexec.mainClass="ar.com.mercadolibre.mutants.MutantsApp"
```

### API Url

URL local: http://localhost:4567

URL hosteada en Amazon: http://ec2-13-58-238-161.us-east-2.compute.amazonaws.com:4567

### Servicios
#### Es mutante

Request: 
- POST http://ec2-13-58-238-161.us-east-2.compute.amazonaws.com:4567/mutants/

Request body (caso ADN mutante):

```
  {"dna":["ATGCGA", "CAGGGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]}
```

Response:

```
  200 OK
```
Request body (caso ADN humano):

```
  {"dna":["AATACT", "CCCAGA", "GGGATT", "AATTCC", "GGATCG", "TCACTG"]}
```

Response:

```
  403 Forbidden
```

#### Estadisticas

Request: 
- GET http://ec2-13-58-238-161.us-east-2.compute.amazonaws.com:4567/stats

Response: 200 (application/json)

```
{
    count_mutant_dna: 4,
    count_human_dna: 1,
    ratio: 0.8
}
```

### Test

#### Automaticos

Para la ejecucion de los test automaticos utilice jUnit.

Para poder probar los componentes de base de datos utilice una base de datos MongoDB embebida, esta se levanta durante 
el test y luego se destruye.
De esta forma no necesito tener una instancia de base de datos levantada, ni hosteada en algun servidor.

Ademas me aseguro de que la base de datos siempre este consistente en cada ejecucion de los test.

#### Scripts

Cree dos shell scripts para invocar a la API en forma masiva (uno por servicio) y ver los tiempos de respuesta de cada 
invocacion.
Los scripts utilizan el comando Unix _parallel_, en caso de ejecutar en entorno Mac OS se puede instalar el comando 
ejecutando en una terminal el siguiente comando: 

```
brew install parallel
```

Para el servicio de verificacion de ADN, el script envia en el body de la peticion la secuencia de ADN que se encuentra
en el archivo [dna-mutant.json](./scripts/invoke-mutants.sh).

La cantidad de peticiones en paralelo que se quieren ejecutar se corresponde con el valor que se encuentra seguido del 
comando _seq_: seq *1000* | parallel....