# A07 JPA Basic Types vs Java primitive/wrapper types

## Contexto
Durante la creación de las entidades, me tope con casos de uso para usar la anotación `@ElementCollection` que, de acuerdo con su especificación en la documentación, define una colección de instancias de un basic type o clases "embebibles". Tuve problemas para terminar de definir dicha @ElementCollection ya que confundí el término de *basic type* con *primitive type* en Java; lo interpreté como sinónimo.

## ¿Qué es un *basic type* y de dónde sale?
Los tipos en Java, ya sean primitivos o wrappers, su principal diferencia en como se comportan en memoria. Sin embargo, Hibernate tiene que resolver como persistir esos tipos en la base de datos. JPA solo necesita saber como  traducir `int` o `Integer` a SQL `INTEGER` de igual forma. 

Acabamos de dar un ejemplo de un basic type. Estos, son un tipo cuyo valor puede persistirse directamente como una única columna de la base de datos. 

La confusión vino cuando me tope con un enum de literales String. Efectivamente, String una clase de Java pero sabe persistirla directamente. 

## Conclusión
Basic Type no significa "tipo primitivo". Un basic type tiene un valor lo suficientemente simple como para representarlo directamente en una columna

## Fuentes consultadas
- JPA @Basic Annotation: https://www.baeldung.com/jpa-basic-annotation
