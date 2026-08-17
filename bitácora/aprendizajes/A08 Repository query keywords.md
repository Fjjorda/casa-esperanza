# A08 Repository query keywords
Existen diversas clases repository que se pueden utilizar como base para implementar un patrón DAO sobre nuestras entidades JPA. Usamos **JpaRepository** que hereda de CrudRepository y sabemos que podemos definir métodos que se anotan con `@Query` donde se indica la consulta en JPQL que se hará sobre la entidad.

Sin embargo, también podemos declarar métodos cuyo nombre sigue un conjunto de reglas que definen el comportamiento de ese método; las *repository query keywords*. Estos métodos se construyen de la siguiente manera:

(Subject + SubjectModifiers) * 'By' + Property + PredicateOperator + LogicalOperator + Modifiers + Ordering

Más, no se tienen que rellenar todos los campos. 
Un ejemplo completo sería `findTop10DistinctByFirstNameContainingIgnoreCaseAndAgeGreaterThanOrderByLastNameAsc` y el más básico `findByUser`.

## Ejemplos de sintaxis
### Subject
findBy...
existsBy...
countBy...
deleteBy...

### SubjectModifiers
Distinct
Top
First

### Property
Indica sobre *qué* atributo de la entidad realizamos la consulta.

FirstName
Age
Email
AddressCity
ProviderName
...

### PredicateOperator
GreaterThan
LessThan
Between
In
NotIn
Containing
StartingWith
EndingWith
IsNull
IsNotNull
True
False

### LogicalOperator
And
Or

### Modifiers
IgnoreCase
AllIgnoreCase

### Ordering
OrderBy...Asc
OrderBy...Desc

## Comparación con @Query
Usando JPQL declaramos explícitamente qué quiero obtener de la consulta. Podría decirse que es más concreto en lugar de "abstracto" como se ve con las keywords.


## Path property (Joins)
...

## Fuentes Consultadas
- Repository query keywords: https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html