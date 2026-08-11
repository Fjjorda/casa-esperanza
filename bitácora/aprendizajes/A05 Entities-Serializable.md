# A05 Entities - Cuando deben ser Serializable

Todas las **@Entity** que trabajamos en clase venían acompañadas de `implements Serializable` y los apuntes de la asignatura indican que uno de los requisitos de las entidades es "debe ser Serializable (no es obligatorio si no se utilizan sus interfaces remotas)". Mi investigación me llevó a encontrar el *por qué* es opcional y a que se refiere con *interfaces remotas*.

## Serializable en Java
Interfaz que permite una clase Java convertirse en una secuencia de bytes (serialization). Esto permite "mover" a los objetos ya sea a un archivo, una base de datos o por la red, y después reconstruirlos en destino: a partir de los bytes se crea el objeto (deserialization).[(1)]

## ¿Qué son las interfaces remotas?
RMI (*Java Remote Method Invocation*) es un mecanismo ofrecido por Java para invocar un método de manera remota. A través de RMI, un programa Java puede exportar un objeto, con lo que dicho objeto estará accesible **a través de la red** y el programa permanece a la espera de peticiones en un puerto TCP. A partir de ese momento, un cliente puede conectarse e invocar los métodos proporcionados por el objeto.[(2)]

Como nuestro backend está desarrollado en local o mejor dicho, que no trabaja con ningún servicio externo o intermediario, no necesitamos hacer uso de esta funcionalidad; nunca se llega a enviar un objeto. El stack actual se comunica entre front y backend por medio de JSONs: API REST. Podríamos decir que "serializamos a json".

Es verdad que no pasaría nada si incluimos `implements Serializable` en cada entidad pero no lo hacemos deliberadamente sabiendo que no es necesario en nuestro contexto.

## Otras razones para ser Serializable
De acuerdo con la discusión seguida en Stack Overflow,[(3)] un usuario comparte un fragmento interesante de un requisito técnico de la infraestructura interna de Hibernate: cuando usamos `@JoinColumn` y se declara el parámetro `referencedColumnName` (indica qué columna de la *otra* tabla se conecta con la clave foránea), pero se referencia una columna que **no** es la clave primaria, entonces la entidad asociada tiene que ser Serializable.[(4)] 


## Fuentes Consultadas
- Serializable Interface in Java: https://www.geeksforgeeks.org/java/serializable-interface-in-java/
- Java Remote Method Invocation: https://es.wikipedia.org/wiki/Java_Remote_Method_Invocation
- When and why JPA entities should implement the Serializable interface?: https://stackoverflow.com/questions/2020904/when-and-why-jpa-entities-should-implement-the-serializable-interface
- Hibernate - Chapter 2. Mapping Entities https://docs.hibernate.org/stable/annotations/reference/en/html/entity.html#entity-mapping-association

[(1)]: https://www.geeksforgeeks.org/java/serializable-interface-in-java/ (Serializable in Java)
[(2)]: https://es.wikipedia.org/wiki/Java_Remote_Method_Invocation (RMI and Remote Methods)
[(3)]: https://stackoverflow.com/questions/2020904/when-and-why-jpa-entities-should-implement-the-serializable-i (When entities are Serializable?)
[(4)]: https://docs.hibernate.org/stable/annotations/reference/en/html/entity.html#entity-mapping-association (Hibernate Docs - Mapping)