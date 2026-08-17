# Glosario de anotaciones

## ¿Qué es una anotación?
> **TL;DR**
>
> Metadatos que se añaden a clases, métodos, campos o parámetros para proporcionar información adicional sobre ellos sin modificar su lógica, indicando al framework cómo debe crear, configurar o utilizar esos elementos durante la ejecución.
---

Spring en un framework para Java que se basa en el uso de objetos simples de Java (POJOs[^1]). Se creó con la idea de "un bueno modelo está formado por POJOs débilmente acoplados por medio de interfaces", por lo tanto, es extremadamente modular y flexible.

Una de sus principales características es la **Inversión de Control (IoC)**, pero antes de explicarla contextualizamos. 

En la programación orientada a objetos es habitual que las clases colaboren entre sí mediante instancias de otras clases, estableciendo así relaciones de dependencia que la misma clase es responsable de crear y gestionar, aumentado el grado de acoplamiento entre componentes.

Spring establece el principio o patrón arquitectónico IoC que, como su nombre indica, invierte la responsabilidad de crear, configurar y asociar los objetos del desarrollador al contenedor de Spring. Cuando arranca, detecta las anotaciones en cada clase, crea los objetos necesarios conocidos como *beans*[^2] las cuales declaran las dependencias[^3] que necesitan. El contenedor se encarga de resolverlas mediante **Inyección de Dependencias**[^4]. Por eso se dice que este principio sigue la Filosofía Hollywood: *"No nos llame; ya le llamaremos"*. El contenedor se encarga de inyectarlas solo cuando son necesarias y desecharlas cuando cumplen su labor.

Con todo eso en mente, formalmente podemos definir las anotaciones y entender su funcionamiento en el entorno de Spring. Las anotaciones son metadatos que proporcionan información al framework sobre cómo debe interpretar determinados elementos del código (clases, métodos, campos o parámetros). Dependiendo de la anotación, pueden indicar que una clase debe registrarse como un bean, definir cómo deben resolverse las dependencias entre beans, configurar el comportamiento de métodos o habilitar funcionalidades específicas del framework.

Consecuencias directas de usar anotaciones son:

- Reduce el código boilerplate[^5].
- Proporcionan una forma declarativa de comunicar al contenedor qué debe hacer con cada clase y cómo debe integrarla en la aplicación.
- Mejora la legibilidad del código.

Como última nota mencionar que el *scope* de una anotación se aplica al elemento que la sigue inmediatamente (la proxima línea) y una anotación no añade lógica de negocio a la clase.


## Anotaciones JPA
- `@Entity`: especifica que la clase de trata como una entidad; contenedores de datos que representan registros de la base de datos en formato Orientado a Objetos.

### Usadas en @Entity:
- `@Data`: proporcionado por el pluggin [Lombok](https://projectlombok.org/setup/intellij).  Inyecta métodos `Get`, `Set`, `Equals`, `HashCode`, `ToString` y `RequiredArgsConstructor`.
- `@Table`: especifica la tabla principal de la entidad anotada.
    - name: nombre que tendrá la tabla en la base de datos.
    - uniqueConstraints: establece que columnas de la tabla en combinación no pueden tener valores repetidos.
    - indexes: define indices sobre una columna.
- `@Id`: especifica un atributo cuyo columna en la base de datos es clave primaria.
- `@GeneratedValue`: genera automáticamente el valor de la clave primaria.
- `@Column`: especifica la columna de la base de datos a la que se mapea un atributo.
    - name: nombre que tendrá el atributo en la base de datos. Lo omitimos para que tenga el mismo que el atributo.
    - unique: determina si el atributo puede tener valores repetidos.
    - nullable: define si el atributo puede tener valor indefinido (`null`). Caso paradigmático: definimos un atributo como Integer para que sea posible asignar el valor null en Java. Si definimos nullable = false, indica que el tipo *permite* null, pero *se prohíbe* que se guarde así en la base de datos.+
- `@Enumerated`: indica al framework como almacenar un clase Java enum en una columna de la base de datos.Con esta anotación no necesitamos mapear cada valor por separado. JPA ofrece dos formas de hacer el mapeo:
    - EnumType.STRING almacena el propio valor.
    - EnumType.ORDINAL almacena la posición del valor; un número entero.
    A pesar que STRING ocupa más espacio en la base de datos, es más legible y menos propenso a errores, en especial con enums con tantas entradas como las que usaré.
- `@ElementCollection`: define una colección de instancias de basic types o embeddable class. Esta anotación nos proporciona una forma sencilla de implementar una relación one-to-many con un basic type; en este caso,String. Le dice a JPA que la entidad padre tendrá una colección de objetos que no son entidades en sí mismos,sino valores que pertenece a ella, y la almacene en la base de datos sin crear una entidad adicional.
    - fetch: por defecto es Lazy. Especificamos FetchType.EAGER para tener los datos de la colección al momento que se consulte al entidad padre.
- `@CollectionTable`: especifica la tabla que será usada para mapear una colección. En concreto,@ElementCollection si crea una tabla adicional en la base de datos pero no es una entidad desde el punto de vista de JPA.
    - name: nombre de la tabla que se creará.
    - joinColumns: indicar bajo que atributo se relacionan la tabla de la colección y la entidad padre. Si no se especifica, JPA asume que la colección se relaciona con la entidad propietaria mediante su clave primaria (@Id).

#### Joins:


#### Bean Validation
- 

## Fuentes consultadas
- Apuntes de Tecnologías de Aplicaciones Web. Grado Ing. Sw. por Eduardo Guzmán De los Riscos: **Spring Framework y Spring Boot**. 
- [Jakarta Persistence](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1.pdf?utm_source=chatgpt.com)
- [Comprehensive Guide to Spring Annotations: Under-the-Hood Working](https://medium.com/@sharmapraveen91/comprehensive-guide-to-spring-annotations-under-the-hood-working-43e9570002c4)
- [Spring @Component , anotaciones y jerarquía](https://www.arquitecturajava.com/spring-component-anotaciones-y-jerarquia/)
- [Introduction to the Spring IoC Container and Beans](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html)
- [High-Performance Hibernate Tutorial](https://vladmihalcea.com/tutorials/hibernate/)

**Para @Entity:**

- [Mapping Enum Values in JPA - @Enumerated, @Transient, and Lifecycles Callbacks](https://medium.com/@kulshresthjangid/mapping-enum-values-in-jpa-explore-into-enumerated-transient-and-lifecycles-callbacks-3f66d2d48596)
- [Difference between @OneToMany and @ElementCollection?](https://stackoverflow.com/questions/8969059/difference-between-onetomany-and-elementcollection)

**Para Joins:**
- [Hibernate Community Documentation - Chapter 2. Mapping Entities](https://docs.hibernate.org/stable/annotations/reference/en/html/entity.html#entity-mapping)


**Bean Validation:**
- [Jakarta Validation specification](https://jakarta.ee/specifications/bean-validation/4.0/jakarta-validation-spec-4.0.0-m1)
- [Spring Boot and Validation: A Complete Guide with @Valid and @Validated](https://dev.to/gianfcop98/spring-boot-and-validation-a-complete-guide-with-valid-and-validated-471p)


## Footnotes
[^1]: Plain Old Java Object. Clase Java simple que no dependen de un framework especial que representa datos. Del mismo modo, un objeto POJO es una instancia de una clase que no extiende ni implementa nada en especial.

[^2]: Objeto cuya creación y ciclo de vida son gestionados por el contenedor de Spring (el IoC Container).

[^3]: En este contexto, una dependencia es simplemente un objeto que otro objeto necesita para realizar su trabajo.

[^4]: Recibir los *otros* objetos necesarios desde el exterior, en lugar de crearlos en la misma clase.

[^5]: Termino que se usa para referirse a código repetitivo, predecible y necesario para que algo funcione, pero que no aporta lógica de negocio