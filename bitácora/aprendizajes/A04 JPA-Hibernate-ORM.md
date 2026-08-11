# A04 JPA/Hibernate/ORM y su relación

## Contexto
En la fase inicial del proyecto, se desarrollaron diagramas de clase como un preámbulo a como puede modelarse la base de datos con sus relaciones entre tablas. Como usamos Spring en el backend, hacemos uso de **Hibernate** para pasar ese *boceto* a clases Java que se usarán para la creación del **Modelo Relacional** sin tocar directamente la base de datos gracias a JPA.

## ¿Qué es JPA?
*Jakarta/Java Persistence API* (JPA) es el estándar para la persistencia de objetos en Java. Es la API para la gestión del acceso a sistemas gestores de bases de datos relaciones y la que define las **anotaciones** que usamos en el código. Se basa en el mapeo entidad-relacional (*Object Relational Mapping*, ORM) que actúa como puente entre el código y la base de datos. Permite manipular los datos en la BD Relacional usando objetos en lugar de escribir sentencias SQL directamente. Las equivalencias clave de esta capa de abstracción son:

- Cada tabla se modela como una clase (una *entidad*).
- Cada fila de la tabla se modela como un objeto en Java.
- Las relaciones entre tablas se modelan como relaciones de
asociación entre clases.

En otras palabras, mapea los tipos de datos en Java a los tipos de datos en SQL.

Con esta herramienta modelamos la base de datos como un conjunto de clases simples que identificamos como entidades o POJOs que el resto de capas de la aplicación usan para acceder a los datos. En concreto, mediante el lenguaje [JPQL](TODO: link a ficha sobre JPQL) que es a nivel de objeto. 

## ¿Cómo se relaciona con Hibernate?
Hibernate es un framework ORM que implementa JPA y es el encargado de pasar los objetos a SQL y gestionar el contexto de persistencia. Es decir, JPA define cómo debe comportarse un ORM en Java y Hibernate es la implementación más usada de esa especificación. Se incluye en Spring Boot al integrar la dependencia **Spring Data JPA**.

Cabe mencionar que esa dependencia es la que permite generar los repositorios, dándonos las operaciones CRUD sobre las @Entity sin escribir su implementación. Por ejemplo: `save()`, `findById()`, `findAll()`, `delete()`.

## Fuentes Consultadas
- Java - JPA vs Hibernate: https://www.geeksforgeeks.org/java/java-jpa-vs-hibernate/
- Apuntes de Tecnologías de Aplicaciones Web. Grado Ing. Sw. por Eduardo Guzmán De los Riscos: **Java/Jakarta Persistence API (JPA)**. 
